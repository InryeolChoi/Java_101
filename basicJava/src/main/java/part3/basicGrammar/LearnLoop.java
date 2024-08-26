package part3.basicGrammar;

import java.io.*;

public class LearnLoop {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

    public static void main(String[] args) throws IOException {
        LearnLoop obj = new LearnLoop();

        obj.thinkfor();
        obj.thinkWhile();
    }

    public void thinkfor() throws IOException {
        for (int i = 0; i < 10; i++) {
            bw.write(i);
        }
    }

    public void thinkWhile() throws IOException {
        int i = 10;
        while (i < 10) {
            bw.write(br.readLine());
            i--;
        }
    }
}
