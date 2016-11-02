package cn.chen.entity;

/**
 * 名字表的定义
 * Created by chen on 2016/11/1 0001.
 */
public class TableStruct {
    public String name;//标识符
    public Constant.nameTable type;//标识符的类型
    public int level;//所在层
    public int addr;//地址
    public int size;//大小

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
        return ("类型:" + type + " 标识符:" + name + " 所在层" + level + " 相对地址" + addr + " 大小" + size);
    }
}
