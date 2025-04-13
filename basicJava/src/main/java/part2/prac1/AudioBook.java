package part2.prac1;

public class AudioBook extends LibraryItem implements Playable{
    double duration;

    public AudioBook(String title, String author, double duration) {
        super(title, author);
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }
        this.duration = duration;
    }

    @Override
    public String getDescription() {
        return "[AudioBook] " + title + " by " + author + ", Duration: " + duration + " hours";
    }

    @Override
    public String play() {
        return "▶ Playing audio: " + title + "...";
    }
}
