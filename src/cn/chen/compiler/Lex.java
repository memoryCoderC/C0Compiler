package cn.chen.compiler;

import cn.chen.entity.Constant;
import cn.chen.entity.LexReturn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static cn.chen.entity.Constant.keywords;
import static cn.chen.entity.Constant.single;


/**
 * 词法分析器实现
 * Created by chen on 2016/10/31 0031.
 */
public class Lex {
    private static String code;//全部代码
    private static int position = 0;//当前检测到的字符位置
    public static int count;
    public Lex(String path) throws IOException {//初始化并读取全部代码
        BufferedReader br = new BufferedReader(new FileReader(path));
        StringBuilder sb = new StringBuilder();
        String temp;
        while ((temp = br.readLine()) != null) {
            sb.append(temp);
        }
        code = sb.toString();
    }

    /**
     * 识别一个单词
     *
     * @return 取出的单词
     */
    public LexReturn getsym() {
        StringBuilder sb = new StringBuilder();
        char achar;
        try {
            achar = code.charAt(position++);
        } catch (Exception e) {//如果没有代码就返回空
            return null;
        }

        while (achar == ' ' || achar == 10 || achar == 9) {//如果是空格换行和tab，则取下一个字符
            achar = code.charAt(position++);
        }
        if (Character.isLetter(achar)) {//如果是字母就获取完整单词
            //获取完整单词
            while (Character.isLetterOrDigit(achar)) {
                sb.append(achar);
                achar = code.charAt(position++);
            }
            --position;
            String newStr = sb.toString();
            for (Constant.symbol a : keywords) {//遍历关键字表
                if (newStr.equals(a.toString())) {//判断是哪个关键字
                    return new LexReturn(a, newStr);
                }
            }
            return new LexReturn(Constant.symbol.ident, newStr);//搜索失败为名字
        } else if (Character.isDigit(achar)) {//如果是数字
            while (Character.isDigit(achar)) { //获取完整数字
                sb.append(achar);
                achar = code.charAt(position++);
            }
            --position;
            int value = Integer.parseInt(sb.toString());
            return new LexReturn(Constant.symbol.number, value);//返回常量
        } else {
            for (Constant.symbol a : single) {//如果是单字符
                if (a.toString().charAt(0) == achar) {
                    if(achar == '{'){
                        count++;
                    }else if(achar == '}'){
                        count--;
                    }
                    return new LexReturn(a, achar);//返回操作符
                }

            }
        }
        return null;//都不是的话返回空
    }

    public static void main(String[] args) {
        String s = "2.txt";
        try {
            Lex l = new Lex(s);
            System.out.println();
            System.out.println(l.getsym().value);
            System.out.println(l.getsym().value);
            System.out.println(l.getsym().value);
            System.out.println(l.getsym().value);
            System.out.println(l.getsym().value);
            l.getsym();l.getsym();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
