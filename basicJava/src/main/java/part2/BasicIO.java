package part2;
import java.util.Scanner;

// 기본적인 입출력
public class BasicIO {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s1 = sc.next();
        System.out.println(s1);

        // 마지막에 파일 디스크립터를 닫아주는 것이 필요
        sc.close();
    }
}
