// Trainer.java - Trainer Class extending User
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class Trainer extends User {
    private String specialization;
    private String experience;
    private String availability;
    private double hourlyRate;
    private int maxClients;
    private int currentClients;
    private List<String> certifications;
    private List<String> assignedMemberIds;
    private double totalEarnings;
    private String phoneNumber;
    private String address;
    private List<String> workoutPlansCreated;
    private List<String> sessionsCompleted;
    private String workingHours;

    public Trainer(String userId, String name, String email, String password,
                   String specialization, String experience, double hourlyRate, int maxClients) {
        super(userId, name, email, password, "TRAINER");
        this.specialization = specialization;
        this.experience = experience;
        this.hourlyRate = hourlyRate;
        this.maxClients = maxClients;
        this.currentClients = 0;
        this.availability = "Available";
        this.certifications = new ArrayList<>();
        this.assignedMemberIds = new ArrayList<>();
        this.totalEarnings = 0.0;
        this.phoneNumber = "";
        this.address = "";
        this.workoutPlansCreated = new ArrayList<>();
        this.sessionsCompleted = new ArrayList<>();
        this.workingHours = "9:00 AM - 6:00 PM";
    }

    @Override
    public void displayDashboard() {
        System.out.println(GymUI.BOLD + GymUI.YELLOW + "\n╔══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                          TRAINER DASHBOARD                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════╝" + GymUI.RESET);
        System.out.println(GymUI.GREEN + "Welcome, " + name + "! 💪" + GymUI.RESET);
        System.out.println("🎯 Specialization: " + GymUI.CYAN + specialization + GymUI.RESET);
        System.out.println("📈 Experience: " + experience);
        System.out.println("👥 Current Clients: " + GymUI.YELLOW + currentClients + "/" + maxClients + GymUI.RESET);
        System.out.println("💰 Rate: $" + hourlyRate + "/hour | Total Earnings: $" + totalEarnings);
        System.out.println("🕒 Working Hours: " + workingHours);
        System.out.println("📊 Status: " + (availability.equals("Available") ?
                GymUI.GREEN + availability : GymUI.RED + availability) + GymUI.RESET);

        if (!certifications.isEmpty()) {
            System.out.println("🏆 Certifications: " + certifications.size());
        }

        System.out.println("\n" + GymUI.BOLD + "Available Options:" + GymUI.RESET);
        System.out.println(GymUI.GREEN + "1. 👥 View Assigned Members" + GymUI.RESET);
        System.out.println(GymUI.CYAN + "2. 📋 Create/Manage Workout Plans" + GymUI.RESET);
        System.out.println(GymUI.YELLOW + "3. ✅ Mark Member Attendance" + GymUI.RESET);
        System.out.println(GymUI.PURPLE + "4. 🕒 Update Availability & Schedule" + GymUI.RESET);
        System.out.println(GymUI.BLUE + "5. 📊 View Statistics & Earnings" + GymUI.RESET);
        System.out.println(GymUI.WHITE + "6. ⚙️  Update Profile & Certifications" + GymUI.RESET);
        System.out.println(GymUI.RED + "7. 🚪 Logout" + GymUI.RESET);
        System.out.println("═".repeat(70));
    }

    public void displayTrainerInfo() {
        System.out.println(GymUI.BOLD + GymUI.YELLOW + "👨‍🏫 " + name + " (" + userId + ")" + GymUI.RESET);
        System.out.println("   📧 Email: " + email);
        System.out.println("   📞 Phone: " + (phoneNumber.isEmpty() ? "Not provided" : phoneNumber));
        System.out.println("   🏠 Address: " + (address.isEmpty() ? "Not provided" : address));
        System.out.println("   🎯 Specialization: " + specialization);
        System.out.println("   📈 Experience: " + experience);
        System.out.println("   💰 Rate: $" + hourlyRate + "/hour");
        System.out.println("   💵 Total Earnings: $" + totalEarnings);
        System.out.println("   👥 Clients: " + currentClients + "/" + maxClients);
        System.out.println("   🕒 Working Hours: " + workingHours);
        System.out.println("   📊 Status: " + availability);
        System.out.println("   🏆 Certifications: " + certifications.size());
        System.out.println("   📋 Workout Plans Created: " + workoutPlansCreated.size());
        System.out.println("   ✅ Sessions Completed: " + sessionsCompleted.size());
    }

    public void displayDetailedStats() {
        System.out.println(GymUI.BOLD + GymUI.BLUE + "\n📊 TRAINER STATISTICS" + GymUI.RESET);
        System.out.println("Total Clients: " + currentClients + "/" + maxClients);
        System.out.println("Total Earnings: $" + totalEarnings);
        System.out.println("Workout Plans Created: " + workoutPlansCreated.size());
        System.out.println("Sessions Completed: " + sessionsCompleted.size());
        System.out.println("Average Rating: " + calculateAverageRating() + "/5.0");

        if (!certifications.isEmpty()) {
            System.out.println("\n" + GymUI.BOLD + "Certifications:" + GymUI.RESET);
            for (String cert : certifications) {
                System.out.println("• " + cert);
            }
        }

        if (!workoutPlansCreated.isEmpty()) {
            System.out.println("\n" + GymUI.BOLD + "Recent Workout Plans:" + GymUI.RESET);
            int limit = Math.min(5, workoutPlansCreated.size());
            for (int i = workoutPlansCreated.size() - limit; i < workoutPlansCreated.size(); i++) {
                System.out.println("• " + workoutPlansCreated.get(i));
            }
        }
    }

    public boolean canTakeMoreClients() {
        return currentClients < maxClients && isActive();
    }

    public void addClient(String memberId) {
        if (canTakeMoreClients() && !assignedMemberIds.contains(memberId)) {
            assignedMemberIds.add(memberId);
            currentClients++;
            if (currentClients >= maxClients) {
                availability = "Fully Booked";
            }
        }
    }

    public void removeClient(String memberId) {
        if (assignedMemberIds.remove(memberId)) {
            currentClients--;
            if (currentClients < maxClients) {
                availability = "Available";
            }
        }
    }

    public void createWorkoutPlan(String memberId, String planDetails) {
        String planEntry = LocalDate.now() + " - Member: " + memberId + " - " + planDetails;
        workoutPlansCreated.add(planEntry);
    }

    public void completeSession(String memberId, double hoursWorked) {
        String sessionEntry = LocalDate.now() + " - Member: " + memberId + " - Hours: " + hoursWorked;
        sessionsCompleted.add(sessionEntry);
        double earnings = hoursWorked * hourlyRate;
        totalEarnings += earnings;
    }

    public void updateSchedule(String newWorkingHours, String newAvailability) {
        this.workingHours = newWorkingHours;
        if (!newAvailability.equals("Fully Booked") || currentClients < maxClients) {
            this.availability = newAvailability;
        }
    }

    private double calculateAverageRating() {
        // Placeholder for rating calculation
        // In a real system, this would calculate from member feedback
        return 4.5;
    }

    // Getters and Setters
    public String getSpecialization() { return specialization; }
    public String getExperience() { return experience; }
    public String getAvailability() { return availability; }
    public double getHourlyRate() { return hourlyRate; }
    public int getMaxClients() { return maxClients; }
    public int getCurrentClients() { return currentClients; }
    public List<String> getAssignedMemberIds() { return assignedMemberIds; }
    public double getTotalEarnings() { return totalEarnings; }
    public List<String> getCertifications() { return certifications; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getAddress() { return address; }
    public List<String> getWorkoutPlansCreated() { return workoutPlansCreated; }
    public List<String> getSessionsCompleted() { return sessionsCompleted; }
    public String getWorkingHours() { return workingHours; }

    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public void setExperience(String experience) { this.experience = experience; }
    public void setAvailability(String availability) { this.availability = availability; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }
    public void setMaxClients(int maxClients) { this.maxClients = maxClients; }
    public void setCurrentClients(int currentClients) { this.currentClients = currentClients; }
    public void setTotalEarnings(double totalEarnings) { this.totalEarnings = totalEarnings; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setAddress(String address) { this.address = address; }
    public void setWorkingHours(String workingHours) { this.workingHours = workingHours; }

    public void addEarnings(double amount) { totalEarnings += amount; }
    public void addCertification(String certification) {
        if (!certifications.contains(certification)) {
            certifications.add(certification);
        }
    }
    public void removeCertification(String certification) { certifications.remove(certification); }
}