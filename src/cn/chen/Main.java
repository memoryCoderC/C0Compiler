package cn.chen;

import cn.chen.compiler.Block;
import cn.chen.entity.Instruction;
import cn.chen.entity.TableStruct;
import cn.chen.interprter.Interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by chen on 2016/11/28 0028.
 */
public class Main {
    public static List<TableStruct> tableStructs = new ArrayList<TableStruct>();//名字表的数组
    public static ArrayList<Instruction> aimCode = new ArrayList<Instruction>();//存放虚拟机生成的代码
    // public static int aim_codePos = 0;
    public static void main(String[] args) {
        System.out.println("请输入文件名");
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        Block block = new Block(s);
        block.doBlock();
        Interpreter interpreter = new Interpreter(aimCode);
        System.out.println("名字表：");
        for (TableStruct t : tableStructs) {
            System.out.println(t);
        }
        System.out.println();
        System.out.println("虚拟机代码：");
        int j=0;
        for (Instruction i : aimCode) {
            System.out.print("第"+j+"个 ");
            System.out.println(i);
            j++;
        }
        interpreter.paser();
    }
}
