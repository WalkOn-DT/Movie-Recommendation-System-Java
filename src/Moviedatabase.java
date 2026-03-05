import java.io.*;
import java.util.*;

/**
 * Manages movie data storage and retrieval
 * Handles CSV file operations and ID format conversion
 */
public class Moviedatabase {
    private HashMap<Integer, Movie> movies = new HashMap<>();

    /**
     * Loads movies from CSV file
     * @param filename the CSV file name
     * @return true if loading successful
     */
    public boolean loadMovies(String filename) {
        movies.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // Skip header

            int lineNumber = 1;
            int successCount = 0;

            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    try {
                        // Parse movie ID (handles M001 format)
                        String idStr = parts[0].trim();
                        int id;
                        if (idStr.toUpperCase().startsWith("M")) {
                            id = Integer.parseInt(idStr.substring(1));
                        } else {
                            id = Integer.parseInt(idStr);
                        }

                        String title = parts[1].trim();
                        String genre = parts[2].trim();
                        int year = Integer.parseInt(parts[3].trim());
                        double rating = Double.parseDouble(parts[4].trim());

                        // Data validation
                        if (year < 1880 || year > 2030) {
                            System.out.println(String.format("Warning: Invalid year on line %d", lineNumber));
                            continue;
                        }

                        if (rating < 0 || rating > 10) {
                            System.out.println(String.format("Warning: Invalid rating on line %d", lineNumber));
                            continue;
                        }

                        movies.put(id, new Movie(id, title, genre, year, rating));
                        successCount++;

                    } catch (Exception e) {
                        System.out.println(String.format("Warning: Invalid data on line %d: %s", lineNumber, e.getMessage()));
                    }
                }
            }

            System.out.println(String.format(">>> Movies loaded: %d successful", successCount));
            return successCount > 0;

        } catch (FileNotFoundException e) {
            System.out.println(String.format("Error: Movie file not found: %s", filename));
            return false;
        } catch (IOException e) {
            System.out.println(String.format("Error reading movie file: %s", e.getMessage()));
            return false;
        }
    }

    /**
     * Gets a movie by ID
     * @param id the movie ID
     * @return Movie object or null
     */
    public Movie getMovie(int id) {
        return movies.get(id);
    }

    /**
     * Gets all movies
     * @return collection of all movies
     */
    public Collection<Movie> getAllMovies() {
        return movies.values();
    }

    /**
     * Checks if movie exists
     * @param id the movie ID
     * @return true if movie exists
     */
    public boolean exists(int id) {
        return movies.containsKey(id);
    }

    /**
     * Gets movies by genre
     * @param genre the genre to filter by
     * @return list of movies in genre
     */
    public List<Movie> getMoviesByGenre(String genre) {
        List<Movie> result = new ArrayList<>();
        for (Movie movie : movies.values()) {
            if (movie.getGenre().equalsIgnoreCase(genre)) {
                result.add(movie);
            }
        }
        return result;
    }
}