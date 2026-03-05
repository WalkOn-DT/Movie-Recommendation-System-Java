import java.io.*;
import java.util.*;

/**
 * Manages user data storage, authentication, and registration
 * Implements ENCRYPTED STORAGE (Direction 6)
 */
public class UserDatabase {
    private HashMap<String, User> users = new HashMap<>();
    private String filename;

    /**
     * Custom simple encryption function - Shift Cipher
     * @param text the text to encrypt
     * @return encrypted text
     */
    private String encrypt(String text) {
        if (text == null) return "";
        String result = "";
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            // Shift character by 3 positions
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                c = (char)((c - base + 3) % 26 + base);
            } else if (Character.isDigit(c)) {
                c = (char)((c - '0' + 3) % 10 + '0');
            }
            result += c;
        }
        return result;
    }

    // Note: Decrypt method removed as passwords should be stored encrypted.

    /**
     * Loads users from CSV file
     * Modification: Reads the encrypted password directly from file into memory.
     * @param filename the CSV file name
     */
    public void loadUsers(String filename) {
        this.filename = filename;
        users.clear();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", -1);

                if (parts.length >= 2) {
                    String username = parts[0].trim();
                    String storedPassword = parts[1].trim(); // This is already ciphertext

                    String watchlistStr = (parts.length > 2) ? parts[2].trim() : "";
                    String historyStr = (parts.length > 3) ? parts[3].trim() : "";

                    // Create user with the encrypted password directly
                    User user = new User(username, storedPassword);

                    parseIds(watchlistStr, user.getWatchlist());
                    parseIds(historyStr, user.getHistory());

                    users.put(username, user);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Warning: Users file not found. Starting with empty database.");
        } catch (Exception e) {
            System.out.println(String.format("Error loading users: %s", e.getMessage()));
        }
    }

    /**
     * Saves all users to CSV file
     * Modification: Saves the encrypted password directly to CSV (Ciphertext).
     */
    public void saveUsers() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("Username,Password,Watchlist,History");
            for (User user : users.values()) {

                // Get the encrypted password from memory and write it to file
                // This ensures the CSV contains unreadable ciphertext
                String encryptedPassword = user.getPassword();

                pw.println(String.format("%s,%s,%s,%s",
                        user.getUsername(),
                        encryptedPassword,
                        user.watchlistToString(),
                        user.historyToString()));
            }
        } catch (IOException e) {
            System.out.println(String.format("Error saving users: %s", e.getMessage()));
        }
    }

    /**
     * Authenticates user
     * Logic: Encrypts input password and compares with stored ciphertext.
     */
    public User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null) {
            // Encrypt input password to compare with stored ciphertext
            String encryptedInput = encrypt(password);
            if (user.getPassword().equals(encryptedInput)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Registers a new user
     * Logic: Encrypts password before storing in memory.
     */
    public boolean register(String username, String password) {
        if (users.containsKey(username)) {
            return false;
        }
        // Encrypt password immediately upon registration
        String encryptedPassword = encrypt(password);
        users.put(username, new User(username, encryptedPassword));
        saveUsers();
        return true;
    }

    /**
     * Changes user password
     * Logic: Encrypts new password before updating.
     */
    public boolean changePassword(String username, String currentPassword, String newPassword) {
        User user = authenticate(username, currentPassword);
        if (user != null) {
            // Encrypt new password and update user object
            User updatedUser = new User(username, encrypt(newPassword));
            updatedUser.getWatchlist().getMovieIds().addAll(user.getWatchlist().getMovieIds());
            updatedUser.getHistory().getMovieIds().addAll(user.getHistory().getMovieIds());

            users.put(username, updatedUser);
            saveUsers();
            return true;
        }
        return false;
    }

    /**
     * Resets password (Recovery)
     */
    public boolean resetPassword(String username, String newPassword) {
        if (users.containsKey(username)) {
            User user = users.get(username);
            User updatedUser = new User(username, encrypt(newPassword));
            updatedUser.getWatchlist().getMovieIds().addAll(user.getWatchlist().getMovieIds());
            updatedUser.getHistory().getMovieIds().addAll(user.getHistory().getMovieIds());

            users.put(username, updatedUser);
            saveUsers();
            return true;
        }
        return false;
    }

    /**
     * Checks if user exists
     */
    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    /**
     * Parses ID strings from CSV format
     */
    private void parseIds(String str, Object targetList) {
        if (str == null || str.isEmpty()) return;
        String[] items = str.split(";");
        for (String item : items) {
            try {
                String cleanId = item.trim();
                if (cleanId.contains("@")) cleanId = cleanId.split("@")[0];
                if (cleanId.toUpperCase().startsWith("M")) cleanId = cleanId.substring(1);
                if (!cleanId.isEmpty()) {
                    int id = Integer.parseInt(cleanId);
                    if (targetList instanceof Watchlist) {
                        ((Watchlist) targetList).addMovie(id);
                    } else if (targetList instanceof History) {
                        ((History) targetList).addMovie(id);
                    }
                }
            } catch (NumberFormatException e) {
                // Ignore invalid IDs
            }
        }
    }
}