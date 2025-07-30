// GymManagementSystem.java - Enhanced Core System Class
import java.util.*;
import java.time.LocalDate;

public class GymManagementSystem {
    private UserManager userManager;
    private Scanner scanner;

    public GymManagementSystem() {
        this.userManager = new UserManager();
        this.scanner = new Scanner(System.in);
    }

    public UserManager getUserManager() { return userManager; }
    public Scanner getScanner() { return scanner; }

    public void handleUserSession() {
        User currentUser = userManager.getCurrentUser();
        if (currentUser == null) return;

        GymUI.clearScreen();
        currentUser.displayDashboard();
        handleUserActions(currentUser);
    }

    private void handleUserActions(User user) {
        try {
            if (user instanceof Admin) {
                int choice = GymUI.getMenuChoice(scanner, 8);
                handleAdminActions((Admin) user, choice);
            } else if (user instanceof Trainer) {
                int choice = GymUI.getMenuChoice(scanner, 7);
                handleTrainerActions((Trainer) user, choice);
            } else if (user instanceof Member) {
                int choice = GymUI.getMenuChoice(scanner, 7);
                handleMemberActions((Member) user, choice);
            }
        } catch (Exception e) {
            GymUI.showErrorMessage("Error: " + e.getMessage());
            GymUI.pauseForInput(scanner);
        }
    }

    // Admin Actions Handler
    private void handleAdminActions(Admin admin, int choice) {
        admin.incrementActions();
        switch (choice) {
            case 1: admin.logAction("Accessed Member Management"); manageMembers(); break;
            case 2: admin.logAction("Accessed Trainer Management"); manageTrainers(); break;
            case 3: admin.logAction("Viewed System Reports"); viewReports(); break;
            case 4: admin.logAction("Accessed Payment Management"); managePayments(); break;
            case 5: admin.logAction("Accessed Workout Management"); manageWorkouts(); break;
            case 6: admin.logAction("Accessed System Settings"); systemSettings(); break;
            case 7: admin.logAction("Viewed Action History"); viewActionHistory(admin); break;
            case 8: admin.logAction("Logged out"); logout(); break;
        }
    }

    private void handleTrainerActions(Trainer trainer, int choice) {
        switch (choice) {
            case 1: viewAssignedMembers(trainer.getUserId()); break;
            case 2: createWorkoutPlans(trainer); break;
            case 3: markMemberAttendance(trainer); break;
            case 4: updateTrainerAvailability(trainer); break;
            case 5: viewTrainerStatistics(trainer); break;
            case 6: updateTrainerProfile(trainer); break;
            case 7: logout(); break;
        }
    }

    private void handleMemberActions(Member member, int choice) {
        switch (choice) {
            case 1: viewWorkoutSchedule(member); break;
            case 2: markAttendance(member); break;
            case 3: viewPaymentHistory(member); break;
            case 4: updateMemberProfile(member); break;
            case 5: viewMemberProgress(member); break;
            case 6: contactSupport(); break;
            case 7: logout(); break;
        }
    }

    // Admin Management Methods
    private void manageMembers() {
        while (true) {
            GymUI.clearScreen();
            GymUI.printHeader("MEMBER MANAGEMENT", GymUI.BLUE);

            System.out.println(GymUI.GREEN + "1. 👥 View All Members" + GymUI.RESET);
            System.out.println(GymUI.CYAN + "2. ➕ Add New Member" + GymUI.RESET);
            System.out.println(GymUI.YELLOW + "3. 🔍 Search Members" + GymUI.RESET);
            System.out.println(GymUI.PURPLE + "4. 👨‍🏫 Manage Trainer Assignments" + GymUI.RESET);
            System.out.println(GymUI.BLUE + "5. ⚙️ Update Member Status" + GymUI.RESET);
            System.out.println(GymUI.RED + "6. 🗑️ Delete Member" + GymUI.RESET);
            System.out.println(GymUI.WHITE + "7. ⬅️ Back to Dashboard" + GymUI.RESET);
            System.out.println();

            int choice = GymUI.getMenuChoice(scanner, 7);
            if (choice == 7) break;

            switch (choice) {
                case 1: viewAllMembers(); break;
                case 2: registerNewMember(); break;
                case 3: searchMembers(); break;
                case 4: manageTrainerAssignments(); break;
                case 5: updateMemberStatus(); break;
                case 6: deleteMember(); break;
            }
        }
    }

    private void manageTrainers() {
        while (true) {
            GymUI.clearScreen();
            GymUI.printHeader("TRAINER MANAGEMENT", GymUI.YELLOW);

            System.out.println(GymUI.GREEN + "1. 👨‍🏫 View All Trainers" + GymUI.RESET);
            System.out.println(GymUI.CYAN + "2. ➕ Add New Trainer" + GymUI.RESET);
            System.out.println(GymUI.YELLOW + "3. 🔍 Search Trainers" + GymUI.RESET);
            System.out.println(GymUI.PURPLE + "4. ⚙️ Update Trainer Status" + GymUI.RESET);
            System.out.println(GymUI.RED + "5. 🗑️ Delete Trainer" + GymUI.RESET);
            System.out.println(GymUI.WHITE + "6. ⬅️ Back to Dashboard" + GymUI.RESET);
            System.out.println();

            int choice = GymUI.getMenuChoice(scanner, 6);
            if (choice == 6) break;

            switch (choice) {
                case 1: viewAllTrainers(); break;
                case 2: registerNewTrainer(); break;
                case 3: searchTrainers(); break;
                case 4: updateTrainerStatus(); break;
                case 5: deleteTrainer(); break;
            }
        }
    }

    private void viewAllMembers() {
        GymUI.clearScreen();
        List<Member> members = userManager.getAllMembers();
        GymUI.printHeader("ALL MEMBERS (" + members.size() + ")", GymUI.GREEN);

        if (members.isEmpty()) {
            GymUI.showWarningMessage("No members registered yet.");
        } else {
            for (Member member : members) {
                member.displayMemberInfo();
                System.out.println();
            }
        }
        GymUI.pauseForInput(scanner);
    }

    private void viewAllTrainers() {
        GymUI.clearScreen();
        List<Trainer> trainers = userManager.getAllTrainers();
        GymUI.printHeader("ALL TRAINERS (" + trainers.size() + ")", GymUI.YELLOW);

        if (trainers.isEmpty()) {
            GymUI.showWarningMessage("No trainers registered yet.");
        } else {
            for (Trainer trainer : trainers) {
                trainer.displayTrainerInfo();
                System.out.println();
            }
        }
        GymUI.pauseForInput(scanner);
    }

    private void searchMembers() {
        GymUI.clearScreen();
        GymUI.printHeader("SEARCH MEMBERS", GymUI.CYAN);

        String searchTerm = GymUI.getValidInput(scanner, "Enter search term (name/ID/email): ", "Search term cannot be empty!");
        List<User> userResults = userManager.searchUsers(searchTerm);
        List<Member> results = new ArrayList<>();

        for (User user : userResults) {
            if (user instanceof Member) {
                results.add((Member) user);
            }
        }

        if (results.isEmpty()) {
            GymUI.showWarningMessage("No members found.");
        } else {
            System.out.println(GymUI.BOLD + "\nSearch Results (" + results.size() + "):" + GymUI.RESET);
            for (Member member : results) {
                member.displayMemberInfo();
                System.out.println();
            }
        }
        GymUI.pauseForInput(scanner);
    }

    private void searchTrainers() {
        GymUI.clearScreen();
        GymUI.printHeader("SEARCH TRAINERS", GymUI.CYAN);

        String searchTerm = GymUI.getValidInput(scanner, "Enter search term (name/ID/specialization): ", "Search term cannot be empty!");
        List<User> userResults = userManager.searchUsers(searchTerm);
        List<Trainer> results = new ArrayList<>();

        for (User user : userResults) {
            if (user instanceof Trainer) {
                results.add((Trainer) user);
            }
        }

        if (results.isEmpty()) {
            GymUI.showWarningMessage("No trainers found.");
        } else {
            System.out.println(GymUI.BOLD + "\nSearch Results (" + results.size() + "):" + GymUI.RESET);
            for (Trainer trainer : results) {
                trainer.displayTrainerInfo();
                System.out.println();
            }
        }
        GymUI.pauseForInput(scanner);
    }

    private void manageTrainerAssignments() {
        GymUI.clearScreen();
        GymUI.printHeader("TRAINER ASSIGNMENTS", GymUI.PURPLE);

        System.out.println("1. Assign Trainer to Member");
        System.out.println("2. Unassign Trainer from Member");
        System.out.println();

        int choice = GymUI.getMenuChoice(scanner, 2);

        if (choice == 1) {
            assignTrainerToMember();
        } else {
            unassignTrainerFromMember();
        }
    }

    private void assignTrainerToMember() {
        String memberId = GymUI.getValidInput(scanner, "Enter Member ID: ", "Member ID cannot be empty!");
        Member member = userManager.getMemberById(memberId);

        if (member == null) {
            GymUI.showErrorMessage("Member not found!");
            GymUI.pauseForInput(scanner);
            return;
        }

        List<Trainer> availableTrainers = new ArrayList<>();
        for (Trainer trainer : userManager.getAllTrainers()) {
            if (trainer.canTakeMoreClients()) {
                availableTrainers.add(trainer);
            }
        }

        if (availableTrainers.isEmpty()) {
            GymUI.showWarningMessage("No available trainers.");
            GymUI.pauseForInput(scanner);
            return;
        }

        System.out.println("\nAvailable Trainers:");
        for (Trainer trainer : availableTrainers) {
            System.out.println("ID: " + trainer.getUserId() + " - " + trainer.getName() +
                    " (" + trainer.getSpecialization() + ")");
        }

        String trainerId = GymUI.getValidInput(scanner, "\nEnter Trainer ID: ", "Trainer ID cannot be empty!");

        if (userManager.assignTrainerToMember(memberId, trainerId)) {
            GymUI.showSuccessMessage("Trainer assigned successfully!");
        } else {
            GymUI.showErrorMessage("Failed to assign trainer.");
        }
        GymUI.pauseForInput(scanner);
    }

    private void unassignTrainerFromMember() {
        String memberId = GymUI.getValidInput(scanner, "Enter Member ID: ", "Member ID cannot be empty!");

        if (userManager.unassignTrainerFromMember(memberId)) {
            GymUI.showSuccessMessage("Trainer unassigned successfully!");
        } else {
            GymUI.showErrorMessage("Failed to unassign trainer or member not found.");
        }
        GymUI.pauseForInput(scanner);
    }

    private void updateMemberStatus() {
        String memberId = GymUI.getValidInput(scanner, "Enter Member ID: ", "Member ID cannot be empty!");
        Member member = userManager.getMemberById(memberId);

        if (member == null) {
            GymUI.showErrorMessage("Member not found!");
            GymUI.pauseForInput(scanner);
            return;
        }

        GymUI.clearScreen();
        member.displayMemberInfo();
        System.out.println();

        System.out.println("1. " + (member.isActive() ? "Deactivate" : "Activate") + " Member");
        System.out.println("2. Update Email");
        System.out.println("3. Update Password");
        System.out.println("4. Renew Membership");

        int choice = GymUI.getMenuChoice(scanner, 4);

        switch (choice) {
            case 1:
                boolean newStatus = !member.isActive();
                if (userManager.updateUserStatus(memberId, newStatus)) {
                    GymUI.showSuccessMessage("Status updated to: " + (newStatus ? "Active" : "Inactive"));
                }
                break;
            case 2:
                String newEmail = GymUI.getValidInput(scanner, "New email: ", "Email cannot be empty!");
                userManager.updateUserEmail(memberId, newEmail);
                break;
            case 3:
                String newPassword = GymUI.getValidInput(scanner, "New password: ", "Password cannot be empty!");
                userManager.updateUserPassword(memberId, newPassword);
                break;
            case 4:
                renewMembership(member);
                break;
        }
        GymUI.pauseForInput(scanner);
    }

    private void updateTrainerStatus() {
        String trainerId = GymUI.getValidInput(scanner, "Enter Trainer ID: ", "Trainer ID cannot be empty!");
        Trainer trainer = userManager.getTrainerById(trainerId);

        if (trainer == null) {
            GymUI.showErrorMessage("Trainer not found!");
            GymUI.pauseForInput(scanner);
            return;
        }

        GymUI.clearScreen();
        trainer.displayTrainerInfo();
        System.out.println();

        System.out.println("1. " + (trainer.isActive() ? "Deactivate" : "Activate") + " Trainer");
        System.out.println("2. Update Email");
        System.out.println("3. Update Password");
        System.out.println("4. Update Hourly Rate");

        int choice = GymUI.getMenuChoice(scanner, 4);

        switch (choice) {
            case 1:
                boolean newStatus = !trainer.isActive();
                if (userManager.updateUserStatus(trainerId, newStatus)) {
                    GymUI.showSuccessMessage("Status updated to: " + (newStatus ? "Active" : "Inactive"));
                }
                break;
            case 2:
                String newEmail = GymUI.getValidInput(scanner, "New email: ", "Email cannot be empty!");
                userManager.updateUserEmail(trainerId, newEmail);
                break;
            case 3:
                String newPassword = GymUI.getValidInput(scanner, "New password: ", "Password cannot be empty!");
                userManager.updateUserPassword(trainerId, newPassword);
                break;
            case 4:
                double newRate = GymUI.getValidDouble(scanner, "New hourly rate: $");
                trainer.setHourlyRate(newRate);
                GymUI.showSuccessMessage("Hourly rate updated to: $" + newRate);
                break;
        }
        GymUI.pauseForInput(scanner);
    }

    private void renewMembership(Member member) {
        System.out.println("Current expiry: " + member.getMembershipExpiry());
        int months = GymUI.getValidInt(scanner, "Extend by months: ", 1, 60);
        double payment = GymUI.getValidDouble(scanner, "Payment amount: $");

        String newExpiry = LocalDate.parse(member.getMembershipExpiry()).plusMonths(months).toString();
        member.renewMembership(newExpiry, payment);
        GymUI.showSuccessMessage("Membership renewed until: " + newExpiry);
    }

    private void deleteMember() {
        String memberId = GymUI.getValidInput(scanner, "Enter Member ID to delete: ", "Member ID cannot be empty!");
        Member member = userManager.getMemberById(memberId);

        if (member == null) {
            GymUI.showErrorMessage("Member not found!");
            GymUI.pauseForInput(scanner);
            return;
        }

        System.out.println();
        member.displayMemberInfo();

        if (GymUI.confirmAction(scanner, "delete this member")) {
            if (userManager.deleteUser(memberId)) {
                GymUI.showSuccessMessage("Member deleted successfully!");
            } else {
                GymUI.showErrorMessage("Failed to delete member!");
            }
        }
        GymUI.pauseForInput(scanner);
    }

    private void deleteTrainer() {
        String trainerId = GymUI.getValidInput(scanner, "Enter Trainer ID to delete: ", "Trainer ID cannot be empty!");
        Trainer trainer = userManager.getTrainerById(trainerId);

        if (trainer == null) {
            GymUI.showErrorMessage("Trainer not found!");
            GymUI.pauseForInput(scanner);
            return;
        }

        System.out.println();
        trainer.displayTrainerInfo();

        if (GymUI.confirmAction(scanner, "delete this trainer")) {
            if (userManager.deleteUser(trainerId)) {
                GymUI.showSuccessMessage("Trainer deleted successfully!");
            } else {
                GymUI.showErrorMessage("Failed to delete trainer!");
            }
        }
        GymUI.pauseForInput(scanner);
    }

    private void viewReports() {
        GymUI.clearScreen();
        GymUI.printHeader("SYSTEM REPORTS", GymUI.GREEN);
        userManager.displayDetailedReport();
        GymUI.pauseForInput(scanner);
    }

    private void managePayments() {
        while (true) {
            GymUI.clearScreen();
            GymUI.printHeader("PAYMENT MANAGEMENT", GymUI.PURPLE);

            System.out.println(GymUI.GREEN + "1. 💰 Process Member Payment" + GymUI.RESET);
            System.out.println(GymUI.CYAN + "2. 📊 Payment Reports" + GymUI.RESET);
            System.out.println(GymUI.YELLOW + "3. 💳 View Outstanding Dues" + GymUI.RESET);
            System.out.println(GymUI.WHITE + "4. ⬅️ Back to Dashboard" + GymUI.RESET);
            System.out.println();

            int choice = GymUI.getMenuChoice(scanner, 4);
            if (choice == 4) break;

            switch (choice) {
                case 1: processPayment(); break;
                case 2: showPaymentReports(); break;
                case 3: viewOutstandingDues(); break;
            }
        }
    }

    private void processPayment() {
        String memberId = GymUI.getValidInput(scanner, "Enter Member ID: ", "Member ID cannot be empty!");
        Member member = userManager.getMemberById(memberId);

        if (member == null) {
            GymUI.showErrorMessage("Member not found!");
            GymUI.pauseForInput(scanner);
            return;
        }

        System.out.println("\nMember: " + member.getName());
        System.out.println("Current total payments: $" + member.getTotalPayments());

        double amount = GymUI.getValidDouble(scanner, "Payment amount: $");
        member.addPayment(amount);

        GymUI.showSuccessMessage("Payment of $" + amount + " processed for " + member.getName());
        GymUI.pauseForInput(scanner);
    }

    private void showPaymentReports() {
        GymUI.clearScreen();
        GymUI.printHeader("PAYMENT REPORTS", GymUI.CYAN);

        System.out.println("📊 Revenue Statistics:");
        System.out.println("Total Member Payments: $" + String.format("%.2f", userManager.getTotalRevenue()));
        System.out.println("Total Trainer Earnings: $" + String.format("%.2f", userManager.getTotalTrainerEarnings()));
        System.out.println("Net Revenue: $" + String.format("%.2f",
                userManager.getTotalRevenue() - userManager.getTotalTrainerEarnings()));

        GymUI.pauseForInput(scanner);
    }

    private void viewOutstandingDues() {
        GymUI.clearScreen();
        GymUI.printHeader("OUTSTANDING DUES", GymUI.YELLOW);

        List<Member> expiredMembers = new ArrayList<>();
        for (Member member : userManager.getAllMembers()) {
            if (member.isMembershipExpired()) {
                expiredMembers.add(member);
            }
        }

        if (expiredMembers.isEmpty()) {
            GymUI.showSuccessMessage("No outstanding dues!");
        } else {
            System.out.println("Members with expired memberships:");
            for (Member member : expiredMembers) {
                System.out.println("• " + member.getName() + " (" + member.getUserId() +
                        ") - Expired: " + member.getMembershipExpiry());
            }
        }
        GymUI.pauseForInput(scanner);
    }

    private void manageWorkouts() {
        GymUI.clearScreen();
        GymUI.printHeader("WORKOUT MANAGEMENT", GymUI.BLUE);
        GymUI.showFeatureComingSoon("Comprehensive Workout Management");
        GymUI.pauseForInput(scanner);
    }

    private void systemSettings() {
        // Only accessible to admins - already handled in caller
        while (true) {
            GymUI.clearScreen();
            GymUI.printHeader("SYSTEM SETTINGS", GymUI.WHITE);

            System.out.println(GymUI.GREEN + "1. 💾 Backup Data" + GymUI.RESET);
            System.out.println(GymUI.YELLOW + "2. 📊 System Information" + GymUI.RESET);
            System.out.println(GymUI.CYAN + "3. 🔍 Search All Users" + GymUI.RESET);
            System.out.println(GymUI.PURPLE + "4. ➕ Register New Admin" + GymUI.RESET);
            System.out.println(GymUI.RED + "5. ⚠️ Clear All Data" + GymUI.RESET);
            System.out.println(GymUI.WHITE + "6. ⬅️ Back to Dashboard" + GymUI.RESET);
            System.out.println();

            int choice = GymUI.getMenuChoice(scanner, 6);
            if (choice == 6) break;

            switch (choice) {
                case 1:
                    if (userManager.backupData()) {
                        GymUI.showSuccessMessage("Data backup completed!");
                    }
                    GymUI.pauseForInput(scanner);
                    break;
                case 2:
                    showSystemInformation();
                    break;
                case 3:
                    searchAllUsers();
                    break;
                case 4:
                    registerNewAdmin();
                    break;
                case 5:
                    if (GymUI.confirmAction(scanner, "clear ALL system data")) {
                        userManager.clearAllUsers();
                        // Recreate default admin
                        userManager.registerAdmin("Admin001", "System Administrator",
                                "admin@gym.com", "admin001", "Super");
                    }
                    GymUI.pauseForInput(scanner);
                    break;
            }
        }
    }

    private void showSystemInformation() {
        GymUI.clearScreen();
        GymUI.printHeader("SYSTEM INFORMATION", GymUI.BLUE);

        System.out.println(GymUI.BOLD + "🏋️ Gym Management System v2.0" + GymUI.RESET);
        System.out.println();

        // Display system statistics
        userManager.displaySystemStats();

        System.out.println();
        System.out.println(GymUI.BOLD + "📋 Features:" + GymUI.RESET);
        System.out.println("• Complete user management");
        System.out.println("• Membership tracking and billing");
        System.out.println("• Trainer-member assignments");
        System.out.println("• Payment processing");
        System.out.println("• Data persistence and backup");

        GymUI.pauseForInput(scanner);
    }

    private void searchAllUsers() {
        String searchTerm = GymUI.getValidInput(scanner, "Enter search term: ", "Search term cannot be empty!");
        List<User> results = userManager.searchUsers(searchTerm);

        System.out.println();
        if (results.isEmpty()) {
            GymUI.showWarningMessage("No users found matching: " + searchTerm);
        } else {
            System.out.println(GymUI.BOLD + "Search Results (" + results.size() + "):" + GymUI.RESET);
            System.out.println();
            for (User user : results) {
                String roleColor = GymUI.getColorForRole(user.getRole());
                String roleIcon = GymUI.getRoleIcon(user.getRole());
                System.out.println(roleColor + roleIcon + " " + user.getName() + " (" + user.getUserId() + ")" + GymUI.RESET);
                System.out.println("   📧 " + user.getEmail() + " | Role: " + user.getRole());
                System.out.println("   Status: " + (user.isActive() ? GymUI.GREEN + "Active" : GymUI.RED + "Inactive") + GymUI.RESET);
                System.out.println();
            }
        }
        GymUI.pauseForInput(scanner);
    }

    private void viewActionHistory(Admin admin) {
        GymUI.clearScreen();
        GymUI.printHeader("ADMIN ACTION HISTORY", GymUI.PURPLE);
        admin.displayActionHistory();
        GymUI.pauseForInput(scanner);
    }

    // Trainer-specific methods
    private void viewAssignedMembers(String trainerId) {
        GymUI.clearScreen();
        GymUI.printHeader("ASSIGNED MEMBERS", GymUI.CYAN);

        List<Member> assignedMembers = userManager.getMembersAssignedToTrainer(trainerId);
        if (assignedMembers.isEmpty()) {
            GymUI.showInfoMessage("No members assigned yet.");
        } else {
            System.out.println(GymUI.BOLD + "Your Assigned Members (" + assignedMembers.size() + "):" + GymUI.RESET);
            System.out.println();
            for (Member member : assignedMembers) {
                member.displayMemberInfo();
                System.out.println();
            }
        }
        GymUI.pauseForInput(scanner);
    }

    private void createWorkoutPlans(Trainer trainer) {
        GymUI.clearScreen();
        GymUI.printHeader("CREATE WORKOUT PLANS", GymUI.GREEN);

        List<Member> assignedMembers = userManager.getMembersAssignedToTrainer(trainer.getUserId());
        if (assignedMembers.isEmpty()) {
            GymUI.showWarningMessage("You have no assigned members yet.");
            GymUI.pauseForInput(scanner);
            return;
        }

        System.out.println("Select a member:");
        for (int i = 0; i < assignedMembers.size(); i++) {
            Member member = assignedMembers.get(i);
            System.out.println((i + 1) + ". " + member.getName() + " (" + member.getUserId() + ")");
        }

        int choice = GymUI.getMenuChoice(scanner, assignedMembers.size());
        Member selectedMember = assignedMembers.get(choice - 1);

        System.out.println("\nCreating workout for: " + selectedMember.getName());
        System.out.println("Fitness Goal: " + selectedMember.getFitnessGoal());

        String workoutPlan = GymUI.getValidInput(scanner, "\nEnter workout plan: ", "Workout plan cannot be empty!");

        trainer.createWorkoutPlan(selectedMember.getUserId(), workoutPlan);
        selectedMember.addWorkout(workoutPlan);

        GymUI.showSuccessMessage("Workout plan created successfully!");
        GymUI.pauseForInput(scanner);
    }

    private void markMemberAttendance(Trainer trainer) {
        GymUI.clearScreen();
        GymUI.printHeader("MARK ATTENDANCE", GymUI.YELLOW);

        List<Member> assignedMembers = userManager.getMembersAssignedToTrainer(trainer.getUserId());
        if (assignedMembers.isEmpty()) {
            GymUI.showWarningMessage("No assigned members.");
            GymUI.pauseForInput(scanner);
            return;
        }

        System.out.println("Select member:");
        for (int i = 0; i < assignedMembers.size(); i++) {
            Member member = assignedMembers.get(i);
            System.out.println((i + 1) + ". " + member.getName() + " (" + member.getUserId() + ")");
        }

        int choice = GymUI.getMenuChoice(scanner, assignedMembers.size());
        Member selectedMember = assignedMembers.get(choice - 1);

        selectedMember.markAttendance();
        double hours = GymUI.getValidDouble(scanner, "Hours worked: ");
        trainer.completeSession(selectedMember.getUserId(), hours);

        GymUI.showSuccessMessage("Attendance marked! Earnings: $" + (hours * trainer.getHourlyRate()));
        GymUI.pauseForInput(scanner);
    }

    private void updateTrainerAvailability(Trainer trainer) {
        GymUI.clearScreen();
        GymUI.printHeader("UPDATE AVAILABILITY", GymUI.PURPLE);

        String newHours = GymUI.getValidInput(scanner, "Working hours [" + trainer.getWorkingHours() + "]: ", "");
        if (newHours.isEmpty()) newHours = trainer.getWorkingHours();

        System.out.println("\n1. Available");
        System.out.println("2. Busy");
        System.out.println("3. On Leave");

        int choice = GymUI.getMenuChoice(scanner, 3);
        String availability = choice == 1 ? "Available" : choice == 2 ? "Busy" : "On Leave";

        trainer.updateSchedule(newHours, availability);
        GymUI.showSuccessMessage("Availability updated!");
        GymUI.pauseForInput(scanner);
    }

    private void viewTrainerStatistics(Trainer trainer) {
        GymUI.clearScreen();
        GymUI.printHeader("TRAINER STATISTICS", GymUI.BLUE);
        trainer.displayDetailedStats();
        GymUI.pauseForInput(scanner);
    }

    private void updateTrainerProfile(Trainer trainer) {
        GymUI.clearScreen();
        GymUI.printHeader("UPDATE PROFILE", GymUI.WHITE);

        System.out.println("1. Contact Information");
        System.out.println("2. Professional Details");
        System.out.println("3. Add Certification");
        System.out.println("4. Change Password");

        int choice = GymUI.getMenuChoice(scanner, 4);

        switch (choice) {
            case 1:
                String phone = GymUI.getValidInput(scanner, "Phone: ", "");
                String address = GymUI.getValidInput(scanner, "Address: ", "");
                trainer.setPhoneNumber(phone);
                trainer.setAddress(address);
                break;
            case 2:
                String spec = GymUI.getValidInput(scanner, "Specialization: ", "");
                String exp = GymUI.getValidInput(scanner, "Experience: ", "");
                double rate = GymUI.getValidDouble(scanner, "Hourly rate: $");
                trainer.setSpecialization(spec);
                trainer.setExperience(exp);
                trainer.setHourlyRate(rate);
                break;
            case 3:
                String cert = GymUI.getValidInput(scanner, "Certification: ", "");
                trainer.addCertification(cert);
                break;
            case 4:
                String newPassword = GymUI.getValidInput(scanner, "New password: ", "Password cannot be empty!");
                userManager.updateUserPassword(trainer.getUserId(), newPassword);
                break;
        }
        GymUI.showSuccessMessage("Profile updated!");
        GymUI.pauseForInput(scanner);
    }

    // Member-specific methods
    private void viewWorkoutSchedule(Member member) {
        GymUI.clearScreen();
        GymUI.printHeader("WORKOUT SCHEDULE", GymUI.CYAN);

        if (member.getWorkoutHistory().isEmpty()) {
            GymUI.showInfoMessage("No workouts assigned yet.");
        } else {
            System.out.println(GymUI.BOLD + "Your Workouts:" + GymUI.RESET);
            for (String workout : member.getWorkoutHistory()) {
                System.out.println("• " + workout);
            }
        }

        if (member.getAssignedTrainerId() != null) {
            Trainer trainer = userManager.getTrainerById(member.getAssignedTrainerId());
            if (trainer != null) {
                System.out.println("\n" + GymUI.BOLD + "Your Trainer:" + GymUI.RESET);
                System.out.println("Name: " + trainer.getName());
                System.out.println("Specialization: " + trainer.getSpecialization());
                System.out.println("Phone: " + trainer.getPhoneNumber());
            }
        }
        GymUI.pauseForInput(scanner);
    }

    private void markAttendance(Member member) {
        GymUI.clearScreen();
        GymUI.printHeader("MARK ATTENDANCE", GymUI.GREEN);

        member.markAttendance();
        GymUI.showSuccessMessage("Attendance marked for today!");
        System.out.println("Total attendance: " + member.getAttendanceHistory().size() + " days");
        GymUI.pauseForInput(scanner);
    }

    private void viewPaymentHistory(Member member) {
        GymUI.clearScreen();
        GymUI.printHeader("PAYMENT HISTORY", GymUI.PURPLE);

        System.out.println("Total Payments: $" + member.getTotalPayments());
        System.out.println("Membership: " + member.getMembershipType());
        System.out.println("Expires: " + member.getMembershipExpiry());
        System.out.println("Status: " + (member.isMembershipExpired() ?
                GymUI.RED + "Expired" : GymUI.GREEN + "Active") + GymUI.RESET);

        System.out.println("\n1. Make Payment");
        System.out.println("2. Just Viewing");

        int choice = GymUI.getMenuChoice(scanner, 2);

        if (choice == 1) {
            double amount = GymUI.getValidDouble(scanner, "Payment amount: $");
            member.addPayment(amount);
            GymUI.showSuccessMessage("Payment of $" + amount + " recorded!");
        }
        GymUI.pauseForInput(scanner);
    }

    private void updateMemberProfile(Member member) {
        GymUI.clearScreen();
        GymUI.printHeader("UPDATE PROFILE", GymUI.YELLOW);

        System.out.println("1. Contact Information");
        System.out.println("2. Fitness Goal");
        System.out.println("3. Physical Information");
        System.out.println("4. Change Password");

        int choice = GymUI.getMenuChoice(scanner, 4);

        switch (choice) {
            case 1:
                String phone = GymUI.getValidInput(scanner, "Phone: ", "");
                String address = GymUI.getValidInput(scanner, "Address: ", "");
                String emergency = GymUI.getValidInput(scanner, "Emergency contact: ", "");
                member.setPhoneNumber(phone);
                member.setAddress(address);
                member.setEmergencyContact(emergency);
                break;
            case 2:
                String goal = GymUI.getValidInput(scanner, "Fitness goal: ", "");
                member.setFitnessGoal(goal);
                break;
            case 3:
                double weight = GymUI.getValidDouble(scanner, "Weight (kg): ");
                double height = GymUI.getValidDouble(scanner, "Height (cm): ");
                String medical = GymUI.getValidInput(scanner, "Medical conditions: ", "");
                member.setWeight(weight);
                member.setHeight(height);
                member.setMedicalConditions(medical.isEmpty() ? "None" : medical);
                break;
            case 4:
                String newPassword = GymUI.getValidInput(scanner, "New password: ", "Password cannot be empty!");
                userManager.updateUserPassword(member.getUserId(), newPassword);
                break;
        }
        GymUI.showSuccessMessage("Profile updated!");
        GymUI.pauseForInput(scanner);
    }

    private void viewMemberProgress(Member member) {
        GymUI.clearScreen();
        GymUI.printHeader("MEMBER PROGRESS", GymUI.BLUE);
        member.displayDetailedStats();
        GymUI.pauseForInput(scanner);
    }

    private void contactSupport() {
        GymUI.clearScreen();
        GymUI.printHeader("CONTACT SUPPORT", GymUI.WHITE);

        System.out.println("📞 Gym Support:");
        System.out.println("🏢 Reception: (555) 123-4567");
        System.out.println("📧 Email: support@gym.com");
        System.out.println("🕒 Hours: Mon-Fri 6AM-10PM, Sat-Sun 7AM-9PM");
        System.out.println("🆘 Emergency: (555) 911-HELP");

        GymUI.pauseForInput(scanner);
    }

    private void logout() {
        GymUI.showLoadingAnimation("Logging out");
        userManager.logout();
        GymUI.showSuccessMessage("Logged out successfully!");
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }

    // Registration Methods - Only accessible by Admin
    public void registerNewMember() {
        GymUI.clearScreen();
        GymUI.printHeader("MEMBER REGISTRATION", GymUI.CYAN);

        try {
            String userId = GymUI.getValidInput(scanner, "User ID: ", "User ID cannot be empty!");
            if (userManager.userExists(userId)) {
                GymUI.showErrorMessage("User ID already exists!");
                GymUI.pauseForInput(scanner);
                return;
            }

            String name = GymUI.getValidInput(scanner, "Full Name: ", "Name cannot be empty!");
            String email = GymUI.getValidInput(scanner, "Email: ", "Email cannot be empty!");
            String phone = GymUI.getValidInput(scanner, "Phone Number: ", "Phone cannot be empty!");

            if (userManager.isEmailTaken(email)) {
                GymUI.showErrorMessage("Email already registered!");
                GymUI.pauseForInput(scanner);
                return;
            }

            String password = GymUI.getValidInput(scanner, "Password (min 4 chars): ", "Password cannot be empty!");
            if (password.length() < 4) {
                GymUI.showErrorMessage("Password too short!");
                GymUI.pauseForInput(scanner);
                return;
            }

            System.out.println("\nMembership Types:");
            System.out.println("1. Basic ($50/month)");
            System.out.println("2. Premium ($80/month)");
            System.out.println("3. VIP ($120/month)");

            int typeChoice = GymUI.getMenuChoice(scanner, 3);
            String membershipType = typeChoice == 1 ? "Basic" : typeChoice == 2 ? "Premium" : "VIP";

            String joinDate = LocalDate.now().toString();
            int duration = GymUI.getValidInt(scanner, "Duration (months): ", 1, 60);
            String membershipExpiry = LocalDate.now().plusMonths(duration).toString();
            String fitnessGoal = GymUI.getValidInput(scanner, "Fitness Goal: ", "Goal cannot be empty!");

            if (userManager.registerMember(userId, name, email, password, membershipType, joinDate, membershipExpiry, fitnessGoal)) {
                // Set phone number
                Member newMember = userManager.getMemberById(userId);
                if (newMember != null) {
                    newMember.setPhoneNumber(phone);
                }
                GymUI.showSuccessMessage("Member registered successfully!");
                System.out.println("ID: " + userId + " | Type: " + membershipType + " | Valid until: " + membershipExpiry);
            }
        } catch (Exception e) {
            GymUI.showErrorMessage("Registration error: " + e.getMessage());
        }
        GymUI.pauseForInput(scanner);
    }

    public void registerNewTrainer() {
        GymUI.clearScreen();
        GymUI.printHeader("TRAINER REGISTRATION", GymUI.YELLOW);

        try {
            String userId = GymUI.getValidInput(scanner, "User ID: ", "User ID cannot be empty!");
            if (userManager.userExists(userId)) {
                GymUI.showErrorMessage("User ID already exists!");
                GymUI.pauseForInput(scanner);
                return;
            }

            String name = GymUI.getValidInput(scanner, "Full Name: ", "Name cannot be empty!");
            String email = GymUI.getValidInput(scanner, "Email: ", "Email cannot be empty!");
            String phone = GymUI.getValidInput(scanner, "Phone Number: ", "Phone cannot be empty!");

            if (userManager.isEmailTaken(email)) {
                GymUI.showErrorMessage("Email already registered!");
                GymUI.pauseForInput(scanner);
                return;
            }

            String password = GymUI.getValidInput(scanner, "Password (min 4 chars): ", "Password cannot be empty!");
            if (password.length() < 4) {
                GymUI.showErrorMessage("Password too short!");
                GymUI.pauseForInput(scanner);
                return;
            }

            String specialization = GymUI.getValidInput(scanner, "Specialization: ", "Specialization cannot be empty!");
            String experience = GymUI.getValidInput(scanner, "Experience (years): ", "Experience cannot be empty!");
            double hourlyRate = GymUI.getValidDouble(scanner, "Hourly Rate ($): ");
            int maxClients = GymUI.getValidInt(scanner, "Max Clients: ", 1, 50);

            if (userManager.registerTrainer(userId, name, email, password, specialization, experience, hourlyRate, maxClients)) {
                // Set phone number
                Trainer newTrainer = userManager.getTrainerById(userId);
                if (newTrainer != null) {
                    newTrainer.setPhoneNumber(phone);
                }
                GymUI.showSuccessMessage("Trainer registered successfully!");
                System.out.println("ID: " + userId + " | Specialization: " + specialization + " | Rate: $" + hourlyRate + "/hr");
            }
        } catch (Exception e) {
            GymUI.showErrorMessage("Registration error: " + e.getMessage());
        }
        GymUI.pauseForInput(scanner);
    }

    public void registerNewAdmin() {
        GymUI.clearScreen();
        GymUI.printHeader("ADMIN REGISTRATION", GymUI.PURPLE);

        try {
            String userId = GymUI.getValidInput(scanner, "User ID: ", "User ID cannot be empty!");
            if (userManager.userExists(userId)) {
                GymUI.showErrorMessage("User ID already exists!");
                GymUI.pauseForInput(scanner);
                return;
            }

            String name = GymUI.getValidInput(scanner, "Full Name: ", "Name cannot be empty!");
            String email = GymUI.getValidInput(scanner, "Email: ", "Email cannot be empty!");
            String phone = GymUI.getValidInput(scanner, "Phone Number: ", "Phone cannot be empty!");

            if (userManager.isEmailTaken(email)) {
                GymUI.showErrorMessage("Email already registered!");
                GymUI.pauseForInput(scanner);
                return;
            }

            String password = GymUI.getValidInput(scanner, "Password (min 4 chars): ", "Password cannot be empty!");
            if (password.length() < 4) {
                GymUI.showErrorMessage("Password too short!");
                GymUI.pauseForInput(scanner);
                return;
            }

            System.out.println("\nAdmin Levels:");
            System.out.println("1. Basic - Limited access");
            System.out.println("2. Manager - Most features");
            System.out.println("3. Super - Full access");

            int levelChoice = GymUI.getMenuChoice(scanner, 3);
            String adminLevel = levelChoice == 1 ? "Basic" : levelChoice == 2 ? "Manager" : "Super";

            if (userManager.registerAdmin(userId, name, email, password, adminLevel)) {
                // Set phone number
                Admin newAdmin = userManager.getAdminById(userId);
                if (newAdmin != null) {
                    newAdmin.setPhoneNumber(phone);
                }
                GymUI.showSuccessMessage("Admin registered successfully!");
                System.out.println("ID: " + userId + " | Level: " + adminLevel);
            }
        } catch (Exception e) {
            GymUI.showErrorMessage("Registration error: " + e.getMessage());
        }
        GymUI.pauseForInput(scanner);
    }

    public void cleanup() {
        if (scanner != null) {
            scanner.close();
        }
    }
}