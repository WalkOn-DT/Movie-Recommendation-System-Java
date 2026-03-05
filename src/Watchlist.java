import java.util.ArrayList;

/**
 * Manages user's movie watchlist
 */
public class Watchlist {
    private ArrayList<Integer> movieIds;

    /**
     * Creates a new empty watchlist
     */
    public Watchlist() {
        this.movieIds = new ArrayList<>();
    }

    /**
     * Adds a movie to the watchlist
     */
    public void addMovie(int movieId) {
        if (!movieIds.contains(movieId)) {
            movieIds.add(movieId);
        }
    }

    /**
     * Removes a movie from the watchlist
     */
    public void removeMovie(int movieId) {
        movieIds.remove(Integer.valueOf(movieId));
    }

    /**
     * Checks if watchlist contains a movie
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