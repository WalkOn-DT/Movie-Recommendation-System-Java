/**
 * Represents a movie with details and metadata
 */
public class Movie {
    private int id;
    private String title;
    private String genre;
    private int year;
    private double rating;

    /**
     * Creates a new Movie object
     */
    public Movie(int id, String title, String genre, int year, double rating) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.rating = rating;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public int getYear() { return year; }
    public double getRating() { return rating; }

    /**
     * Returns formatted string representation
     */
    public String toString() {
        return String.format("[%d] %s (%d) - %s [Rating: %.1f]",
                id, title, year, genre, rating);
    }
}