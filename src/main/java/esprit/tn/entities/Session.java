package esprit.tn.entities;

import esprit.tn.entities.User; // Assuming you have a User model class

public class Session {
    private static Session instance;
    private User currentUser;

    private Session() {
        // Private constructor to enforce singleton pattern
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void clearSession() {
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}