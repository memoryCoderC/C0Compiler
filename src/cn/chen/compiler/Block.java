package cn.chen.compiler;

import cn.chen.Main;
import cn.chen.entity.Constant;
import cn.chen.entity.LexReturn;
import cn.chen.entity.TableStruct;

import java.io.IOException;
import java.util.List;

import static cn.chen.compiler.Error.showError;
import static cn.chen.entity.Constant.Operator.*;
import static cn.chen.entity.Constant.nameTable.INT_PROCEDUR;
import static cn.chen.entity.Constant.nameTable.VOID_PROCEDUR;
import static cn.chen.entity.Constant.symbol.*;

/**
 * Created by chen on 2016/11/28 0028.
 */
public class Block {
    Lex lex;//词法分析器
    Gen gen = new Gen();//虚拟机代码生成
    LexReturn lexReturn;//lex的返回值
    List<TableStruct> nametable;//名字表
    String identVale;//保存标识符

    /**
     *
     * @param path 要读的文件路径
     */
    public Block( String path) {
        nametable = Main.tableStructs;
        try {
            lex = new Lex(path);
        } catch (IOException e) {
            System.out.println("文件错误");
            return;
        }
    }
    /**
     * 过程处理
     */
    public void produce() {
        int varAssignPos;//变量名字分配到的相对地址    //dx
        int cx1,cx2;//用于保存虚拟机指令位置
        cx2 = nametable.size()-1;
        varAssignPos = 2;
        cx1 = Main.aimCode.size();
        gen.doGen(INT, 0, 2);
        while (true) {
            varAssignPos = statement(varAssignPos);
            if(lexReturn.wordLexType.equals(rbrace)&&Lex.count==0){
                break;
            }
        }
        Main.aimCode.get(cx1).second = varAssignPos;
        nametable.get(cx2).size = varAssignPos;
        gen.doGen(RET, 0, 0);
        gen.doGen(RED, 0, 2);
    }

    /**
     * 执行bolck
     */
    public void doBlock() {
        int flag = 0;//用于检测主函数
        int varAssignPos = 3;//变量名字分配到的相对地址
        gen.doGen(INT, 0, varAssignPos);
        gen.doGen(JMP, 0, 0);//虚拟机代码调到主函数默认为调到第一个//需要修改为主函数地址
        while ((lexReturn = lex.getsym()) != null) {
            if (lexReturn.wordLexType.equals(intsym)) {//先检测int符号
                lexReturn = lex.getsym();
                if (lexReturn.wordLexType.equals(ident)) {
                    identVale = (String) lexReturn.value;
                    lexReturn = lex.getsym();
                    if (!lexReturn.wordLexType.equals(lparen)) {//如果检测到的不是括号证明是变量的声明
                        varAssignPos = intDeal(0, varAssignPos, identVale);//进行全局变量声明
                    } else {//如果是左括号就进行int过程的声明
                        lexReturn = lex.getsym();
                        enter(INT_PROCEDUR, 0, identVale);//int过程声明
                        if (lexReturn.wordLexType.equals(rparen)) {
                            lexReturn = lex.getsym();
                            if (lexReturn.wordLexType.equals(lbrace)) {
                                produce();//进行函数内部的处理
                            } else {
                                showError(13);
                            }
                        } else {
                            showError(2);
                        }
                    }
                } else {
                    showError(11);
                }
            } else if (lexReturn.wordLexType.equals(voidsym)) {
                lexReturn = lex.getsym();
                if (lexReturn.wordLexType.equals(ident)) {
                    if (lexReturn.value.equals("main")) {
                        if (flag == 1) {
                            showError(17);
                        }
                        flag = 1;
                        Main.aimCode.get(1).second = Main.aimCode.size();//修改主函数的跳转地址
                        gen.doGen(CAL, 0, Main.aimCode.size() + 1);//跳转到主函数
                    }
                    enter(VOID_PROCEDUR, 0, (String) lexReturn.value);
                    lexReturn = lex.getsym();
                    paren();
                    if (lexReturn.wordLexType.equals(lbrace)) {
                        produce();//进行函数内部的处理
                    } else {
                        showError(13);//缺少左大括号
                    }
                } else {
                    showError(11);
                }
            }
        }
        if (flag == 0) {
            showError(16);
        }
        Main.aimCode.get(0).second = varAssignPos;
        gen.doGen(RET,0,0);
    }
    /**
     * 查找标识符在名字表的位置
     * @param ident 要查找的名字
     * @return 找到则返回在名字表中的位置否则返回-1
     */
    int position(String ident) {
        for (TableStruct table : nametable) {
            if (table.name.equals(ident)) {
                return nametable.indexOf(table);
            }
        }
        return -1;
    }
    /**
     * 赋值语句处理//函数调用
     */
    private void assignDeal() {
        int i = position((String) lexReturn.value);//保存取得的变量在名字表中的位置
        if (i == -1) {
            showError(4);//不是变量
        } else if (nametable.get(i).type != Constant.nameTable.VARIABLE && nametable.get(i).type != VOID_PROCEDUR) {//如果不是变量或过程
            showError(9);//应该以变量或过程名开始
        } else if (nametable.get(i).type == Constant.nameTable.VARIABLE) {//如果是变量
            lexReturn = lex.getsym();
            if (lexReturn.wordLexType.equals(becomes)) {//检测到等号
                lexReturn = lex.getsym();
            } else {
                showError(10);//缺少赋值符号
            }
            expression();//处理复制符号右侧的表达式
            if (i != -1) {
                gen.doGen(STO, nametable.get(i).level, nametable.get(i).addr);//生成sto指令
            }
            if (lexReturn.wordLexType.equals(semicolon)) {

            }else{
                showError(3);
            }
        } else if (nametable.get(i).type.equals(VOID_PROCEDUR)) {
            lexReturn = lex.getsym();
            if (nametable.get(i).type.equals(VOID_PROCEDUR)) {
                gen.doGen(CAL, 0, nametable.get(i).addr);//生成call指令
            } else {
                showError(12);
            }
        }
    }

    /**
     * 输入语句处理
     */
    private void scanfDeal() {
        int i;//保存取得的变量在名字表中的位置
        lexReturn = lex.getsym();
        if (lexReturn.wordLexType.equals(lparen)) {
            lexReturn = lex.getsym();
            if (lexReturn.wordLexType.equals(ident)) {
                i = position((String) lexReturn.value);
                if (i == -1) {
                    showError(4);
                } else {
                    gen.doGen(RED, 0, 0);
                    gen.doGen(STO, nametable.get(i).level, nametable.get(i).addr);
                    lexReturn = lex.getsym();
                    if (lexReturn.wordLexType.equals(rparen)) {
                        lexReturn = lex.getsym();
                        if (lexReturn.wordLexType.equals(semicolon)) {

                        } else {
                            showError(3);//缺少分号
                        }
                    } else {
                        showError(2);//缺少右括号
                    }
                }
            } else {
                showError(6);//标识符未声明
            }
        } else {
            showError(1);
        }
    }

    /**
     * 打印语句处理
     */
    private void printDeal() {
        lexReturn = lex.getsym();
        if (lexReturn.wordLexType.equals(lparen)) {
            lexReturn = lex.getsym();
            expression();//表达式处理
            gen.doGen(WRT, 0, 0);
            if (lexReturn.wordLexType.equals(rparen)) {
                lexReturn = lex.getsym();
                if (lexReturn.wordLexType.equals(semicolon)) {

                } else {
                    showError(3);//缺少分号
                }
            } else {
                showError(2);//缺少右括号
            }
        } else {
            showError(1);//缺少左括号
        }
    }

    /**
     * 条件语句处理
     * @param varAssignPos 变量声明的相对位置
     */
    private void ifDeal(int varAssignPos) {
        int cx1;//保存虚拟机代码指针
        lexReturn = lex.getsym();
        if (lexReturn.wordLexType.equals(lparen)) {
            lexReturn = lex.getsym();
            expression();
            cx1 = Main.aimCode.size();//保存当前指令地址
            gen.doGen(JPC, 0, 0);//生成条件跳转指令跳转地址暂时先填0
            if (lexReturn.wordLexType.equals(rparen)) {
                lexReturn = lex.getsym();
                if (lexReturn.wordLexType.equals(lbrace)) {
                    while (!lexReturn.wordLexType.equals(rbrace)) {
                        varAssignPos = statement(varAssignPos);//如果不是有大括号就一直进行语句处理
                    }
                } else {
                    showError(13);
                }
                Main.aimCode.get(cx1).second = Main.aimCode.size();//修改之前生成的跳转地址
                if (lexReturn.wordLexType.equals(elsesym)) {
                    lexReturn = lex.getsym();
                    if (lexReturn.wordLexType.equals(lbrace)) {
                        while (!lexReturn.wordLexType.equals(rbrace)) {
                            varAssignPos = statement(varAssignPos);//不是右大括号一直进行语句处理
                        }
                    } else {//与先下面一样
                        showError(13);
                    }
                }
            } else {
                showError(2);
            }

        } else {
            showError(1);
        }
    }

    /**
     * 循环语句处理
     * @param varAssignPos 变量声明的相对位置
     */
    private void whileDeal(int varAssignPos) {
        int cx1, cx2;//保存虚拟机指令地址
        lexReturn = lex.getsym();
        if (lexReturn.wordLexType.equals(lparen)) {
            lexReturn = lex.getsym();
            cx2 = Main.aimCode.size();
            expression(); //括号里的表达式处理
            cx1 = Main.aimCode.size();
            gen.doGen(JPC, 0, 0);//生成条件跳转指令地址暂时为0
            if (lexReturn.wordLexType.equals(rparen)) {//右括号
                lexReturn = lex.getsym();
                if (lexReturn.wordLexType.equals(lbrace)) {
                    while (!lexReturn.wordLexType.equals(rbrace)) {
                        varAssignPos = statement(varAssignPos);//不是右大括号一直进行语句处理
                    }
                }
            } else {//！！！！！！！！！！！感觉不对，co中括号中没有处理语句
                //varAssignPos = statement(varAssignPos);
                showError(2);
            }
            gen.doGen(JMP, 0, cx2);
            Main.aimCode.get(cx1).second = Main.aimCode.size();
        } else {
            showError(1);
        }
    }

    /**
     * 变量声明语句处理
     * @param lev          当前所在层，0代表全局变量，1代表局部变量
     * @param varAssignPos 变量声明的相对位置
     * @param identVale    标识符的值
     * @return 变量声明的位置
     */
    private int intDeal(int lev, int varAssignPos, String identVale) {
        enter(Constant.nameTable.VARIABLE, lev, varAssignPos, identVale);  //填写名字表
        varAssignPos++;
        while (lexReturn.wordLexType.equals(comma)) {//如果检测到逗号就接着声明常量
            lexReturn = lex.getsym();
            if (lexReturn.wordLexType.equals(ident)) {//检测名字
                identVale = (String) lexReturn.value;
                enter(Constant.nameTable.VARIABLE, lev, varAssignPos, identVale);  //填写名字表
                varAssignPos++;
                lexReturn = lex.getsym();
            }
        }
        if (lexReturn.wordLexType.equals(semicolon)) {//检测分号

        } else {
            showError(3);       //缺分号
        }
        return varAssignPos;
    }
    /**
     * 返回语句处理
     */
    private void returnDeal() {
        lexReturn = lex.getsym();
        expression();
        gen.doGen(STO, 0, 3);
        if (lexReturn.wordLexType.equals(semicolon)) {
            lexReturn = lex.getsym();
        } else {
            showError(3);
        }
    }

    /**
     * 语句处理
     * @param varAssignPos 变量声明的相对位置
     */
    private int statement(int varAssignPos) {
        lexReturn = lex.getsym();
        if(lexReturn==null){
            showError(14);
        }
        if (lexReturn.wordLexType.equals(ident)) {//准备按照赋值语句处理
            assignDeal();
        } else if (lexReturn.wordLexType.equals(scanfsym)) {//处理输入语句
            scanfDeal();
        } else if (lexReturn.wordLexType.equals(printfsym)) {//处理打印
            printDeal();
        } else if (lexReturn.wordLexType.equals(ifsym)) {//条件语句处理
            ifDeal(varAssignPos);
        } else if (lexReturn.wordLexType.equals(whilesym)) {//循环语句处理
            whileDeal(varAssignPos);
        } else if (lexReturn.wordLexType.equals(returnsym)) {//return语句处理
            returnDeal();
        } else if (lexReturn.wordLexType.equals(intsym)) {//声明语句处理
            lexReturn = lex.getsym();
            if (lexReturn.wordLexType.equals(ident)) {
                identVale = (String) lexReturn.value;
                lexReturn = lex.getsym();
                varAssignPos = intDeal(1, varAssignPos, identVale);
            } else {
                showError(11);
            }
        }
        return varAssignPos;
    }

    /**
     * 表达式处理
     */
    private void expression() {
        Constant.symbol addop; //用于保存正负号
        if (lexReturn.wordLexType.equals(plus) || lexReturn.wordLexType.equals(minus)) {
            addop = lexReturn.wordLexType;
            lexReturn = lex.getsym();
            if (addop.equals(minus)) {
                gen.doGen(LIT, 0, 0);
            }
            term();
            if (addop.equals(minus)) {
                gen.doGen(SUB, 0, 0);
            }
        } else {
            term();
        }
        while (lexReturn.wordLexType.equals(plus) || lexReturn.wordLexType.equals(minus)) {
            addop = lexReturn.wordLexType;
            lexReturn = lex.getsym();
            term();
            if (addop.equals(plus)) {
                gen.doGen(ADD, 0, 0);
            } else {
                gen.doGen(SUB, 0, 0);
            }
        }
    }

    /**
     * 项处理
     */
    private void term() {
        Constant.symbol mul; //用于保存乘除法
        factor();
        while (lexReturn.wordLexType.equals(times) || lexReturn.wordLexType.equals(slash)) {//如果是乘除
            mul = lexReturn.wordLexType;
            lexReturn = lex.getsym();
            factor();
            if (mul.equals(times)) {
                gen.doGen(MUL, 0, 0);//生成乘法指令
            } else if (mul.equals(slash)) {
                gen.doGen(DIV, 0, 0);//生成除法指令
            }
        }
    }

    /**
     * 检测括号
     * @return
     */
    int paren() {
        if (lexReturn.wordLexType.equals(lparen)) {
            lexReturn = lex.getsym();
            if (!lexReturn.wordLexType.equals(rparen)) {
                showError(3);
            }
        } else {
            showError(2);
        }
        lexReturn = lex.getsym();
        return 0;
    }

    /**
     * 因子处理
     */
    private void factor() {
        int i;//存放名字表中的位置
        if (lexReturn.wordLexType.equals(ident)) {
            i = position((String) lexReturn.value);//查找名字表
            if (i == -1) {
                showError(6);//该标识符未声明
            } else {
                TableStruct current = nametable.get(i);
                switch (current.type) {
                    case VARIABLE:
                        gen.doGen(LOD, current.level, current.addr);
                        lexReturn = lex.getsym();
                        break;
                    case INT_PROCEDUR:
                        gen.doGen(CAL, 0, current.addr);
                        gen.doGen(LOD, 1, 0);
                        lexReturn = lex.getsym();
                        paren();
                        break;
                    case VOID_PROCEDUR:
                        showError(2);
                        lexReturn = lex.getsym();
                        break;
                }
            }
        } else if (lexReturn.wordLexType.equals(number)) {//如果因子是数字
            gen.doGen(LIT, 0, (int) lexReturn.value);
            lexReturn = lex.getsym();
        } else if (lexReturn.wordLexType.equals(lparen)) {
            lexReturn = lex.getsym();
            expression();
            if (lexReturn.wordLexType.equals(rparen)) {
                lexReturn = lex.getsym();
            } else {
                showError(2);//缺少右括号
            }
        }
    }

    /**
     * 在名字表中加入一项
     *
     * @param type 类型（变量声明还是过程声明）
     * @param lev  所在层
     */
    private void enter(Constant.nameTable type, int lev, String identVale) {
        this.enter(type, lev, 0, identVale);
    }

    /**
     * 在名字表中加入一项
     *
     * @param type         类型（变量声明还是过程声明）
     * @param lev          所在层
     * @param varAssignPos 变量在该层的相对地址
     */
    private void enter(Constant.nameTable type, int lev, int varAssignPos, String identVale) {
        TableStruct tableStruct = new TableStruct();
        tableStruct.name = identVale;
        tableStruct.type = type;
        if (type.equals(Constant.nameTable.VARIABLE)) {//是变量声明
            tableStruct.level = lev;
            tableStruct.addr = varAssignPos;//变量地址
        } else if (type.equals(VOID_PROCEDUR)) {//是过程声明
            tableStruct.level = 0;
            tableStruct.addr = Main.aimCode.size();        //过程地址==当前指针的值
            tableStruct.size = 0;    //初始默认为0
        } else if (type.equals(INT_PROCEDUR)) {//是过程声明
            tableStruct.level = 0;
            tableStruct.addr = Main.aimCode.size();        //过程地址==当前指针的值
            tableStruct.size = 0;    //初始默认为0
        }
        Main.tableStructs.add(tableStruct);
    }
}