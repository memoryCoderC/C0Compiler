package Entity;

/**
 * Created by chen on 2016/11/1 0001.
 */
public class LexReturn<T> {
    public Constant.lexType wordLexType;
    public T value;
    public LexReturn(Constant.lexType wordLexType, T value) {
        this.wordLexType = wordLexType;
        this.value = value;
    }

    public static void main(String[] args) {
        LexReturn a = new LexReturn(Constant.lexType.CONSTANT,1);
        System.out.println(a.value);
    }
}
