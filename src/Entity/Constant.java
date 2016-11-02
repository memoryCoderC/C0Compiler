package Entity;

/**
 * 常量的定义
 * Created by chen on 2016/10/31 0031.
 */
public class Constant {
    public static final int MAX_SIZE = 1000;//运行栈的大小
    public static final String[] keyWords = new String[]{//系统关键字
            "int", "if", "while", "return", "scanf", "printf", "return", "else"
    };
    public static final char[] operator = new char[]{//操作符
            '+', '-', '*', '/', '='
    };
    public static final char[] Delimiters = new char[]{//界符
            '(', ')', ';', ',', '{', '}'
    };

    public static enum lexType {//词法分析类型的定义
        CONSTANT,//常量
        KEYWORD,//关键字
        OPERATOR,//运算符
        DELIMITERS,//界符
        IDENTIFIER//标识符
    }

    public static enum nameTable {
        VARIABLE,//变量
        PROCEDUR//过程
    }

}
