package com.zone.online_english_learning;

import java.util.Scanner;

/**
 * @ClassName Text
 * @Author zone
 * @Date 2019/4/25  15:16
 * @Version 1.0
 * @Description
 */
public class Text {

    public void text(){
        Scanner sc=new Scanner(System.in);
        Integer i=sc.nextInt();

    }
    public void printN(int n){
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=1;i<=n;i++){
            for(int j=1;j<=n;j++){
                stringBuilder.append(j);
            }
            stringBuilder.append(";");


        }
    }
}
