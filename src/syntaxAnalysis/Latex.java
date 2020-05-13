package syntaxAnalysis;

import lexicalAnalysis.Token;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Latex {
    private String input_file, output_file, output_file2,output_file3;
    public static Map<String, Integer> Symbol = new HashMap<String, Integer>();// 符号表HashMap
    public static int Symbol_pos = 0;// 记录符号表位置
    public ArrayList<Token> tokens = new ArrayList<>();

    // 定义关键字，运算符，界符
    public static String Keywords[] = { "auto", "double", "int", "struct", "break", "else", "long", "switch", "case",
            "enum", "register", "typedef", "char", "extern", "return", "union", "const", "float", "short", "unsigned",
            "continue", "for", "signed", "void", "default", "goto", "sizeof", "volatile", "do", "if", "while", "static",
            "main", "String","proc","begin","end" };
    //运算符定义：+|-|*|/|%|++|--|>|<|>=|<=|==|!=|&&| '||' | !   同时加入几种常见的赋值语句： += -= /= *=
    public static char Operator[] = { '+', '-', '*', '=', '<', '>', '&', '|', '!','%' };
    public static char Boundary[] = { ',', ';', '[', ']', '(', ')', '.', '{', '}','=' };

    // DFA of digit
    public static String digitDFA[] = {
            "d.#e##",
            "##d###",
            "##de##",
            "####-d",
            "#####d",
            "#####d" };
    //a代表任意字符，b代表除\和'之外的字符
    public static String charDFA[] = {
            "#\\b#",
            "##a#",
            "###\'",
            "####" };
    //a代表任意字符，b代表除\和'之外的字符
    public static String stringDFA[] = {
            "#\\b\"",
            "##a#",
            "#\\b\"",
            "####" };
    //多行注释DFA,c代表除*以外的其他字符，d代表除*和/外的其他字符
    public static String noteDFA[] = {
            "#####",
            "##*##",
            "##c*#",
            "##d*/",
            "#####" };
    //多行注释DFA,d代表0-9和a-f
    public static String HEXDFA[] = {
            "#0##",
            "##x#",
            "###d",
            "###d"};


    public Latex(String input, String output1 ,String output2,String output3) {
        this.input_file = input;
        this.output_file = output1;
        this.output_file2 = output2;
        this.output_file3 = output3;
        clearResult(output_file);
        clearResult(output_file2);
        clearResult(output_file3);
    }

    public void analyze() {
        ArrayList<String> arrayList = getInput(input_file);
        Symbol.clear();
        Symbol_pos = 0;
        String token = "";
        StringBuilder builder = new StringBuilder();
        for (int m = 0; m < arrayList.size(); m++) {
            String str = arrayList.get(m);
            builder = new StringBuilder();
            if (str.equals("")) {
                continue;
            } else {
                // 将字符串转化为字符串数组
                char[] strline = str.toCharArray();

                for (int i = 0; i < strline.length;i++ ) {
                    // 遍历strline中的每个字符
                    char ch = strline[i];
                    // 初始化token字符串为空
                    token = "";
                    builder = new StringBuilder();
                    // 开始识别部分
                    // 首先是识别关键字和标识符，关键字是一类特殊的标识符
                    if (isAlpha(ch)) {
                        // 未到达结束符，组成为字母数字下划线且在本行内
                        i = analyze_IDN(strline, i, m);
                    } else if (isDigit(ch)) {
                        i = analyze_digit(strline, i, m);
                    }else if (ch == '\'') {
                        //识别字符常量，用单引号括起来的单个字符或转义字符\
                        i = analyze_Char(strline, i, m);
                    }else if (ch == '"') {
                        //识别字符串类型
                        i = analyze_String(strline, i, m);
                    }else if (isOp(ch)) {
                        //识别运算符, '/'在识别注释里进行识别
                        if (isDouble(ch)) {
                            if(i+1 <strline.length && ch == strline[i+1]) {
                                token = token + ch + strline[i+1];
                                builder.append("<0,"+ch +",1> ");
                                builder.append("<1,"+ch +",2> ");
                                i = i + 1;
                                writeResult(token + "\t" + "<" + "OP" + "," + "\t" + token + ">" + "\t" + (m + 1) + "\n",output_file);
                                writeResult(token + "\t" + builder.toString() + "\n", output_file3);
                                tokens.add(new Token(token,token,"_",m+1));
                            }else if (ch == '=') {
                                token = token + ch;
                                builder.append("<0,"+ch +",1> ");
                                writeResult(token + "\t" + "<" + "界符" + "," + "\t" + token + ">" + "\t" + (m + 1) + "\n",output_file);
                                writeResult(token + "\t" + builder.toString() + "\n", output_file3);
                                tokens.add(new Token(token,token,"_",m+1));
                            }else {
                                writeResult((m+1) + "\t" + token + "存在不合法字符" + "\n",output_file2);
                            }

                        }else {
                            if (isPlusEqu(ch) && i+1 <strline.length &&  strline[i+1] == '=') {
                                token = token + ch + strline[i+1];
                                builder.append("<0,"+ch +",1> ");
                                builder.append("<1,"+strline[i+1] +",2> ");
                                i = i + 1;

                            } else if (isPlusSame(ch) && i+1 <strline.length && ch == strline[i+1]) {
                                token = token + ch + strline[i+1];
                                builder.append("<0,"+ch +",1> ");
                                builder.append("<1,"+ch +",2> ");
                                i = i + 1;
                            }else {
                                token = token + ch;
                                builder.append("<0,"+ch +",1> ");

                            }
                            writeResult(token + "\t" + "<" + "OP" + "," + "\t" + token + ">" + "\t" + (m + 1) + "\n",output_file);
                            writeResult(token + "\t" + builder.toString() + "\n", output_file3);
                            tokens.add(new Token(token,token,"_",m+1));
                        }
                        token = "";
                    }else if (isBoundary(ch)) {
                        //识别界符,=在运算符里进行识别
                        token = token + ch;
                        builder.append("<0,"+ch +",1> ");
                        writeResult(token + "\t" + "<" + "界符" + "," + "\t" + token + ">" + "\t" + (m + 1) + "\n",output_file);
                        writeResult(token + "\t" + builder.toString() + "\n", output_file3);
                        tokens.add(new Token(token,token,"_",m+1));
                        token = "";

                    }else if (ch == '/') {
                        //识别注释,包括/*...*/   和 //...两种形式 ,也可能将除号误以为是注释开始符号
                        i = analyze_Note(strline, i, m);

                    }else {
                        //不合法字符
                        if(ch != ' ' && ch != '\t' && ch != '\0' && ch != '\n' && ch != '\r')
                        {
                            writeResult((m+1) + "\t" + token + "存在不合法字符" + "\n",output_file2);

                        }
                    }
                }
            }
        }
    }

    public int analyze_IDN(char[] strline, int i, int m) {
        char ch = strline[i];
        String token = "";
        StringBuilder builder = new StringBuilder();
        while (ch != '\0' && (isAlpha(ch) || isDigit(ch))) {
            token += ch;
            builder.append("<1,"+ch +",2> ");
            i++;
            if (i >= strline.length) {
                break;
            }
            ch = strline[i];
        }
        i--;
        // 判断是否为关键字
        if (isMatchKeyword(token)) {
            writeResult(token + "\t" + "<" + token.toUpperCase() + "," + "\t" + "_" + ">" + "\t"+ (m + 1)
                    + "\n",output_file);
            writeResult(token + "\t" + builder.toString() + "\n", output_file3);
            tokens.add(new Token(token,token,"_",m+1));
        } else {
            // 是标识符
            // 如果符号表为空或符号表中不包含当前token，则加入
            if (Symbol.isEmpty() || (!Symbol.isEmpty() && !Symbol.containsKey(token))) {
                Symbol.put(token, Symbol_pos);
                Symbol_pos++;
            }
            writeResult(token + "\t" + "<" + "IDN" + "," + "\t" + token + ">" + "\t" + (m + 1) + "\n",output_file);
            writeResult(token + "\t" + builder.toString() + "\n", output_file3);
            tokens.add(new Token(token,"id",token,m+1));
        }
        return i;
    }

    public int analyze_digit(char[] strline,int i , int m) {
        int index = i;
        char ch = strline[i];
        String token = "";
        StringBuilder builder = new StringBuilder();
        int state = 0;// 初始化dfa状态
        boolean haveMistake = false;
        int k;
        if (ch == '0' && i+1 <strline.length && isDigit(strline[i+1])) {
            //识别8进制整数 OCT
            token += ch;
            builder.append("<0,"+ch +",1> ");
            i++;
            ch = strline[i];
            while (isDigit(ch)) {
                token += ch;
                builder.append("<1,"+ch +",2> ");
                if (!isOCT(ch)) {
                    haveMistake = true;
                }
                i++;
                if (i >= strline.length) {break;}
                ch = strline[i];
            }

            //错误处理
            if (haveMistake) {

                String str = String.valueOf(strline);
                int ls = i - 1;
                str = str.substring(0,ls);
                if (str.length() == index) {
                    writeResult(m+1 + "\t" + token + " 确认8进制整数输入正确" + "\n",output_file2);
                }else {
                    ls = analyze_digit(str.toCharArray(), index, m);
                    return ls;
                }



            } else {
                int a = OTCtoDEC(token);
                writeResult(token + "\t" + "<" + "OCT" + "," + "\t" + a + ">" + "\t" + (m + 1) + "\n",output_file);
                writeResult(token + "\t" + builder.toString() + "\n", output_file3);
                tokens.add(new Token(token,"OCT",token,m+1));
            }
        } else if (ch == '0' && i+1 <strline.length && strline[i+1] == 'x') {
            //识别16进制数字 HEX
            while (isHex(ch) || ch == 'x') {
                for ( k = 0; k < 4; k++) {
                    char tmpstr[] = HEXDFA[state].toCharArray();
                    if (in_HEXDFA(ch, tmpstr[k])) {
                        token += ch;
                        builder.append("<"+state  +","+ch +"," + k+"> ");
                        state = k;
                        break;
                    }
                }
                if (k > 3) {  break;  }
                i++;
                if (i >= strline.length) {  break;  }
                ch = strline[i];
            }

            if (state != 3) {
                //出错
                String str = String.valueOf(strline);
                int ls = i - 1;
                str = str.substring(0,ls);
                if (str.length() == index) {
                    writeResult(m+1 + "\t" + token + " 确认16进制整数输入正确" + "\n",output_file2);
                }else {
                    ls = analyze_digit(str.toCharArray(), index, m);
                    return ls;
                }

            }else {
                int a = HEXtoDEC(token);
                writeResult(token + "\t" + "<" + "HEX" + "," + "\t" + a + ">" + "\t" + (m + 1) + "\n",output_file);
                writeResult(token + "\t" + builder.toString() + "\n", output_file3);
            }
        }else {
            //识别正常数字
            //int k;
            boolean isfloat = false;

            while ((ch != '\0') && (isDigit(ch) || ch == '.' || ch == 'e' || ch == '-'|| ch == '+')) {
                if (ch == '.') { isfloat = true;}
                //dfa转移至下一状态
                for (k = 0; k < 6; k++) {
                    char tmpstr[] = digitDFA[state].toCharArray();
                    if (tmpstr[k] != '#' && in_digitDFA(ch, tmpstr[k])) {
                        token += ch;
                        builder.append("<"+state  +","+ch +"," + k+"> ");
                        state = k;
                        break;
                    }
                }
                if (k > 5) {break;}
                i++;
                if (i >= strline.length) {break;}
                ch = strline[i];

            }

            //判断是否出错

            if (state == 1 || state == 3 || state == 4)
            {
                haveMistake = true;
            }

            //错误处理
            if (haveMistake) {
                String str = String.valueOf(strline);
                int ls = i - 1;
                str = str.substring(0,ls);
                if (str.length() == index) {
                    writeResult(m+1 + "\t" + token + " 确认无符号常数输入正确" + "\n",output_file2);
                }else {
                    ls = analyze_digit(str.toCharArray(), index, m);
                    return ls;
                }

            }else {
                writeResult(token + "\t" + "<" + "CONST" + "," + "\t" + token + ">" + "\t" + (m + 1) + "\n",output_file);
                writeResult(token + "\t" + builder.toString() + "\n", output_file3);
                if (isfloat){
                    tokens.add(new Token(token,"num",token,m+1));
                }else{
                    tokens.add(new Token(token,"int",token,m+1));
                }
            }
        }

        i--;
        return i;
    }

    public int analyze_Char(char[] strline, int i,int m) {
        int index = i;
        char ch = strline[i];
        String token = "";
        StringBuilder builder = new StringBuilder();
        token += ch;
        builder.append("<0,"+ch +",1> ");
        int state = 0;
        while (state != 3) {
            i++;
            if(i >= strline.length) break;
            ch = strline[i];
            for (int k = 0; k < 4; k++)
            {
                char tmpstr[] = charDFA[state].toCharArray();
                if (in_charDFA(ch, tmpstr[k]))
                {
                    token += ch;
                    builder.append("<"+state  +","+ch +"," + k+"> ");
                    state = k;
                    break;
                }
            }
        }


        if (state != 3) {
            String str = String.valueOf(strline);
            int ls = i - 1;
            str = str.substring(0,ls);
            if (str.length() == index) {
                writeResult(m+1 + "\t" + token + " 确认\'输入正确" + "\n",output_file2);
                i--;
            }else {
                ls = analyze_Char(str.toCharArray(), index, m);
                return ls;
            }

        }else {
			/*
			String subtoken = "";
			if (token.length() == 3) {
				subtoken = token.substring(1, 2);
			} else if (token.length() == 4) {
				subtoken = token.substring(2, 3);
			}
			*/
            writeResult(token + "\t" + "<" + "char" + "," + "\t" + token + ">" + "\t" + (m + 1) + "\n",output_file);
            writeResult(token + "\t" + builder.toString() + "\n", output_file3);
        }
        token = "";
        return i;
    }

    public int analyze_String(char[] strline, int i,int m) {
        int index = i;
        char ch = strline[i];
        String token = "";
        StringBuilder builder = new StringBuilder();
        //识别字符串类型
        token += ch;
        builder.append("<0,"+ch +",1> ");
        int state = 0;
        while(state != 3) {
            i++;
            if(i >= strline.length) break;
            ch = strline[i];

            for (int k = 0; k < 4; k++) {
                char tmpstr[] = stringDFA[state].toCharArray();
                if (in_stringDFA(ch, tmpstr[k])) {
                    if (state == 1) {
                        token = token.substring(0, token.length()-1) + '\\' + ch;
                    } else {
                        token += ch;
                    }
                    builder.append("<"+state  +","+ch +"," + k+"> ");
                    state = k;
                    break;
                }
            }
        }
        if (state != 3) {

            String str = String.valueOf(strline);
            int ls = i - 1;
            str = str.substring(0,ls);

            if (str.length() == index) {
                writeResult(m+1 + "\t" + token + " 确认\"输入正确" + "\n",output_file2);
                i--;
            }else {
                ls = analyze_String(str.toCharArray(), index, m);
                return ls;
            }

        }else {
            writeResult(token + "\t" + "<" + "String" + "," + "\t" + token + ">" + "\t" + (m + 1) + "\n",output_file);
            writeResult(token + "\t" + builder.toString() + "\n", output_file3);
        }
        return i;
    }

    public int analyze_Note(char[] strline, int i, int m) {
        //System.out.println(i);
        int index = i;
        char ch = strline[i];
        String token = "";
        StringBuilder builder = new StringBuilder();
        token += ch;
        i++;
        if(i>=strline.length) {
            writeResult(token + "\t" + "<" + "运算符" + "," + "\t" + token + ">" + "\t" + (m + 1) + "\n",output_file);
            //System.out.println(i);
            i--;
            return i;
        }
        ch = strline[i];

        //注释识别
        if (ch == '/') {
            // 识别//类型的单行注释
            String str = String.valueOf(strline);
            token = str.substring(i-1, str.length());
            i = strline.length;
            writeResult(token + "\t" + "<" + "注释" + "," + "\t" + token.substring(2) + ">" + "\t" + (m + 1) + "\n",output_file);
        }else if (ch == '*') {
            //识别注释/*...*/
            //Boolean haveMistake = false;
            //int State = 0;
            token += ch;
            int state = 2;
            while (state != 4) {
                i++;
                if(i>=strline.length) break;
                ch = strline[i];
                if (ch == '\0') {
                    //haveMistake = true;
                    break;
                }
                for (int k = 2; k <= 4; k++) {
                    char tmpstr[] = noteDFA[state].toCharArray();
                    if (in_noteDFA(ch, tmpstr[k])) {
                        token += ch;
                        state = k;
                        break;
                    }
                }
            }
            if (state != 4) {
                //writeResult((m+1) + "\t" + token + " 注释未封闭" + "\n",output_file2);
                String str = String.valueOf(strline);
                int ls = i - 1;
                str = str.substring(0,ls);
                ls = analyze_Note(str.toCharArray(), index, m);

                return ls;
            } else {
                writeResult(token + "\t" + "<" + "注释" + "," + "\t" + token.substring(2,token.length()-2) + ">" + "\t" + (m + 1) + "\n",output_file);
            }

        }else {
            //是除号
            writeResult(token + "\t" + "<" + "运算符" + "," + "\t" + token + ">" + "\t" + (m + 1) + "\n",output_file);
        }

        i--;
        return i;
    }

    // 读取文件输入
    public ArrayList<String> getInput(String filename) {
        ArrayList<String> array = new ArrayList<String>();
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filename))));
            String line;
            while ((line = bf.readLine()) != null) {

                array.add(line);
            }
            bf.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return array;
    }

    // 写入结果文件
    public void writeResult(String str,String filename) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, true)));
            bw.write(str);

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //清空结果文件
    public void clearResult(String filename) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
            bw.write("");

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 判断字母及下划线
    public Boolean isAlpha(char ch) {
        return ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_');
    }
    //判断数字
    public Boolean isDigit(char ch) {
        return (ch >= '0' && ch <= '9');
    }
    //是否为8进制的数字
    public Boolean isOCT(char ch) {
        return (ch >= '0' && ch <= '7');
    }
    //是否为16进制数字
    public Boolean isHex(char ch) {
        return (ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'F') || (ch >= 'a' && ch <= 'f');
    }
    //8进制转化为10进制
    public int OTCtoDEC(String str) {
        int num = 0;
        char ch;
        for (int i = 0; i < str.length(); i++) {
            ch = str.charAt(i);
            num = num * 8 + (ch - '0');
        }
        return num;
    }

    //16进制转化为10进制
    public int HEXtoDEC(String str) {
        int num = 0;
        int temp;
        char ch;
        for (int i = 2; i < str.length(); i++) {
            ch = str.charAt(i);
            if (ch >= '0' && ch <= '9') {
                temp = ch - '0';
            } else if (ch >= 'a' && ch <= 'f') {
                temp = ch - 'a';
            }else {
                temp = ch - 'A';
            }
            num = num * 16 + temp;
        }
        return num;
    }

    // 判断是否是运算符
    public Boolean isOp(char ch) {
        for (int i = 0; i < Operator.length; i++)
            if (ch == Operator[i]) {
                return true;
            }
        return false;
    }
    //判断是否为界符
    public Boolean isBoundary(char ch) {
        for (int i = 0; i < Boundary.length; i++)
            if (ch == Boundary[i]) {
                return true;
            }
        return false;
    }
    //是否为关键字
    public Boolean isMatchKeyword(String str) {
        Boolean flag = false;
        for (int i = 0; i < Keywords.length; i++) {
            if (str.equals(Keywords[i])) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    // 运算符后可加等于
    public Boolean isPlusEqu(char ch) {
        return ch == '>' || ch == '<' || ch == '!'|| ch == '+'|| ch == '-'|| ch == '/'|| ch == '*';
    }
    //运算符由两个字符构成，且两个字符必须一样
    public Boolean isDouble(char ch) {
        if (ch == '=' || ch == '&' || ch == '|') {
            return true;
        }
        return false;
    }

    // 可以连续两个运算符一样
    public Boolean isPlusSame(char ch) {
        return ch == '+' || ch == '-';
    }

    public boolean in_HEXDFA(char ch,char test) {
        if (test == '#') {
            return false;
        } else if (test == 'd') {
            return isHex(ch);
        } else {
            return ch == test;
        }
    }

    public boolean in_digitDFA(char ch, char test)
    {
        if (test == 'd') {
            return isDigit(ch);
        } else if(test == '-'){
            return ch == '+' || ch == '-';
        }else {
            return ch == test;
        }
    }

    public boolean in_charDFA(char ch, char test) {
        if (test == 'a')
            return isEsSt(ch);
        if (test == '\\')
            return ch == test;
        if (test == '\'')
            return ch == test;
        if (test == 'b')
            return ch != '\\' && ch != '\'';
        return false;
    }

    public boolean in_stringDFA(char ch, char test) {
        if (test == '\\') {
            return ch == test;
        }else if (test == '"') {
            return ch == test;
        }else if (test == 'a') {
            return isEsSt(ch);
        }else if (test == 'b') {
            return ch != '\\' && ch != '"';
        }
        return false;
    }

    //判断是否可构成转义字符
    public  Boolean isEsSt(char ch) {
        return ch == 'a' || ch == 'b' || ch == 'f' || ch == 'n' || ch == 'r'
                || ch == 't' || ch == 'v' || ch == '?' || ch == '0'|| ch == '\\'|| ch == '\''|| ch == '\"';
    }

    public boolean in_noteDFA(char ch , char test) {
        if (test == '#') {
            return false;
        }else if (test == 'c') {
            return ch != '*';
        }else if (test == 'd') {
            return ch != '*' && ch != '/';
        } else {
            return ch == test;
        }
    }

    public static void main(String[] args) {
        //LexicalAnalyser lexicalAnalyser = new LexicalAnalyser("data//conversionTable.txt", "data//input.txt", "data//result1.txt");
        //lexicalAnalyser.lexicalAnalyse();
        Latex latex = new Latex("src//input//inputfile.txt","src//output//output1.txt" ,"src//output//output2.txt","src//output//output3.txt" );
        latex.analyze();

    }
}

