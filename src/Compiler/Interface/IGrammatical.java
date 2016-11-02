package Compiler.Interface;

/**
 * 语法分析程序接口
 * Created by chen on 2016/10/31 0031.
 */
public interface IGrammatical {
    public void block();//分程序
    public void varDeclaration();//变量定义
    public void statement();//语句
    public void condition();//条件
    public void expression();//表达式
    public void term();//项
    public void factor();//因子

}
