package myException;

public class IncompleteError extends Exception {

    private static final long serialVersionUID = 1L;

    public IncompleteError(String s) {
        super(s);
    }
}
