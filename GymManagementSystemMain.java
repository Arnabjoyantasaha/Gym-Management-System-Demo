// GymManagementSystemMain.java - Fixed version without debug output and duplicate admin creation
import java.util.Scanner;

public class GymManagementSystemMain {
    private GymManagementSystem gymSystem;
    private Scanner scanner;
    private boolean isRunning;

    public GymManagementSystemMain() {
        this.gymSystem = new GymManagementSystem();
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
        initializeDefaultAdmin();
    }

    private void initializeDefaultAdmin() {
        // Only create default admin if NO users exist at all
        if (gymSystem.getUserManager().getTotalUserCount() == 0) {
            // System is completely empty, create default admin
            boolean created = gymSystem.getUserManager().registerAdmin("Admin001", "System Administrator",
                    "admin@gym.com", "admin001", "Super");

            if (created) {
                // Silent creation - no output needed for normal operation
            } else {
                System.out.println(GymUI.RED + "❌ Failed to create default admin!" + GymUI.RESET);
            }
        }
        // If users exist, don't create anything - let the existing system work
    }

    public static void main(String[] args) {
        GymManagementSystemMain app = new GymManagementSystemMain();
        app.run();
    }

    public void run() {
        try {
            showWelcomeScreen();
            while (isRunning) {
                showMainMenu();
            }
        } catch (Exception e) {
            GymUI.showErrorMessage("System error: " + e.getMessage());
        } finally {
            cleanup();
        }
    }

    private void showWelcomeScreen() {
        GymUI.clearScreen();
        GymUI.printWelcomeBanner();
        GymUI.showLoadingAnimation("Initializing system");
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }

    private void showMainMenu() {
        try {
            GymUI.clearScreen();
            GymUI.printWelcomeBanner();
            printLoginOptions();

            int choice = GymUI.getMenuChoice(scanner, 4);

            switch (choice) {
                case 1: handleUserLogin("MEMBER", "Member"); break;
                case 2: handleUserLogin("TRAINER", "Trainer"); break;
                case 3: handleUserLogin("ADMIN", "Admin"); break;
                case 4: exitSystem(); break;
            }
        } catch (Exception e) {
            GymUI.showErrorMessage("Error: " + e.getMessage());
            GymUI.pauseForInput(scanner);
        }
    }

    private void printLoginOptions() {
        System.out.println(GymUI.BOLD + GymUI.CYAN + "\n╔══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                            🔐 LOGIN PORTAL                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝" + GymUI.RESET);
        System.out.println();
        System.out.println(GymUI.GREEN + "1. 👤 Member Login" + GymUI.RESET);
        System.out.println(GymUI.YELLOW + "2. 👨‍🏫 Trainer Login" + GymUI.RESET);
        System.out.println(GymUI.PURPLE + "3. ⚡ Admin Login" + GymUI.RESET);
        System.out.println(GymUI.RED + "4. 🚪 Exit System" + GymUI.RESET);
        System.out.println();

        // Only show default credentials if no other admins exist (for first-time setup)
        if (gymSystem.getUserManager().getAdminCount() == 1 &&
                gymSystem.getUserManager().getAdminById("Admin001") != null) {
            System.out.println(GymUI.GRAY + "💡 Default Admin: Admin001 / admin001" + GymUI.RESET);
            System.out.println();
        }
    }

    private void handleUserLogin(String expectedRole, String roleName) {
        GymUI.clearScreen();
        GymUI.printHeader(roleName.toUpperCase() + " LOGIN", GymUI.getColorForRole(expectedRole));
        System.out.println();

        try {
            String userId = GymUI.getValidInput(scanner, "User ID: ", "User ID cannot be empty!");
            String password = GymUI.getValidInput(scanner, "Password: ", "Password cannot be empty!");

            GymUI.showLoadingAnimation("Authenticating");

            if (gymSystem.getUserManager().login(userId, password)) {
                User currentUser = gymSystem.getUserManager().getCurrentUser();

                if (!currentUser.getRole().equals(expectedRole)) {
                    GymUI.showErrorMessage("Access denied! This is a " + roleName + " login portal.");
                    gymSystem.getUserManager().logout();
                    GymUI.pauseForInput(scanner);
                    return;
                }

                GymUI.showSuccessMessage("Welcome, " + currentUser.getName() + "!");
                Thread.sleep(800);

                // Enter user session loop
                while (gymSystem.getUserManager().getCurrentUser() != null) {
                    gymSystem.handleUserSession();
                }
            } else {
                GymUI.showErrorMessage("Invalid credentials or inactive account!");
                System.out.println(GymUI.YELLOW + "💡 Please check your User ID and Password" + GymUI.RESET);

                // Only show default admin hint if it's the only admin and login failed for admin portal
                if (expectedRole.equals("ADMIN") &&
                        gymSystem.getUserManager().getAdminCount() == 1 &&
                        gymSystem.getUserManager().getAdminById("Admin001") != null) {
                    System.out.println(GymUI.YELLOW + "💡 Default Admin: Admin001 / admin001" + GymUI.RESET);
                }

                GymUI.pauseForInput(scanner);
            }
        } catch (Exception e) {
            GymUI.showErrorMessage("Login error: " + e.getMessage());
            GymUI.pauseForInput(scanner);
        }
    }

    private void exitSystem() {
        GymUI.clearScreen();
        GymUI.printHeader("SYSTEM SHUTDOWN", GymUI.RED);
        System.out.println("Thank you for using Gym Management System!");
        System.out.println("💪 Stay fit, stay healthy!");

        GymUI.showLoadingAnimation("Shutting down");

        if (gymSystem.getUserManager().getCurrentUser() != null) {
            gymSystem.getUserManager().logout();
        }

        isRunning = false;
        System.out.println(GymUI.GREEN + "✅ System shutdown complete." + GymUI.RESET);
    }

    private void cleanup() {
        try {
            if (gymSystem != null) gymSystem.cleanup();
            if (scanner != null) scanner.close();
        } catch (Exception e) {
            System.err.println("Cleanup error: " + e.getMessage());
        }
    }
}