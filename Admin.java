// Admin.java - Admin Class extending User
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Admin extends User {
    private String adminLevel;
    private String department;
    private int actionsPerformed;
    private List<String> actionHistory;
    private String phoneNumber;
    private String address;
    private double salary;
    private String workingHours;

    public Admin(String userId, String name, String email, String password, String adminLevel) {
        super(userId, name, email, password, "ADMIN");
        this.adminLevel = adminLevel;
        this.department = "Management";
        this.actionsPerformed = 0;
        this.actionHistory = new ArrayList<>();
        this.phoneNumber = "";
        this.address = "";
        this.salary = 0.0;
        this.workingHours = "9:00 AM - 5:00 PM";
    }

    @Override
    public void displayDashboard() {
        System.out.println(GymUI.BOLD + GymUI.PURPLE + "\n╔══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                           ADMIN DASHBOARD                           ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝" + GymUI.RESET);
        System.out.println(GymUI.GREEN + "Welcome, " + name + "! ⚡" + GymUI.RESET);
        System.out.println("🔐 Admin Level: " + GymUI.YELLOW + adminLevel + GymUI.RESET);
        System.out.println("🏢 Department: " + department);
        System.out.println("📊 Actions Performed: " + actionsPerformed);
        System.out.println("🕒 Working Hours: " + workingHours);
        System.out.println("💼 Access Level: " + getPermissionLevel());

        System.out.println("\n" + GymUI.BOLD + "Administrative Options:" + GymUI.RESET);
        System.out.println(GymUI.GREEN + "1. 👥 Manage Members" + GymUI.RESET);
        System.out.println(GymUI.CYAN + "2. 👨‍🏫 Manage Trainers" + GymUI.RESET);
        System.out.println(GymUI.YELLOW + "3. 📊 View Reports & Analytics" + GymUI.RESET);
        System.out.println(GymUI.PURPLE + "4. 💳 Manage Payments & Billing" + GymUI.RESET);
        System.out.println(GymUI.BLUE + "5. 🏋️  Manage Workouts & Equipment" + GymUI.RESET);
        System.out.println(GymUI.WHITE + "6. ⚙️  System Settings & Backup" + GymUI.RESET);
        System.out.println(GymUI.CYAN + "7. 📋 View Action History" + GymUI.RESET);
        System.out.println(GymUI.RED + "8. 🚪 Logout" + GymUI.RESET);
        System.out.println("═".repeat(70));
    }

    public void displayAdminInfo() {
        System.out.println(GymUI.BOLD + GymUI.PURPLE + "⚡ " + name + " (" + userId + ")" + GymUI.RESET);
        System.out.println("   📧 Email: " + email);
        System.out.println("   📞 Phone: " + (phoneNumber.isEmpty() ? "Not provided" : phoneNumber));
        System.out.println("   🏠 Address: " + (address.isEmpty() ? "Not provided" : address));
        System.out.println("   🔐 Admin Level: " + adminLevel);
        System.out.println("   🏢 Department: " + department);
        System.out.println("   👑 Role: Administrator");
        System.out.println("   💼 Access: " + getPermissionLevel());
        System.out.println("   📊 Actions: " + actionsPerformed);
        System.out.println("   💰 Salary: $" + (salary > 0 ? salary : "Not specified"));
        System.out.println("   🕒 Working Hours: " + workingHours);
    }

    public void displayActionHistory() {
        System.out.println(GymUI.BOLD + GymUI.BLUE + "\n📋 ADMIN ACTION HISTORY" + GymUI.RESET);
        System.out.println("Total Actions Performed: " + actionsPerformed);

        if (actionHistory.isEmpty()) {
            System.out.println(GymUI.YELLOW + "No actions recorded yet." + GymUI.RESET);
            return;
        }

        System.out.println("\n" + GymUI.BOLD + "Recent Actions:" + GymUI.RESET);
        int limit = Math.min(10, actionHistory.size());
        for (int i = actionHistory.size() - limit; i < actionHistory.size(); i++) {
            System.out.println("• " + actionHistory.get(i));
        }
    }

    public void logAction(String action) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String logEntry = timestamp + " - " + action;
        actionHistory.add(logEntry);
        actionsPerformed++;
    }

    // Admin specific methods
    public boolean hasPermission(String action) {
        if (!isActive()) return false;

        switch (adminLevel.toLowerCase()) {
            case "super":
                return true; // Super admin has all permissions
            case "manager":
                return !action.equals("DELETE_ADMIN") &&
                        !action.equals("SYSTEM_CONFIG") &&
                        !action.equals("BACKUP_RESTORE");
            case "basic":
                return action.equals("VIEW_REPORTS") ||
                        action.equals("MANAGE_MEMBERS") ||
                        action.equals("VIEW_PAYMENTS");
            default:
                return false;
        }
    }

    public String getPermissionLevel() {
        switch (adminLevel.toLowerCase()) {
            case "super":
                return "Full System Access";
            case "manager":
                return "Limited Administrative Access";
            case "basic":
                return "Basic Management Access";
            default:
                return "No Special Permissions";
        }
    }

    public List<String> getAvailableActions() {
        List<String> actions = new ArrayList<>();

        if (hasPermission("MANAGE_MEMBERS")) {
            actions.add("Manage Members");
        }
        if (hasPermission("MANAGE_TRAINERS")) {
            actions.add("Manage Trainers");
        }
        if (hasPermission("VIEW_REPORTS")) {
            actions.add("View Reports");
        }
        if (hasPermission("MANAGE_PAYMENTS")) {
            actions.add("Manage Payments");
        }
        if (hasPermission("MANAGE_WORKOUTS")) {
            actions.add("Manage Workouts");
        }
        if (hasPermission("SYSTEM_CONFIG")) {
            actions.add("System Configuration");
        }
        if (hasPermission("BACKUP_RESTORE")) {
            actions.add("Backup & Restore");
        }
        if (hasPermission("DELETE_ADMIN")) {
            actions.add("Admin Management");
        }

        return actions;
    }

    public void updateSalary(double newSalary) {
        if (newSalary >= 0) {
            this.salary = newSalary;
            logAction("Salary updated to $" + newSalary);
        }
    }

    public void updateWorkingHours(String newWorkingHours) {
        this.workingHours = newWorkingHours;
        logAction("Working hours updated to: " + newWorkingHours);
    }

    public void promoteAdminLevel(String newLevel) {
        String oldLevel = this.adminLevel;
        this.adminLevel = newLevel;
        logAction("Admin level changed from " + oldLevel + " to " + newLevel);
    }

    // Getters and Setters
    public String getAdminLevel() { return adminLevel; }
    public String getDepartment() { return department; }
    public int getActionsPerformed() { return actionsPerformed; }
    public List<String> getActionHistory() { return actionHistory; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getAddress() { return address; }
    public double getSalary() { return salary; }
    public String getWorkingHours() { return workingHours; }

    public void setAdminLevel(String adminLevel) {
        String oldLevel = this.adminLevel;
        this.adminLevel = adminLevel;
        logAction("Admin level changed from " + oldLevel + " to " + adminLevel);
    }
    public void setDepartment(String department) {
        this.department = department;
        logAction("Department changed to: " + department);
    }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setAddress(String address) { this.address = address; }
    public void setSalary(double salary) { this.salary = salary; }
    public void setWorkingHours(String workingHours) { this.workingHours = workingHours; }

    public void incrementActions() {
        this.actionsPerformed++;
    }
}