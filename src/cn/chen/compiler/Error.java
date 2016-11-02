package cn.chen.compiler;

/**
 * Created by chen on 2016/11/29 0029.
 */
public class Error {
    public static void showError(int error) {
        switch(error) {
            case 1:System.out.println("缺少左括号");break;
            case 2:System.out.println("缺少右括号");break;
            case 3:System.out.println("缺少分号");break;
            case 4:System.out.println("不是变量");break;
            case 5:System.out.println("不能作为函数的名字");break;
            case 6:System.out.println("该标识符未声明");break;
            case 7:System.out.println("不能作为过程名");break;
            case 8:System.out.println("当前数字超过了规定上限");break;
            case 9:System.out.println("语句格式错误,开头应为变量或者是函数名");break;
            case 10:System.out.println("缺少赋值符号");break;
            case 11:System.out.println("不是标识符");break;
            case 12:System.out.println("不是过程");break;
            case 13:System.out.println("缺少左大括号");break;
            case 14:System.out.println("缺少右大括号");break;
            case 15:System.out.println("超出数字允许的最大长度");break;
            case 16:System.out.println("没有主函数");break;
            case 17:System.out.println("已经存在主函数");break;
        }
        System.exit(0);
    }
}
