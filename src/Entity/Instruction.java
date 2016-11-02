package Entity;

/**
 * 代码的一行
 * Created by chen on 2016/10/28 0028.
 */
public class Instruction {
    public String operator;
    public int frist;
    public int second;
    /**
     * 一段代码
     *
     * @param operator 操作符
     * @param frist  第一个操作数
     * @param second   第二个操作数
     */
    public Instruction(String operator, int frist, int second) {
        this.operator = operator;
        this.frist = frist;
        this.second = second;
    }
}
