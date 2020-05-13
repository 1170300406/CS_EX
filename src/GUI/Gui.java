package GUI;

import lexicalAnalysis.LexicalAnalyser;
import myException.IncompleteError;
import myException.Not8Const;
import myException.UnInitError;
import myException.UnknownWord;
import semanticAnalysis.Api;
import semanticAnalysis.Properties;
import semanticAnalysis.SemanticAnalysis;
import syntaxAnalysis.Node;
import syntaxAnalysis.Symbol;
import syntaxAnalysis.SyntaxAnalysis;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Stack;

public class Gui extends JPanel implements ActionListener {

    JLabel label1 = new JLabel("编译器前端");

    JLabel label2 = new JLabel("词法分析");
    JLabel label3 = new JLabel("DFA转换表");
    JLabel label4 = new JLabel("分析代码");
    JLabel label5 = new JLabel("Token序列");
    JLabel label6 = new JLabel("错误报告");
    JLabel label7 = new JLabel("DFA转换序列");

    JLabel label8 = new JLabel("语法分析");
    JLabel label9 = new JLabel("文法");
    JLabel label12 = new JLabel("FOLLOW集");
    JLabel label10 = new JLabel("First集");
    JLabel label11 = new JLabel("LR(1)分析表");
    JLabel label13 = new JLabel("分析代码");
    JLabel label14 = new JLabel("Token序列");
    JLabel label15 = new JLabel("错误报告");
    JLabel label16 = new JLabel("语法分析结果");


    JButton button1 = new JButton("词法分析");
    JButton button2 = new JButton("语法分析");
    JButton button3 = new JButton("语义分析");

    JButton button4 = new JButton("返回");
    JButton button5 = new JButton("读入转换表");
    JButton button6 = new JButton("编辑分析代码");
    JButton button7 = new JButton("返回");
    JButton button8 = new JButton("写入");
    JButton button9 = new JButton("返回");

    JButton button10 = new JButton("返回");
    JButton button11 = new JButton("查看语法分析器");
    JButton button12 = new JButton("编辑分析代码");
    JButton button13 = new JButton("返回");
    JButton button14 = new JButton("写入");
    JButton button15 = new JButton("返回");

    JLabel label17 = new JLabel("语义分析");
    JLabel label18 = new JLabel("翻译方案展示");
    JLabel label19 = new JLabel("分析代码");
    JLabel label21 = new JLabel("四元式");
    JLabel label22 = new JLabel("符号表");
    JLabel label23 = new JLabel("错误报告");

    JButton button16 = new JButton("返回");
    JButton button17 = new JButton("查看翻译方案");
    JButton button18 = new JButton("编辑分析代码");
    JButton button19 = new JButton("返回");
    JButton button20 = new JButton("写入");
    JButton button21 = new JButton("返回");

    JTextArea textArea14 = new JTextArea();
    JScrollPane scrollPane14 = new JScrollPane();

    JTextArea textArea15 = new JTextArea();
    JScrollPane scrollPane15 = new JScrollPane();

    JTextArea textArea16 = new JTextArea();
    JScrollPane scrollPane16 = new JScrollPane();

    JScrollPane scrollPane17 = new JScrollPane();

    JTextArea textArea18 = new JTextArea();
    JScrollPane scrollPane18 = new JScrollPane();


    JTextArea textArea1 = new JTextArea();
    JScrollPane scrollPane1 = new JScrollPane();

    JTextArea textArea2 = new JTextArea();
    JScrollPane scrollPane2 = new JScrollPane();

    JTextArea textArea3 = new JTextArea();
    JScrollPane scrollPane3 = new JScrollPane();

    JTextArea textArea4 = new JTextArea();
    JScrollPane scrollPane4 = new JScrollPane();

    JTextArea textArea5 = new JTextArea();
    JScrollPane scrollPane5 = new JScrollPane();

    JTextArea textArea6 = new JTextArea();
    JScrollPane scrollPane6 = new JScrollPane();

    JTextArea textArea7 = new JTextArea();
    JScrollPane scrollPane7 = new JScrollPane();

    JScrollPane scrollPane8 = new JScrollPane();

    JTextArea textArea9 = new JTextArea();
    JScrollPane scrollPane9 = new JScrollPane();

    JTextArea textArea10 = new JTextArea();
    JScrollPane scrollPane10 = new JScrollPane();

    JTextArea textArea11 = new JTextArea();
    JScrollPane scrollPane11 = new JScrollPane();

    JTextArea textArea12 = new JTextArea();
    JScrollPane scrollPane12 = new JScrollPane();

    JTree jTree13;
    JScrollPane scrollPane13 = new JScrollPane();

    LexicalAnalyser lexicalAnalyser = new LexicalAnalyser();
    SyntaxAnalysis syntaxAnalysis = new SyntaxAnalysis();

    public Gui() {
        Font font = new Font("Monospaced", Font.BOLD, 32);
        Font font1 = new Font("宋体", Font.BOLD, 15);

        add(label1);
        label1.setBounds(600, 10, 200, 80);
        label1.setFont(font);
        label1.setVisible(true);

        add(label2);
        label2.setBounds(617, 10, 200, 80);
        label2.setFont(font);
        label2.setVisible(false);
        add(label3);
        label3.setBounds(580, 10, 200, 80);
        label3.setFont(font);
        label3.setVisible(false);
        add(label4);
        label4.setBounds(105, 5, 200, 80);
        label4.setFont(font);
        label4.setVisible(false);
        add(label5);
        label5.setBounds(418, 5, 200, 80);
        label5.setFont(font);
        label5.setVisible(false);
        add(label6);
        label6.setBounds(1088, 5, 200, 80);
        label6.setFont(font);
        label6.setVisible(false);
        add(label7);
        label7.setBounds(737, 5, 200, 80);
        label7.setFont(font);
        label7.setVisible(false);


        add(label17);
        label17.setBounds(617, 10, 200, 80);
        label17.setFont(font);
        label17.setVisible(false);
        add(label18);
        label18.setBounds(580, 10, 200, 80);
        label18.setFont(font);
        label18.setVisible(false);

        add(label19);
        label19.setBounds(105, 5, 200, 80);
        label19.setFont(font);
        label19.setVisible(false);
        add(label21);
        label21.setBounds(442, 5, 200, 80);
        label21.setFont(font);
        label21.setVisible(false);
        add(label22);
        label22.setBounds(774, 5, 200, 80);
        label22.setFont(font);
        label22.setVisible(false);
        add(label23);
        label23.setBounds(1088, 5, 200, 80);
        label23.setFont(font);
        label23.setVisible(false);

        add(label8);
        label8.setBounds(617, 10, 200, 80);

        label8.setFont(font);
        label8.setVisible(false);
        add(label9);
        label9.setBounds(129, 5, 200, 80);
        label9.setFont(font);
        label9.setVisible(false);
        add(label10);
        label10.setBounds(430, 5, 200, 80);
        label10.setFont(font);
        label10.setVisible(false);
        add(label11);
        label11.setBounds(1062, 5, 200, 80);
        label11.setFont(font);
        label11.setVisible(false);
        add(label12);
        label12.setBounds(755, 5, 200, 80);
        label12.setFont(font);
        label12.setVisible(false);

        add(label13);
        label13.setBounds(105, 5, 200, 80);
        label13.setFont(font);
        label13.setVisible(false);
        add(label14);
        label14.setBounds(418, 5, 200, 80);
        label14.setFont(font);
        label14.setVisible(false);
        add(label15);
        label15.setBounds(1088, 5, 200, 80);
        label15.setFont(font);
        label15.setVisible(false);
        add(label16);
        label16.setBounds(729, 5, 200, 80);
        label16.setFont(font);
        label16.setVisible(false);

        add(button1);
        button1.setBounds(583, 140, 200, 80);
        button1.addActionListener(this);
        add(button2);
        button2.setBounds(583, 260, 200, 80);
        button2.addActionListener(this);
        add(button3);
        button3.setBounds(583, 380, 200, 80);
        button3.addActionListener(this);
        add(button4);
        button4.setBounds(583, 380, 200, 80);
        button4.addActionListener(this);
        button4.setVisible(false);
        add(button5);
        button5.setBounds(583, 140, 200, 80);
        button5.addActionListener(this);
        button5.setVisible(false);
        add(button6);
        button6.setBounds(583, 260, 200, 80);
        button6.addActionListener(this);
        button6.setVisible(false);
        add(button7);
        button7.setBounds(610, 500, 100, 40);
        button7.addActionListener(this);
        button7.setVisible(false);
        add(button8);
        button8.setBounds(120, 500, 100, 40);
        button8.addActionListener(this);
        button8.setVisible(false);
        add(button9);
        button9.setBounds(450, 500, 100, 40);
        button9.addActionListener(this);
        button9.setVisible(false);

        add(button16);
        button16.setBounds(583, 380, 200, 80);
        button16.addActionListener(this);
        button16.setVisible(false);
        add(button17);
        button17.setBounds(583, 140, 200, 80);
        button17.addActionListener(this);
        button17.setVisible(false);
        add(button18);
        button18.setBounds(583, 260, 200, 80);
        button18.addActionListener(this);
        button18.setVisible(false);
        add(button19);
        button19.setBounds(610, 500, 100, 40);
        button19.addActionListener(this);
        button19.setVisible(false);
        add(button20);
        button20.setBounds(120, 500, 100, 40);
        button20.addActionListener(this);
        button20.setVisible(false);
        add(button21);
        button21.setBounds(450, 500, 100, 40);
        button21.addActionListener(this);
        button21.setVisible(false);

        add(button10);
        button10.setBounds(583, 380, 200, 80);
        button10.addActionListener(this);
        button10.setVisible(false);
        add(button11);
        button11.setBounds(583, 140, 200, 80);
        button11.addActionListener(this);
        button11.setVisible(false);
        add(button12);
        button12.setBounds(583, 260, 200, 80);
        button12.addActionListener(this);
        button12.setVisible(false);
        add(button13);
        button13.setBounds(610, 500, 100, 40);
        button13.addActionListener(this);
        button13.setVisible(false);
        add(button14);
        button14.setBounds(120, 500, 100, 40);
        button14.addActionListener(this);
        button14.setVisible(false);
        add(button15);
        button15.setBounds(450, 500, 100, 40);
        button15.addActionListener(this);
        button15.setVisible(false);

        add(textArea1);
        add(scrollPane1);
        scrollPane1.setBounds(40, 80, 1260, 400);
        textArea1.setBounds(40, 80, 1260, 400);
        scrollPane1.setViewportView(textArea1);
        textArea1.setVisible(false);
        textArea1.setFont(font1);
        scrollPane1.setVisible(false);

        add(textArea2);
        add(scrollPane2);
        scrollPane2.setBounds(40, 80, 260, 400);
        textArea2.setBounds(40, 80, 260, 400);
        scrollPane2.setViewportView(textArea2);
        textArea2.setVisible(false);
        textArea2.setFont(font1);
        scrollPane2.setVisible(false);

        add(textArea3);
        add(scrollPane3);
        scrollPane3.setBounds(370, 80, 260, 400);
        textArea3.setBounds(370, 80, 260, 400);
        scrollPane3.setViewportView(textArea3);
        textArea3.setVisible(false);
        textArea3.setFont(font1);
        scrollPane3.setVisible(false);

        add(textArea4);
        add(scrollPane4);
        scrollPane4.setBounds(1030, 80, 260, 400);
        textArea4.setBounds(1030, 80, 260, 400);
        scrollPane4.setViewportView(textArea4);
        textArea4.setVisible(false);
        textArea4.setFont(font1);
        scrollPane4.setVisible(false);

        add(textArea5);
        add(scrollPane5);
        scrollPane5.setBounds(700, 80, 260, 400);
        textArea5.setBounds(700, 80, 260, 400);
        scrollPane5.setViewportView(textArea5);
        textArea5.setVisible(false);
        textArea5.setFont(font1);
        scrollPane5.setVisible(false);

        add(textArea14);
        add(scrollPane14);
        scrollPane14.setBounds(40, 80, 1260, 400);
        textArea14.setBounds(40, 80, 1260, 400);
        scrollPane14.setViewportView(textArea14);
        textArea14.setVisible(false);
        textArea14.setFont(font1);
        scrollPane14.setVisible(false);

        add(textArea15);
        add(scrollPane15);
        scrollPane15.setBounds(40, 80, 260, 400);
        textArea15.setBounds(40, 80, 260, 400);
        scrollPane15.setViewportView(textArea15);
        textArea15.setVisible(false);
        textArea15.setFont(font1);
        scrollPane15.setVisible(false);


        add(scrollPane17);
        scrollPane17.setBounds(700, 80, 260, 400);
        scrollPane17.setVisible(false);

        add(textArea16);
        add(scrollPane16);
        scrollPane16.setBounds(370, 80, 260, 400);
        textArea16.setBounds(370, 80, 260, 400);
        scrollPane16.setViewportView(textArea16);
        textArea16.setVisible(false);
        textArea16.setFont(font1);
        scrollPane16.setVisible(false);

        add(textArea18);
        add(scrollPane18);
        scrollPane18.setBounds(1030, 80, 260, 400);
        textArea18.setBounds(1030, 80, 260, 400);
        scrollPane18.setViewportView(textArea18);
        textArea18.setVisible(false);
        textArea18.setFont(font1);
        scrollPane18.setVisible(false);


        add(textArea6);
        add(scrollPane6);
        scrollPane6.setBounds(40, 80, 260, 400);
        textArea6.setBounds(40, 80, 260, 400);
        scrollPane6.setViewportView(textArea6);
        textArea6.setVisible(false);
        textArea6.setFont(font1);
        scrollPane6.setVisible(false);

        add(textArea7);
        add(scrollPane7);
        scrollPane7.setBounds(370, 80, 260, 400);
        textArea7.setBounds(370, 80, 260, 400);
        scrollPane7.setViewportView(textArea7);
        textArea7.setVisible(false);
        textArea7.setFont(font1);
        scrollPane7.setVisible(false);


        scrollPane8.setBounds(1030, 80, 260, 400);
        scrollPane8.setVisible(false);
        scrollPane8.setVisible(false);
        add(scrollPane8);
        scrollPane8.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        add(textArea9);
        add(scrollPane9);
        scrollPane9.setBounds(700, 80, 260, 400);
        textArea9.setBounds(700, 80, 260, 400);
        scrollPane9.setViewportView(textArea9);
        textArea9.setVisible(false);
        textArea9.setFont(font1);
        scrollPane9.setVisible(false);

        add(textArea10);
        add(scrollPane10);
        scrollPane10.setBounds(40, 80, 260, 400);
        textArea10.setBounds(40, 80, 260, 400);
        scrollPane10.setViewportView(textArea10);
        textArea10.setVisible(false);
        textArea10.setFont(font1);
        scrollPane10.setVisible(false);

        add(textArea11);
        add(scrollPane11);
        scrollPane11.setBounds(370, 80, 260, 400);
        textArea11.setBounds(370, 80, 260, 400);
        scrollPane11.setViewportView(textArea11);
        textArea11.setVisible(false);
        textArea11.setFont(font1);
        scrollPane11.setVisible(false);

        add(textArea12);
        add(scrollPane12);
        scrollPane12.setBounds(1030, 80, 260, 400);
        textArea12.setBounds(1030, 80, 260, 400);
        scrollPane12.setViewportView(textArea12);
        textArea12.setVisible(false);
        textArea12.setFont(font1);
        scrollPane12.setVisible(false);

        add(scrollPane13);
        scrollPane13.setBounds(700, 80, 260, 400);
        scrollPane13.setVisible(false);

        setLayout(null);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button1) {
            label1.setVisible(false);
            button1.setVisible(false);
            button2.setVisible(false);
            button3.setVisible(false);
            button4.setVisible(true);
            button5.setVisible(true);
            button6.setVisible(true);
            label2.setVisible(true);
        }

        if (e.getSource() == button2) {
            label1.setVisible(false);
            button1.setVisible(false);
            button2.setVisible(false);
            button3.setVisible(false);
            button10.setVisible(true);
            button11.setVisible(true);
            button12.setVisible(true);
            label8.setVisible(true);
        }
        if (e.getSource() == button3) {
            label1.setVisible(false);
            button1.setVisible(false);
            button2.setVisible(false);
            button3.setVisible(false);
            button16.setVisible(true);
            button17.setVisible(true);
            button18.setVisible(true);
            label17.setVisible(true);
        }

        if (e.getSource() == button10) {
            button10.setVisible(false);
            button11.setVisible(false);
            button12.setVisible(false);
            label8.setVisible(false);
            label1.setVisible(true);
            button1.setVisible(true);
            button2.setVisible(true);
            button3.setVisible(true);
        }

        if (e.getSource() == button11) {
            label9.setVisible(true);
            label10.setVisible(true);
            label11.setVisible(true);
            label12.setVisible(true);

            label8.setVisible(false);
            button10.setVisible(false);
            button11.setVisible(false);
            button12.setVisible(false);
            button13.setVisible(true);

            textArea6.setVisible(true);
            scrollPane6.setVisible(true);
            textArea7.setVisible(true);
            scrollPane7.setVisible(true);
            scrollPane8.setVisible(true);
            textArea9.setVisible(true);
            scrollPane9.setVisible(true);
            lexicalAnalyser.input_path1 = "data//conversionTable.txt";
            lexicalAnalyser.input_path2 = "data//input2.txt";
            lexicalAnalyser.output_path = "data//result2.txt";
            StringBuilder stringBuilder = new StringBuilder();
            try {
                BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/production.txt"))));
                String lines;
                while ((lines = bf.readLine()) != null) {
                    stringBuilder.append(lines + "\n");
                }
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            textArea6.setText(stringBuilder.toString());
            try {
                if (syntaxAnalysis.symbol_list.size() == 0) {
                    syntaxAnalysis.init();
                    syntaxAnalysis.getFirst();
                    syntaxAnalysis.getFollow();
                    syntaxAnalysis.getSelect();
                    syntaxAnalysis.buildItemFamily();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            StringBuilder stringBuilder1 = new StringBuilder();
            StringBuilder stringBuilder2 = new StringBuilder();

            for (Symbol s : syntaxAnalysis.symbols) {
                if (s.getFlag() == 'N') {
                    stringBuilder1.append(s.getName() + ": { ");
                    for (int i = 0; i < s.follow.size(); i++) {
                        if (i != s.follow.size() - 1) {
                            stringBuilder1.append(s.follow.get(i) + ", ");
                        } else {
                            stringBuilder1.append(s.follow.get(i) + " }\n");
                        }
                    }
                }
            }

            stringBuilder2.append("非终结符：\n");
            for (Symbol s : syntaxAnalysis.symbols) {
                if (s.getFlag() == 'N') {
                    stringBuilder2.append(s.getName() + ": { ");
                    for (int i = 0; i < s.first.size(); i++) {
                        if (i != s.first.size() - 1) {
                            stringBuilder2.append(s.first.get(i) + ", ");
                        } else {
                            stringBuilder2.append(s.first.get(i) + " }\n");
                        }
                    }
                }
            }
            stringBuilder2.append("终结符：\n");
            for (Symbol s : syntaxAnalysis.symbols) {
                if (s.getFlag() == 'T') {
                    stringBuilder2.append(s.getName() + ": { ");
                    for (int i = 0; i < s.first.size(); i++) {
                        if (i != s.first.size() - 1) {
                            stringBuilder2.append(s.first.get(i) + ", ");
                        } else {
                            stringBuilder2.append(s.first.get(i) + " }\n");
                        }
                    }
                }
            }
            textArea7.setText(stringBuilder2.toString());
            textArea9.setText(stringBuilder1.toString());
            ArrayList<String> headers = new ArrayList<>();
            for (int i = 0; i < syntaxAnalysis.symbol_list.size(); i++) {
                headers.add(syntaxAnalysis.symbol_list.get(i));
            }
            String[] header = new String[headers.size()];

            for (int i = 0; i < syntaxAnalysis.symbol_list.size(); i++) {
                header[i] = syntaxAnalysis.symbol_list.get(i);
            }

            String datas[][] = syntaxAnalysis.get_action_go();

            DefaultTableModel model = new DefaultTableModel(datas, header);
            JTable jTable = new JTable(model);
            jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            scrollPane8.setViewportView(jTable);
        }
        if (e.getSource() == button13) {
            label9.setVisible(false);
            label10.setVisible(false);
            label11.setVisible(false);
            label12.setVisible(false);

            label8.setVisible(true);
            button10.setVisible(true);
            button11.setVisible(true);
            button12.setVisible(true);
            button13.setVisible(false);

            textArea6.setVisible(false);
            scrollPane6.setVisible(false);
            textArea7.setVisible(false);
            scrollPane7.setVisible(false);
            scrollPane8.setVisible(false);
            textArea9.setVisible(false);
            scrollPane9.setVisible(false);
        }

        if (e.getSource() == button4) {
            label1.setVisible(true);
            button1.setVisible(true);
            button2.setVisible(true);
            button3.setVisible(true);
            button4.setVisible(false);
            button5.setVisible(false);
            button6.setVisible(false);
            label2.setVisible(false);
        }
        if (e.getSource() == button16) {
            label1.setVisible(true);
            button1.setVisible(true);
            button2.setVisible(true);
            button3.setVisible(true);
            button16.setVisible(false);
            button17.setVisible(false);
            button18.setVisible(false);
            label17.setVisible(false);
        }


        if (e.getSource() == button17) {
            label18.setVisible(true);
            label17.setVisible(false);
            button16.setVisible(false);
            button17.setVisible(false);
            button18.setVisible(false);
            button19.setVisible(true);
            textArea14.setVisible(true);
            scrollPane14.setVisible(true);
            try {
                BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/translate.txt"))));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                int flag = 1;
                while ((line = bf.readLine()) != null) {
                    stringBuilder.append(flag + ". " + line + "\n");
                    flag += 1;
                }
                textArea14.setText(stringBuilder.toString());
            } catch (IOException e1) {
            }
        }


        if (e.getSource() == button19) {
            label18.setVisible(false);
            label17.setVisible(true);
            button16.setVisible(true);
            button17.setVisible(true);
            button18.setVisible(true);
            button19.setVisible(false);
            textArea14.setVisible(false);
            scrollPane14.setVisible(false);
        }


        if (e.getSource() == button5) {
            label3.setVisible(true);
            label2.setVisible(false);
            button4.setVisible(false);
            button5.setVisible(false);
            button6.setVisible(false);
            button7.setVisible(true);
            textArea1.setVisible(true);
            scrollPane1.setVisible(true);
            try {
                lexicalAnalyser.input_path1 = JOptionPane.showInputDialog("请输入词法分析dfa转换表文件存储路径：(data/conversiontable.txt)");

                BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(new File(lexicalAnalyser.input_path1))));

                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bf.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                textArea1.setText(stringBuilder.toString());
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(this, "输入文件路径有误，请重新输入！");
                label3.setVisible(false);
                button4.setVisible(true);
                button5.setVisible(true);
                button6.setVisible(true);
                label2.setVisible(true);
                textArea1.setVisible(false);
                scrollPane1.setVisible(false);
                button7.setVisible(false);
            } catch (NullPointerException e2) {
                label3.setVisible(false);
                button7.setVisible(false);
                button4.setVisible(true);
                button5.setVisible(true);
                button6.setVisible(true);
                label2.setVisible(true);
                textArea1.setVisible(false);
                scrollPane1.setVisible(false);
            }
        }


        if (e.getSource() == button7) {
            button4.setVisible(true);
            button5.setVisible(true);
            button6.setVisible(true);
            label3.setVisible(false);
            label2.setVisible(true);
            textArea1.setVisible(false);
            scrollPane1.setVisible(false);
            button7.setVisible(false);
        }


        if (e.getSource() == button18) {
            label17.setVisible(false);
            button16.setVisible(false);
            button17.setVisible(false);
            button18.setVisible(false);
            button20.setVisible(true);
            button21.setVisible(true);
            textArea15.setVisible(true);
            scrollPane15.setVisible(true);
            scrollPane16.setVisible(true);
            scrollPane17.setVisible(true);
            textArea16.setVisible(true);
            scrollPane18.setVisible(true);
            textArea18.setVisible(true);
            label19.setVisible(true);
            label21.setVisible(true);
            label22.setVisible(true);
            label23.setVisible(true);
        }
        if (e.getSource() == button21) {
            label17.setVisible(true);
            button16.setVisible(true);
            button17.setVisible(true);
            button18.setVisible(true);
            button20.setVisible(false);
            button21.setVisible(false);
            textArea15.setVisible(false);
            textArea16.setVisible(false);
            textArea18.setVisible(false);
            scrollPane15.setVisible(false);
            scrollPane16.setVisible(false);
            scrollPane17.setVisible(false);
            scrollPane18.setVisible(false);
            label19.setVisible(false);
            label21.setVisible(false);
            label22.setVisible(false);
            label23.setVisible(false);
        }

        if (e.getSource() == button6) {
            label2.setVisible(false);
            button4.setVisible(false);
            button5.setVisible(false);
            button6.setVisible(false);
            button8.setVisible(true);
            button9.setVisible(true);
            textArea2.setVisible(true);
            scrollPane2.setVisible(true);
            scrollPane3.setVisible(true);
            textArea3.setVisible(true);
            scrollPane4.setVisible(true);
            textArea4.setVisible(true);
            scrollPane5.setVisible(true);
            textArea5.setVisible(true);
            label4.setVisible(true);
            label5.setVisible(true);
            label6.setVisible(true);
            label7.setVisible(true);
        }
        if (e.getSource() == button12) {
            label8.setVisible(false);
            button10.setVisible(false);
            button11.setVisible(false);
            button12.setVisible(false);

            button14.setVisible(true);
            button15.setVisible(true);

            textArea10.setVisible(true);
            scrollPane10.setVisible(true);
            scrollPane11.setVisible(true);
            textArea11.setVisible(true);
            scrollPane12.setVisible(true);
            textArea12.setVisible(true);
            scrollPane13.setVisible(true);
            label13.setVisible(true);
            label14.setVisible(true);
            label15.setVisible(true);
            label16.setVisible(true);
        }

        if (e.getSource() == button20) {
            try {
                BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/input2.txt"))));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                int flag = 1;
                while ((line = bf.readLine()) != null) {
                    stringBuilder.append(flag + ". " + line + "\n");
                    flag += 1;
                }
                textArea15.setText(stringBuilder.toString());
            } catch (IOException e1) {
            }

            SemanticAnalysis semanticAnalysis = new SemanticAnalysis();
            LexicalAnalyser lexicalAnalyser = new LexicalAnalyser("data//conversionTable.txt", "data//input2.txt", "data//result2.txt");
            try {
                lexicalAnalyser.lexicalAnalyse();
            } catch (Not8Const not8Const) {
                not8Const.printStackTrace();
            } catch (UnknownWord unknownWord) {
                unknownWord.printStackTrace();
            } catch (IncompleteError incompleteError) {
                incompleteError.printStackTrace();
            }

            SyntaxAnalysis syntaxAnalysis = new SyntaxAnalysis();
            try {
                syntaxAnalysis.init();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            syntaxAnalysis.getFirst();
            syntaxAnalysis.getFollow();
            syntaxAnalysis.getSelect();
            syntaxAnalysis.buildItemFamily();
            System.out.println("");
            Node root = null;
            try {
                root = syntaxAnalysis.analyze(lexicalAnalyser.tokens);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            int size = semanticAnalysis.RootToTree(root, 0);
            int tree_size = semanticAnalysis.Stree.size();
            for (int i = 0; i < size; i++) {
                semanticAnalysis.tree_pro.add(new Properties());
            }
            semanticAnalysis.dfs(root);
            textArea16.setText(Api.print_ins(semanticAnalysis.three_addr, semanticAnalysis.four_addr, semanticAnalysis.initial));
//            Api.print_table(semanticAnalysis.table);

            String[] header = new String[4];
            header[0] = "栈编号";
            header[1] = "符号名";
            header[2] = "类型";
            header[3] = "偏移量";
            int sum = 0;
            for (Stack<semanticAnalysis.Symbol> s : semanticAnalysis.table) {
                sum = sum + s.size();
            }

            String datas[][] = new String[sum][4];
            int flag = 0;
            for (int i = 0; i < semanticAnalysis.table.size(); i++) {
                for (semanticAnalysis.Symbol s : semanticAnalysis.table.get(i)) {
                    datas[flag][0] = String.valueOf(i);
                    datas[flag][1] = s.getName();
                    datas[flag][2] = s.getType();
                    datas[flag][3] = String.valueOf(s.getOffset());
                    flag += 1;
                }
            }

            DefaultTableModel model = new DefaultTableModel(datas, header);
            JTable jTable = new JTable(model);
            jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            scrollPane17.setViewportView(jTable);


            textArea18.setText(Api.print_errors(semanticAnalysis.errors));
        }


        if (e.getSource() == button14) {
            try {
                if (syntaxAnalysis.symbol_list.size() == 0) {
                    throw new UnInitError("请先查看语法分析器！");
                }

                lexicalAnalyser.input_path1 = "data//conversionTable.txt";
                lexicalAnalyser.input_path2 = "data//input2.txt";
                lexicalAnalyser.output_path = "data//result2.txt";
                try {
                    BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/input2.txt"))));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    int flag = 1;
                    while ((line = bf.readLine()) != null) {
                        stringBuilder.append(flag + ". " + line + "\n");
                        flag += 1;
                    }
                    textArea10.setText(stringBuilder.toString());
                    lexicalAnalyser.lexicalAnalyse();

                    BufferedReader bf1 = new BufferedReader(new InputStreamReader(new FileInputStream(new File(lexicalAnalyser.output_path))));
                    StringBuilder stringBuilder1 = new StringBuilder();
                    String line1;
                    int flag1 = 1;
                    while ((line1 = bf1.readLine()) != null) {
                        stringBuilder1.append(flag1 + ". " + line1 + "\n");
                        flag1 += 1;
                    }
                    textArea11.setText(stringBuilder1.toString());


                    Node root = syntaxAnalysis.analyze(lexicalAnalyser.tokens);
                    System.out.println(root.getSymbolName());
                    DefaultMutableTreeNode t = new DefaultMutableTreeNode(root.toString());
                    syntaxAnalysis.addNode(root, t);
                    jTree13 = new JTree(t);
                    scrollPane13.getViewport().add(jTree13, null);
                    jTree13.setVisible(true);
//                    setSize(300,500);
//                    JTree jtree = new JTree(t);
//                    add(jtree);
//                    setVisible(true);
//                    BufferedReader bf2 = new BufferedReader(new InputStreamReader(new FileInputStream("data/result3.txt")));
//                    StringBuilder stringBuilder2 = new StringBuilder();
//                    String line2;
//                    while ((line2 = bf2.readLine()) != null) {
//                        stringBuilder2.append(line2 + "\n");
//                    }
//                    jTree13.setText(stringBuilder2.toString());
                    StringBuilder stringBuilder3 = new StringBuilder();
                    int flag2 = 1;
                    for (String s : syntaxAnalysis.error_message) {
                        stringBuilder3.append(flag2 + ". " + s + "\n");
                        flag2++;
                    }
                    textArea12.setText(stringBuilder3.toString());

                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (IncompleteError incompleteError) {
                    incompleteError.printStackTrace();
                } catch (UnknownWord unknownWord) {
                    unknownWord.printStackTrace();
                } catch (Not8Const not8Const) {
                    not8Const.printStackTrace();
                }
            } catch (UnInitError error) {
                JOptionPane.showMessageDialog(this, error.getMessage());
                label8.setVisible(true);
                button10.setVisible(true);
                button11.setVisible(true);
                button12.setVisible(true);

                button14.setVisible(false);
                button15.setVisible(false);

                textArea10.setVisible(false);
                scrollPane10.setVisible(false);
                scrollPane11.setVisible(false);
                textArea11.setVisible(false);
                scrollPane12.setVisible(false);
                textArea12.setVisible(false);
                scrollPane13.setVisible(false);
//                jTree13.setVisible(false);
                label13.setVisible(false);
                label14.setVisible(false);
                label15.setVisible(false);
                label16.setVisible(false);
            }
        }

        if (e.getSource() == button15) {
            label8.setVisible(true);
            button10.setVisible(true);
            button11.setVisible(true);
            button12.setVisible(true);

            button14.setVisible(false);
            button15.setVisible(false);

            textArea10.setVisible(false);
            scrollPane10.setVisible(false);
            scrollPane11.setVisible(false);
            textArea11.setVisible(false);
            scrollPane12.setVisible(false);
            textArea12.setVisible(false);
            scrollPane13.setVisible(false);
//            jTree13.setVisible(false);
            label13.setVisible(false);
            label14.setVisible(false);
            label15.setVisible(false);
            label16.setVisible(false);
        }


        if (e.getSource() == button8) {
            try {
                if (lexicalAnalyser.input_path1 == null) {
                    JOptionPane.showMessageDialog(this, "尚未读入DFA转换表，请点击返回，读入转换表！");
                } else {
                    lexicalAnalyser.input_path2 = JOptionPane.showInputDialog("请输入需要进行词法分析的文件存储路径：(data/input1.txt)");
                    BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(new File(lexicalAnalyser.input_path2))));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    int flag = 1;
                    while ((line = bf.readLine()) != null) {
                        stringBuilder.append(flag + ". " + line + "\n");
                        flag += 1;
                    }
                    textArea2.setText(stringBuilder.toString());
                    lexicalAnalyser.output_path = "data//result1.txt";

                    lexicalAnalyser.lexicalAnalyse();

                    BufferedReader bf1 = new BufferedReader(new InputStreamReader(new FileInputStream(new File(lexicalAnalyser.output_path))));
                    StringBuilder stringBuilder1 = new StringBuilder();
                    String line1;
                    int flag1 = 1;
                    while ((line1 = bf1.readLine()) != null) {
                        stringBuilder1.append(flag1 + ". " + line1 + "\n");
                        flag1 += 1;
                    }
                    textArea3.setText(stringBuilder1.toString());

                    StringBuilder stringBuilder2 = new StringBuilder();
                    for (int i = 0; i < lexicalAnalyser.errorlist.size(); i++) {
                        stringBuilder2.append((i + 1) + ". " + lexicalAnalyser.errorlist.get(i) + "\n");
                    }
                    textArea4.setText(stringBuilder2.toString());

                    StringBuilder stringBuilder3 = new StringBuilder();
                    for (Integer key : lexicalAnalyser.DFA_Seq.keySet()) {
                        stringBuilder3.append(key + ". 0");
                        for (Integer a : lexicalAnalyser.DFA_Seq.get(key))
                            stringBuilder3.append("-->" + a);
                        stringBuilder3.append("\n");
                    }
                    textArea5.setText(stringBuilder3.toString());
                }
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(this, "输入文件路径有误，请重新输入！");
            } catch (NullPointerException e2) {
            } catch (Not8Const not8Const) {
            } catch (UnknownWord unknownWord) {
            } catch (IncompleteError incompleteError) {
            }
        }
        if (e.getSource() == button9) {
            label2.setVisible(true);
            button4.setVisible(true);
            button5.setVisible(true);
            button6.setVisible(true);
            button8.setVisible(false);
            button9.setVisible(false);
            textArea2.setVisible(false);
            scrollPane2.setVisible(false);
            scrollPane3.setVisible(false);
            textArea3.setVisible(false);
            scrollPane4.setVisible(false);
            textArea4.setVisible(false);
            scrollPane5.setVisible(false);
            textArea5.setVisible(false);
            label4.setVisible(false);
            label5.setVisible(false);
            label6.setVisible(false);
            label7.setVisible(false);
        }
    }

    public static void main(String[] args) {
        Gui gui = new Gui();
        JFrame jFrame = new JFrame();
        jFrame.add(gui);
        jFrame.setVisible(true);
        jFrame.setSize(1353, 600);
    }
}
