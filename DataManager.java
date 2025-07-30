// DataManager.java - Handles data persistence
import java.io.*;
import java.util.*;

public class DataManager {
    private static final String DATA_DIR = "gym_data/";
    private static final String USERS_FILE = DATA_DIR + "users.dat";
    private static final String ASSIGNMENTS_FILE = DATA_DIR + "assignments.dat";
    private static final String WORKOUTS_FILE = DATA_DIR + "workouts.dat";
    private static final String PAYMENTS_FILE = DATA_DIR + "payments.dat";

    public DataManager() {
        createDataDirectory();
    }

    private void createDataDirectory() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // Save all users to file
    public boolean saveUsers(Map<String, User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
            System.out.println(GymUI.GREEN + "✅ Data saved successfully!" + GymUI.RESET);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
            return false;
        }
    }

    // Load all users from file
    @SuppressWarnings("unchecked")
    public Map<String, User> loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            Map<String, User> loadedUsers = (Map<String, User>) ois.readObject();
            System.out.println(GymUI.CYAN + "📂 Data loaded successfully!" + GymUI.RESET);
            return loadedUsers;
        } catch (FileNotFoundException e) {
            System.out.println(GymUI.YELLOW + "⚠️ No existing data found. Starting fresh." + GymUI.RESET);
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading users: " + e.getMessage());
            return new HashMap<>();
        }
    }

    // Save trainer-member assignments
    public boolean saveAssignments(Map<String, String> assignments) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ASSIGNMENTS_FILE))) {
            oos.writeObject(assignments);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving assignments: " + e.getMessage());
            return false;
        }
    }

    // Load trainer-member assignments
    @SuppressWarnings("unchecked")
    public Map<String, String> loadAssignments() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ASSIGNMENTS_FILE))) {
            return (Map<String, String>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading assignments: " + e.getMessage());
            return new HashMap<>();
        }
    }

    // Save workout plans
    public boolean saveWorkouts(Map<String, List<String>> workouts) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(WORKOUTS_FILE))) {
            oos.writeObject(workouts);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving workouts: " + e.getMessage());
            return false;
        }
    }

    // Load workout plans
    @SuppressWarnings("unchecked")
    public Map<String, List<String>> loadWorkouts() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(WORKOUTS_FILE))) {
            return (Map<String, List<String>>) ois.readObject();
        } catch (FileNotFoundException e) {
            return new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading workouts: " + e.getMessage());
            return new HashMap<>();
        }
    }

    // Check if data files exist
    public boolean dataFilesExist() {
        return new File(USERS_FILE).exists();
    }

    // Backup data
    public boolean backupData() {
        try {
            String backupDir = DATA_DIR + "backup_" + System.currentTimeMillis() + "/";
            File backup = new File(backupDir);
            backup.mkdirs();

            // Copy all data files to backup directory
            copyFile(USERS_FILE, backupDir + "users.dat");
            copyFile(ASSIGNMENTS_FILE, backupDir + "assignments.dat");
            copyFile(WORKOUTS_FILE, backupDir + "workouts.dat");

            System.out.println(GymUI.GREEN + "✅ Backup created successfully!" + GymUI.RESET);
            return true;
        } catch (Exception e) {
            System.err.println("Error creating backup: " + e.getMessage());
            return false;
        }
    }

    private void copyFile(String source, String destination) throws IOException {
        File sourceFile = new File(source);
        if (sourceFile.exists()) {
            try (FileInputStream fis = new FileInputStream(sourceFile);
                 FileOutputStream fos = new FileOutputStream(destination)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
            }
        }
    }
}