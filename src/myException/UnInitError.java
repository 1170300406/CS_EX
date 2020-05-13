package myException;

public class UnInitError extends Exception {

    private static final long serialVersionUID = 1L;

    public UnInitError(String s) {
        super(s);
    }
}