// UserManager.java - Clean version without debug output
import java.util.*;

public class UserManager {
    private Map<String, User> users;
    private User currentUser;
    private DataManager dataManager;
    private Map<String, String> trainerMemberAssignments; // memberId -> trainerId

    public UserManager() {
        this.dataManager = new DataManager();

        // Initialize with empty map if loading fails
        Map<String, User> loadedUsers = dataManager.loadUsers();
        this.users = (loadedUsers != null) ? loadedUsers : new HashMap<>();

        Map<String, String> loadedAssignments = dataManager.loadAssignments();
        this.trainerMemberAssignments = (loadedAssignments != null) ? loadedAssignments : new HashMap<>();

        this.currentUser = null;

        // Only restore relationships if we have users
        if (!users.isEmpty()) {
            restoreTrainerMemberRelationships();
        }
    }

    private void restoreTrainerMemberRelationships() {
        try {
            // Clear existing assignments to avoid duplicates
            for (User user : users.values()) {
                if (user instanceof Trainer) {
                    ((Trainer) user).getAssignedMemberIds().clear();
                    ((Trainer) user).setCurrentClients(0);
                }
            }

            // Rebuild relationships from assignments map
            for (Map.Entry<String, String> entry : trainerMemberAssignments.entrySet()) {
                String memberId = entry.getKey();
                String trainerId = entry.getValue();

                Member member = getMemberById(memberId);
                Trainer trainer = getTrainerById(trainerId);

                if (member != null && trainer != null && member.isActive() && trainer.isActive()) {
                    member.setAssignedTrainerId(trainerId);
                    if (!trainer.getAssignedMemberIds().contains(memberId)) {
                        trainer.getAssignedMemberIds().add(memberId);
                        trainer.setCurrentClients(trainer.getCurrentClients() + 1);
                    }
                    // Update trainer availability based on client count
                    if (trainer.getCurrentClients() >= trainer.getMaxClients()) {
                        trainer.setAvailability("Fully Booked");
                    } else {
                        trainer.setAvailability("Available");
                    }
                } else {
                    // Remove invalid assignments
                    trainerMemberAssignments.remove(memberId);
                    if (member != null) {
                        member.setAssignedTrainerId(null);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error restoring relationships: " + e.getMessage());
        }
    }

    private void saveData() {
        try {
            boolean saved = dataManager.saveUsers(users) && dataManager.saveAssignments(trainerMemberAssignments);
            if (!saved) {
                System.out.println(GymUI.RED + "⚠️ Data save failed!" + GymUI.RESET);
            }
        } catch (Exception e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    // Clean Authentication without debug output
    public boolean login(String userId, String password) {
        if (userId == null || password == null || userId.trim().isEmpty()) {
            return false;
        }

        String trimmedUserId = userId.trim();
        User user = users.get(trimmedUserId);

        if (user == null) {
            return false;
        }

        if (user.getPassword().equals(password) && user.isActive()) {
            currentUser = user;
            user.updateLastLogin();
            saveData();
            return true;
        } else {
            return false;
        }
    }

    public void logout() {
        if (currentUser != null) {
            saveData();
            currentUser = null;
        }
    }

    public User getCurrentUser() { return currentUser; }

    // Clean Registration without debug output
    public boolean registerAdmin(String userId, String name, String email, String password, String adminLevel) {
        try {
            if (userId == null || userId.trim().isEmpty()) {
                return false;
            }

            userId = userId.trim();

            if (users.containsKey(userId)) {
                return false;
            }

            if (!isValidUserData(userId, name, email, password)) {
                return false;
            }

            if (isEmailTaken(email)) {
                return false;
            }

            // Validate admin level
            if (!isValidAdminLevel(adminLevel)) {
                return false;
            }

            Admin newAdmin = new Admin(userId, name.trim(), email.trim(), password, adminLevel);
            users.put(userId, newAdmin);

            saveData();
            return true;
        } catch (Exception e) {
            System.err.println("Error registering admin: " + e.getMessage());
            return false;
        }
    }

    // Registration methods for Member and Trainer
    public boolean registerMember(String userId, String name, String email, String password,
                                  String membershipType, String joinDate, String membershipExpiry, String fitnessGoal) {
        try {
            if (userId == null || userId.trim().isEmpty()) {
                return false;
            }

            userId = userId.trim();

            if (users.containsKey(userId)) {
                return false;
            }

            if (!isValidUserData(userId, name, email, password)) {
                return false;
            }

            if (isEmailTaken(email)) {
                return false;
            }

            Member newMember = new Member(userId, name.trim(), email.trim(), password,
                    membershipType, joinDate, membershipExpiry, fitnessGoal);
            users.put(userId, newMember);
            saveData();
            return true;
        } catch (Exception e) {
            System.err.println("Error registering member: " + e.getMessage());
            return false;
        }
    }

    public boolean registerTrainer(String userId, String name, String email, String password,
                                   String specialization, String experience, double hourlyRate, int maxClients) {
        try {
            if (userId == null || userId.trim().isEmpty()) {
                return false;
            }

            userId = userId.trim();

            if (users.containsKey(userId)) {
                return false;
            }

            if (!isValidUserData(userId, name, email, password)) {
                return false;
            }

            if (isEmailTaken(email)) {
                return false;
            }

            if (hourlyRate < 0 || maxClients < 1 || maxClients > 50) {
                return false;
            }

            Trainer newTrainer = new Trainer(userId, name.trim(), email.trim(), password,
                    specialization, experience, hourlyRate, maxClients);
            users.put(userId, newTrainer);
            saveData();
            return true;
        } catch (Exception e) {
            System.err.println("Error registering trainer: " + e.getMessage());
            return false;
        }
    }

    // Validation methods
    private boolean isValidUserData(String userId, String name, String email, String password) {
        return userId != null && !userId.trim().isEmpty() &&
                name != null && !name.trim().isEmpty() &&
                email != null && isValidEmail(email) &&
                password != null && password.length() >= 4;
    }

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".") &&
                email.length() > 5 && !email.startsWith("@") && !email.endsWith("@");
    }

    private boolean isValidAdminLevel(String adminLevel) {
        return adminLevel != null &&
                (adminLevel.equalsIgnoreCase("Basic") ||
                        adminLevel.equalsIgnoreCase("Manager") ||
                        adminLevel.equalsIgnoreCase("Super"));
    }

    public boolean isEmailTaken(String email) {
        if (email == null) return false;
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(email.trim()));
    }

    public boolean userExists(String userId) {
        return userId != null && users.containsKey(userId.trim());
    }

    // User Retrieval methods
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        try {
            for (User user : users.values()) {
                if (user instanceof Member) {
                    members.add((Member) user);
                }
            }
            members.sort((m1, m2) -> {
                if (m1.getName() == null) return 1;
                if (m2.getName() == null) return -1;
                return m1.getName().compareToIgnoreCase(m2.getName());
            });
        } catch (Exception e) {
            System.err.println("Error getting members: " + e.getMessage());
        }
        return members;
    }

    public List<Trainer> getAllTrainers() {
        List<Trainer> trainers = new ArrayList<>();
        try {
            for (User user : users.values()) {
                if (user instanceof Trainer) {
                    trainers.add((Trainer) user);
                }
            }
            trainers.sort((t1, t2) -> {
                if (t1.getName() == null) return 1;
                if (t2.getName() == null) return -1;
                return t1.getName().compareToIgnoreCase(t2.getName());
            });
        } catch (Exception e) {
            System.err.println("Error getting trainers: " + e.getMessage());
        }
        return trainers;
    }

    public List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        try {
            for (User user : users.values()) {
                if (user instanceof Admin) {
                    admins.add((Admin) user);
                }
            }
            admins.sort((a1, a2) -> {
                if (a1.getName() == null) return 1;
                if (a2.getName() == null) return -1;
                return a1.getName().compareToIgnoreCase(a2.getName());
            });
        } catch (Exception e) {
            System.err.println("Error getting admins: " + e.getMessage());
        }
        return admins;
    }

    public Member getMemberById(String memberId) {
        if (memberId == null) return null;
        User user = users.get(memberId.trim());
        return (user instanceof Member) ? (Member) user : null;
    }

    public Trainer getTrainerById(String trainerId) {
        if (trainerId == null) return null;
        User user = users.get(trainerId.trim());
        return (user instanceof Trainer) ? (Trainer) user : null;
    }

    public Admin getAdminById(String adminId) {
        if (adminId == null) return null;
        User user = users.get(adminId.trim());
        return (user instanceof Admin) ? (Admin) user : null;
    }

    // Statistics methods
    public int getTotalUserCount() {
        return users != null ? users.size() : 0;
    }

    public int getMemberCount() {
        return getAllMembers().size();
    }

    public int getTrainerCount() {
        return getAllTrainers().size();
    }

    public int getAdminCount() {
        return getAllAdmins().size();
    }

    public Map<String, User> getAllUsers() {
        return new HashMap<>(users);
    }

    public void displaySystemStats() {
        try {
            System.out.println(GymUI.BOLD + GymUI.CYAN + "\n╔══════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                            SYSTEM STATS                             ║");
            System.out.println("╚══════════════════════════════════════════════════════════════════════╝" + GymUI.RESET);

            int members = getMemberCount();
            int trainers = getTrainerCount();
            int admins = getAdminCount();

            System.out.println("👥 Members: " + GymUI.GREEN + members + GymUI.RESET);
            System.out.println("👨‍🏫 Trainers: " + GymUI.YELLOW + trainers + GymUI.RESET);
            System.out.println("⚡ Admins: " + GymUI.PURPLE + admins + GymUI.RESET);
            System.out.println("📊 Total Users: " + GymUI.CYAN + getTotalUserCount() + GymUI.RESET);
            GymUI.printSeparator();
        } catch (Exception e) {
            System.err.println("Error displaying system stats: " + e.getMessage());
        }
    }

    // Search users by name, ID, or email
    public List<User> searchUsers(String searchTerm) {
        List<User> results = new ArrayList<>();
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return results;
        }

        String lowerSearchTerm = searchTerm.toLowerCase().trim();

        try {
            for (User user : users.values()) {
                if (user != null && matchesSearchTerm(user, lowerSearchTerm)) {
                    results.add(user);
                }
            }

            // Sort results by name
            results.sort((u1, u2) -> {
                if (u1.getName() == null) return 1;
                if (u2.getName() == null) return -1;
                return u1.getName().compareToIgnoreCase(u2.getName());
            });
        } catch (Exception e) {
            System.err.println("Error searching users: " + e.getMessage());
        }

        return results;
    }

    private boolean matchesSearchTerm(User user, String searchTerm) {
        try {
            // Check user ID
            if (user.getUserId() != null && user.getUserId().toLowerCase().contains(searchTerm)) {
                return true;
            }

            // Check name
            if (user.getName() != null && user.getName().toLowerCase().contains(searchTerm)) {
                return true;
            }

            // Check email
            if (user.getEmail() != null && user.getEmail().toLowerCase().contains(searchTerm)) {
                return true;
            }

            // Check role-specific fields
            if (user instanceof Member) {
                Member member = (Member) user;
                if (member.getMembershipType() != null &&
                        member.getMembershipType().toLowerCase().contains(searchTerm)) {
                    return true;
                }
                if (member.getFitnessGoal() != null &&
                        member.getFitnessGoal().toLowerCase().contains(searchTerm)) {
                    return true;
                }
            } else if (user instanceof Trainer) {
                Trainer trainer = (Trainer) user;
                if (trainer.getSpecialization() != null &&
                        trainer.getSpecialization().toLowerCase().contains(searchTerm)) {
                    return true;
                }
            } else if (user instanceof Admin) {
                Admin admin = (Admin) user;
                if (admin.getAdminLevel() != null &&
                        admin.getAdminLevel().toLowerCase().contains(searchTerm)) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            System.err.println("Error matching search term for user " + user.getUserId() + ": " + e.getMessage());
            return false;
        }
    }

    // Trainer-Member assignment methods
    public boolean assignTrainerToMember(String memberId, String trainerId) {
        try {
            if (memberId == null || trainerId == null) {
                return false;
            }

            memberId = memberId.trim();
            trainerId = trainerId.trim();

            Member member = getMemberById(memberId);
            Trainer trainer = getTrainerById(trainerId);

            if (member == null || trainer == null) {
                return false;
            }

            if (!member.isActive() || !trainer.isActive()) {
                return false;
            }

            if (!trainer.canTakeMoreClients()) {
                return false;
            }

            // Remove previous assignment if exists
            if (member.getAssignedTrainerId() != null) {
                unassignTrainerFromMember(memberId);
            }

            // Create new assignment
            member.setAssignedTrainerId(trainerId);
            trainer.addClient(memberId);
            trainerMemberAssignments.put(memberId, trainerId);

            saveData();
            return true;
        } catch (Exception e) {
            System.err.println("Error assigning trainer to member: " + e.getMessage());
            return false;
        }
    }

    public boolean unassignTrainerFromMember(String memberId) {
        try {
            if (memberId == null) {
                return false;
            }

            memberId = memberId.trim();
            Member member = getMemberById(memberId);

            if (member == null) {
                return false;
            }

            String trainerId = member.getAssignedTrainerId();
            if (trainerId != null) {
                Trainer trainer = getTrainerById(trainerId);
                if (trainer != null) {
                    trainer.removeClient(memberId);
                }

                member.setAssignedTrainerId(null);
                trainerMemberAssignments.remove(memberId);
                saveData();
            }

            return true;
        } catch (Exception e) {
            System.err.println("Error unassigning trainer from member: " + e.getMessage());
            return false;
        }
    }

    public List<Member> getMembersAssignedToTrainer(String trainerId) {
        List<Member> assignedMembers = new ArrayList<>();

        try {
            if (trainerId == null) {
                return assignedMembers;
            }

            trainerId = trainerId.trim();

            for (Member member : getAllMembers()) {
                if (trainerId.equals(member.getAssignedTrainerId()) && member.isActive()) {
                    assignedMembers.add(member);
                }
            }

            // Sort by name
            assignedMembers.sort((m1, m2) -> {
                if (m1.getName() == null) return 1;
                if (m2.getName() == null) return -1;
                return m1.getName().compareToIgnoreCase(m2.getName());
            });
        } catch (Exception e) {
            System.err.println("Error getting members assigned to trainer: " + e.getMessage());
        }

        return assignedMembers;
    }

    // Revenue calculation methods
    public double getTotalRevenue() {
        double total = 0.0;
        try {
            for (Member member : getAllMembers()) {
                total += member.getTotalPayments();
            }
        } catch (Exception e) {
            System.err.println("Error calculating total revenue: " + e.getMessage());
        }
        return total;
    }

    public double getTotalTrainerEarnings() {
        double total = 0.0;
        try {
            for (Trainer trainer : getAllTrainers()) {
                total += trainer.getTotalEarnings();
            }
        } catch (Exception e) {
            System.err.println("Error calculating total trainer earnings: " + e.getMessage());
        }
        return total;
    }

    // User management methods
    public boolean deleteUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return false;
        }

        try {
            userId = userId.trim();
            User user = users.get(userId);
            if (user != null) {
                users.remove(userId);
                saveData();
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error deleting user: " + e.getMessage());
        }
        return false;
    }

    public void clearAllUsers() {
        try {
            users.clear();
            trainerMemberAssignments.clear();
            currentUser = null;
            saveData();
        } catch (Exception e) {
            System.err.println("Error clearing all users: " + e.getMessage());
        }
    }

    public boolean backupData() {
        try {
            return dataManager.backupData();
        } catch (Exception e) {
            System.err.println("Error backing up data: " + e.getMessage());
            return false;
        }
    }

    // User status update methods
    public boolean updateUserStatus(String userId, boolean isActive) {
        try {
            if (userId == null) {
                return false;
            }

            User user = users.get(userId.trim());
            if (user != null) {
                user.setActive(isActive);
                saveData();
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error updating user status: " + e.getMessage());
        }
        return false;
    }

    public boolean updateUserEmail(String userId, String newEmail) {
        try {
            if (userId == null || newEmail == null) {
                return false;
            }

            if (isEmailTaken(newEmail)) {
                return false;
            }

            User user = users.get(userId.trim());
            if (user != null) {
                user.setEmail(newEmail.trim());
                saveData();
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error updating user email: " + e.getMessage());
        }
        return false;
    }

    public boolean updateUserPassword(String userId, String newPassword) {
        try {
            if (userId == null || newPassword == null || newPassword.length() < 4) {
                return false;
            }

            User user = users.get(userId.trim());
            if (user != null) {
                user.setPassword(newPassword);
                saveData();
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error updating user password: " + e.getMessage());
        }
        return false;
    }

    // Enhanced reporting method
    public void displayDetailedReport() {
        try {
            System.out.println(GymUI.BOLD + GymUI.GREEN + "\n╔══════════════════════════════════════════════════════════════════════╗");
            System.out.println("║                         DETAILED SYSTEM REPORT                      ║");
            System.out.println("╚══════════════════════════════════════════════════════════════════════╝" + GymUI.RESET);

            // User statistics
            int totalMembers = getMemberCount();
            int totalTrainers = getTrainerCount();
            int totalAdmins = getAdminCount();
            int activeMembers = 0;
            int expiredMembers = 0;

            // Calculate active/expired members
            for (Member member : getAllMembers()) {
                if (member.isActive() && !member.isMembershipExpired()) {
                    activeMembers++;
                } else if (member.isMembershipExpired()) {
                    expiredMembers++;
                }
            }

            // Display statistics
            System.out.println(GymUI.BOLD + "📊 USER STATISTICS:" + GymUI.RESET);
            System.out.println("Total Members: " + GymUI.CYAN + totalMembers + GymUI.RESET);
            System.out.println("  └─ Active: " + GymUI.GREEN + activeMembers + GymUI.RESET);
            System.out.println("  └─ Expired: " + GymUI.RED + expiredMembers + GymUI.RESET);
            System.out.println("Total Trainers: " + GymUI.YELLOW + totalTrainers + GymUI.RESET);
            System.out.println("Total Admins: " + GymUI.PURPLE + totalAdmins + GymUI.RESET);

            // Financial statistics
            double totalRevenue = getTotalRevenue();
            double totalTrainerEarnings = getTotalTrainerEarnings();
            double netRevenue = totalRevenue - totalTrainerEarnings;

            System.out.println("\n" + GymUI.BOLD + "💰 FINANCIAL STATISTICS:" + GymUI.RESET);
            System.out.println("Total Revenue: " + GymUI.GREEN + "$" + String.format("%.2f", totalRevenue) + GymUI.RESET);
            System.out.println("Trainer Earnings: " + GymUI.YELLOW + "$" + String.format("%.2f", totalTrainerEarnings) + GymUI.RESET);
            System.out.println("Net Revenue: " + GymUI.CYAN + "$" + String.format("%.2f", netRevenue) + GymUI.RESET);

            // Membership type breakdown
            Map<String, Integer> membershipTypes = new HashMap<>();
            for (Member member : getAllMembers()) {
                String type = member.getMembershipType();
                membershipTypes.put(type, membershipTypes.getOrDefault(type, 0) + 1);
            }

            if (!membershipTypes.isEmpty()) {
                System.out.println("\n" + GymUI.BOLD + "📋 MEMBERSHIP BREAKDOWN:" + GymUI.RESET);
                for (Map.Entry<String, Integer> entry : membershipTypes.entrySet()) {
                    System.out.println(entry.getKey() + ": " + GymUI.BLUE + entry.getValue() + GymUI.RESET);
                }
            }

            // Trainer utilization
            int availableTrainers = 0;
            int fullyBookedTrainers = 0;

            for (Trainer trainer : getAllTrainers()) {
                if (trainer.isActive()) {
                    if (trainer.canTakeMoreClients()) {
                        availableTrainers++;
                    } else {
                        fullyBookedTrainers++;
                    }
                }
            }

            if (totalTrainers > 0) {
                System.out.println("\n" + GymUI.BOLD + "👨‍🏫 TRAINER UTILIZATION:" + GymUI.RESET);
                System.out.println("Available: " + GymUI.GREEN + availableTrainers + GymUI.RESET);
                System.out.println("Fully Booked: " + GymUI.RED + fullyBookedTrainers + GymUI.RESET);
            }

        } catch (Exception e) {
            System.err.println("Error displaying detailed report: " + e.getMessage());
            GymUI.showErrorMessage("Failed to generate report");
        }
    }
}