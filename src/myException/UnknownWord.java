package myException;

public class UnknownWord extends Exception {

    private static final long serialVersionUID = 1L;

    public UnknownWord(String s) {
        super(s);
    }
}
