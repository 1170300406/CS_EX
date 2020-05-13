package syntaxAnalysis;

import java.util.List;


public class Production {
    private int num;
    private String left;
    private String[] right;
    public List<String> select;

    public Production() {
    }

    public Production(int num, String left) {
        this.num = num;
        this.left = left;
        this.right = new String[0];
    }

    public Production(int num, String left, String[] right) {
        this.num = num;
        this.left = left;
        this.right = right;
    }


    public String getLeft() {
        return left;
    }

    public String[] getRight() {
        return right;
    }

    public int getNum() {
        return num;
    }

    public List<String> getSelect() {
        return select;
    }

    @Override
    public String toString() {
        String ret = left + "  -->  ";
        if (right.length == 0)
            ret += " #";
        for (int i = 0; i < right.length; i++) {
            ret += "  " + right[i];
        }
        return ret;
    }
}
