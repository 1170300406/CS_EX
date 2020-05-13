package syntaxAnalysis;

import lexicalAnalysis.LexicalAnalyser;
import lexicalAnalysis.Token;
import myException.IncompleteError;
import myException.Not8Const;
import myException.UnknownWord;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.*;
import java.util.*;

public class SyntaxAnalysis {
    public HashSet<Symbol> symbols = new HashSet<>();
    private ArrayList<Production> productions = new ArrayList<>();
    private HashMap<Integer, Integer> visits = new HashMap<>();//记录产生式是否被使用过
    public HashSet<Items> items_set = new HashSet<>();//项集族的集合
    public ArrayList<String> symbol_list = new ArrayList<>();
    public ArrayList<String> non_symbol_list = new ArrayList<>();
    public ArrayList<String> error_message = new ArrayList<>();
    public Node root_node = null;


    /**
     * 初始化，读入文法。
     */
    public void init() throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data//production.txt"))));
//        String temp[] = bf.readLine().split("\\s+");
//        for (int j = 1; j < temp.length; j++) {
//            symbols.add(new Symbol(j - 1, 'N', temp[j]));
//        }
//        int length = symbols.size();
//        String temp1[] = bf.readLine().split("\\s+");
//        for (int i = 1; i < temp1.length; i++) {
//            symbols.add(new Symbol(length + i - 1, 'T', temp1[i]));
//        }
//        bf.readLine();
        //
        String temp3;
        int num = 0;
        while ((temp3 = bf.readLine()) != null) {
            String temp4[] = temp3.split("\\s+");
//            int num = Integer.valueOf(temp4[0]).intValue();
            String left = temp4[0];
            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 2; j < temp4.length; j++) {
                stringBuilder.append(temp4[j] + " ");
            }
            String[] right = stringBuilder.toString().split("\\s+");
            Production production;
            if (!right[0].equals("")) {
                production = new Production(num, left, right);
            } else {
                production = new Production(num, left, new String[0]);
            }
            productions.add(production);
            visits.put(num, 0);
            num++;
        }
        //
        HashSet<String> non_symbols = new HashSet<>();
        for (Production production : productions) {
            if (!non_symbols.contains(production.getLeft())) {
                non_symbols.add(production.getLeft());
            }
        }
        int flag_symbol = 0;//记录符号标号
        for (String s : non_symbols) {
            symbols.add(new Symbol(flag_symbol, 'N', s));
            flag_symbol++;
        }
        HashSet<String> t_symbols = new HashSet<>();
        for (Production production : productions) {
            for (int i = 0; i < production.getRight().length; i++) {
                if ((!non_symbols.contains(production.getRight()[i])) && (!t_symbols.contains(production.getRight()[i]))) {
                    t_symbols.add(production.getRight()[i]);
                }
            }
        }
        for (String s : t_symbols) {
            symbols.add(new Symbol(flag_symbol, 'T', s));
            flag_symbol++;
        }
        symbols.add(new Symbol(flag_symbol, 'T', "$"));
        System.out.println("");

        for (int i = 0; i < symbols.size(); i++) {
            symbol_list.add(getSymbolN(i).getName());
        }
        for (int i = 0; i < symbol_list.size(); i++) {
            if (getSymbol(symbol_list.get(i)).getFlag() == 'N') {
                non_symbol_list.add(symbol_list.get(i));
            }
        }
    }

    /**
     * 得到每个字符的First集。
     */
    public void getFirst() {
        boolean flag = true;
        while (flag) {
            for (Production production : productions) {
                String left = production.getLeft();
                String[] right = production.getRight();
                Symbol left_symbol = getSymbol(left);
                if (right.length == 0 && !left_symbol.first.contains("ε")) {
                    left_symbol.first.add("ε");
                }
                boolean is_null = true;
                for (int i = 0; i < right.length; i++) {
                    Symbol right_symbol = getSymbol(right[i]);
                    for (String element : right_symbol.first) {
                        if (!left_symbol.first.contains(element)) {
                            left_symbol.first.add(element);
                            flag = false;
                        }
                    }
                    boolean temp = canBeBlank(right[i]);
                    for (Integer j : visits.keySet()) {
                        visits.put(j, 0);
                    }
                    if (right_symbol.first.contains("ε") && is_null == true) {
                        is_null = true;
                    } else {
                        is_null = false;
                    }
                    if (i == right.length - 1 && is_null == true && !left_symbol.first.contains("ε")) {
                        right_symbol.first.add("ε");
                    }
                    if (!temp) {
                        break;
                    }
                }
            }
            flag = !flag;
        }
    }

    /**
     * 得到每个非终结符的follow集。
     */
    public void getFollow() {
        getSymbol("Program").follow.add("$");
        int flag = 0;
        int counts = 0;
        do {
            if (flag == 1) {
                counts = 0;
            }
            flag = 0;
            counts++;
            for (Production production : productions) {
                String left = production.getLeft();
                String[] right = production.getRight();
                if (right.length == 0) {
                    continue;
                }
                Symbol left_symbol = getSymbol(left);
                for (int i = 0; i < right.length - 1; i++) {
                    Symbol right_symbol = getSymbol(right[i]);
                    if (right_symbol.getFlag() == 'T') {
                        continue;
                    }
                    Symbol follow_symbol = getSymbol(right[i + 1]);
                    for (String element : follow_symbol.first) {
                        if (!right_symbol.follow.contains(element)) {
                            right_symbol.follow.add(element);
                            flag = 1;
                        }
                    }
                    int flag_blank = 0;
                    for (int j = i + 1; j < right.length; j++) {
                        boolean temp = canBeBlank(right[j]);
                        for (Integer k : visits.keySet()) {
                            visits.put(k, 0);
                        }
                        if (temp) {
                            if (j + 1 < right.length) {
                                Symbol rr_symbol = getSymbol(right[j + 1]);
                                for (String element : rr_symbol.first) {
                                    if (!right_symbol.follow.contains(element)) {
                                        right_symbol.follow.add(element);
                                        flag = 1;
                                    }
                                }
                            }
                        } else {
                            flag_blank = 1;
                            break;
                        }
                    }
                    //当right_symbol后非终结符均可空时，将左部follow集加入其中
                    if (flag_blank == 0) {
                        for (String element : left_symbol.follow) {
                            if (!right_symbol.follow.contains(element)) {
                                right_symbol.follow.add(element);
                                flag = 1;
                            }
                        }
                    }
                }
                Symbol last_symbol = getSymbol(right[right.length - 1]);
                if (last_symbol.getFlag() == 'T') {
                    continue;
                }
                for (String element : left_symbol.follow) {
                    if (!last_symbol.follow.contains(element)) {
                        last_symbol.follow.add(element);
                        flag = 1;
                    }
                }
            }
        } while (!(flag == 0 && counts == 2));
        for (Symbol s : symbols) {
            if (s.getFlag() == 'N') {
                if (s.follow.contains("ε")) {
                    s.follow.remove("ε");
                }
            }
        }
    }

    /**
     * 得到文法每条产生式的select集。
     */
    public void getSelect() {
        for (Production production : productions) {
            if (production.getRight().length == 0) {
                ArrayList<String> select = new ArrayList<>();
                List<String> follow = getSymbol(production.getLeft()).follow;
                for (int i = 0; i < follow.size(); i++) {
                    select.add(follow.get(i));
                }
                production.select = select;
            } else {
                ArrayList<String> select = new ArrayList<>();
                String[] right = production.getRight();
                int flag_blank = 0;
                for (int i = 0; i < right.length; i++) {
                    List<String> first = getSymbol(right[i]).first;
                    for (String f : first) {
                        select.add(f);
                    }
                    boolean temp = canBeBlank(right[i]);
                    for (Integer k : visits.keySet()) {
                        visits.put(k, 0);
                    }
                    if (!temp) {
                        flag_blank = 1;
                        break;
                    }
                }
                if (flag_blank == 0) {
                    List<String> follow = getSymbol(production.getLeft()).follow;
                    for (String f : follow) {
                        if (!select.contains(f)) {
                            select.add(f);
                        }
                    }
                }
                production.select = select;
            }
        }
    }


    /**
     * 判断某个非终结符是否可空。
     *
     * @param name 非终结符的name。
     * @return 是否可空。
     */
    private boolean canBeBlank(String name) {
        for (int i = 0; i < this.productions.size(); i++) {
            Production temp = this.productions.get(i);
            if (temp.getLeft().equals(name) && visits.get(temp.getNum()) == 0) {
                visits.put(temp.getNum(), 1);
                String[] right = temp.getRight();
                if (right.length == 0) {
                    return true;
                }
                boolean flag = true;
                for (int j = 0; j < right.length; j++) {
                    if (!canBeBlank(right[j])) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 通过符号的name得到符号变量。
     *
     * @param name 符号name。
     * @return 符号变量
     */
    public Symbol getSymbol(String name) {
        for (Symbol symbol : symbols) {
            Symbol temp = symbol;
            if (temp.getName().equals(name)) {
                return temp;
            }
        }
        return null;
    }

    /**
     * 通过符号的num得到符号变量。
     *
     * @param num 符号的标号。
     * @return 符号变量
     */
    Symbol getSymbolN(int num) {
        for (Symbol symbol : symbols) {
            Symbol temp = symbol;
            if (temp.getNum() == num) {
                return temp;
            }
        }
        return null;
    }

    /**
     * 求某个项目的项集族闭包。
     *
     * @param items 初始的项目集闭包。
     * @return 该项目的项集族闭包。
     */
    public HashSet<Item> closure(HashSet<Item> items) {
        while (true) {
            HashSet<Item> temps = new HashSet<>();
            for (Item item : items) {
                if (item.visit == 0) {
                    item.visit = 1;
                    if (productions.get(item.getNum()).getRight().length != item.getFlag()) {
                        for (Production production : productions) {
                            if (production.getLeft().equals(productions.get(item.getNum()).getRight()[item.getFlag()])) {
                                if (item.getFlag() + 1 != productions.get(item.getNum()).getRight().length) {
                                    int b_all_flag = 0;
                                    for (int j = 0; j < productions.get(item.getNum()).getRight().length - item.getFlag()-1; j++) {
                                        int b_flag = 0;
                                        for (String s : getSymbol(productions.get(item.getNum()).getRight()[item.getFlag() + 1 + j]).first) {
                                            if (s != "ε") {
                                                Item item1 = new Item(production.getNum(), 0, s);
                                                temps.add(item1);
                                            }else {
                                                b_flag = 1;
                                            }
                                        }
                                        if(b_flag==0){
                                            break;
                                        }
                                        if(b_flag==1 && j == productions.get(item.getNum()).getRight().length - item.getFlag()-2){
                                            b_all_flag = 1;
                                        }
                                    }

                                    if (b_all_flag==1) {
                                        Item item2 = new Item(production.getNum(), 0, item.getExpect());
                                        temps.add(item2);
                                    }


                                } else {
                                    Item item2 = new Item(production.getNum(), 0, item.getExpect());
                                    temps.add(item2);
                                }
                            }
                        }
                    } else {
                        continue;
                    }
                }
            }
            for (Item item1 : temps) {
                items.add(item1);
            }

            int flag = 0;
            for (Item item1 : items) {
                if (item1.visit == 0) {
                    flag = 1;
                }
            }
            if (flag == 0) {
                break;
            }
        }
        return items;
    }

    /**
     * 项集族转移函数。
     *
     * @param items 当前项集族。
     * @param x     遇到符号x。
     * @return 转移到的项集族。
     */
    public HashSet<Item> GOTO(Items items, String x) {
        HashSet<Item> items1 = new HashSet<>();
        for (Item item : items.getItems()) {
            if (productions.get(item.getNum()).getRight().length != item.getFlag()) {
                if (productions.get(item.getNum()).getRight()[item.getFlag()].equals(x)) {
                    Item item1 = new Item(item.getNum(), item.getFlag() + 1, item.getExpect());
                    items1.add(item1);
                }
            }
        }
        return closure(items1);
    }

    /**
     * 构造lr(1)项集族转移图。
     */
    public void buildItemFamily() {
        HashSet<Item> items = new HashSet<>();
        Item item = new Item(0, 0, "$");
        items.add(item);
        int num = 0;
        closure(items);
        ArrayList<Item> items1 = new ArrayList<>();
        for (Item i : items) {
            items1.add(i);
        }
        Items items2 = new Items(num, items1);
        items_set.add(items2);
        num++;
        while (true) {
            ArrayList<HashSet<Items>> temps = new ArrayList<>();
            for (Items items3 : items_set) {
                if (items3.visit == 0) {
                    items3.visit = 1;
                    HashSet<Items> temps1 = new HashSet<>();
                    for (Symbol symbol : symbols) {
                        HashSet<Item> items4 = GOTO(items3, symbol.getName());
                        if (items4.size() != 0) {
                            ArrayList<Item> items5 = new ArrayList<>();
                            for (Item i : items4) {
                                items5.add(i);
                            }
                            //判断闭包集是否有过已经
                            int flag = 0;
                            int to_num = -1;
                            for (Items items6 : items_set) {
                                for (int i = 0; i < items6.getItems().size(); i++) {
                                    if (i < items6.getItems().size() - 1 && items5.contains(items6.getItems().get(i))) {
                                        continue;
                                    } else if (i == items6.getItems().size() - 1 && items5.contains(items6.getItems().get(i))) {
                                        if (items5.size() == items6.getItems().size()) {
                                            flag = 1;
                                            to_num = items6.getNum();
                                            break;
                                        }
                                    } else if (!items5.contains(items6.getItems().get(i))) {
                                        break;
                                    }
                                }
                            }
                            if (flag == 1) {
                                items3.conversion.put(symbol.getName(), to_num);
                                continue;
                            }
                            items3.conversion.put(symbol.getName(), num);
                            Items items6 = new Items(num, items5);
                            temps1.add(items6);
                            num++;
                        }
                    }
                    temps.add(temps1);
                }
            }
            for (HashSet<Items> temps2 : temps) {
                for (Items t : temps2) {
                    items_set.add(t);
                }
            }
            int flag = 0;
            for (Items items3 : items_set) {
                if (items3.visit == 0) {
                    flag = 1;
                }
            }
            if (flag == 0) {
                break;
            }
        }
    }


    /**
     * 根据项集族转移图，导出action和goto表。
     *
     * @return
     */
    public String[][] get_action_go() {
        String tables[][] = new String[items_set.size()][symbols.size()];

        for (int i = 0; i < items_set.size(); i++) {
            for (int j = 0; j < symbols.size(); j++) {
                tables[i][j] = "";
            }
        }
        for (Items items : items_set) {
            for (String s : items.getConversion().keySet()) {
                for (Symbol symbol : symbols) {
                    if (symbol.getName().equals(s)) {
                        if (symbol.getFlag() == 'N') {
                            tables[items.getNum()][symbol.getNum()] = String.valueOf(items.getConversion().get(s));
                        } else {
                            tables[items.getNum()][symbol.getNum()] = "s" + String.valueOf(items.getConversion().get(s));
                        }
                        break;
                    }
                }
            }
            for (Item item1 : items.getItems()) {
                if (item1.getFlag() == productions.get(item1.getNum()).getRight().length) {
                    for (Symbol symbol : symbols) {
                        if (symbol.getName().equals(item1.getExpect())) {
                            if (item1.getNum() == 0) {
                                tables[items.getNum()][symbol.getNum()] = "acc";
//                                if (tables[items.getNum()][symbol.getNum()].equals("")) {
//                                    tables[items.getNum()][symbol.getNum()] = tables[items.getNum()][symbol.getNum()] + "acc";
//                                } else {
//                                    tables[items.getNum()][symbol.getNum()] = tables[items.getNum()][symbol.getNum()] + "|acc";
//                                }
                            } else {
                                tables[items.getNum()][symbol.getNum()] = "r" + item1.getNum();
//                                if (tables[items.getNum()][symbol.getNum()].equals("")) {
//                                    tables[items.getNum()][symbol.getNum()] = tables[items.getNum()][symbol.getNum()] + "r" + item1.getNum();
//                                } else {
//                                    tables[items.getNum()][symbol.getNum()] = tables[items.getNum()][symbol.getNum()] + "|r" + item1.getNum();
//                                }
                            }
                        }
                    }
                }
            }
        }
        return tables;
    }

    public void addNode(Node father_node, DefaultMutableTreeNode father) {
        List<Node> sons = father_node.getSons();
        for (int i = 0; i < sons.size(); i++) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(sons.get(i).toString());
            father.add(node);
            addNode(sons.get(i), node);
        }
    }

    /**
     * 利用导出的action和goto表对Token序列做语法分析。
     *
     * @param tokens Token序列。
     * @return 分析结果生成的树形图的根节点。
     */
    public Node analyze(ArrayList<Token> tokens) throws IOException {
        Stack<Node> symbol_stack = new Stack<>();
        Stack<Integer> state_stack = new Stack<>();
        state_stack.push(0);
        Node stack_button = new Node("#", "", -1);
        symbol_stack.push(stack_button);
        int state = 0;//用来记录action表的状态
        int index = 0;
        List<Node> nodeList = ToNode(tokens);
        int length = nodeList.size();
        String tables[][] = get_action_go();//获取action表，goto表
        int column;

        while (!state_stack.isEmpty()) {
            state = state_stack.peek();
            String input = "";
            Node node = null;
            if (index >= length) {
                input = "$";
                node = new Node("$", "_", nodeList.size());
                // column = getColumn("$");
            } else {
                node = nodeList.get(index);
                input = node.getSymbolName();
                // column = getColumn(node.getSymbolName());
            }
//            System.out.println(input);
            column = getColumn(input);
            if (tables[state][column] == "") {
                String message = "Error at Line [" + node.getLinenum() + "]:";
                int pop_num = 0;
                state = state_stack.peek();
                int num = 0;
                int linenum = -1;
                while (true) {
                    String tem_str = "";

                    while ((column = Isfinal(state, num, tables)) == -1) {
                        //System.out.println(state);
                        state_stack.pop();
                        pop_num++;
                        linenum = symbol_stack.peek().getLinenum();
                        tem_str = symbol_stack.peek().toString() + " " + tem_str;
                        symbol_stack.pop();
                        num = 0;
                        state = state_stack.peek();
                    }

                    //System.out.println(symbol_list.get(column));
                    num++;
                    //System.out.println(column);
                    Node father_node = new Node(symbol_list.get(column), "", linenum);
                    int next_state = Integer.valueOf(tables[state][column]);
                    int tem_index = index;
                    for (; tem_index < length; tem_index++) {
//                        System.out.println(nodeList.get(tem_index).getSymbolName());
                        column = getColumn(nodeList.get(tem_index).getSymbolName());
                        if (tables[next_state][column] != "") {
                            break;
                        }
                        tem_str = tem_str + " " + nodeList.get(tem_index).toString();
                    }
                    if (tem_index >= length) {
                        column = getColumn("$");
                        if (tables[next_state][column] != "") {
                            father_node.setLinenum(linenum);
                            state_stack.push(next_state);
                            symbol_stack.push(father_node);
                            index = tem_index;
                            tem_str = father_node.toString() + " -> " + tem_str;

                            message = message + tem_str;
                            error_message.add(message);
                            break;
                        }
                    } else {
                        father_node.setLinenum(linenum);
                        state_stack.push(next_state);
                        symbol_stack.push(father_node);
                        if (pop_num == 0 && index == tem_index) {
                            break;
                        }
                        index = tem_index;
                        tem_str = father_node.toString() + " -> " + tem_str;

                        message = message + tem_str;
                        error_message.add(message);
                        break;
                    }
                }
            } else if (tables[state][column].equals("acc")) {
                //分析完成，退出程序
                Node root = new Node("program", "", 1);
                while (!symbol_stack.isEmpty()) {
                    node = symbol_stack.pop();
                    if (!symbol_stack.isEmpty()) {
                        root.addSons(node);
                    }
                }
                StringBuilder stringBuilder = new StringBuilder();
                PreOrder(root, 1, stringBuilder);
                root_node = root;
                DefaultMutableTreeNode father = new DefaultMutableTreeNode(root.toString());
                addNode(root, father);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/result3.txt")));
                bw.write(stringBuilder.toString());
                bw.close();
                return root_node;
            } else if (tables[state][column].charAt(0) == 's') {
                //移进处理
                int next_state = Integer.valueOf(tables[state][column].substring(1));
                state_stack.push(next_state);
                symbol_stack.push(nodeList.get(index));
                index++;
            } else if (tables[state][column].charAt(0) == 'r') {
                //规约处理
                int num = Integer.valueOf(tables[state][column].substring(1));//产生式编号
                Production production = productions.get(num);//获取对应产生式
                Node father_node = new Node(production.getLeft(), "", -1);
                father_node.setItemnum(num);
                num = production.getRight().length;
                for (int i = 0; i < num; i++) {
                    //从栈顶弹出
                    Node son_node = symbol_stack.pop();
                    state_stack.pop();
                    son_node.setFather(father_node);
                    father_node.addSons(son_node);
                    if (i == num - 1) {
                        father_node.setLinenum(son_node.getLinenum());
                    }
                }

                state = state_stack.peek();
                column = getColumn(production.getLeft());
                if (tables[state][column] != "") {
                    state_stack.push(Integer.valueOf(tables[state][column]));
                    symbol_stack.push(father_node);
                } else {
                    System.out.println("出错" + "state:" + state + "符号" + production.getLeft());
                    break;
                }
            } else {
                System.out.println("出错" + "state:" + state + "列号" + column);
                break;
            }
        }
        return null;
    }

    public int getColumn(String str) {
        int i = 0;
        for (; i < symbol_list.size(); i++) {
            if (symbol_list.get(i).equals(str)) {
                return i;
            }
        }
        return -1;
    }

    //将token序列转化为node序列
    public List<Node> ToNode(ArrayList<Token> tokens) {
        List<Node> nodeList = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i++) {
            Node node = new Node(tokens.get(i).getSeedCode(), tokens.get(i).getAttributeValue(), tokens.get(i).getLineNum());
            nodeList.add(node);
        }
        return nodeList;
    }

    //先序输出语法分析树
    public void PreOrder(Node node, int num, StringBuilder stringBuilder) {
        String str = "";
        for (int i = 0; i < num; i++) {
            str += "  ";
        }
        str += node.toString();
        stringBuilder.append(str + '\n');
        List<Node> sons = node.getSons();
        for (int i = 0; i < sons.size(); i++) {
            PreOrder(sons.get(i), num + 1, stringBuilder);
        }
    }

    //判断是当前状态是否有可接受的非终结符
    public int Isfinal(int state, int num, String[][] tables) {
        int column;
        int k = 0;
        for (int i = 0; i < non_symbol_list.size(); i++) {
            column = getColumn(non_symbol_list.get(i));
            if (tables[state][column] != "") {
                if (k >= num) {
                    return column;
                }
                k++;
            }
        }
        return -1;
    }

    public static void main(String[] args) throws Not8Const, UnknownWord, IncompleteError, IOException {
        LexicalAnalyser lexicalAnalyser = new LexicalAnalyser("data//conversionTable.txt", "data//input2.txt", "data//result2.txt");
        lexicalAnalyser.lexicalAnalyse();

        SyntaxAnalysis syntaxAnalysis = new SyntaxAnalysis();
        syntaxAnalysis.init();
        syntaxAnalysis.getFirst();
        syntaxAnalysis.getFollow();
        syntaxAnalysis.getSelect();
        syntaxAnalysis.buildItemFamily();
        System.out.println("");
        syntaxAnalysis.analyze(lexicalAnalyser.tokens);


    }
}
