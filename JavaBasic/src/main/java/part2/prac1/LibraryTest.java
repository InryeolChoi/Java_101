package part2.prac1;

import java.util.List;


public class LibraryTest {
    public static void main(String[] args) {
        try {
            Book book = Book.builder()
                    .title("Effective Java")
                    .author("Joshua Bloch")
                    .pages(412)
                    .build();

            Magazine magazine = new Magazine("Java Monthly", "Oracle", 4);
            AudioBook audioBook = new AudioBook("Clean Code", "Robert Martin", 3.5);

            List<LibraryItem> items = List.of(book, magazine, audioBook);
            for (LibraryItem item : items) {
                System.out.println(item.getDescription());
                if (item instanceof Playable) {
                    ((Playable) item).play();
                }
            }

        } catch (IllegalArgumentException e) {
            System.out.println("잘못된 입력: " + e.getMessage());
        }
    }
}