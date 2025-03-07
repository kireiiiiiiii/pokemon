/*
 * Author: Matěj Šťastný aka Kirei
 * Date created: 12/17/2023
 * Github link: https://github.com/kireiiiiiiii/pokemon
 */

package pokemon.common;

import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Represents a User in the game. Handles user authentication and file
 * management.
 */
public class User {

    private static final Logger LOGGER = Logger.getLogger(User.class.getName());
    private final Scanner console = new Scanner(System.in);
    private static final String SECRET_KEY = "ThisIsASecretKey";
    private static final String INIT_VECTOR = "RandomInitVector";

    private final String gameDataDirPath;
    private String username;
    private String password;
    private File userFile;

    // Constructor --------------------------------------------------------------

    public User(String gamePath) {
        this.gameDataDirPath = gamePath + File.separator + "data";
        createDataFolder();
        authenticateUser();
    }

    // Accesors -----------------------------------------------------------------

    public String getUsername() {
        return username;
    }

    public File getUserFile() {
        return userFile;
    }

    // Private ------------------------------------------------------------------

    private void createDataFolder() {
        File dataFolder = new File(gameDataDirPath);
        if (!dataFolder.exists() && dataFolder.mkdirs()) {
        }
    }

    private void authenticateUser() {
        String[] users = listUsers();
        while (true) {
            userFile = promptUserSelection(users);
            if (userFile == null) {
                userFile = createUser(users);
                if (setPassword()) {
                    break;
                }
                userFile.delete();
            } else {
                this.password = Util.readFileLine(userFile, 2);
                if (validatePassword()) {
                    break;
                }
            }
        }
        loadUserData();
    }

    private File promptUserSelection(String[] users) {
        System.out.print("Select a user or type 'NEW USER':\n" + formatUsers(users) + "\n> ");
        String input = console.nextLine().trim();

        while (!isUserValid(input, users)) {
            System.out.print("Invalid choice. Try again: ");
            input = console.nextLine().trim();
        }

        return input.equalsIgnoreCase("NEW USER") ? null : new File(gameDataDirPath, input + "USER.txt");
    }

    private boolean isUserValid(String input, String[] users) {
        if (input.equalsIgnoreCase("NEW USER"))
            return true;
        for (String user : users) {
            if (user.equalsIgnoreCase(input))
                return true;
        }
        return false;
    }

    private File createUser(String[] users) {
        System.out.print("Enter a username: ");
        String name;
        while (true) {
            name = console.nextLine().trim();
            if (!isUserValid(name, users) && !name.equalsIgnoreCase("NEW USER"))
                break;
            System.out.print("Username taken or invalid. Try again: ");
        }

        File newUser = new File(gameDataDirPath, name + "USER.txt");
        try (FileWriter writer = new FileWriter(newUser)) {
            writer.write(name + "\n");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to create user file", e);
        }
        return newUser;
    }

    private void loadUserData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            username = reader.readLine();
            password = reader.readLine();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading user file", e);
        }
    }

    private boolean validatePassword() {
        Console consoleReader = System.console();
        if (consoleReader == null) {
            LOGGER.severe("No console available. Cannot validate password.");
            return false;
        }

        for (int attempts = 0; attempts < 5; attempts++) {
            System.out.print("Enter password: ");
            String input = encrypt(new String(consoleReader.readPassword()));
            if (input.equals(password)) {
                System.out.println("Login successful!");
                return true;
            }
            System.out.println("Incorrect password. Attempts remaining: " + (4 - attempts));
            System.out.println(input + " | " + password);
        }
        return false;
    }

    private boolean setPassword() {
        Console consoleReader = System.console();
        if (consoleReader == null) {
            LOGGER.severe("No console available. Cannot set password.");
            return false;
        }

        System.out.print("Create password: ");
        String pass = encrypt(new String(consoleReader.readPassword()));

        System.out.print("Confirm password: ");
        String confirm = encrypt(new String(consoleReader.readPassword()));

        if (!pass.equals(confirm)) {
            System.out.println("Passwords do not match. Try again.");
            return false;
        }

        try (FileWriter writer = new FileWriter(userFile, true)) {
            writer.append(pass).append("\n");
            return true;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save password", e);
            return false;
        }
    }

    private String encrypt(String target) {
        try {
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
            return Base64.getEncoder().encodeToString(cipher.doFinal(target.getBytes()));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Encryption failed", e);
        }
        return null;
    }

    private String[] listUsers() {
        File dir = new File(gameDataDirPath);
        File[] files = dir.listFiles((d, name) -> name.endsWith("USER.txt"));
        if (files == null) {
            return new String[0];
        }

        String[] users = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            users[i] = files[i].getName().replace("USER.txt", "");
        }
        return users;
    }

    private String formatUsers(String[] users) {
        if (users.length == 0) {
            return "No users found.";
        }
        StringBuilder sb = new StringBuilder();
        for (String user : users) {
            sb.append("-- ").append(user).append("\n");
        }
        return sb.toString();
    }

}
