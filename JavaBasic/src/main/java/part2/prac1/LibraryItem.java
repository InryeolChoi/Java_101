package part2.prac1;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
public abstract class LibraryItem {
    protected String title;
    protected String author;

    public abstract String getDescription();
}
