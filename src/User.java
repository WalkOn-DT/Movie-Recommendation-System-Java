import java.util.ArrayList;

/**
 * Represents a user in the movie recommendation system
 */
public class User {
    private String username;
    private String password;
    private Watchlist watchlist;
    private History history;

    /**
     * Creates a new user
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.watchlist = new Watchlist();
        this.history = new History();
    }

    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Watchlist getWatchlist() { return watchlist; }
    public History getHistory() { return history; }

    /**
     * Converts watchlist to CSV string format
     */
    public String watchlistToString() {
        return idsToString(watchlist.getMovieIds());
    }

    /**
     * Converts history to CSV string format
     */
    public String historyToString() {
        return idsToString(history.getMovieIds());
    }

    /**
     * Converts integer ID list to formatted string
     */
    private String idsToString(ArrayList<Integer> list) {
        if (list.isEmpty()) return "";

        String result = "";
        for (int i = 0; i < list.size(); i++) {
            result += String.format("M%03d", list.get(i));
            if (i < list.size() - 1) result += ";";
        }
        return result;
    }
}