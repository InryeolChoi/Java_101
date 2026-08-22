package part1.prac1;
import java.util.*;

public class Library {
    ArrayList<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
        System.out.println("[추가] " + book.getTitle() + " 도서가 도서관에 추가되었습니다.\n");
    }

    public void borrowBook(String title) {
        while (true) {
            for (Book book : books) {
                if (book.getTitle().equals(title)) {
                    book.borrow();
                    break;
                }
            }
            System.out.println("해당 책이 없습니다.");
        }
    }

    public void returnBook(String title) {
        while (true) {
            for (Book book : books) {
                if (book.getTitle().equals(title)) {
                    book.returnBook();
                    break;
                }
            }
        System.out.println("해당 책은 도서관에 없습니다.");
        }
    }

    public void printAllBooks() {
        for (Book book : books) {
            book.printInfo();
        }
    }
}
