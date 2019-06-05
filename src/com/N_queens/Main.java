package com.N_queens;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static int MAXN = 4000000;           //最大皇后个数
    public static int N = 0;                    //皇后个数
    public static int[] Pos = new int[MAXN];    //皇后位置数组Pos[N]：下标为行号，对应值为列号

    /**
     * 冲突表，分别记录对角线上的冲突数 [][0] 副对角线 [][1] 主对角线
     * 副对角线 行数+列数   主对角线 列数-行数+N-1
     * 以N=6为例
     *          副对角线    主对角线
     * （0，0）    0           5
     * （0，5）    5           10
     * （5，5）    10          5
     * （5，0）    5           0
     */
    public static int[][] CollTable = new int[MAXN * 2][2]; //冲突表

    public static void main(String[] args) throws IOException {
        Queen queen = new Queen(N);
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("输入皇后个数N：");
            N = sc.nextInt();
            //System.out.println(N);
            for(int i = 0; i<Pos.length; i++){
                Pos[i] = -1;
            }
            if(N <= 0 || N >= MAXN || N == 2|| N==3)
                System.out.println("N不符合，请重新输入！");
            else
                queen.cal(N);
        }
    }

}
