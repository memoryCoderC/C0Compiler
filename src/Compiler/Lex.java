package Compiler;

import Compiler.Interface.ILex;
import Entity.Constant;
import Entity.LexReturn;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;



/**
 * 词法分析器实现
 * Created by chen on 2016/10/31 0031.
 * */
public class Lex implements ILex {
    private String code;//全部代码
    private int position = 0;//当前检测到的字符位置
    public Lex(String path) throws IOException {//初始化并读取全部代码
        BufferedReader br = new BufferedReader(new FileReader(path));
        StringBuilder sb = new StringBuilder();
        String temp;
        while ((temp = br.readLine()) != null) {
            sb.append(temp);
        }
        code=sb.toString();
    }

    /**
     * 识别一个单词
     * @return 取出的单词
     */
    @Override
    public LexReturn getsym() {
        StringBuilder sb = new StringBuilder();
        char achar=code.charAt(position++);
        while(achar==' '||achar == 10 || achar == 9){//如果是空格换行和tab，则取下一个字符
            achar=code.charAt(++position);
        }
        if(Character.isLetter(achar)){//如果是字母就获取完整单词
            //获取完整单词
            while(Character.isLetterOrDigit(achar)){
                sb.append(achar);
                achar=code.charAt(++position);
            }
            --position;
            String newStr= sb.toString();
            for(int n=0; n< Constant.keyWords.length; n++){//遍历关键字表
                if(newStr.equals( Constant.keyWords[n])){//判断是哪个关键字
                    //System.out.println("单词的值是："+newStr);
                    return new LexReturn(Constant.lexType.KEYWORD,newStr);
                }
            }
            return  new LexReturn(Constant.lexType.IDENTIFIER,newStr);//否则返回标识符
        }else if(Character.isDigit(achar)){//如果是数字
            while(Character.isDigit(achar)){ //获取完整数字
                sb.append(achar);
                achar=code.charAt(++position);
            }
            --position;
            int value = Integer.parseInt(sb.toString());
            return new LexReturn(Constant.lexType.CONSTANT,value);//返回常量
        }else{
            for (char s:Constant.operator) {//如果是操作符
                if(s==achar){
                    return  new LexReturn(Constant.lexType.OPERATOR,achar);//返回操作符
                }
            }
            for (char s:Constant.Delimiters) {//如果是界符
                if(s==achar){
                    return  new LexReturn(Constant.lexType.DELIMITERS, achar);//返回界符
                }
            }
        }
        return null;//都不是的话返回空
    }

    public static void main(String[] args) {
        String s = "1.txt";
        try {
            Lex l = new Lex(s);
            l.getsym();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
