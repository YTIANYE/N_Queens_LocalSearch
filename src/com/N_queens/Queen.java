package com.N_queens;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.N_queens.Main.*;

public class Queen {

    private int N = 0;
    List<String> strs=new ArrayList<String>();

    Queen(int N) throws IOException {
        this.N = N;
    }

    /**
     * 初始化函数
     * 初始化棋盘，不同行不同列放置一个皇后
     * 冲突表清零
     */
    public void InitialPos() {
        for (int i = 0; i < N; i++) {   //(1,1)(2,2)...(8,8)
            Pos[i] = i;
        }
        for (int i = 0; i < N; i++) {
            int pos = (int) (Math.random() * N);    //假设pos=2
            int tmp = Pos[pos];                     //结果 列交换 (2,5)(5,2)
            Pos[pos] = Pos[N - pos - 1];
            Pos[N - pos - 1] = tmp;
        }
        for (int i = 0; i < CollTable.length; i++){ //冲突表清零
            for (int j = 0; j < 2; ++j){
                CollTable[i][j] = 0;
            }
        }
    }

    /**
     * 计算冲突个数
     * @return
     */

    private long CalCollision() {
        long CollCnt = 0;       //主副对角线皇后冲突计数
        for(int i =0; i<N; i++){
            CollTable[Pos[i]+i][0]++;   //副对角线 数值对应皇后个数
            CollTable[Pos[i]-i+N-1][1]++;   //主对角线
        }
        for(int i=0; i<2*N; i++){
            for(int j=0; j<2; j++){
                if(CollTable[i][j] > 1) //对角线上个数大于1，发生冲突
                    CollCnt += CollTable[i][j]-1;
            }
        }
        //System.out.println();
        //System.out.println("CalCollision()之后");
        //OutputSolution();
        return CollCnt;
    }

    /**
     * 随机交换两个位置 更新冲突表
     * 如果冲突减小 返回最新的冲突数
     * 如果冲突没有变小，恢复冲突表，返回原冲突数
     */

    public long SwapQueen(long OrigCollCnt) {
        int pos1=(int)(Math.random()*N);
        int pos2=(int)(Math.random()*N);
        while(pos1 == pos2){
            pos2=(int)(Math.random()*N);
        }
        long CurrCollCnt = NewCollsion(pos1,pos2,OrigCollCnt);
        if (CurrCollCnt < OrigCollCnt)		//计算冲突数是否减小 ，是则交换
        {
            //swap(Pos[pos1],Pos[pos2]);
            int tmp = Pos[pos1];
            Pos[pos1] = Pos[pos2];
            Pos[pos2] = tmp;
        }
        else{
            CurrCollCnt=OrigCollCnt;
            RecoverCollision(pos1,pos2);    //恢复冲突表
        }
        return CurrCollCnt;
    }

    /**
     *恢复冲突表
     */

    private void RecoverCollision(int pos1, int pos2) {
        //恢复原有冲突
        ++CollTable[Pos[pos1]+pos1][0];
        ++CollTable[Pos[pos1]-pos1+N-1][1];
        ++CollTable[Pos[pos2]+pos2][0];
        ++CollTable[Pos[pos2]-pos2+N-1][1];
        //删去现有冲突
        --CollTable[Pos[pos1]+pos2][0];
        --CollTable[Pos[pos1]-pos2+N-1][1];
        --CollTable[Pos[pos2]+pos1][0];
        --CollTable[Pos[pos2]-pos1+N-1][1];
    }

    /**
     * 改变冲突表
     * 返回交换位置后的冲突数
     */

    public long NewCollsion(int pos1,int pos2,long oc ){
        long currcollcnt = oc;

        //System.out.println("NewCollsion开始");
        //OutputSolution();
        //删去原有冲突
        //对角线个数-1后>0说明此位置有冲突
        if(--CollTable[Pos[pos1]+pos1][0]>0)    //Pos[pos1]+pos1 ：pos1行Pos[pos1]对应对角线编号
            currcollcnt--;
        if(--CollTable[Pos[pos1]-pos1+N-1][1]>0)
            currcollcnt--;
        if(--CollTable[Pos[pos2]+pos2][0]>0)
            currcollcnt--;
        if(--CollTable[Pos[pos2]-pos2+N-1][1]>0)
            currcollcnt--;

        //System.out.println("currcollcnt " + currcollcnt);
        //System.out.println("NewCollsion开始2");
        //OutputSolution();
        //计算现有冲突
        //对角线个数+1后>1，说明此位置有冲突
        if(++CollTable[Pos[pos1]+pos2][0]>1)
            currcollcnt++;
        if(++CollTable[Pos[pos1]-pos2+N-1][1]>1)
            currcollcnt++;
        if(++CollTable[Pos[pos2]+pos1][0]>1)
            currcollcnt++;
        if(++CollTable[Pos[pos2]-pos1+N-1][1]>1)
            currcollcnt++;
        //System.out.println("currcollcnt  " + currcollcnt);
        return currcollcnt;
    }

    /**
     *
     * 计算过程
     */

    public void cal(int N) {
        this.N = N;     //假设 N = 8
        long Markov = N * N;    //64
        long startTime = System.currentTimeMillis();    //获取开始时间
        while1:
        while (true) {
            long IterCnt = 0;   //迭代次数
            InitialPos();       //初始化
            //System.out.println("");
            //System.out.println("初始化之后");
            //OutputSolution();
            long collcnt = CalCollision();//获取冲突个数
            if (isEnd(startTime, collcnt)) break while1;
            while (IterCnt < Markov) {  //初始化迭代次数不超过N*N，如果等于，说明陷入局部极小
                //System.out.println();
                //System.out.println("Swap前");
                collcnt = SwapQueen(collcnt);   //随机交换位置，获取新的冲突数
                //System.out.println();
                //System.out.println("Swap后");
                if (isEnd(startTime, collcnt)) break while1;
                IterCnt++;
                //System.out.println("**"+ IterCnt);
            }
        }
    }

    /**
     * 如果无冲突 计算运行时间 输出结果
     */
    private boolean isEnd(long startTime, long collcnt) {
        if (collcnt == 0) {
            long endTime = System.currentTimeMillis();    //获取结束时间
            double time = (endTime - startTime)/1000.0;
            System.out.println("程序运行时间：" + time + "s");
            OutputSolution();
            return true;
        }
        return false;
    }

    /**
     * 打印结果
     */
    public void OutputSolution() {
        //System.out.print("{");
        //strs.add("{");
        for(int i =0; i< N; i++){
            //System.out.print(Arrays.toString(IntToByte(Pos[i])) + ", ");
            //System.out.print(Pos[i]+", ");
            //System.out.print(toFullBinaryString(Pos[i])+", ");
            strs.add(toFullBinaryString(Pos[i])+", ");
        }
        //System.out.print("}");
        //strs.add("}");

        /**
         * java将数据写入txt文本，带换行
         */
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("D:/n_queens.txt");//创建文本文件
            int i=0;
            Iterator it1 = strs.iterator();
            while( (it1 !=null) && it1.hasNext() && i<N){//循环写入

                fileWriter.write(strs.get(i).toString() +"\r\n");//写入 \r\n换行
                i++;
            }
            fileWriter.write("共"+i+"条");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 打印整型数值的完整二进制格式
     */
    private static String toFullBinaryString(int x) {
        int[] buffer = new int[Integer.SIZE];
        for (int i = (Integer.SIZE - 1); i >= 0; i--) {
            buffer[i] = x >> i & 1;
        }
        String s = "";
        for (int j = (Integer.SIZE - 1); j >= 0; j--) {
            s = s + buffer[j];
        }
        return s;
    }

}



























