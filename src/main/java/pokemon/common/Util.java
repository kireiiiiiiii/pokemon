package pokemon.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Utility class for handling arrays, lists, and files, as well as formatting
 * strings.
 */
public class Util {

    // File handeling -----------------------------------------------------------

    public static String getApplicationDataFolder() {
        String os = System.getProperty("os.name").toLowerCase();
        String appDataFolder = System.getenv("APPDATA");

        if (os.contains("mac")) {
            appDataFolder = System.getProperty("user.home") + File.separator + "Library" + File.separator + "Application Support";
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            appDataFolder = System.getProperty("user.home") + File.separator + ".config";
        } else if (appDataFolder == null) {
            appDataFolder = File.separator;
        }

        File folder = new File(appDataFolder);
        if (!folder.exists() && !folder.mkdirs()) {
            throw new RuntimeException("Failed to create application data folder at: " + appDataFolder);
        }
        return appDataFolder;
    }

    /**
     * Reads a specific line from a file.
     *
     * @param file The file to read from.
     * @param line The line number to retrieve (1-based index).
     * @return The content of the specified line, or null if an error occurs.
     */
    public static String readFileLine(File file, int line) {
        if (!file.exists()) {
            System.err.println("Error: File does not exist - " + file.getAbsolutePath());
            return null;
        }
        if (line <= 0) {
            System.err.println("Error: Invalid line number (must be >= 1)");
            return null;
        }

        try (Scanner scanner = new Scanner(file)) {
            int currLine = 1;
            while (scanner.hasNextLine()) {
                String content = scanner.nextLine();
                if (currLine == line) {
                    return content;
                }
                currLine++;
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + file.getAbsolutePath());
        }
        return null;
    }

    /**
     * Converts a file to a list of strings, where each line becomes an element.
     *
     * @param file The target file.
     * @return A list containing the file's contents, or null if an error occurs.
     */
    public static List<String> fileToList(File file) {
        if (!file.exists()) {
            System.err.println("Error: File not found - " + file.getAbsolutePath());
            return null;
        }

        List<String> contents = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                contents.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not accessible - " + file.getAbsolutePath());
            return null;
        }
        return contents;
    }

    /**
     * Writes the contents of a list to a file, replacing any existing content.
     *
     * @param list The list of strings to write.
     * @param file The target file.
     * @throws IOException If an I/O error occurs.
     */
    public static void listToFile(List<String> list, File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, false))) {
            for (String line : list) {
                writer.println(line);
            }
        }
    }

    /**
     * Counts the number of lines in a file.
     *
     * @param file The file to count lines in.
     * @return The number of lines, or -1 if the file does not exist or cannot be
     *         read.
     */
    public static int countFileLines(File file) {
        if (!file.exists()) {
            System.err.println("Error: File does not exist - " + file.getAbsolutePath());
            return -1;
        }

        int lines = 0;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                scanner.nextLine();
                lines++;
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + file.getAbsolutePath());
            return -1;
        }
        return lines;
    }

    // Array handeling ----------------------------------------------------------

    /**
     * Checks if a string exists in an array, ignoring case.
     *
     * @param search   The string to search for.
     * @param searched The array to search in.
     * @return True if found, false otherwise.
     */
    public static boolean containsIgnoreCase(String search, String[] searched) {
        for (String element : searched) {
            if (search.equalsIgnoreCase(element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Converts an array of strings into a single formatted string.
     *
     * @param array   The array to convert.
     * @param divider The separator between elements.
     * @param prefix  A prefix to add before each element.
     * @return A formatted string.
     */
    public static String arrayToString(String[] array, String divider, String prefix) {
        if (array == null || array.length == 0) {
            return "";
        }

        StringBuilder output = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            output.append(prefix).append(array[i]);
            if (i < array.length - 1) {
                output.append(divider);
            }
        }
        return output.toString();
    }

}
