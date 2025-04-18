import model.User;
import service.AuthService;
import enums.Role;
import model.WorkoutPlan;
import java.util.ArrayList;
import java.util.List;

import java.util.Scanner;

public class GymApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AuthService authService = new AuthService();
        boolean running = true;

        while (running) {
            System.out.println("\n--- Gym Registration System ---");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleRegistration(scanner, authService);
                    break;
                case "2":
                    handleLogin(scanner, authService);
                    break;
                case "3":
                    running = false;
                    System.out.println("Exiting. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }

        scanner.close();
    }

    private static void handleRegistration(Scanner scanner, AuthService authService) {
        String username = promptNonEmpty(scanner, "Enter username: ");
        String password;
        while (true) {
            System.out.print("Enter password (min 5 chars, must include a special character): ");
            password = scanner.nextLine().trim();

            if (password.length() >= 5 && password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
                break;
            } else {
                System.out.println("Password must be at least 5 characters and contain a special character.");
            }
    }

        String email;
        while (true) {
            System.out.print("Enter email (must contain '@' and end with '.com'): ");
            email = scanner.nextLine().trim();
            if (email.contains("@") && email.endsWith(".com")) {
                break;
            } else {
                System.out.println("Invalid email. Make sure it contains '@' and ends with '.com'.");
            }
}

        String phone = promptValidPhone(scanner);
        String address = promptNonEmpty(scanner, "Enter address: ");
        

        Role role = null;
        while (role == null) {
            String roleInput = promptNonEmpty(scanner, "Enter role (ADMIN / TRAINER / MEMBER): ").toUpperCase();
            try {
                role = Role.valueOf(roleInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid role. Please enter ADMIN, TRAINER, or MEMBER.");
            }
        }

        authService.registerUser(username, password, email, phone, address, role);
    }

    private static void handleLogin(Scanner scanner, AuthService authService) {
        String username = promptNonEmpty(scanner, "Enter username: ");
        String password = promptNonEmpty(scanner, "Enter password: ");

        User user = authService.login(username, password);
        if (user != null) {
            if (user.getRole() == Role.MEMBER) {
                System.out.println("Membership Status: " + (user.hasMembership() ? "Active" : "Not Purchased"));
            }
            showRoleMenu(user, scanner, authService);
        }
    }

    private static void showRoleMenu(User user, Scanner scanner, AuthService authService){
        System.out.println("\nWelcome, " + user.getUsername() + "! Your role is: " + user.getRole());

        switch (user.getRole()) {
            case ADMIN:
                boolean adminMenuRunning = true;
                while (adminMenuRunning) {
                    System.out.println("\nAdmin Menu:");
                    System.out.println("1. View Reports");
                    System.out.println("2. View Monthly Gym Earnings");
                    System.out.println("3. Back to Main Menu");
                    System.out.println("4. Delete a User");
                    System.out.print("Choose an option: ");
                    String choice = scanner.nextLine();

                    switch (choice) {
                        case "1":
                            authService.showUserCountReport();
                            break;
                        case "2":
                            System.out.println("Total earnings this month: $" + AuthService.getGymEarnings());
                            break;
                        case "3":
                            adminMenuRunning = false;
                            break;
                        case "4":
                            System.out.print("Enter the username of the user to delete: ");
                            String firstEntry = scanner.nextLine().trim();
                            System.out.print("Please confirm by entering the username again: ");
                            String secondEntry = scanner.nextLine().trim();                        
                            if (firstEntry.equalsIgnoreCase(secondEntry)) {
                                boolean deleted = authService.deleteUser(firstEntry);
                                if (deleted) {
                                    System.out.println("User '" + firstEntry + "' has been deleted.");
                                } else {
                                    System.out.println("No user found with that username.");
                                }
                            } else {
                                System.out.println("Usernames did not match. Cancelling process.");
                            }
                            break;
                        
                        default:
                            System.out.println("Invalid choice.");
                    }
                }
                break;

            case TRAINER:
                boolean trainerMenuRunning = true;
                while (trainerMenuRunning) {
                    System.out.println("\nTrainer Menu:");
                    System.out.println("1. View Workout Plans");
                    System.out.println("2. Add Workout Plan");
                    System.out.println("3. Edit Workout Plan");
                    System.out.println("4. Delete Workout Plan");
                    System.out.println("5. Back to Main Menu");
                    System.out.print("Choose an option: ");
                    String choice = scanner.nextLine();

                    switch (choice) {
                        case "1":
                            if (user.getWorkoutPlans().isEmpty()) {
                                System.out.println("No workout plans available.");
                            } else {
                                System.out.println("Your Workout Plans:");
                                for (WorkoutPlan plan : user.getWorkoutPlans()) {
                                    System.out.println("- " + plan);
                                }
                            }
                            break;
                            case "2":
                            String activity = promptNonEmpty(scanner, "Enter activity name: ");
                            String time = promptValidMilitaryTime(scanner);
                            user.getWorkoutPlans().add(new WorkoutPlan(activity, time));
                            System.out.println("Workout plan added.");
                            break;
            
                        case "3":
                            System.out.print("Enter existing activity name: ");
                            String oldActivity = scanner.nextLine();
                            System.out.print("Enter existing time: ");
                            String oldTime = scanner.nextLine();
                            WorkoutPlan toEdit = null;
                            for (WorkoutPlan plan : user.getWorkoutPlans()) {
                                if (plan.getActivity().equalsIgnoreCase(oldActivity) && plan.getTime().equalsIgnoreCase(oldTime)) {
                                    toEdit = plan;
                                    break;
                                }
                            }
                            if (toEdit != null) {
                                System.out.print("New activity (If same, re-enter activity): ");
                                String newActivity = scanner.nextLine();
                                System.out.print("New time (leave blank to keep same): ");
                                String newTime = scanner.nextLine();
                                if (!newActivity.isEmpty()) toEdit.setActivity(newActivity);
                                if (!newTime.isEmpty()) toEdit.setTime(newTime);
                                System.out.println("Workout plan updated.");
                            } else {
                                System.out.println("Workout plan not found.");
                            }
                            break;
            
                        case "4":
                            System.out.print("Enter activity name to delete: ");
                            String delActivity = scanner.nextLine();
                            System.out.print("Enter time of the activity: ");
                            String delTime = scanner.nextLine();
                            boolean removed = user.getWorkoutPlans().removeIf(plan ->
                                    plan.getActivity().equalsIgnoreCase(delActivity) &&
                                    plan.getTime().equalsIgnoreCase(delTime));
                            if (removed) {
                                System.out.println("Workout plan deleted.");
                            } else {
                                System.out.println("No matching workout plan found.");
                            }
                            break;
            
                        case "5":
                            trainerMenuRunning = false;
                            break;
            
                        default:
                            System.out.println("Invalid choice.");
                    }
                }
                break;

                case MEMBER:
                boolean memberMenuRunning = true;
                while (memberMenuRunning) {
                    System.out.println("\nMember Menu:");
                    System.out.println("1. View Workout Plan");
                    System.out.println("2. Book Classes");
                    System.out.println("3. Purchase Membership");
                    System.out.println("4. View Workout Classes");
                    System.out.println("5. Back to Main Menu");
                    System.out.print("Choose an option: ");
                    String choice = scanner.nextLine();
            
                    switch (choice) {
                        case "1":
                            System.out.println("Showing workout plan...");
                            break;
            
                        case "2":
                            System.out.println("Booking classes...");
                            break;
            
                        case "3":
                            if (user.hasMembership()) {
                                System.out.println("You already have a membership.");
                            } else {
                                user.setHasMembership(true);
                                AuthService.addToEarnings(25.0);
                                System.out.println("Membership purchased successfully!");
                            }
                            break;
            
                        case "4":
                            if (user.getWorkoutPlans().isEmpty()) {
                                System.out.println("No workout plans available.");
                            } else {
                                System.out.println("Your Workout Plans:");
                                for (WorkoutPlan plan : user.getWorkoutPlans()) {
                                    System.out.println("- " + plan);
                                }
                            }
                            break;
            
                        case "5":
                            memberMenuRunning = false;
                            break;
            
                        default:
                            System.out.println("Invalid choice.");
                    }
                }
                break;
        }
    }

    private static String promptNonEmpty(Scanner scanner, String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("This field cannot be left blank.");
            }
        }
    }

    private static String promptValidPhone(Scanner scanner) {
        String input;
        while (true) {
            System.out.print("Enter phone number: ");
            input = scanner.nextLine().trim();
            if (input.matches("\\d{10}")) {
                return input;
            } else {
                System.out.println("Invalid phone number. It must be exactly 10 digits and contain only numbers.");
            }
        }
    }
    private static String promptValidMilitaryTime(Scanner scanner) {
        String input;
        while (true) {
            System.out.print("Enter time (HHMM - 24hr format): ");
            input = scanner.nextLine().trim();
    
            if (input.matches("([01]\\d|2[0-3])[0-5]\\d")) {
                return input;
            } else {
                System.out.println("Invalid time. Please use 4-digit military time, e.g., 0930 or 1800.");
            }
        }
    }
    private static String promptValidEmail(Scanner scanner) {
        String input;
        while (true) {
            System.out.print("Enter email: ");
            input = scanner.nextLine().trim();
    
            if (input.contains("@") && input.endsWith(".com")) {
                return input;
            } else {
                System.out.println("Invalid email. Make sure it contains '@' and ends with '.com'.");
            }
        }
    }
    

}
