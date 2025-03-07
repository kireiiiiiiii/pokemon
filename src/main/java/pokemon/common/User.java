/*
 * Author: Matěj Šťastný aka Kirei
 * Date created: 12/17/2023
 * Github link: https://github.com/kireiiiiiiii/pokemon
 */

package pokemon.common;

import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * User object class
 *
 */
public class User {
    private String username;
    @SuppressWarnings("unused")
    private String password;
    private File userFile;
    private String presetPath;
    private String folderPath;

    private Scanner console = new Scanner(System.in);

    public User(String path) {
        createUserFolder(path);
        Scanner console = new Scanner(System.in);
        this.folderPath = getUserFilesFolder(path);
        System.out.println("nyaa  " + this.folderPath); // TODO delete
        this.presetPath = folderPath + username + "USER.txt";
        while (true) {
            String[] usersList = getUsers(folderPath);
            userFile = getUser(console, usersList, this.presetPath); // returns null if new user selected

            /* NEW USER */
            if (userFile == null) { // user file doesn't exist = new user has been created
                userFile = createUser(usersList, this.presetPath);
                if (setPassword()) {
                    break; // exits loop, if succesful
                } else {
                    userFile.delete(); // delets the file, so no empty files
                    continue;
                }
                // setUser(user, USERPATH);
            }

            /* OLD USER */
            else {
                if (getPassword(5)) {
                    break; // exits loop if succesful
                } else {
                    continue; // goes through the loop again
                }
            }
        }
        setDataFromFile(userFile);
    }

    /* ACCESORS */

    /**
     * Path accesor
     *
     * @return Path of the UserFile directory
     */
    public String getPath() {
        return presetPath;
    }

    /**
     * Username accesor
     *
     * @return Username of the user saved in the object
     */
    public String getUsername() {
        return username;
    }

    /**
     * User file accesor
     *
     * @return User data file object of the user saved in this object
     */
    public File getUserFile() {
        return userFile;
    }

    /* PRIVATE METHODS */

    /**
     * Gets the path of the UserFiles folder from the path of the directory of the
     * whole game
     *
     * @param path - String of the path of the directory of the game
     * @return String of the path of the UserFile folder
     */
    private String getUserFilesFolder(String path) {
        return path + "/data";
    }

    /**
     * Checks, if the folder for user files exists, and if it does not, it will
     * create one
     *
     * @param path - path of the game
     */
    private void createUserFolder(String path) {
        File userFolder = new File(path + "/data");
        if (!userFolder.exists()) {
            userFolder.mkdirs();
        }
    }

    /**
     * Converts a string array to a String with elements being separated by coma and
     * space (', ')
     *
     * @param array - parameter - its the array that is going to be printed
     * @return return all the strings in the array separated by commas and spaces
     */
    private String arrayToString(String[] array, String divider, String start) {
        int lenght = array.length - 1;
        if (lenght < 0) {
            return "";
        }
        String output = "";
        for (int i = 0; i < lenght; i++) {
            output += start + array[i] + divider;
        }
        output += start + array[lenght];
        return output;
    }

    /**
     * Sets local variables of the objects from a user file according to
     * 'userExample.txt' file
     *
     * @param userFile
     */
    private void setDataFromFile(File userFile) {
        username = Util.readFileLine(userFile, 1);
        password = Util.readFileLine(userFile, 2);
    }

    /**
     * Prompts user to select an account
     *
     * @param console - Scanner with System.in
     * @param users   - array of names of current user accounts
     * @param path    - path of user files
     * @return - return a file object set to the file of the user selected
     */
    private File getUser(Scanner console, String[] users, String path) {
        System.out.print("Which user do you select?:\n" + arrayToString(users, "\n", "--") + "\nNEW USER" + "\n\n> ");
        String user = console.nextLine();
        while (!Util.laysInArray(user, users) && !user.equalsIgnoreCase("new user")) {
            System.out.print("This user does not exist...\nTry another one: ");
            user = console.nextLine();
        }
        if (!user.equalsIgnoreCase("new user")) {
            System.out.println("Welcome back " + user + "!");
        } else {
            return null;
        }
        File currUser = new File(path + "/" + user + "USER.txt");
        return currUser;
    }

    /**
     * This method lists all users (*USER.txt files) in the path given
     *
     * @param path - path being searched
     * @return - returnes an array of Strings of names of all users
     */
    private String[] getUsers(String path) {
        File dir = new File(path);
        int userCount = 0;
        if (dir.listFiles() == null) {
            return new String[0];
        }
        for (File file : dir.listFiles()) {
            if (isUser(file.getName())) {
                userCount++;
            }
        }
        String[] users = new String[userCount];
        int i = 0;
        for (File file : dir.listFiles()) {
            if (isUser(file.getName())) {
                users[i] = file.getName().substring(0, file.getName().length() - 8);
                i++;
            }
        }
        return users;
    }

    /**
     * Checks if file is a user data file (ends with 'USER.txt')
     *
     * @param file - name of the file its checking
     * @return - returnes true/false
     */
    private boolean isUser(String file) {
        int lenght = file.length();
        if (lenght < 8) {
            return false;
        }
        return file.substring(lenght - 8).equals("USER.txt");
    }

    /**
     * Prompts user to create his new account, it will create a new file named
     * 'accountnameUSER.txt'
     *
     * @param users - array of current user accounts
     * @return - returns a file obj set to the file of the newly created user
     */
    private File createUser(String[] users, String presetPath) {
        System.out.print("Create a username: ");
        String username = console.nextLine();
        while (true) {
            if (Util.laysInArray(username, users)) {
                System.out.print("This username already exists...\nTry another one: ");
            } else if (username.equals("new user")) {
                System.out.print("You can't use this username... \nTry another one: ");
            } else {
                break;
            }
            username = console.nextLine();
        }
        System.out.println(presetPath);
        File newUser = new File(presetPath);
        try {
            newUser.createNewFile();
            FileWriter fw = new FileWriter(newUser);
            fw.append(username);
            fw.close();
            System.out.println("User created succesfully!");
        } catch (IOException e) {
            System.out.println("There was an error while creating new user file");
        }
        return newUser;
    }

    /**
     * It will delete the data file of the user, it will promt the user to confirm
     * its deletion
     *
     * @param currUser - user which file will be deleted
     * @param console  - scanner with System.in
     */
    public void deleteUser(File currUser, File currPreset, Scanner console) {
        System.out.print("Are you sure to delete your account? (y/n): ");
        String answer = console.nextLine();
        while (!answer.equalsIgnoreCase("y") && !answer.equalsIgnoreCase("n")) {
            System.out.println("I didn't understand...\nTry typing y/n: ");
            answer = console.nextLine();
        }
        if (answer.equalsIgnoreCase("n")) {

        } else {
            String path = currUser.getAbsolutePath();
            boolean wasSuccesful = currUser.delete();
            if (!wasSuccesful) {
                System.out.println("File deletion unsuccesful, path to file: " + path);
            }
            if (currPreset.exists()) {
                wasSuccesful = currPreset.delete();
                path = currPreset.getAbsolutePath();
                if (!wasSuccesful) {
                    System.out.println("File deletion unsuccesful, path to file: " + path);
                }
            }
        }
    }

    /**
     * Encrypts a string
     *
     * @param target - target string
     * @return - returns the encrypted String
     */
    private String encrypt(String target) {
        String SECRET_KEY = "ThisIsASecretKey";
        String INIT_VECTOR = "RandomInitVector";
        try {
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);

            byte[] encrypted = cipher.doFinal(target.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Prompts user to enter password to the account he selected
     *
     * @param maxAttempts - maximum ammount of wrong password attempts
     * @return - returns boolean, true if password correct, false if not
     */
    private boolean getPassword(int maxAttempts) {
        Console passwordReader = System.console();
        String password = Util.readFileLine(userFile, 2);
        if (password == null) {
            return setPassword();
        } else {
            String input = "";
            int attempts = 0;
            System.out.print("Enter password: ");
            input = encrypt(new String(passwordReader.readPassword()));
            while (!input.equals(password) && attempts < maxAttempts) {
                System.out.print("Incorrect  password, try again... \n" + (maxAttempts - attempts) + " attempts remaining: ");
                input = encrypt(new String(passwordReader.readPassword()));
                attempts++;
            }
            if (input.equals(password)) {
                System.out.println("Password correct!");
                return true;
            } else {
                System.out.println("Too many attempts...");
                return false;
            }
        }
    }

    /**
     * Sets password to a user
     *
     * @param console - scanner with system.in
     * @return - returns a boolean, true if password settings succesful, false if
     *         not
     */
    private boolean setPassword() {
        Console passwordReader = System.console();
        String password = "";
        String confirm = "";
        System.out.print("Create password: ");
        password = encrypt(new String(passwordReader.readPassword()));
        System.out.print("Confirm password: ");
        confirm = encrypt(new String(passwordReader.readPassword()));
        int attempts = 0;
        while (!password.equals(confirm) && attempts < 6) {
            System.out.print("Passwords don't match...\nTry again: ");
            confirm = encrypt(new String(passwordReader.readPassword()));
            attempts++;
        }
        if (!password.equals(confirm)) {
            System.out.println("Too many attempts, try again.");
            return false;
        } else {
            try {
                FileWriter fw = new FileWriter(userFile, true);
                fw.append("\n" + password);
                fw.close();
            } catch (IOException e) {
                System.out.println("IOException");
                return false;
            }
            System.out.println("Password set sucesfully!");
            return true;
        }
    }
}
