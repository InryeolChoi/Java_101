package part2.prac1;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class Book extends LibraryItem{
    private int pages;

    @Override
    public String getDescription() {
        return "[Book] " + title + " by " + author + ", " + pages + " pages";
    }
}
