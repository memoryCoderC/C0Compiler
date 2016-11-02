package cn.chen.entity;

/**
 * 代码的一行
 * Created by chen on 2016/10/28 0028.
 */
public class Instruction {
    public Constant.Operator operator;
    public int frist;
    public int second;
    /**
     * 一段代码
     *
     * @param operator 操作符
     * @param frist  第一个操作数
     * @param second   第二个操作数
     */
    public Instruction(Constant.Operator operator, int frist, int second) {
        this.operator = operator;
        this.frist = frist;
        this.second = second;
    }
    /**
     * 一段代码
     * @param operator 字符串类型操作符
     * @param frist  第一个操作数
     * @param second   第二个操作数
     */
    public Instruction(String operator, int frist, int second) {
        this.operator = Enum.valueOf(Constant.Operator.class, operator);
        this.frist = frist;
        this.second = second;
    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return operator+" "+frist+" "+second;
    }

    public static void main(String[] args) {
        System.out.println(new Instruction("LIT",1,1).operator);
    }
}
