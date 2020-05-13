package semanticAnalysis;

import lexicalAnalysis.LexicalAnalyser;
import myException.IncompleteError;
import myException.Not8Const;
import myException.UnknownWord;
import syntaxAnalysis.Node;
import syntaxAnalysis.SyntaxAnalysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class SemanticAnalysis {
    public ArrayList<Node> Stree = new ArrayList<Node>();  // 语法树
    public List<Properties> tree_pro = new ArrayList<Properties>();  // 语法树节点属性

    public List<Stack<Symbol>> table = new ArrayList<Stack<Symbol>>();  // 符号表
    public List<Integer> tablesize = new ArrayList<Integer>();  // 记录各个符号表大小

    public List<String> three_addr = new ArrayList<String>();  // 三地址指令序列
    public List<FourAddr> four_addr = new ArrayList<FourAddr>();  // 四元式指令序列
    public List<String> errors = new ArrayList<String>();  // 错误报告序列

    public String t;  // 类型
    public int w;  // 大小
    public int offset;  // 偏移量
    public int temp_cnt = 0;  // 新建变量计数
    public int nextquad = 1;  // 指令位置

    public List<String> queue = new ArrayList<String>();  // 过程调用参数队列
    public Stack<Integer> tblptr = new Stack<Integer>();  // 符号表指针栈
    public Stack<Integer> off = new Stack<Integer>();  //  符号表偏移大小栈

    public int initial = nextquad;

    public SemanticAnalysis() {

    }

    public SemanticAnalysis(String filename) throws Not8Const, UnknownWord, IncompleteError, IOException {
        LexicalAnalyser lexicalAnalyser = new LexicalAnalyser("data//conversionTable.txt", filename, "data//result2.txt");
        lexicalAnalyser.lexicalAnalyse();

        SyntaxAnalysis syntaxAnalysis = new SyntaxAnalysis();
        syntaxAnalysis.init();
        syntaxAnalysis.getFirst();
        syntaxAnalysis.getFollow();
        syntaxAnalysis.getSelect();
        syntaxAnalysis.buildItemFamily();
        System.out.println("");
        Node root = syntaxAnalysis.analyze(lexicalAnalyser.tokens);

        int size = RootToTree(root, 0);
        int tree_size = Stree.size();
        for (int i = 0; i < size; i++) {
            tree_pro.add(new Properties());
        }
        System.out.println(size);
        System.out.println(tree_size);
        dfs(root);
        Api.print_ins(three_addr, four_addr, initial);
        Api.print_table(table);
        Api.print_errors(errors);
    }

    public int RootToTree(Node node, int id) {
        node.setid(id);
        Stree.add(node);
        id++;
        if (node.getSons().isEmpty()) {
            return id;
        } else {
            for (int i = 0; i < node.getSons().size(); i++) {
                id = RootToTree(node.getSons().get(i), id);
            }
            return id;
        }
    }

    public List<Stack<Symbol>> getTable() {
        return this.table;
    }

    public List<String> getThree_addr() {
        return this.three_addr;
    }

    public List<String> getErrors() {
        return this.errors;
    }

    public List<FourAddr> getFour_addr() {
        return this.four_addr;
    }


    public void dfs(Node node) {
        List<Node> sons = node.getSons();
        for (int i = 0; i < sons.size(); i++) {
            dfs(sons.get(i));
            semantic(sons.get(i));
        }
    }

    /**
     * 向符号表中增加元素
     *
     * @param i      第i个符号表
     * @param name   元素名字
     * @param type   元素类型
     * @param offset 偏移量
     */
    public void enter(int i, String name, String type, int offset) {
        if (table.size() == 0) {
            table.add(new Stack<Symbol>());
        }
        Symbol s = new Symbol(name, type, offset);
        table.get(i).push(s);
    }

    /**
     * 查找符号表，查看变量是否存在
     *
     * @param s 名字
     * @return 该名字在符号表中的位置
     */
    public int[] lookup(String s) {
        int[] a = new int[2];
        for (int i = 0; i < table.size(); i++) {
            for (int j = 0; j < table.get(i).size(); j++) {
                if (table.get(i).get(j).getName().equals(s)) {
                    a[0] = i;
                    a[1] = j;
                    return a;
                }
            }
        }
        a[0] = -1;
        a[1] = -1;
        return a;
    }

    /**
     * 新建一个临时变量
     *
     * @return 新建变量名
     */
    public String newtemp() {
        return "t" + (++temp_cnt);
    }

    /**
     * 回填地址
     *
     * @param list 需要回填的指令序列
     * @param quad 回填的地址
     */
    public void backpatch(List<Integer> list, int quad) {
        for (int i = 0; i < list.size(); i++) {
            int x = list.get(i) - initial;
            three_addr.set(x, three_addr.get(x) + quad);
            four_addr.get(x).setToaddr(String.valueOf(quad));
        }
    }

    /**
     * 合并列表
     *
     * @param a 列表
     * @param b 列表
     * @return a与b合并后的列表
     */
    public static List<Integer> merge(List<Integer> a, List<Integer> b) {
        List<Integer> a1 = a;
        a1.addAll(b);
        return a1;
    }

    /**
     * 返回下一条指令地址
     *
     * @return 下一条指令地址
     */
    public int nextquad() {
        return three_addr.size() + nextquad;
    }

    /**
     * 新建包含i的列表并返回
     *
     * @param i
     * @return 列表
     */
    public static List<Integer> makelist(int i) {
        List<Integer> a1 = new ArrayList<Integer>();
        a1.add(i);
        return a1;
    }

    /**
     * 新增一个符号表
     */
    public void mktable() {
        table.add(new Stack<Symbol>());
    }


    public String arrayString(Array a) {
        String b = a.getBaseType();
        if (!b.equals("array")) {
            return "array" + "(" + a.getLength() + "," + b + ")";
        } else {
            return "array" + "(" + a.getLength() + "," + arrayString(a.getType()) + ")";
        }
    }

    public String elemType(String s) {
        if (!s.contains("array"))
            return "integer";

        int i;
        int len = s.length();
        for (i = 0; i < len; i++) {
            if (s.charAt(i) == ',') {
                break;
            }
        }
        return s.substring(i + 1, len - 1);
    }

    public int typeWidth(String s) {
        if (!s.contains("array"))
            return 4;

        int i, j = 0;
        int len = s.length();
        for (i = 0; i < len; i++) {
            if (s.charAt(i) == '(') {
                j = i;
            }
            if (s.charAt(i) == ',') {
                break;
            }
        }
        return Integer.valueOf(s.substring(j + 1, i));
    }

    //P -> proc id ; M0 begin D S end  {addwidth(top(tblptr),top(offset));pop(tblptr);pop(offset)}
    public void semantic_1(Node node) {
        tblptr.pop();
        off.pop();
    }

    // S -> S1 M S2  {backpatch(S1.nextlist,M.quad); S.nextlist=S2.nextlist;}
    public void semantic_3(Node node) {
        backpatch(tree_pro.get(node.getSons().get(0).getid()).getNext(), tree_pro.get(node.getSons().get(1).getid()).getQuad());
        Properties a1 = new Properties();
        a1.setNext(tree_pro.get(node.getSons().get(2).getid()).getNext());
        tree_pro.set(node.getid(), a1);
    }


    //D -> T id ; {enter(top(tblptr),id.name,T.type,top(offset));
    //                  top(offset) = top(offset)+T.width}
    public void semantic_5(Node node) {
        int t = node.getSons().get(0).getid();
        String id = node.getSons().get(1).getAttribute();
        int[] i = lookup(id);
        if (i[0] == -1) {
            enter(tblptr.peek(), id, tree_pro.get(t).getType(), off.peek());
            int s = off.pop();
            off.push(s + tree_pro.get(t).getWidth());
            offset = offset + tree_pro.get(t).getWidth();
        } else {
            String s = "Error at Line [" + +node.getLinenum() +
                    "]:\t[" + "变量" + id + "重复声明]";
            errors.add(s);
        }
    }

    // T -> X C {T.type=C.type; T.width=C.width;}
    public void semantic_6(Node node) {
        Properties a1 = new Properties();
        a1.setType(tree_pro.get(node.getSons().get(1).getid()).getType());
        a1.setWidth(tree_pro.get(node.getSons().get(1).getid()).getWidth());
        tree_pro.set(node.getid(), a1);
    }

    //T -> record N2 D end   {T.type=record(top(tblptr));
    //                       T.width=top(offset); pop(tblptr); pop(offset)}
    public void semantic_7(Node node) {
        Properties a1 = new Properties();
        a1.setType("record");
        a1.setWidth(off.pop());
        tblptr.pop();
        tree_pro.set(node.getid(), a1);
    }

    // X -> inte  {X.type=integer; X.width=4;}{t=X.type; w=X.width;}
    public void semantic_8(Node node) {
        t = "int";
        w = 4;

        Properties a1 = new Properties();
        a1.setType("int");
        a1.setWidth(4);
        tree_pro.set(node.getid(), a1);
    }

    // X -> real  {X.type=real; X.width=8;}{t=X.type; w=X.width;}
    public void semantic_9(Node node) {
        t = "real";
        w = 8;

        Properties a1 = new Properties();
        a1.setType("real");
        a1.setWidth(8);
        tree_pro.set(node.getid(), a1);
    }

    // C -> [ num ] C1  {C.type=array(num.val,C1.type); C.width=num.val*C1.width;}
    public void semantic_10(Node node) {
        int num = Integer.valueOf(node.getSons().get(1).getAttribute());
        Properties a1 = new Properties();
        Array a2 = new Array();

        a2.setLength(num);
        String type = tree_pro.get(node.getSons().get(3).getid()).getType();

        //若为多维数组
        if (type.startsWith("array")) {
            a2.setType(tree_pro.get(node.getSons().get(3).getid()).getArray());
            a2.setBaseType("array");
        } else {
            a2.setBaseType(type);
        }
        a1.setArray(a2);
        a1.setType(arrayString(a2));
        a1.setWidth(num * tree_pro.get(node.getSons().get(3).getid()).getWidth());
        tree_pro.set(node.getid(), a1);
    }

    // C -> ε  {C.type=t; C.width=w;}
    public void semantic_11(Node node) {
        Properties a1 = new Properties();
        a1.setType(t);
        a1.setWidth(w);
        tree_pro.set(node.getid(), a1);
    }

    //  S -> id = E ;  {p=lookup(id.lexeme); if p==nil then error;
    //                  gencode(p'='E.addr); S.nextlist=null}
    public void semantic_12(Node node) {
        String id = node.getSons().get(0).getAttribute();
        int[] i = lookup(id);
        if (i[0] == -1) {
            String s = "Error at Line [" + node.getSons().get(0).getLinenum() + "]:\t[" +
                    "变量" + id + "引用前未声明]";
            errors.add(s);
            enter(tblptr.peek(), id, "integer", offset);
            offset = offset + 4;
        }

        String three_code = id + "=" + tree_pro.get(node.getSons().get(2).getid()).getAddr();
        three_addr.add(three_code);
        four_addr.add(new FourAddr("=", tree_pro.get(node.getSons().get(2).getid()).getAddr(), "-", id));

        Properties a1 = new Properties();
        a1.setNext(new ArrayList<Integer>());
        tree_pro.set(node.getid(), a1);
    }

    //  S -> L = E ;  {gencode(L.array'['L.offset']''='E.addr); S.nextlist=null}
    public void semantic_13(Node node) {
        int L = node.getSons().get(0).getid();
        int E = node.getSons().get(2).getid();

        String code = tree_pro.get(L).getName() + "[" + tree_pro.get(L).getOffset()
                + "] = " + tree_pro.get(E).getAddr();
        three_addr.add(code);
        four_addr.add(new FourAddr("=", tree_pro.get(E).getAddr(), "-",
                tree_pro.get(L).getName() + "[" + tree_pro.get(L).getOffset() + "]"));

        Properties a1 = new Properties();
        a1.setNext(new ArrayList<Integer>());
        tree_pro.set(node.getid(), a1);
    }

    //  E -> E1 + E2  {E.addr=newtemp(); gencode(E.addr'='E1.addr'+'E2.addr);}
    public void semantic_14(Node node) {
        int E1 = node.getSons().get(0).getid();
        int E2 = node.getSons().get(2).getid();
        String newtemp1 = newtemp();

        //System.out.println(tree_pro.get(E1).getType() + " "+tree_pro.get(E2).getType());
        //运算类型相同
        if (tree_pro.get(E1).getType().equals(tree_pro.get(E2).getType()) &&
                (tree_pro.get(E1).getType().equals("real") || tree_pro.get(E1).getType().equals("int"))) {
            Properties a1 = new Properties();
            a1.setAddr(newtemp1);
            a1.setType(tree_pro.get(E1).getType());
            tree_pro.set(node.getid(), a1);

            String code = newtemp1 + " = " + tree_pro.get(E1).getAddr() +
                    "+" + tree_pro.get(E2).getAddr();
            three_addr.add(code);
            four_addr.add(new FourAddr("+", tree_pro.get(E1).getAddr(),
                    tree_pro.get(E2).getAddr(), newtemp1));
        } else if (!tree_pro.get(E1).getType().equals(tree_pro.get(E2).getType()) &&
                !tree_pro.get(E1).getType().contains("array") && !tree_pro.get(E2).getType().contains("array")) {
            //类型不相同但是不为数组类型变量
            String newtemp2 = newtemp();
            Properties a1 = new Properties();
            a1.setAddr(newtemp2);
            a1.setType("real");
            tree_pro.set(node.getid(), a1);

            int a = tree_pro.get(E1).getType().equals("real") ? E2 : E1;
            int b = tree_pro.get(E1).getType().equals("real") ? E1 : E2;
            String code1 = newtemp1 + " = IntToReal " + tree_pro.get(a).getAddr();
            String code2 = newtemp2 + " = " + newtemp1 + "+" + tree_pro.get(b).getAddr();
            three_addr.add(code1);
            three_addr.add(code2);
            four_addr.add(new FourAddr("=", "intTOreal" + tree_pro.get(a).getAddr(), "-", newtemp1));
            four_addr.add(new FourAddr("+", newtemp1, tree_pro.get(b).getAddr(), newtemp2));
        } else {
            //数组类型参与运算
            String newtemp2 = newtemp();
            Properties a1 = new Properties();
            a1.setAddr(newtemp2);
            a1.setType("int");
            tree_pro.set(node.getid(), a1);

            String s = "Error at Line [" + node.getSons().get(0).getLinenum() + "]:\t[" +
                    "整型变量与数组变量相加减]";
            errors.add(s);
        }
    }

    //  E -> E1  {E.addr=E1.addr}
    public void semantic_15(Node node) {
        Properties a1 = new Properties();
        a1.setAddr(tree_pro.get(node.getSons().get(0).getid()).getAddr());
        a1.setType(tree_pro.get(node.getSons().get(0).getid()).getType());
        tree_pro.set(node.getid(), a1);
    }

    //  E -> E1 * E2  {E.addr=newtemp(); gencode(E.addr'='E1.addr'*'E2.addr);}
    public void semantic_16(Node node) {
        String newtemp = newtemp();
        Properties a1 = new Properties();
        a1.setAddr(newtemp);
        tree_pro.set(node.getid(), a1);

        String code = newtemp + " = " + tree_pro.get(node.getSons().get(0).getid()).getAddr() +
                "*" + tree_pro.get(node.getSons().get(2).getid()).getAddr();
        three_addr.add(code);
        four_addr.add(new FourAddr("*", tree_pro.get(node.getSons().get(0).getid()).getAddr(),
                tree_pro.get(node.getSons().get(2).getid()).getAddr(), newtemp));
    }

    //  E -> ( E1 )  {E.addr=E1.addr}
    public void semantic_18(Node node) {
        int E1 = node.getSons().get(1).getid();
        tree_pro.get(node.getid()).setAddr(tree_pro.get(E1).getAddr());
        tree_pro.get(node.getid()).setType(tree_pro.get(E1).getType());
    }

    //  E -> - E1  {E.addr=newtemp(); gencode(E.addr'=''uminus'E1.addr);}
    public void semantic_19(Node node) {
        int E1 = node.getSons().get(0).getid();
        String newtemp = newtemp();
        tree_pro.get(node.getid()).setAddr(newtemp);
        tree_pro.get(node.getid()).setType(tree_pro.get(E1).getType());

        String code = newtemp + " = -" + tree_pro.get(E1).getAddr();
        three_addr.add(code);
        four_addr.add(new FourAddr("=", "-" + tree_pro.get(E1).getAddr(),
                "-", newtemp));
    }

    //  E -> id  {E.addr=lookup(id.lexeme); if E.addr==null then error;}
    public void semantic_20(Node node) {
        String id = node.getSons().get(0).getAttribute();
        int[] i = lookup(id);
        String Type;
        if (i[0] == -1) {
            errors.add("Error at Line [" + node.getSons().get(0).getLinenum() + "]:\t[" +
                    "变量" + id + "引用前未声明]");
            enter(tblptr.peek(), id, "integer", offset);
            offset = offset + 4;
            Type = "int";
            //创建变量
        } else {
            Type = table.get(i[0]).get(i[1]).getType();
        }
        tree_pro.get(node.getid()).setAddr(id);
        tree_pro.get(node.getid()).setType(Type);
    }

    //  E -> num  {E.addr = num}
    public void semantic_21(Node node) {
        String num = node.getSons().get(0).getAttribute();
        tree_pro.get(node.getid()).setAddr(num);
        tree_pro.get(node.getid()).setType("int");
    }

    //  E -> L {E.addr=newtemp(); gencode(E.addr'='L.array'['L.offset']');}
    public void semantic_22(Node node) {
        String newtemp = newtemp();
        tree_pro.get(node.getid()).setAddr(newtemp);
        tree_pro.get(node.getid()).setType("int");

        String code = newtemp + " = " + tree_pro.get(node.getSons().get(0).getid()).getName() +
                "[" + tree_pro.get(node.getSons().get(0).getid()).getOffset() + "] ";
        three_addr.add(code);
        four_addr.add(new FourAddr("=",
                tree_pro.get(node.getSons().get(0).getid()).getName() + "[" + tree_pro.get(node.getSons().get(0).getid()).getOffset() + "]", "-", newtemp));
    }

    //  L -> id [ E ] {L.array=lookup(id.lexeme); if L.array==nil then error;
    //  L.type=L.array.type.elem; L.offset=newtemp(); gencode(L.offset'='E.addr'*'L.type.width);}
    public void semantic_23(Node node) {
        String id = node.getSons().get(0).getAttribute();
        int[] i = lookup(id);
        String newtemp = newtemp();
        if (i[0] == -1) {
            String error = "Error at Line [" + node.getSons().get(0).getLinenum() + "]:\t[" +
                    "数组变量" + id + "引用前未声明]";
            errors.add(error);
            tree_pro.get(node.getid()).setName(id);
            tree_pro.get(node.getid()).setType("array(1,int)");
            tree_pro.get(node.getid()).setOffset(newtemp);

            String code = newtemp + " = " + 4;
            four_addr.add(new FourAddr("=", String.valueOf(4), "-", newtemp));
            three_addr.add(code);
            return;
        }
        if (!table.get(i[0]).get(i[1]).getType().contains("array")) {
            String s = "Error at Line [" + node.getSons().get(0).getLinenum() + "]:\t[" +
                    "非数组变量" + id + "访问数组]";
            errors.add(s);
        }

        String type = elemType(table.get(i[0]).get(i[1]).getType());
        tree_pro.get(node.getid()).setName(id);
        tree_pro.get(node.getid()).setType(type);
        tree_pro.get(node.getid()).setOffset(newtemp);

        //此时L的类型仍为数组
        if (type.contains("array")) {
            three_addr.add(newtemp + " = " + tree_pro.get(node.getSons().get(2).getid()).getAddr() + "*" + typeWidth(type));
            four_addr.add(new FourAddr("*", tree_pro.get(node.getSons().get(2).getid()).getAddr(),
                    String.valueOf(typeWidth(type)), newtemp));
        } else {
            three_addr.add(newtemp + " = " + tree_pro.get(node.getSons().get(2).getid()).getAddr());
            four_addr.add(new FourAddr("=", tree_pro.get(node.getSons().get(2).getid()).getAddr(), "-", newtemp));
        }
    }


    //  L -> L1 [ E ]  {L.array=L1.array; L.type=L1.type.elem; t=newtemp();
    //  gencode(t'='E.addr'*'L.type.width); L.offset=newtemp(); gencode(L.offset'='L1.offset'+'t);}
    public void semantic_24(Node node) {
        int L = node.getid();  // L
        int L1 = node.getSons().get(0).getid();  // L1
        int E = node.getSons().get(2).getid();  // E
        String newtemp1 = newtemp();
        String newtemp2 = newtemp();

        String type = elemType(tree_pro.get(L1).getType());
        tree_pro.get(L).setName(tree_pro.get(L1).getName());
        tree_pro.get(L).setType(type);
        tree_pro.get(L).setOffset(newtemp2);

        if (type.contains("array")) {
            three_addr.add(newtemp1 + " = " + tree_pro.get(E).getAddr() + "*" + typeWidth(type));
            four_addr.add(new FourAddr("*", tree_pro.get(E).getAddr(),
                    String.valueOf(typeWidth(type)), newtemp1));
        } else {
            three_addr.add(newtemp1 + " = " + tree_pro.get(E).getAddr() + "*" + w);
            four_addr.add(new FourAddr("*", tree_pro.get(E).getAddr(),
                    String.valueOf(w), newtemp1));
        }

        String code2 = newtemp2 + " = " + tree_pro.get(L1).getOffset() +
                "+" + newtemp1;
        three_addr.add(code2);
        four_addr.add(new FourAddr("+", tree_pro.get(L1).getOffset(), newtemp1, newtemp2));
    }

    //  B -> B1 or M B2  {backpatch(B1.falselist,M.quad);
    //                    B.truelist=merge(B1.truelist,B2.truelist);
    //                    B.falselist=B2.falselist}
    public void semantic_25(Node node) {
        backpatch(tree_pro.get(node.getSons().get(0).getid()).getFalse(), tree_pro.get(node.getSons().get(2).getid()).getQuad());
        tree_pro.get(node.getid()).setTrue(merge(tree_pro.get(node.getSons().get(0).getid()).getTrue(), tree_pro.get(node.getSons().get(3).getid()).getTrue()));
        tree_pro.get(node.getid()).setFalse(tree_pro.get(node.getSons().get(3).getid()).getFalse());
    }

    //  B -> B1  {B.truelist=B1.truelist; B.falselist=B1.falselist}
    public void semantic_26(Node node) {
        tree_pro.get(node.getid()).setTrue(tree_pro.get(node.getSons().get(0).getid()).getTrue());
        tree_pro.get(node.getid()).setFalse(tree_pro.get(node.getSons().get(0).getid()).getFalse());
    }

    //  B -> B1 and M B2  {backpatch(B1.truelist M.quad); B.truelist=B2.truelist;
    //                     B.falselist=merge(B1.falselist, B2.falselist)}
    public void semantic_27(Node node) {
        backpatch(tree_pro.get(node.getSons().get(0).getid()).getTrue(), tree_pro.get(node.getSons().get(2).getid()).getQuad());
        tree_pro.get(node.getid()).setTrue(tree_pro.get(node.getSons().get(3).getid()).getTrue());
        tree_pro.get(node.getid()).setFalse(
                merge(tree_pro.get(node.getSons().get(0).getid()).getFalse(), tree_pro.get(node.getSons().get(3).getid()).getFalse())
        );
    }


    //  B -> not B1  {B.truelist=B1.falselist; B.falselist=B1.truelist}
    public void semantic_29(Node node) {
        tree_pro.get(node.getid()).setTrue(tree_pro.get(node.getSons().get(0).getid()).getFalse());
        tree_pro.get(node.getid()).setFalse(tree_pro.get(node.getSons().get(0).getid()).getTrue());
    }

    //  B -> ( B1 )  {B.truelist := B1.truelist; B.falselist := B1.falselist}
    public void semantic_30(Node node) {
        tree_pro.get(node.getid()).setTrue(tree_pro.get(node.getSons().get(0).getid()).getTrue());
        tree_pro.get(node.getid()).setFalse(tree_pro.get(node.getSons().get(0).getid()).getFalse());
    }


    //  B -> E1 R E2  {B.truelist=makelist(nextquad); B.falselist= makelist(nextquad+1);
    //                gencode('if' E1.addr relop.op E2.addr 'goto –'); gencode('goto –')}
    public void semantic_31(Node node) {
        tree_pro.get(node.getid()).setTrue(makelist(nextquad()));
        tree_pro.get(node.getid()).setFalse(makelist(nextquad() + 1));

        int E1 = node.getSons().get(0).getid();
        int R = node.getSons().get(1).getid();
        int E2 = node.getSons().get(2).getid();
        String code1 = "if " + tree_pro.get(E1).getAddr() + tree_pro.get(R).getName()
                + tree_pro.get(E2).getAddr() + " goto ";
        three_addr.add(code1);
        four_addr.add(new FourAddr("j" + tree_pro.get(R).getName(),
                tree_pro.get(E1).getAddr(), tree_pro.get(E2).getAddr(), "-"));

        String code2 = "goto ";
        three_addr.add(code2);
        four_addr.add(new FourAddr("j", "-", "-", "-"));
    }


    //  B -> true  {B.truelist=makelist(nextquad); gencode('goto –')}
    public void semantic_32(Node node) {
        tree_pro.get(node.getid()).setTrue(makelist(nextquad()));
        String code = "goto ";
        three_addr.add(code);
        four_addr.add(new FourAddr("j", "-", "-", "-"));
    }

    //  B -> false  {B.falselist=makelist(nextquad); gencode('goto –')}
    public void semantic_33(Node node) {
        tree_pro.get(node.getid()).setFalse(makelist(nextquad()));
        String code = "goto ";
        three_addr.add(code);
        four_addr.add(new FourAddr("j", "-", "-", "-"));
    }

    //  R -> < | <= | == | != | > | >=  {R.name=op}
    public void semantic_34_to_39(Node node) {
        tree_pro.get(node.getid()).setName(node.getSons().get(0).getSymbolName());
    }

    //  S -> if B then M1 S1 N else M2 S2
    //  {backpatch(B.truelist, M1.quad); backpatch(B.falselist,M2.quad);
    //  S.nextlist=merge(S1.nextlist,merge(N.nextlist, S2.nextlist))}
    public void semantic_41(Node node) {
        int S = node.getid();
        int B = node.getSons().get(1).getid();
        int M1 = node.getSons().get(3).getid();  // M1
        int S1 = node.getSons().get(4).getid();  // S1
        int N = node.getSons().get(5).getid();  // N
        int M2 = node.getSons().get(7).getid();  // M2
        int S2 = node.getSons().get(8).getid();  // S2

        backpatch(tree_pro.get(B).getTrue(), tree_pro.get(M1).getQuad());
        backpatch(tree_pro.get(B).getFalse(), tree_pro.get(M2).getQuad());
        Properties a1 = new Properties();
        a1.setNext(merge(tree_pro.get(S1).getNext(),
                merge(tree_pro.get(N).getNext(), tree_pro.get(S2).getNext())));
        tree_pro.set(S, a1);
    }

    //  S -> while M1 B do M2 S1  {backpatch(S1.nextlist, M1.quad);
    //       backpatch(B.truelist,M2.quad); S.nextlist=B.falselist; gencode('goto'M1.quad)}
    public void semantic_42(Node node) {
        int S = node.getid();  // S
        int M1 = node.getSons().get(1).getid();  // M1
        int B = node.getSons().get(2).getid();  // B
        int M2 = node.getSons().get(4).getid();  // M2
        int S1 = node.getSons().get(5).getid();  // S1

        backpatch(tree_pro.get(S1).getNext(), tree_pro.get(M1).getQuad());
        backpatch(tree_pro.get(B).getTrue(), tree_pro.get(M2).getQuad());
        Properties a1 = new Properties();
        a1.setNext(tree_pro.get(B).getFalse());
        tree_pro.set(S, a1);

        String code = "goto " + tree_pro.get(M1).getQuad();
        three_addr.add(code);
        four_addr.add(new FourAddr("j", "-", "-", String.valueOf(tree_pro.get(M1).getQuad())));
    }

    //{t := mktable(nil); push(t, tblptr); push(0, offset)}
    //M0 -> ε {offset=0;}
    public void semantic_43(Node node) {
        mktable();
        int size = table.size() - 1;
        tblptr.push(size);
        off.push(0);
        offset = 0;
    }

    //  M -> ε  {M.quad=nextquad}
    public void semantic_44(Node node) {
        //System.out.println(node.getid());
        tree_pro.get(node.getid()).setQuad(nextquad());
    }

    //  MN -> ε  {N.nextlist=makelist(nextquad); gencode('goto –')}
    public void semantic_45(Node node) {
        tree_pro.get(node.getid()).setNext(makelist(nextquad()));
        String code = "goto ";
        three_addr.add(code);
        four_addr.add(new FourAddr("j", "-", "-", "-"));
    }

    //  S -> call id ( EL )
    //  {n=0; for queue中的每个t do {gencode('param't); n=n+1}
    //   gencode('call'id.addr','n);}
    public void semantic_46(Node node) {
        String id = node.getSons().get(1).getAttribute();
        int[] index = lookup(id);
        if (!table.get(index[0]).get(index[1]).getType().equals("函数")) {
            if (!table.get(index[0]).get(index[1]).getType().equals("函数")) {
                String s = "Error at Line [" + node.getSons().get(0).getLinenum()
                        + "]:\t[" + id + "不是函数,不能用于call语句]";
                errors.add(s);
                tree_pro.get(node.getid()).setNext(new ArrayList<Integer>());
                return;
            }
        }

        int size = queue.size();
        for (int i = 0; i < size; i++) {
            String code = "param " + queue.get(i);
            three_addr.add(code);
            four_addr.add(new FourAddr("param", "-", "-", queue.get(i)));
        }
        String code = "call " + id + " " + size;
        three_addr.add(code);
        four_addr.add(new FourAddr("call", String.valueOf(size), "-", id));
        Properties a1 = new Properties();
        a1.setNext(new ArrayList<Integer>());
        tree_pro.set(node.getid(), a1);
    }


    //  EL -> EL , E  {将E.addr添加到queue的队尾}
    public void semantic_47(Node node) {
        queue.add(tree_pro.get(node.getSons().get(2).getid()).getAddr());
    }

    //  EL -> E  {初始化queue,然后将E.addr加入到queue的队尾}
    public void semantic_48(Node node) {
        queue.clear();
        queue.add(tree_pro.get(node.getSons().get(0).getid()).getAddr());
    }

    //  D -> proc id; N1 D S
    // {t=top(tblptr); addwidth(t, top(offset));
    //  pop(tblptr); pop(offset); enterproc(top(tblptr), id.name,t)}
    public void semantic_49(Node node) {
        String id = node.getSons().get(1).getAttribute();
        int t = tblptr.peek();
        //tablesize.add(off.peek());
        tblptr.pop();
        off.pop();

        enter(tblptr.peek(), id, "函数", t);
    }

    //  N1 -> ε {t:= mktable(top(tblptr)); push(t, tblptr); push(0, offset)}
    public void semantic_50(Node node) {
        mktable();
        int size = table.size() - 1;
        tblptr.push(size);
        off.push(0);
    }

    //  N2 -> ε {t:= mktable(nil); push(t, tblptr); push(0, offset)}
    public void semantic_51(Node node) {
        mktable();
        int size = table.size() - 1;
        tblptr.push(size);
        off.push(0);
    }

    public void semantic(Node node) {
        switch (node.getItemnum()) {
            case 1:
                semantic_1(node);
                break;
            case 3:
                semantic_3(node);
                break;
            case 5:
                semantic_5(node);
                break;
            case 6:
                semantic_6(node);
                break;
            case 7:
                semantic_7(node);
                break;
            case 8:
                semantic_8(node);
                break;
            case 9:
                semantic_9(node);
                break;
            case 10:
                semantic_10(node);
                break;
            case 11:
                semantic_11(node);
                break;
            case 12:
                semantic_12(node);
                break;
            case 13:
                semantic_13(node);
                break;
            case 14:
                semantic_14(node);
                break;
            case 15:
                semantic_15(node);
                break;
            case 17:
                semantic_15(node);
                break;
            case 16:
                semantic_16(node);
                break;
            case 18:
                semantic_18(node);
                break;
            case 19:
                semantic_19(node);
                break;
            case 20:
                semantic_20(node);
                break;
            case 21:
                semantic_21(node);
                break;
            case 22:
                semantic_22(node);
                break;
            case 23:
                semantic_23(node);
                break;
            case 24:
                semantic_24(node);
                break;
            case 25:
                semantic_25(node);
                break;
            case 26:
                semantic_26(node);
                break;
            case 28:
                semantic_26(node);
                break;
            case 27:
                semantic_27(node);
                break;
            case 29:
                semantic_29(node);
                break;
            case 30:
                semantic_30(node);
                break;
            case 31:
                semantic_31(node);
                break;
            case 32:
                semantic_32(node);
                break;
            case 33:
                semantic_33(node);
                break;
            case 34:
                semantic_34_to_39(node);
                break;
            case 35:
                semantic_34_to_39(node);
                break;
            case 36:
                semantic_34_to_39(node);
                break;
            case 37:
                semantic_34_to_39(node);
                break;
            case 38:
                semantic_34_to_39(node);
                break;
            case 39:
                semantic_34_to_39(node);
                break;
            case 40:
                semantic_41(node);
                break;
            case 41:
                semantic_42(node);
                break;
            case 42:
                semantic_43(node);
                break;
            case 43:
                semantic_44(node);
                break;
            case 44:
                semantic_45(node);
                break;
            case 45:
                semantic_46(node);
                break;
            case 46:
                semantic_47(node);
                break;
            case 47:
                semantic_48(node);
                break;
            case 48:
                semantic_49(node);
                break;
            case 49:
                semantic_50(node);
                break;
            case 50:
                semantic_51(node);
                break;
            default:
                break;
        }

    }

    public static void main(String[] args) {
        try {
            SemanticAnalysis semanticAnalysis = new SemanticAnalysis("data//input2.txt");

        } catch (Not8Const not8Const) {
            not8Const.printStackTrace();
        } catch (UnknownWord unknownWord) {
            unknownWord.printStackTrace();
        } catch (IncompleteError incompleteError) {
            incompleteError.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
