package part3.basicGrammar;

import java.io.*;

public class LearnCondition {
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

    public static void main(String[] args) throws IOException {
        LearnCondition learnCondition = new LearnCondition();
        int number;

        try {
            number = Integer.parseInt(learnCondition.br.readLine()); // static으로 써야 함.
            System.out.println(number);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            learnCondition.thinkIf(number);
            learnCondition.thinkSwitch(number);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            learnCondition.br.close();
            learnCondition.bw.close();
        }
    }

    public void thinkIf(int x) throws IOException {
        if (x != 5)
            bw.write("5이다");
        else
            bw.write("5가 아니다");
    }

    public void thinkSwitch(int x) throws IOException {
        switch(x) {
            case 5:
                bw.write("5이다.");
                break;
            case 6:
                bw.write("6이다");
                break;
            default:
                bw.write("지정되지 않은 숫자입니다.\n");
                break;
        }
    }

}
