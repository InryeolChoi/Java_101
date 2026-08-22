package part2.prac1;

public class Magazine extends LibraryItem {
    int month;

    public Magazine(String title, String author, int month) {
        super(title, author);
        this.month = month;
    }

    @Override
    public String getDescription() {
        return "[Magazine] " + title + " by " + author + ", published in " + month + ", month";
    }

}
