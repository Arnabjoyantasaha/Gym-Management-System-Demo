// User.java - Fixed Abstract Base Class
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String userId;
    protected String name;
    protected String email;
    protected String password;
    protected String role;
    protected String phoneNumber;
    protected String createdDate;
    protected String lastLoginDate;
    protected boolean isActive;

    public User(String userId, String name, String email, String password, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phoneNumber = "";
        this.createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.lastLoginDate = null;
        this.isActive = true;
    }

    // Abstract method that must be implemented by subclasses
    public abstract void displayDashboard();

    public void updateLastLogin() {
        this.lastLoginDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getCreatedDate() { return createdDate; }
    public String getLastLoginDate() { return lastLoginDate; }
    public boolean isActive() { return isActive; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setActive(boolean active) { this.isActive = active; }

    @Override
    public String toString() {
        return "User{" + "userId='" + userId + '\'' + ", name='" + name + '\'' +
                ", email='" + email + '\'' + ", role='" + role + '\'' +
                ", active=" + isActive + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}