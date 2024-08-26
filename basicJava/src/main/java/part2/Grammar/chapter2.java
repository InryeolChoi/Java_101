package part2.Grammar;
import java.io.*;

// 더 빠른 입출력
public class chapter2 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        try {
            System.out.print("Enter a line: ");
            String input = br.readLine();

            bw.write("You entered: " + input);
            bw.newLine();
            bw.flush();  // BufferedWriter를 사용 후 반드시 flush 또는 close 호출
        }
        finally {
            if (br != null) br.close();  // BufferedReader 닫기
            if (bw != null) bw.close();  // BufferedWriter 닫기
        }
    }
}
