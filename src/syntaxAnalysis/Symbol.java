package syntaxAnalysis;

import java.util.ArrayList;
import java.util.List;

public class Symbol {
    int num;
    private String name;
    private char flag;//标志是否非终结符
    public List<String> first;
    public List<String> follow;

    public Symbol(int num, char flag, String name) {
        super();
        this.num = num;
        this.name = name;
        this.flag = flag;
        this.first = new ArrayList<String>();

        if (flag=='N') {
            this.follow = new ArrayList<String>();
        } else {
            this.first.add(name);
            this.follow = null;
        }
    }

    public int getNum() {
        return num;
    }

    public char getFlag() {
        return flag;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + num;
        result = prime * result + name.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Symbol other = (Symbol) obj;
        if (num != other.num) {
            return false;
        }
        if (name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
