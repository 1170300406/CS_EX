package semanticAnalysis;

import java.util.List;
import java.util.Stack;

public class Api {
    public static String print_ins(List<String> three_addr, List<FourAddr> four_addr,int initial)
    {
        StringBuffer s = new StringBuffer();
        for (int i=0; i<three_addr.size(); i++)
        {
            s.append((initial + i) + ": ");
            s.append(four_addr.get(i).toString());
            s.append("  ");
            s.append(three_addr.get(i));
//            System.out.println(three_addr.get(i));
            s.append("\n");
        }
//        System.out.println(s);
        return s.toString();
    }


    public static String print_table(List<Stack<Symbol>> table)
    {
        StringBuffer s = new StringBuffer();
        s.append("name," + "\t" + "type," + "\t" + "offset");
        s.append("\n");
        for (int i=0; i<table.size(); i++)
        {
            s.append((i)+" \t");
            s.append(table.get(i).toString());
            s.append("\n");
        }
//        System.out.println(s);
        return s.toString();
    }

    public static String print_errors(List<String> e)
    {
        StringBuffer s = new StringBuffer();
        for (int i=0; i<e.size(); i++)
        {
            s.append(e.get(i));
            //System.out.println(three_addr.get(i));
            s.append("\n");
        }
//        System.out.println(s);
        return s.toString();
    }
}
