package part1.prac1;

import lombok.Getter;

@Getter
public class Book {
    private final String title;
    private final String author;
    private boolean isBorrowed;

    public Book(String title, String author, boolean isBorrowed) {
        this.title = title;
        this.author = author;
        this.isBorrowed = isBorrowed;
    }

    public void borrow() {
        if (!isBorrowed) {
            isBorrowed = true;
            System.out.println(title + "도서를 대여했습니다.");
        }
        else
            System.out.println(title + "도서를 빌릴 수 없습니다.");
    }

    public void returnBook() {
        if (isBorrowed) {
            isBorrowed = false;
            System.out.println(title + "도서를 반납했습니다.");
        }
        else
            System.out.println(title + "도서를 반납할 수 없습니다.");
    }

    public void printInfo() {
        System.out.println("제목: {" + title + "} | 저자: {" + author + "} |");
        if (isBorrowed)
            System.out.println("상태: 대여 가능");
        else
            System.out.println("상태: 대여 불가능");
    }
}
