import java.util.*;

public class IndexEntry {

    // The string that's the index entry.
    String word;

    // All the movies whose titles contain the word.
    ArrayList<Movie> titleMovies = new ArrayList<>();

    // All the movies whose descriptions contain the word.
    ArrayList<Movie> descriptionMovies = new ArrayList<>();
}