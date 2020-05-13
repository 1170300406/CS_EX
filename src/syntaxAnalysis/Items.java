package syntaxAnalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Items {
    private int num;
    public ArrayList<Item> items;
    public HashMap<String, Integer> conversion = new HashMap<>();//项集族转移。
    public int visit = 0;

    public Items(int num, ArrayList<Item> items) {
        this.num = num;
        this.items = items;
    }

    public int getNum() {
        return num;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public HashMap<String, Integer> getConversion() {
        return conversion;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + num;
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
        Items other = (Items) obj;
        if (num != other.num) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Item item = new Item(1, 2, "sss");
        Item item1 = new Item(1, 2, "sss");
        ArrayList<Item> items1 = new ArrayList<>();
        ArrayList<Item> items2 = new ArrayList<>();
        items1.add(item);
        items2.add(item1);
        Items items3 = new Items(1, items1);
        Items items4 = new Items(1, items2);
        HashSet<Items> items = new HashSet<>();
        items.add(items3);
        items.add(items4);

        System.out.println(items3.equals(items4));
    }
}
