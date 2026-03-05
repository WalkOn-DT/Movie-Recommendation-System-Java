import java.util.ArrayList;

/**
 * Manages user's movie viewing history
 */
public class History {
    private ArrayList<Integer> movieIds;

    /**
     * Creates a new empty history
     */
    public History() {
        this.movieIds = new ArrayList<>();
    }

    /**
     * Adds a movie to the history
     */
    public void addMovie(int movieId) {
        if (!movieIds.contains(movieId)) {
            movieIds.add(movieId);
        }
    }

    /**
     * Checks if history contains a movie
     */
    public boolean contains(int movieId) {
        return movieIds.contains(movieId);
    }

    /**
     * Gets all movie IDs
     */
    public ArrayList<Integer> getMovieIds() {
        return movieIds;
    }
}