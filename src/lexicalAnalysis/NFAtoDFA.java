package lexicalAnalysis;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class NFAtoDFA {
    public HashMap<transfer, Integer> dfa = new HashMap<>();
    public HashMap<Integer, ArrayList<Integer>> dfa_state_F = new HashMap<>();

    class transfer {
        public int state;
        public int word_Flag;

        public transfer(int state, int word_Flag) {
            this.state = state;
            this.word_Flag = word_Flag;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + state;
            result = prime * result + word_Flag;
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
            transfer other = (transfer) obj;
            if (state != other.state) {
                return false;
            }
            if (word_Flag != other.word_Flag) {
                return false;
            }
            return true;
        }

    }

    public void Nfatodfa() throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(new File("data/input_nfa.txt"))));
        HashMap<transfer, ArrayList<Integer>> nfa = new HashMap<>();
        HashMap<Integer, ArrayList<Integer>> close = new HashMap<>();
        String line = bf.readLine();
        int words = 0;//字符总数
        int state_flag = 0;
        while ((line = bf.readLine()) != null) {
            String[] temp = line.split("\\s+");
            words = temp.length - 2;
            for (int j = 1; j < temp.length; j++) {
                String temp1[] = temp[j].split(",");
                ArrayList<Integer> temp2 = new ArrayList<>();
                for (String i : temp1) {
                    if (!i.equals("n")) {
                        temp2.add(Integer.valueOf(i).intValue());
                    }
                }
                nfa.put(new transfer(state_flag, j - 1), temp2);
            }
            state_flag++;
        }
        //计算克林闭包
        for (transfer t : nfa.keySet()) {
            if (t.word_Flag == words) {
                ArrayList<Integer> temp3 = new ArrayList<>();
                temp3.add(t.state);
                for (Integer i : nfa.get(t)) {
                    temp3.add(i);
                }
                close.put(t.state, temp3);
            }
        }
        while (true) {
            int change = 0;//闭包集有变化，则继续更新
            for (Integer i : close.keySet()) {
                for (Integer j : close.keySet()) {
                    if (i != j) {
                        for (Integer k : close.get(j)) {
                            if (!close.get(i).contains(k) && close.get(i).contains(j)) {
                                close.get(i).add(k);
                                change = 1;
                            }
                        }
                    }
                }
            }
            if (change == 0)
                break;
        }

        //转化成dfa
        ArrayList<Integer> temp4 = new ArrayList<>();
        temp4.add(0);
        dfa_state_F.put(0, temp4);
        int now_state = 0;
        int new_state = 0;

        while (true) {
            int over = 1;
            for (int word = 0; word < words; word++) {
                int find_key = -1;
                ArrayList<Integer> temp5 = new ArrayList<>();
                for (Integer i : dfa_state_F.get(now_state)) {
                    for (Integer j : nfa.get(new transfer(i, word))) {
                        for (Integer k : close.get(j)) {
                            if (!temp5.contains(k))
                                temp5.add(k);
                        }
                    }
                }
                int is_find = 0;
                for (Integer i : dfa_state_F.keySet()) {
                    int is_exist = 1;
                    for (Integer j : dfa_state_F.get(i)) {
                        if (temp5.contains(j))
                            continue;
                        else {
                            is_exist = 0;
                            break;
                        }
                    }
                    if (is_exist == 1) {
                        find_key = i;
                        for (Integer j : temp5) {
                            if (dfa_state_F.get(i).contains(j))
                                continue;
                            else {
                                is_exist = 0;
                                break;
                            }
                        }
                    }
                    if (is_exist == 0) {
                        continue;
                    } else {
                        is_find = 1;
                        break;
                    }
                }

                if (is_find == 1) {
                    if (dfa.get(new transfer(now_state, word)) == null) {
                        dfa.put(new transfer(now_state, word), find_key);
                        over = 0;
                    }
                } else {
                    new_state++;
                    dfa_state_F.put(new_state, temp5);
                    dfa.put(new transfer(now_state, word), new_state);
                    over = 0;
                }
            }
            int counts = 0;
            for (transfer t : dfa.keySet()) {
                if (t.state == now_state) {
                    counts++;
                }
            }
            if (counts == words) {
                now_state++;
            }

            if (dfa_state_F.get(now_state) == null) {
                break;
            }
            if (over == 1) {
                break;
            }
        }

    }

    public void test() {
        transfer transfer = new transfer(1, 1);
        transfer transfer1 = new transfer(1, 1);
        HashMap<transfer, Integer> hashMap = new HashMap<>();
        hashMap.put(transfer, 1);
        System.out.println(hashMap.get(transfer1));
    }

    public void writeresult() throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/output_dfa.txt")));
        for (Integer i : dfa_state_F.keySet()) {
            bw.write(i + " [ ");
            for (Integer j : dfa_state_F.get(i)) {
                bw.write(j + " ");
            }
            bw.write("]\n");
        }
        bw.write("\n");
        for (transfer t : dfa.keySet()) {
            bw.write(t.state + " " + t.word_Flag + " " + dfa.get(t) + "\n");
        }
        bw.close();
    }

    public static void main(String[] args) throws IOException {
        NFAtoDFA nfAtoDFA = new NFAtoDFA();
        nfAtoDFA.Nfatodfa();
//        nfAtoDFA.test();
        nfAtoDFA.writeresult();
        System.out.println("");
    }
}
