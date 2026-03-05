import java.util.*;

/**
 * Generates personalized movie recommendations
 * Implements multiple recommendation strategies without sorting
 */
public class RecommendationEngine {

    /**
     * Generates top N movie recommendations using multiple strategies
     * Does not use any sorting algorithms as per requirements
     */
    public List<Movie> getRecommendations(User user, Moviedatabase movieDB, int topN) {
        // Try genre-based recommendations first
        List<Movie> recommendations = getGenreBasedRecommendations(user, movieDB, topN);

        // If no genre-based recommendations, try other strategies
        if (recommendations.isEmpty()) {
            recommendations = getDiverseRecommendations(user, movieDB, topN);
        }

        return recommendations;
    }

    /**
     * Genre-based recommendation strategy - Advanced Functionality
     * Recommends movies from user's favorite genres without sorting
     */
    private List<Movie> getGenreBasedRecommendations(User user, Moviedatabase movieDB, int topN) {
        Map<String, Integer> genrePreferences = new HashMap<>();

        // Analyze user preferences from history and watchlist
        for (int id : user.getHistory().getMovieIds()) {
            Movie movie = movieDB.getMovie(id);
            if (movie != null) {
                String genre = movie.getGenre();
                genrePreferences.put(genre, genrePreferences.getOrDefault(genre, 0) + 2);
            }
        }

        for (int id : user.getWatchlist().getMovieIds()) {
            Movie movie = movieDB.getMovie(id);
            if (movie != null) {
                String genre = movie.getGenre();
                genrePreferences.put(genre, genrePreferences.getOrDefault(genre, 0) + 1);
            }
        }

        if (genrePreferences.isEmpty()) {
            return new ArrayList<>();
        }

        // Find favorite genre without sorting
        String favoriteGenre = findFavoriteGenre(genrePreferences);

        System.out.println(String.format("(Info: Favorite genre detected: %s)", favoriteGenre));
        return getMoviesByGenre(user, movieDB, topN, favoriteGenre);
    }

    /**
     * Finds favorite genre without using sorting
     */
    private String findFavoriteGenre(Map<String, Integer> genrePreferences) {
        String favoriteGenre = "";
        int maxScore = -1;

        // Use entrySet to iterate through map (allowed by requirements)
        for (Map.Entry<String, Integer> entry : genrePreferences.entrySet()) {
            if (entry.getValue() > maxScore) {
                maxScore = entry.getValue();
                favoriteGenre = entry.getKey();
            }
        }

        return favoriteGenre;
    }

    /**
     * Gets movies by genre without sorting - just returns first N found
     */
    private List<Movie> getMoviesByGenre(User user, Moviedatabase movieDB, int topN, String genre) {
        List<Movie> candidates = new ArrayList<>();
        int count = 0;

        // Simply take the first N movies that match the criteria
        for (Movie movie : movieDB.getAllMovies()) {
            if (count >= topN) break;

            if (!user.getHistory().contains(movie.getId()) &&
                    !user.getWatchlist().contains(movie.getId()) &&
                    movie.getGenre().equalsIgnoreCase(genre)) {
                candidates.add(movie);
                count++;
            }
        }

        return candidates;
    }

    /**
     * Diverse recommendation strategy - Advanced Functionality
     * Recommends a mix of different genres without sorting
     */
    private List<Movie> getDiverseRecommendations(User user, Moviedatabase movieDB, int topN) {
        System.out.println("(Info: Using diverse movie recommendations)");
        List<Movie> recommendations = new ArrayList<>();
        Set<String> usedGenres = new HashSet<>();
        int count = 0;

        // Simply iterate through movies and pick diverse ones
        for (Movie movie : movieDB.getAllMovies()) {
            if (count >= topN) break;

            if (!user.getHistory().contains(movie.getId()) &&
                    !user.getWatchlist().contains(movie.getId()) &&
                    !usedGenres.contains(movie.getGenre())) {

                recommendations.add(movie);
                usedGenres.add(movie.getGenre());
                count++;
            }
        }

        // If we still need more recommendations, fill with any available movies
        if (recommendations.size() < topN) {
            for (Movie movie : movieDB.getAllMovies()) {
                if (recommendations.size() >= topN) break;

                if (!user.getHistory().contains(movie.getId()) &&
                        !user.getWatchlist().contains(movie.getId()) &&
                        !recommendations.contains(movie)) {

                    recommendations.add(movie);
                }
            }
        }

        return recommendations;
    }

    /**
     * Year-based recommendation strategy - Advanced Functionality
     * Recommends movies from specific years without sorting
     */
    public List<Movie> getRecommendationsByYear(User user, Moviedatabase movieDB, int topN, int targetYear) {
        List<Movie> recommendations = new ArrayList<>();
        int count = 0;

        for (Movie movie : movieDB.getAllMovies()) {
            if (count >= topN) break;

            if (!user.getHistory().contains(movie.getId()) &&
                    !user.getWatchlist().contains(movie.getId()) &&
                    movie.getYear() == targetYear) {

                recommendations.add(movie);
                count++;
            }
        }

        return recommendations;
    }
}