package lexicalAnalysis;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class test {


    public static void main(String[] args) {
        StringBuilder A = new StringBuilder();
        A.append("a");
        A.append("a");
        A.delete(0,A.length()-1);
        System.out.println(A.toString());
    }
}
