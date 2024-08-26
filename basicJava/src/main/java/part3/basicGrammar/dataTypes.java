package part3.basicGrammar;
import java.io.*;

public class dataTypes {
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException {
        dataTypes obj = new dataTypes();
        obj.primitiveTypes();
        obj.WrapperTypes();
    }

    // 원시자료형
    public void primitiveTypes() {
        int num = 5;
        double num2 = 1.23;
        boolean bool = true;
    }

    // 참조 자료형 : string
    public void WrapperTypes() throws IOException {
        String str1 = br.readLine();
        String str2 = br.readLine();
        if (str1.equals(str2)) {
            bw.write("둘은 같음.");
        } else {
            bw.write("둘은 다름");
        }
        bw.flush();
    }
}