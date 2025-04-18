package service;

import model.User;
import enums.Role;

import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private List<User> users = new ArrayList<>();

    // Register a new user
    public void registerUser(String username, String password, String email, String phone, String address, Role role) {
        // Check if username is already taken
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                System.out.println("Username already exists. Please choose a different one.");
                return;
            }
        }

        // Create new user and store
        User newUser = new User(username, password, email, phone, address, role);
        users.add(newUser);
        System.out.println("Registration successful for: " + username);
    }

    // Log in a user by checking credentials
    public User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username) && user.verifyPassword(password)) {
                System.out.println("Login successful.");
                return user;
            }
        }

        System.out.println("Login failed. Incorrect username or password.");
        return null;
    }

    // Optional: Show all registered users (debug)
    public void listUsers() {
        System.out.println("--- Registered Users ---");
        for (User user : users) {
            System.out.println(user.getUsername() + " - " + user.getRole());
        }
    }
    
    private static double gymEarnings = 0.0;

    public static double getGymEarnings() {
        return gymEarnings;
    }

    public static void addToEarnings(double amount) {
        gymEarnings += amount;
    }
    
    public void showUserCountReport() {
        int adminCount = 0;
        int trainerCount = 0;
        int memberCount = 0;
    
        for (User user : users) {
            switch (user.getRole()) {
                case ADMIN:
                    adminCount++;
                    break;
                case TRAINER:
                    trainerCount++;
                    break;
                case MEMBER:
                    memberCount++;
                    break;
            }
        }
    
        int total = adminCount + trainerCount + memberCount;
    
        System.out.println("\n--- User Role Report ---");
        System.out.println("Total Registered Users: " + total);
        System.out.println("Admins: " + adminCount);
        System.out.println("Trainers: " + trainerCount);
        System.out.println("Members: " + memberCount);
    }
    
    public boolean deleteUser(String username) {
        return users.removeIf(u -> u.getUsername().equalsIgnoreCase(username));
    }
    
}
