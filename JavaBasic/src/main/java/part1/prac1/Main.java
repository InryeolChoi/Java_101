package part1.prac1;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Library library = new Library();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String title, author;
        Book book;

        try {
            // 3권 등록
            for (int i = 0; i < 3; i++) {
                System.out.println(i + "번째 책의 제목 입력 :");
                title = br.readLine();
                System.out.println(i + "번째 책의 작가 입력 :");
                author = br.readLine();
                book = new Book(title, author, false);
                library.addBook(book);
            }

            // 1권 빌리기
            System.out.println("빌릴 책을 입력해주세요 : ");
            title = br.readLine();
            library.borrowBook(title);

            // 1권 반납하기
            System.out.println("반납할 책을 입력해주세요 : ");
            title = br.readLine();
            library.returnBook(title);

            // 도서관의 모든 책 정보 출력
            library.printAllBooks();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
