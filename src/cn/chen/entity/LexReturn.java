package cn.chen.entity;

/**
 * Created by chen on 2016/11/1 0001.
 */
public class LexReturn<T> {
    public Constant.symbol wordLexType;
    public T value;
    public LexReturn(Constant.symbol wordLexType, T value) {
        System.out.println("单词类型"+wordLexType);
        System.out.println("单词值"+value);
        this.wordLexType = wordLexType;
        this.value = value;
    }

    public static void main(String[] args) {
        LexReturn a = new LexReturn(Constant.symbol.becomes,1);
        System.out.println(a.value);
    }
}
