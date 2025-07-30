// GymUI.java - Enhanced UI with Improved Screen Clearing
import java.util.Scanner;

public class GymUI {
    // ANSI Color codes
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String BLUE = "\u001B[34m";
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";
    public static final String PURPLE = "\u001B[35m";
    public static final String WHITE = "\u001B[37m";
    public static final String GRAY = "\u001B[90m";

    // Enhanced clear screen method with multiple fallback strategies
    public static void clearScreen() {
        try {
            // Method 1: ANSI escape sequences (works on most modern terminals)
            System.out.print("\033[2J\033[H");
            System.out.flush();

            // Method 2: Try system-specific commands
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("windows")) {
                // Windows command
                try {
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                } catch (Exception e) {
                    // If Windows command fails, use alternative
                    windowsFallback();
                }
            } else {
                // Unix/Linux/Mac command
                try {
                    new ProcessBuilder("clear").inheritIO().start().waitFor();
                } catch (Exception e) {
                    // If Unix command fails, use alternative
                    unixFallback();
                }
            }

        } catch (Exception e) {
            // Ultimate fallback - print enough newlines to clear visible screen
            ultimateFallback();
        }

        // Small delay to ensure clearing is processed
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Windows-specific fallback
    private static void windowsFallback() {
        try {
            // Try alternative Windows clearing methods
            System.out.print("\f"); // Form feed character
            System.out.flush();

            // Print multiple escape sequences
            for (int i = 0; i < 3; i++) {
                System.out.print("\033[2J\033[H");
                System.out.flush();
            }
        } catch (Exception e) {
            ultimateFallback();
        }
    }

    // Unix/Linux/Mac fallback
    private static void unixFallback() {
        try {
            // Try alternative Unix clearing methods
            System.out.print("\033c"); // Reset terminal
            System.out.flush();

            // Additional ANSI sequences
            System.out.print("\033[3J\033[2J\033[H");
            System.out.flush();
        } catch (Exception e) {
            ultimateFallback();
        }
    }

    // Ultimate fallback method
    private static void ultimateFallback() {
        // Print enough newlines to clear a typical terminal screen
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
        System.out.flush();
    }

    // Force clear with visual separator (use when transitioning between major sections)
    public static void forceClearWithSeparator() {
        clearScreen();
        System.out.println("═".repeat(70));
        System.out.println();
    }

    // Clear and show transition message
    public static void clearAndTransition(String fromPanel, String toPanel) {
        clearScreen();
        if (fromPanel != null && toPanel != null) {
            System.out.println(GRAY + "Navigating from " + fromPanel + " to " + toPanel + "..." + RESET);
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            clearScreen();
        }
    }

    public static void printSeparator() {
        System.out.println("═".repeat(70));
    }

    public static void printSubSeparator() {
        System.out.println("─".repeat(50));
    }

    public static void pauseForInput(Scanner scanner) {
        System.out.println();
        System.out.print(YELLOW + "Press Enter to continue..." + RESET);
        scanner.nextLine();
        clearScreen(); // Clear after user input
    }

    public static void showLoadingAnimation(String message) {
        System.out.print(YELLOW + message);
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(300);
                System.out.print(".");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println(" ✓" + RESET);
    }

    // Enhanced Message Methods
    public static void showSuccessMessage(String message) {
        System.out.println(GREEN + "✅ " + message + RESET);
    }

    public static void showErrorMessage(String message) {
        System.out.println(RED + "❌ " + message + RESET);
    }

    public static void showWarningMessage(String message) {
        System.out.println(YELLOW + "⚠️  " + message + RESET);
    }

    public static void showInfoMessage(String message) {
        System.out.println(CYAN + "ℹ️  " + message + RESET);
    }

    public static void showFeatureComingSoon(String feature) {
        clearScreen();
        System.out.println(BOLD + BLUE + "\n╔══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                         🚧 FEATURE COMING SOON                      ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝" + RESET);
        System.out.println();
        System.out.println(YELLOW + "🔨 " + feature + " is under development!" + RESET);
        System.out.println("This feature will be available in future updates.");
    }

    // Header Methods
    public static void printHeader(String title, String color) {
        System.out.println(BOLD + color + "\n╔══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║" + centerText(title, 70) + "║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝" + RESET);
    }

    private static String centerText(String text, int width) {
        if (text.length() >= width) {
            return text.substring(0, width);
        }
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(width - text.length() - padding);
    }

    public static void printWelcomeBanner() {
        System.out.println(BOLD + BLUE + "╔══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║" + " ".repeat(20) + CYAN + "🏋️  GYM MANAGEMENT SYSTEM  🏋️" + BLUE + " ".repeat(20) + "║");
        System.out.println("║" + " ".repeat(70) + "║");
        System.out.println("║" + " ".repeat(18) + GREEN + "🌟 Complete Business Solution 🌟" + BLUE + " ".repeat(18) + "║");
        System.out.println("║" + " ".repeat(70) + "║");
        System.out.println("║" + " ".repeat(28) + YELLOW + "Version 2.0" + BLUE + " ".repeat(32) + "║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝" + RESET);
    }

    // Role-based Colors and Icons
    public static String getColorForRole(String role) {
        switch (role.toUpperCase()) {
            case "MEMBER": return CYAN;
            case "TRAINER": return YELLOW;
            case "ADMIN": return PURPLE;
            default: return WHITE;
        }
    }

    public static String getRoleIcon(String role) {
        switch (role.toUpperCase()) {
            case "MEMBER": return "👤";
            case "TRAINER": return "👨‍🏫";
            case "ADMIN": return "⚡";
            default: return "👥";
        }
    }

    // Confirmation Method
    public static boolean confirmAction(Scanner scanner, String action) {
        System.out.println();
        System.out.println(YELLOW + "⚠️  Are you sure you want to " + action + "?" + RESET);
        System.out.print("Type 'YES' to confirm: ");
        String confirmation = scanner.nextLine().trim();
        boolean confirmed = "YES".equalsIgnoreCase(confirmation);
        if (confirmed) {
            clearScreen(); // Clear after confirmation
        }
        return confirmed;
    }

    // Input Validation Methods with screen clearing
    public static int getMenuChoice(Scanner scanner, int maxOption) {
        while (true) {
            try {
                System.out.print(BOLD + "Choose option (1-" + maxOption + "): " + RESET);
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                if (choice >= 1 && choice <= maxOption) {
                    // Small delay before clearing to show the choice was registered
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return choice;
                } else {
                    showErrorMessage("Please enter a number between 1 and " + maxOption);
                }
            } catch (Exception e) {
                showErrorMessage("Invalid input. Please enter a number.");
                scanner.nextLine(); // consume invalid input
            }
        }
    }

    public static String getValidInput(Scanner scanner, String prompt, String validationMessage) {
        String input;
        do {
            System.out.print(CYAN + prompt + RESET);
            input = scanner.nextLine().trim();
            if (input.isEmpty() && !validationMessage.isEmpty()) {
                showErrorMessage(validationMessage);
            }
        } while (input.isEmpty() && !validationMessage.isEmpty());
        return input;
    }

    public static double getValidDouble(Scanner scanner, String prompt) {
        while (true) {
            try {
                System.out.print(CYAN + prompt + RESET);
                double value = scanner.nextDouble();
                scanner.nextLine(); // consume newline
                if (value >= 0) {
                    return value;
                } else {
                    showErrorMessage("Please enter a positive number.");
                }
            } catch (Exception e) {
                showErrorMessage("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // consume invalid input
            }
        }
    }

    public static int getValidInt(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(CYAN + prompt + RESET);
                int value = scanner.nextInt();
                scanner.nextLine(); // consume newline
                if (value >= min && value <= max) {
                    return value;
                } else {
                    showErrorMessage("Please enter a number between " + min + " and " + max);
                }
            } catch (Exception e) {
                showErrorMessage("Invalid input. Please enter a valid number.");
                scanner.nextLine(); // consume invalid input
            }
        }
    }

    // Business Information Methods
    public static void printMembershipTypeInfo() {
        System.out.println(BOLD + YELLOW + "\n💳 MEMBERSHIP TYPES:" + RESET);
        System.out.println(GREEN + "• Basic ($50/month)" + RESET + " - Gym access + basic equipment");
        System.out.println(BLUE + "• Premium ($80/month)" + RESET + " - Gym + group classes + locker");
        System.out.println(PURPLE + "• VIP ($120/month)" + RESET + " - All features + personal trainer + nutrition");
    }

    public static void printAdminLevelInfo() {
        System.out.println(BOLD + YELLOW + "\n🔐 ADMIN LEVELS:" + RESET);
        System.out.println(GREEN + "• Basic" + RESET + " - View reports, basic member management");
        System.out.println(BLUE + "• Manager" + RESET + " - Full member/trainer management, payments");
        System.out.println(PURPLE + "• Super" + RESET + " - Complete system access, admin management");
    }

    // System Status Display
    public static void showSystemStatus(boolean dataLoaded, int userCount) {
        System.out.println(BOLD + GRAY + "\n════════════════════════════════════════" + RESET);
        String dataStatus = dataLoaded ?
                GREEN + "📂 Data: Loaded Successfully" + RESET :
                YELLOW + "📂 Data: Starting Fresh" + RESET;
        System.out.println(dataStatus);
        System.out.println(CYAN + "👥 Total Users: " + userCount + RESET);
        System.out.println(BOLD + GRAY + "════════════════════════════════════════" + RESET);
    }

    // Progress and Statistics Display
    public static void printStatCard(String title, String value, String color, String icon) {
        System.out.println(color + "┌─────────────────┐");
        System.out.println("│ " + icon + " " + String.format("%-13s", title) + "│");
        System.out.println("│ " + String.format("%-15s", value) + "│");
        System.out.println("└─────────────────┘" + RESET);
    }

    // Clean menu transitions with proper clearing
    public static void showMenuTransition(String from, String to) {
        clearScreen();
        System.out.println(GRAY + "Navigating from " + from + " to " + to + "..." + RESET);
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        clearScreen();
    }

    // Business-specific notifications
    public static void showBusinessAlert(String message, String type) {
        String icon, color;
        switch (type.toLowerCase()) {
            case "revenue":
                icon = "💰";
                color = GREEN;
                break;
            case "warning":
                icon = "⚠️";
                color = YELLOW;
                break;
            case "urgent":
                icon = "🚨";
                color = RED;
                break;
            default:
                icon = "📢";
                color = BLUE;
        }
        System.out.println(color + icon + " " + message + RESET);
    }

    // Method to ensure clean screen before showing any menu
    public static void prepareScreen() {
        clearScreen();
        try {
            Thread.sleep(100); // Brief pause to ensure clearing is complete
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}