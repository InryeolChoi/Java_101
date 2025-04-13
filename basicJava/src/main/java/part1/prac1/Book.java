package part1.prac1;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Book {
    private String title;
    private String author;
    private boolean isBorrowed;

    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

    public Book(String title, String author, boolean isBorrowed) {
        this.title = title;
        this.author = author;
        this.isBorrowed = isBorrowed;
    }

    public void borrow() {
        if (isBorrowed == false) {
            isBorrowed = true;
            try {
                bw.write("'{title}' 도서를 대여했습니다.");
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            try {
                bw.write("'{title}' 도서를 빌릴 수 없습니다.");
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void returnBook() {
        if (isBorrowed == true) {
            isBorrowed = false;
            try {
                bw.write("'{title}' 도서를 반납했습니다.");
                bw.newLine();
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            try {
                bw.write("'{title}' 도서를 반납할 수 없습니다.");
                bw.newLine();
                bw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void printInfo() {
        try {
            bw.write("제목: {title} | 저자: {author} |");
            if (isBorrowed == true)
                bw.write("상태: 대여 가능");
            else {
                bw.write("상태: 대여 불가능");
            }
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
