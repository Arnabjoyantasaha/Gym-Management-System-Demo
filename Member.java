// Member.java - Member Class extending User
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class Member extends User {
    private String membershipType;
    private String joinDate;
    private String membershipExpiry;
    private String fitnessGoal;
    private String assignedTrainerId;
    private List<String> workoutHistory;
    private List<String> attendanceHistory;
    private double totalPayments;
    private String phoneNumber;
    private String address;
    private String emergencyContact;
    private double weight;
    private double height;
    private String medicalConditions;

    public Member(String userId, String name, String email, String password,
                  String membershipType, String joinDate, String membershipExpiry, String fitnessGoal) {
        super(userId, name, email, password, "MEMBER");
        this.membershipType = membershipType;
        this.joinDate = joinDate;
        this.membershipExpiry = membershipExpiry;
        this.fitnessGoal = fitnessGoal;
        this.workoutHistory = new ArrayList<>();
        this.attendanceHistory = new ArrayList<>();
        this.totalPayments = 0.0;
        this.phoneNumber = "";
        this.address = "";
        this.emergencyContact = "";
        this.weight = 0.0;
        this.height = 0.0;
        this.medicalConditions = "None";
    }

    @Override
    public void displayDashboard() {
        System.out.println(GymUI.BOLD + GymUI.CYAN + "\n╔══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                           MEMBER DASHBOARD                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝" + GymUI.RESET);
        System.out.println(GymUI.GREEN + "Welcome back, " + name + "! 👋" + GymUI.RESET);
        System.out.println("📋 Membership: " + GymUI.YELLOW + membershipType + GymUI.RESET + " (Expires: " + membershipExpiry + ")");
        System.out.println("🎯 Fitness Goal: " + fitnessGoal);
        System.out.println("💳 Total Payments: $" + totalPayments);
        System.out.println("📅 Member Since: " + joinDate);

        if (assignedTrainerId != null) {
            System.out.println("👨‍🏫 Assigned Trainer ID: " + GymUI.CYAN + assignedTrainerId + GymUI.RESET);
        } else {
            System.out.println("👨‍🏫 Trainer: " + GymUI.YELLOW + "Not Assigned" + GymUI.RESET);
        }

        // Check membership status
        if (isMembershipExpired()) {
            System.out.println(GymUI.RED + "⚠️ Your membership has expired! Please renew." + GymUI.RESET);
        }

        System.out.println("\n" + GymUI.BOLD + "Available Options:" + GymUI.RESET);
        System.out.println(GymUI.GREEN + "1. 📅 View Workout Schedule" + GymUI.RESET);
        System.out.println(GymUI.CYAN + "2. ✅ Mark Attendance" + GymUI.RESET);
        System.out.println(GymUI.YELLOW + "3. 💳 View Payment History & Make Payment" + GymUI.RESET);
        System.out.println(GymUI.PURPLE + "4. ⚙️  Update Profile" + GymUI.RESET);
        System.out.println(GymUI.BLUE + "5. 📊 View Progress & Statistics" + GymUI.RESET);
        System.out.println(GymUI.WHITE + "6. 💬 Contact Support" + GymUI.RESET);
        System.out.println(GymUI.RED + "7. 🚪 Logout" + GymUI.RESET);
        System.out.println("═".repeat(70));
    }

    public void displayMemberInfo() {
        System.out.println(GymUI.BOLD + GymUI.CYAN + "👤 " + name + " (" + userId + ")" + GymUI.RESET);
        System.out.println("   📧 Email: " + email);
        System.out.println("   📞 Phone: " + (phoneNumber.isEmpty() ? "Not provided" : phoneNumber));
        System.out.println("   🏠 Address: " + (address.isEmpty() ? "Not provided" : address));
        System.out.println("   🎫 Membership: " + membershipType);
        System.out.println("   📅 Joined: " + joinDate + " | Expires: " + membershipExpiry);
        System.out.println("   🎯 Goal: " + fitnessGoal);
        System.out.println("   💳 Total Payments: $" + totalPayments);
        System.out.println("   ⚖️ Weight: " + (weight > 0 ? weight + " kg" : "Not recorded"));
        System.out.println("   📏 Height: " + (height > 0 ? height + " cm" : "Not recorded"));
        System.out.println("   🏥 Medical: " + medicalConditions);
        System.out.println("   " + (isActive() ? GymUI.GREEN + "🟢 Active" : GymUI.RED + "🔴 Inactive") + GymUI.RESET);
        if (assignedTrainerId != null) {
            System.out.println("   👨‍🏫 Trainer ID: " + assignedTrainerId);
        } else {
            System.out.println("   👨‍🏫 Trainer: " + GymUI.YELLOW + "Not Assigned" + GymUI.RESET);
        }
        System.out.println("   📊 Workouts: " + workoutHistory.size() + " | Attendance: " + attendanceHistory.size());
    }

    public void displayDetailedStats() {
        System.out.println(GymUI.BOLD + GymUI.BLUE + "\n📊 MEMBER STATISTICS" + GymUI.RESET);
        System.out.println("Total Workouts: " + workoutHistory.size());
        System.out.println("Attendance Days: " + attendanceHistory.size());
        System.out.println("Total Payments: $" + totalPayments);
        System.out.println("Membership Status: " + (isMembershipExpired() ?
                GymUI.RED + "Expired" : GymUI.GREEN + "Active") + GymUI.RESET);

        if (!workoutHistory.isEmpty()) {
            System.out.println("\n" + GymUI.BOLD + "Recent Workouts:" + GymUI.RESET);
            int limit = Math.min(5, workoutHistory.size());
            for (int i = workoutHistory.size() - limit; i < workoutHistory.size(); i++) {
                System.out.println("• " + workoutHistory.get(i));
            }
        }

        if (!attendanceHistory.isEmpty()) {
            System.out.println("\n" + GymUI.BOLD + "Recent Attendance:" + GymUI.RESET);
            int limit = Math.min(5, attendanceHistory.size());
            for (int i = attendanceHistory.size() - limit; i < attendanceHistory.size(); i++) {
                System.out.println("• " + attendanceHistory.get(i));
            }
        }
    }

    public boolean isMembershipExpired() {
        try {
            LocalDate expiryDate = LocalDate.parse(membershipExpiry);
            return LocalDate.now().isAfter(expiryDate);
        } catch (Exception e) {
            return false;
        }
    }

    public void renewMembership(String newExpiryDate, double paymentAmount) {
        this.membershipExpiry = newExpiryDate;
        this.totalPayments += paymentAmount;
        setActive(true);
    }

    // Getters and Setters
    public String getMembershipType() { return membershipType; }
    public String getJoinDate() { return joinDate; }
    public String getMembershipExpiry() { return membershipExpiry; }
    public String getFitnessGoal() { return fitnessGoal; }
    public String getAssignedTrainerId() { return assignedTrainerId; }
    public List<String> getWorkoutHistory() { return workoutHistory; }
    public List<String> getAttendanceHistory() { return attendanceHistory; }
    public double getTotalPayments() { return totalPayments; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getAddress() { return address; }
    public String getEmergencyContact() { return emergencyContact; }
    public double getWeight() { return weight; }
    public double getHeight() { return height; }
    public String getMedicalConditions() { return medicalConditions; }

    public void setMembershipType(String membershipType) { this.membershipType = membershipType; }
    public void setMembershipExpiry(String membershipExpiry) { this.membershipExpiry = membershipExpiry; }
    public void setFitnessGoal(String fitnessGoal) { this.fitnessGoal = fitnessGoal; }
    public void setAssignedTrainerId(String assignedTrainerId) { this.assignedTrainerId = assignedTrainerId; }
    public void setTotalPayments(double totalPayments) { this.totalPayments = totalPayments; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setAddress(String address) { this.address = address; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setHeight(double height) { this.height = height; }
    public void setMedicalConditions(String medicalConditions) { this.medicalConditions = medicalConditions; }

    public void addWorkout(String workout) {
        workoutHistory.add(LocalDate.now() + ": " + workout);
    }

    public void markAttendance() {
        String today = LocalDate.now().toString();
        if (!attendanceHistory.contains(today)) {
            attendanceHistory.add(today);
        }
    }

    public void addPayment(double amount) {
        totalPayments += amount;
    }
}