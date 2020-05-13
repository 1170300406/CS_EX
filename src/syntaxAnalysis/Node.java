package syntaxAnalysis;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private String symbolName;    //符号名
    private String attribute;    //属性值
    private Node father;         //父节点
    private List<Node> sons;     //子节点
    private int linenum; //行号
    private int itemnum;
    private int id = -1;

    public Node(String symbolName, String attribute, int linenum) {
        this.symbolName = symbolName;
        this.attribute = attribute;
        this.linenum = linenum;
        this.father = null;
        this.sons = new ArrayList<>();
        itemnum = -1;
    }
    public void setFather(Node father){this.father = father;}

    public void addSons(Node son) {
        this.sons.add(0,son);
    }

    public Node getFather() {
        return this.father;
    }

    public List<Node> getSons() {
        return this.sons;
    }

    public String getSymbolName() {
        return this.symbolName;
    }

    public String getAttribute() {
        return this.attribute;
    }

    public int getLinenum(){ return this.linenum;}

    public void setLinenum(int linenum){ this.linenum = linenum;}

    public int getItemnum(){ return  this.itemnum;}

    public void setItemnum(int itemnum){this.itemnum = itemnum;}

    public void setid(int id){this.id = id;}

    public int getid(){return this.id;}
    @Override
    public String toString() {
        String str = symbolName;
        if (!attribute.equals("_")){
            str += " :" + attribute;
        }
        str += " ("+linenum+")";
        return str;
    }
}