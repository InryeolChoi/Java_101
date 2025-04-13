package part1.prac1;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

public class Library {
    ArrayList<Book> books = new ArrayList<>();
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

    public void addBook(Book book) {
        books.add(book);
        try {
            bw.write("[추가] {book.titlie} 도서가 도서관에 추가되었습니다.\n");
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void borrowBook(Book book) {
        books.remove(book);
    }

    public void returnBook(Book book) {
        books.add(book);
    }


}
