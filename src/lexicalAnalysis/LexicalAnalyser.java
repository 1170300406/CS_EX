package lexicalAnalysis;

import myException.IncompleteError;
import myException.Not8Const;
import myException.UnknownWord;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class LexicalAnalyser {
    private static HashSet<String> KEY_WORDS = new HashSet<>();
    private static HashMap<String, String> OPERATORS = new HashMap<>();
    private static HashMap<String, String> BOUNDARY_SYMBOLS = new HashMap<>();
    public String input_path1;//存放dfa转换表
    public String input_path2;//存放输入代码段
    private HashSet<String> non_terminals = new HashSet<>();//非终结符集合
    private int[][] conversion_table;//dfa状态转换表
    private HashSet<Integer> end_states = new HashSet<>();//接收状态集合:状态1：接收无符号整形；状态3：接收无e浮点数；状态6：接收有e浮点数；状态7：接收标识符（需要判断是否是关键字）；状态11：接受注释；状态13：接受其他（需要判断是否是界符或运算符）。
    public ArrayList<Token> tokens = new ArrayList<>();
    public HashMap<Integer, ArrayList<Integer>> DFA_Seq = new HashMap<>();
    public String output_path;//输出结果文件
    public ArrayList<String> errorlist = new ArrayList<>();//存储错误信息的列表。

    public LexicalAnalyser() {
        String[] keyWords = {"for", "do", "while", "switch", "default", "not", "else", "and", "record", "if", "else", "case", "proc", "or", "false", "real", "then", "int", "call", "char", "true", "return","end","proc","begin","end," +
                "true","false","not"};
        for (String a : keyWords) {
            KEY_WORDS.add(a);
        }
        OPERATORS.put("+", "+");
//        OPERATORS.put("-", "MINUS");
        OPERATORS.put("*", "*");
//        OPERATORS.put("/", "DIV");
        OPERATORS.put(">", ">");
        OPERATORS.put("<", "<");
        OPERATORS.put("==", "==");
        OPERATORS.put(">=", ">=");
        OPERATORS.put("<=", "<=");
        OPERATORS.put("++", "++");
        OPERATORS.put("--", "--");
//        OPERATORS.put("+=", "PE");
//        OPERATORS.put("-=", "ME");
//        OPERATORS.put("*=", "MUE");
//        OPERATORS.put("/=", "DE");
//        OPERATORS.put("&&", "AND");
//        OPERATORS.put("&", "BA");
//        OPERATORS.put("||", "OR");
//        OPERATORS.put("|", "BOR");
//        OPERATORS.put("!", "NOT");
//        OPERATORS.put("!=", "!=");
        BOUNDARY_SYMBOLS.put("=", "=");
        BOUNDARY_SYMBOLS.put("{", "{");
        BOUNDARY_SYMBOLS.put("}", "}");
        BOUNDARY_SYMBOLS.put("[", "[");
        BOUNDARY_SYMBOLS.put("]", "]");
        BOUNDARY_SYMBOLS.put("(", "(");
        BOUNDARY_SYMBOLS.put(")", ")");
        BOUNDARY_SYMBOLS.put(";", ";");
        BOUNDARY_SYMBOLS.put(":", ":");
//        BOUNDARY_SYMBOLS.put(".", "FP");
        BOUNDARY_SYMBOLS.put(",", ",");
    }

    public LexicalAnalyser(String input_path1, String input_path2, String output_path) {
        this.input_path1 = input_path1;
        this.input_path2 = input_path2;
        this.output_path = output_path;
        String[] keyWords = {"for", "do", "while", "switch", "default", "not", "else", "and", "record", "if", "else", "case", "proc", "or", "false", "real", "then", "int", "call", "char", "true", "return","begin","end","true","false","not"};
        for (String a : keyWords) {
            KEY_WORDS.add(a);
        }
        OPERATORS.put("+", "+");
//        OPERATORS.put("-", "MINUS");
        OPERATORS.put("*", "*");
//        OPERATORS.put("/", "DIV");
        OPERATORS.put(">", ">");
        OPERATORS.put("<", "<");
        OPERATORS.put("==", "==");
        OPERATORS.put(">=", ">=");
        OPERATORS.put("<=", "<=");
        OPERATORS.put("++", "++");
        OPERATORS.put("--", "--");
//        OPERATORS.put("+=", "PE");
//        OPERATORS.put("-=", "ME");
//        OPERATORS.put("*=", "MUE");
//        OPERATORS.put("/=", "DE");
//        OPERATORS.put("&&", "AND");
//        OPERATORS.put("&", "BA");
//        OPERATORS.put("||", "OR");
//        OPERATORS.put("|", "BOR");
//        OPERATORS.put("!", "NOT");
//        OPERATORS.put("!=", "!=");
        BOUNDARY_SYMBOLS.put("=", "=");
        BOUNDARY_SYMBOLS.put("{", "{");
        BOUNDARY_SYMBOLS.put("}", "}");
        BOUNDARY_SYMBOLS.put("[", "[");
        BOUNDARY_SYMBOLS.put("]", "]");
        BOUNDARY_SYMBOLS.put("(", "(");
        BOUNDARY_SYMBOLS.put(")", ")");
        BOUNDARY_SYMBOLS.put(";", ";");
        BOUNDARY_SYMBOLS.put(":", ":");
//        BOUNDARY_SYMBOLS.put(".", "FP");
        BOUNDARY_SYMBOLS.put(",", ",");
    }


    public void lexicalAnalyse() throws Not8Const, UnknownWord, IncompleteError {
        readTable();
        ArrayList<Character> codes = readCodes();
        int flag = 0;
        int state = 0;//初始状态为0
        StringBuilder words = new StringBuilder();
        int lastReceiveState = 0;
        int lastFlag = 0;
        String lastWord = "";
        int lines1 = 1;
        int reverse_flag = 1;
        int counts = 0;
        ArrayList<Integer> dfa = new ArrayList<>();
        char temp = ' ';


        while (flag != codes.size()) {
            int wordFlag = -1;
            int eflag = 0;
            int xflag = 0;
            int sflag = 0;
            temp = codes.get(flag);

            if (temp == 'e') {
                wordFlag = 5;
                eflag = 1;
            }
            if (temp == 'x') {
                wordFlag = 29;
                xflag = 1;
            }
            if (temp >= 'a' && temp <= 'f' && eflag == 0) {
                wordFlag = 30;
                sflag = 1;
            }
            if (eflag == 0 && xflag == 0 && sflag == 0) {
                if (isLetter(temp)) {
                    wordFlag = 1;
                } else if (temp == '0') {
                    wordFlag = 0;
                } else if (temp == '8' || temp == '9') {
                    wordFlag = 28;
                } else if (isNumber(temp)) {
                    wordFlag = 31;
                } else if (temp == '_') {
                    wordFlag = 2;
                } else if (temp == '.') {
                    wordFlag = 3;
                } else if (temp == '/') {
                    wordFlag = 4;
                } else if (temp == '=') {
                    wordFlag = 6;
                } else if (temp == '{') {
                    wordFlag = 7;
                } else if (temp == '}') {
                    wordFlag = 8;
                } else if (temp == ';') {
                    wordFlag = 9;
                } else if (temp == '\\') {
                    wordFlag = 10;
                } else if (temp == '(') {
                    wordFlag = 11;
                } else if (temp == ')') {
                    wordFlag = 12;
                } else if (temp == '[') {
                    wordFlag = 13;
                } else if (temp == ']') {
                    wordFlag = 14;
                } else if (temp == ':') {
                    wordFlag = 15;
                } else if (temp == '\"') {
                    wordFlag = 16;
                } else if (temp == ',') {
                    wordFlag = 17;
                } else if (temp == '+') {
                    wordFlag = 18;
                } else if (temp == '-') {
                    wordFlag = 19;
                } else if (temp == '*') {
                    wordFlag = 20;
                } else if (temp == '>') {
                    wordFlag = 21;
                } else if (temp == '<') {
                    wordFlag = 22;
                } else if (temp == '\'') {
                    wordFlag = 23;
                } else if (temp == '&') {
                    wordFlag = 24;
                } else if (temp == '|') {
                    wordFlag = 25;
                } else if (temp == '!') {
                    wordFlag = 26;
                } else if (temp == ' ' | temp == '\n') {
                    wordFlag = 27;
                } else {
                    wordFlag = 31;
                    try {
                        throw new UnknownWord("代码第" + lines1 + "行，出现未定义的字符，请检查输入代码！");
                    } catch (UnknownWord e) {
                        errorlist.add(e.getMessage());
                        flag++;
                        continue;
                    }
                }
            }
            if (wordFlag != 32) {
                state = conversion_table[state][wordFlag];
            }
            if (state != 0) {
                dfa.add(state);
            }
            int flag1 = 0;

            if (state == 0 && lastReceiveState != 0) {
                flag1 = 1;
                String seedCode = "";
                String attributeValue = "";

                if (lastReceiveState == 1) {
                    seedCode = "num";
                    attributeValue = lastWord;
                } else if (lastReceiveState == 3 || lastReceiveState == 6 || lastReceiveState == 16) {
                    seedCode = "num";
                } else if (lastReceiveState == 17) {
                    seedCode = "num";
                    attributeValue = lastWord;
                } else if (lastReceiveState == 19) {
                    seedCode = "num";
                    attributeValue = lastWord;
                } else if (lastReceiveState == 10) {
                    if (KEY_WORDS.contains(lastWord)) {
                        seedCode = lastWord;
                        attributeValue = "_";
                    } else {
                        seedCode = "id";
                        attributeValue = lastWord;
                    }
                } else if (lastReceiveState == 9 || lastReceiveState == 34) {
                    seedCode = this.BOUNDARY_SYMBOLS.get(lastWord);
                    attributeValue = "_";
                } else if (lastReceiveState == 14) {
                    seedCode = "block notes";
                    attributeValue = "_";
                } else if (lastReceiveState == 15) {
                    seedCode = "line notes";
                    attributeValue = "_";
                } else if (lastReceiveState == 23) {
                    seedCode = "char";
                    attributeValue = lastWord;
                } else if (lastReceiveState == 27) {
                    seedCode = "string";
                    attributeValue = lastWord;
                } else {
                    seedCode = this.OPERATORS.get(lastWord);
                    attributeValue = "_";
                }
                Token token = new Token(lastWord, seedCode, attributeValue, lines1);
                ArrayList<Integer> temp_dfa = new ArrayList<>();
                for (int i = 0; i < dfa.size() - (flag - lastFlag) + 1; i++) {
                    temp_dfa.add(dfa.get(i));
                }
                counts += 1;
                DFA_Seq.put(counts, temp_dfa);
                dfa.clear();
                tokens.add(token);
                if (temp == '\n') {
                    if (lastFlag + 1 == flag) {
                        lines1++;
                    }
                }

                flag = lastFlag;
                words.delete(0, words.length());

                lastReceiveState = 0;
                lastFlag = 0;
            } else if (state == 0 && lastReceiveState == 0 && ((temp != ' ' && temp != '\n' && words.length() == 0) || words.length() != 0)) {
                flag1 = 1;
                Token token;
                if (words.length() == 0) {
                    token = new Token(String.valueOf(temp), "INCOMPLETE", "_", lines1);
                } else {
                    token = new Token(words.toString(), "INCOMPLETE", "_", lines1);
                    words.delete(0, words.length());
                    flag -= 1;
                }
                ArrayList<Integer> temp_dfa = new ArrayList<>();
                for (Integer i : dfa) {
                    temp_dfa.add(i);
                }
                counts += 1;
                DFA_Seq.put(counts, temp_dfa);
                tokens.add(token);
                try {
                    if (dfa.size() == 0) {
                        dfa.clear();
                        if (temp == '\n' || codes.get(flag + 1) == '\n') {
                            lines1++;
                            throw new IncompleteError("代码第" + (lines1 - 1) + "行，" + "词法分析结果中第" + counts + "个出现了无意义字符!");
                        }
                        throw new IncompleteError("代码第" + lines1 + "行，" + "词法分析结果中第" + counts + "个出现了无意义字符!");
                    } else {
                        int d = dfa.get(dfa.size() - 1);
                        dfa.clear();
                        if (d >= 20 && d <= 22) {
                            if (temp == '\n') {
                                lines1++;
                                throw new IncompleteError("代码第" + (lines1 - 1) + "行，" + "词法分析结果中第" + counts + "个出现了非法字符常数Char!");
                            }
                            throw new IncompleteError("代码第" + lines1 + "行，" + "词法分析结果中第" + counts + "个出现了非法字符常数Char!");
                        }
                        if (d >= 24 && d <= 26) {
                            if (temp == '\n') {
                                lines1++;
                                throw new IncompleteError("代码第" + (lines1 - 1) + "行，" + "词法分析结果中第" + counts + "个出现了非法字符串String!");
                            }
                            throw new IncompleteError("代码第" + lines1 + "行，" + "词法分析结果中第" + counts + "个出现了非法字符串String!");
                        }
                    }

                } catch (IncompleteError e) {
                    errorlist.add(e.getMessage());
                }

            } else if (temp == '\n') {
                if (lastFlag + 1 == flag) {
                    lines1++;
                }
            }


            if (flag1 == 0 && state != 0) {
                words.append(temp);
            }

            if (end_states.contains(state)) {
                lastReceiveState = state;
                lastFlag = flag;
                lastWord = words.toString();
            }
            flag++;
        }

        writeResult();

    }

    /**
     * 判断一个字符是否是字母。
     *
     * @param i 需要判断的字符i。
     * @return 是则为真，否则为否。
     */
    public boolean isLetter(char i) {
        return ((i >= 'A') && (i <= 'Z')) || ((i >= 'a' && i <= 'z'));
    }

    /**
     * 判断一个字符时都是数字
     *
     * @param i 需要判断的字符i。
     * @return 是则为真，否则为否。
     */
    public boolean isNumber(char i) {
        return (i <= '7') && (i >= '1');
    }


    /**
     * 读取dfa状态转换表。
     */
    public void readTable() {
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(new File(input_path1))));
            int rowx = Integer.valueOf(bf.readLine().split("\\s+")[1]);
            int rowy = Integer.valueOf(bf.readLine().split("\\s+")[1]);
            conversion_table = new int[rowx][rowy];
            String temp[] = bf.readLine().split("\\s+");
            int[] end_state = new int[temp.length - 1];
            for (int j = 1; j < temp.length; j++) {
                end_state[j - 1] = Integer.valueOf(temp[j]);
            }
            for (int a : end_state) {
                end_states.add(a);
            }

            String temp2 = bf.readLine();
            String[] non_terminal = bf.readLine().split(" ");
            for (String a : non_terminal) {
                non_terminals.add(a);
            }
            String line;
            int flag = 0;
            while ((line = bf.readLine()) != null) {
                String[] temp1 = line.split("\\s+");
                for (int j = 1; j < temp1.length; j++)
                    conversion_table[flag][j - 1] = Integer.valueOf(temp1[j]);
                flag += 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给定输入文件路径，读入代码。
     *
     * @return 读入的代码，以字符动态数组形式返回。
     */
    public ArrayList<Character> readCodes() {
        ArrayList codes = new ArrayList<Character>();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(new File(input_path2))));
            String line;
            while ((line = bf.readLine()) != null) {
                char[] temp = line.toCharArray();
                for (char a : temp) {
                    codes.add(a);
                }
                codes.add('\n');
            }
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return codes;
    }

    public void writeResult() {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output_path)));
            for (Token token : tokens) {
                bw.write(token.getLineNum() + "\t" + token.getWords() + "\t" + "<" + token.getSeedCode() + "," + "\t" + token.getAttributeValue() + ">" + "\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Not8Const, UnknownWord, IncompleteError {
        LexicalAnalyser LexicalAnalyser = new LexicalAnalyser("data//conversionTable.txt", "data//input1.txt", "data//result1.txt");
        LexicalAnalyser.lexicalAnalyse();
        System.out.println("");
    }
}
