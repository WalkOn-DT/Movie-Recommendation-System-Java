import java.io.*;
import java.util.*;

/**
 * Main application class for Movie Recommendation System
 * Coordinates all system functionality and user interaction
 */
public class MovieApp {
    private Moviedatabase movieDB = new Moviedatabase();
    private UserDatabase userDB = new UserDatabase();
    private RecommendationEngine recommender = new RecommendationEngine();
    private Scanner scanner = new Scanner(System.in);
    private User currentUser = null;

    /**
     * Starts the movie recommendation system
     */
    public void start() {
        System.out.println(">>> System initializing...");

        // Load data files
        movieDB.loadMovies("movies.csv");
        userDB.loadUsers("users.csv");

        System.out.println(">>> Data loaded successfully.");
        System.out.println("Welcome to the Movie Recommender System!");

        mainLoop();
    }

    /**
     * Main program loop handling user navigation
     */
    private void mainLoop() {
        while (true) {
            if (currentUser == null) {
                showMainMenu();
            } else {
                showUserMenu();
            }
        }
    }

    // ---------------- Main Menu (Not Logged In) ----------------
    private void showMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Login");
        System.out.println("2. Create New Account");
        System.out.println("3. Change Password"); // Updated to match new features
        System.out.println("4. Password Recovery");
        System.out.println("5. Exit");
        System.out.print("Select option: ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1": login(); break;
            case "2": register(); break;
            case "3": changePassword(); break;
            case "4": passwordRecovery(); break;
            case "5":
                System.out.println("Thank you for using Movie Recommender System!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    // ---------------- User Menu (Logged In) ----------------
    private void showUserMenu() {
        System.out.println("\n--- User Menu (" + currentUser.getUsername() + ") ---");
        System.out.println("1. Browse Movies");
        System.out.println("2. Add to Watchlist");
        System.out.println("3. Remove from Watchlist");
        System.out.println("4. View Watchlist");
        System.out.println("5. Mark as Watched");
        System.out.println("6. View History & Stats"); // Updated Label
        System.out.println("7. Get Recommendations");
        System.out.println("8. Change Password");
        System.out.println("9. Logout");
        System.out.print("Select option: ");

        String choice = scanner.nextLine();
        switch (choice) {
            case "1": browseMovies(); break;
            case "2": addToWatchlist(); break;
            case "3": removeFromWatchlist(); break;
            case "4": viewWatchlist(); break;
            case "5": markAsWatched(); break;
            case "6": viewHistory(); break; // This method is now updated
            case "7": getRecommendations(); break;
            case "8": changePassword(); break;
            case "9":
                currentUser = null;
                System.out.println("Logged out successfully.");
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    // --- Authentication Methods ---

    private void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Error: Username and password cannot be empty.");
            return;
        }

        currentUser = userDB.authenticate(username, password);
        if (currentUser != null) {
            System.out.println("Login successful! Welcome back, " + username + "!");
        } else {
            System.out.println("Error: Invalid username or password.");
        }
    }

    private void register() {
        System.out.print("Choose a username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Choose a password: ");
        String password = scanner.nextLine().trim();

        if (username.length() < 3 || password.length() < 4) {
            System.out.println("Error: Username > 3 chars, Password > 4 chars.");
            return;
        }

        if (userDB.register(username, password)) {
            System.out.println("Account created successfully! Please login.");
        } else {
            System.out.println("Error: Username already exists.");
        }
    }

    private void changePassword() {
        String username;
        String currentPassword;

        if (currentUser == null) {
            System.out.print("Enter your username: ");
            username = scanner.nextLine().trim();
            System.out.print("Enter your current password: ");
            currentPassword = scanner.nextLine().trim();
        } else {
            username = currentUser.getUsername();
            System.out.print("Enter your current password: ");
            currentPassword = scanner.nextLine().trim();
        }

        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine().trim();

        if (newPassword.length() < 4) {
            System.out.println("Error: New password must be at least 4 chars.");
            return;
        }

        if (userDB.changePassword(username, currentPassword, newPassword)) {
            System.out.println("Password changed successfully!");
            // Re-authenticate to update session if logged in
            if (currentUser != null) {
                currentUser = userDB.authenticate(username, newPassword);
            }
        } else {
            System.out.println("Error: Failed to change password.");
        }
    }

    private void passwordRecovery() {
        System.out.print("Enter your username: ");
        String username = scanner.nextLine().trim();

        if (userDB.userExists(username)) {
            System.out.print("Enter new password: ");
            String newPassword = scanner.nextLine().trim();
            if (userDB.resetPassword(username, newPassword)) {
                System.out.println("Password reset successfully!");
            }
        } else {
            System.out.println("Error: Username not found.");
        }
    }

    // --- Movie Management Methods ---

    private void browseMovies() {
        System.out.println("\n--- All Movies ---");
        Collection<Movie> allMovies = movieDB.getAllMovies();
        if (allMovies.isEmpty()) {
            System.out.println("No movies loaded.");
            return;
        }
        int count = 1;
        for (Movie movie : allMovies) {
            System.out.println(String.format("%d. %s", count++, movie));
        }
    }

    private void addToWatchlist() {
        System.out.print("Enter Movie ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            if (movieDB.exists(id)) {
                if (!currentUser.getWatchlist().contains(id)) {
                    currentUser.getWatchlist().addMovie(id);
                    userDB.saveUsers();
                    System.out.println("Added to watchlist.");
                } else {
                    System.out.println("Already in watchlist.");
                }
            } else {
                System.out.println("Invalid Movie ID.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private void removeFromWatchlist() {
        viewWatchlist();
        System.out.print("Enter Movie ID to remove: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            if (currentUser.getWatchlist().contains(id)) {
                currentUser.getWatchlist().removeMovie(id);
                userDB.saveUsers();
                System.out.println("Removed from watchlist.");
            } else {
                System.out.println("Not in watchlist.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    private void viewWatchlist() {
        System.out.println("\n--- Your Watchlist ---");
        ArrayList<Integer> movieIds = currentUser.getWatchlist().getMovieIds();
        if (movieIds.isEmpty()) System.out.println("Watchlist is empty.");
        for (int id : movieIds) {
            Movie m = movieDB.getMovie(id);
            if (m != null) System.out.println(m);
        }
    }

    private void markAsWatched() {
        System.out.print("Enter Movie ID you watched: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            if (movieDB.exists(id)) {
                currentUser.getHistory().addMovie(id);
                if (currentUser.getWatchlist().contains(id)) {
                    currentUser.getWatchlist().removeMovie(id);
                }
                userDB.saveUsers();
                System.out.println("Marked as watched.");
            } else {
                System.out.println("Invalid Movie ID.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
        }
    }

    /**
     * DISPLAYS HISTORY AND STATISTICS (Direction 6 Requirements)
     * Shows list of movies, total count, and most frequent genre.
     */
    private void viewHistory() {
        System.out.println("\n--- Your Viewing History ---");
        ArrayList<Integer> movieIds = currentUser.getHistory().getMovieIds();

        if (movieIds.isEmpty()) {
            System.out.println("Your viewing history is empty.");
            return;
        }

        // 1. List Movies
        Map<String, Integer> genreCounts = new HashMap<>();

        for (int id : movieIds) {
            Movie movie = movieDB.getMovie(id);
            if (movie != null) {
                System.out.println(movie);
                // Count genres for statistics
                String g = movie.getGenre();
                genreCounts.put(g, genreCounts.getOrDefault(g, 0) + 1);
            }
        }

        // 2. Calculate Most Frequent Genre
        String topGenre = "None";
        int maxCount = 0;
        for (Map.Entry<String, Integer> entry : genreCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                topGenre = entry.getKey();
            }
        }

        // 3. Display Statistics
        System.out.println("\n>>> User Statistics <<<");
        System.out.println("Total Movies Watched: " + movieIds.size());
        System.out.println("Most Frequently Watched Genre: " + topGenre + " (" + maxCount + " times)");
        System.out.println("-------------------------");
    }

    private void getRecommendations() {
        System.out.println("\n--- Recommendations ---");
        List<Movie> recs = recommender.getRecommendations(currentUser, movieDB, 3);
        if (recs.isEmpty()) System.out.println("No recommendations available yet.");
        for (int i = 0; i < recs.size(); i++) {
            System.out.println((i + 1) + ". " + recs.get(i));
        }
    }

    public static void main(String[] args) {
        new MovieApp().start();
    }
}