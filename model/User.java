package model;

import enums.Role;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private UUID userID;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String address;
    private Role role;
    private boolean hasMembership = false;  // NEW: membership status

    private List<WorkoutPlan> workoutPlans = new ArrayList<>();

    public List<WorkoutPlan> getWorkoutPlans() {
        return workoutPlans;
    }


    public User() {
        this.userID = UUID.randomUUID();
    }

    public User(String username, String password, String email, String phoneNumber, String address, Role role) {
        this.userID = UUID.randomUUID();
        this.username = username;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());  // Secure hash
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
    }

    // Getters and setters

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());  // re-hash if changed
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    //  Membership feature
    public boolean hasMembership() {
        return hasMembership;
    }

    public void setHasMembership(boolean hasMembership) {
        this.hasMembership = hasMembership;
    }

    // Verify plaintext password against hashed one
    public boolean verifyPassword(String password) {
        return BCrypt.checkpw(password, this.password);
    }
}
