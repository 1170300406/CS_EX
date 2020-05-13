package syntaxAnalysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Item {
    private int num;//产生式编号
    private int flag;//.的位置
    private String expect;//展望符
    public int visit = 0;//设置是否被访问过

    public Item(int num, int flag, String expect) {
        this.flag = flag;
        this.num = num;
        this.expect = expect;
    }

    public int getFlag() {
        return flag;
    }

    public int getNum() {
        return num;
    }

    public String getExpect() {
        return expect;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + num;
        result = prime * result + flag;
        result = prime * result + expect.hashCode();
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
        Item other = (Item) obj;
        if (num != other.num) {
            return false;
        }
        if (flag != other.flag) {
            return false;
        }
        if (!expect.equals(other.expect)) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Item item = new Item(2, 1, "$");
        Item item1 = new Item(1, 1, "$");
        HashSet<Item> items = new HashSet<>();
        items.add(item1);
        System.out.println(item.hashCode()==item1.hashCode());
        HashSet<String> h = new HashSet<>();
        h.add("ss");
        for (String a : h) {
            h.add("s");
        }

        String s[][] = new String[2][3];
        for(int i = 0;i<2;i++){
            for(int j = 0;j<3;j++){
                s[i][j] = "";
            }
        }
        s[1][2] = s[1][2]+"|e";
        for(int i = 0;i<2;i++){
            for(int j = 0;j<3;j++){
                System.out.print(s[i][j]+"\t");
            }
            System.out.println("");
        }

    }
}
