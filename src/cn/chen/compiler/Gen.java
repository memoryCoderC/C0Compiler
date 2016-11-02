package cn.chen.compiler;

import cn.chen.Main;
import cn.chen.entity.Constant;
import cn.chen.entity.Instruction;

/**
 * 生成虚拟机代码
 * Created by chen on 2016/11/28 0028.
 */
public class Gen {
    public void doGen(Constant.Operator operator,int frist ,int second){
        Instruction aimCode = new Instruction(operator,frist,second);
        Main.aimCode.add(aimCode);
    }
}
