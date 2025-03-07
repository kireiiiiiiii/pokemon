/*
 * Author: Matěj Šťastný aka Kirei
 * Date created: 12/17/2023
 * Github link: https://github.com/kireiiiiiiii/pokemon
 */

package pokemon.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Helper class with methods to help with the handeling of arrays, lists and
 * files, or formating strings
 */
public class Util {

    /* FILE HANDELING METHODS */

    /**
     * Returns a string of a line in a .txt file, prints error message if failed and
     * returns null
     *
     * @param file - the file its reading
     * @param line - number of line its reading
     * @return - string of the file read, null if the read was unsuccesfull
     */
    public static String readFileLine(File file, int line) {
        int lines = 0;
        if (!file.exists()) {
            System.out.println("File does not exist");
            return null;
        }
        if (line <= 0) {
            System.out.println("Not valid line (<=0)");
            return null;
        }
        try {
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                fileScanner.nextLine();
                lines++;
            }
            fileScanner.close();
        } catch (IOException e) {
            System.out.print("There was an error when handeling the file");
            return null;
        }
        if (line > lines) {
            // System.out.println("Line out of index");
            return null;
        }
        try {
            int currLine = 1;
            Scanner fileScanner = new Scanner(file);
            String currString = "";
            while (fileScanner.hasNextLine()) {
                currString = fileScanner.nextLine();
                if (currLine == line) {
                    fileScanner.close();
                    return currString;
                }
                currLine++;
            }
            fileScanner.close();
        } catch (IOException e) {
            System.out.print("There was an error when handeling the file");
            return null;
        }
        return null;
    }

    /**
     * Converts a file to an ArrayList of Strings, putting every line of the file in
     * a separate element
     *
     * @param file - target file
     * @return - ArrayList of Strings
     */
    public static ArrayList<String> fileToList(File file) {
        ArrayList<String> contents = new ArrayList<String>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                contents.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            return null;
        }
        return contents;
    }

    /**
     * Wrties the inside of an ArrayList into a file, fully clearing the file
     * beforehand. Puts every element of the list on a separate line
     *
     * @param list - ArrayList of Strings
     * @param file - target file
     * @throws IOException FileWriter exception
     */
    public static void listToFile(ArrayList<String> list, File file) throws IOException {
        FileWriter fw = new FileWriter(file, false);
        PrintWriter pw = new PrintWriter(fw, false);
        pw.flush();
        pw.close();
        fw.close();
        fw = new FileWriter(file, true);
        for (int i = 0; i < list.size(); i++) {
            fw.write(list.get(i));
            if (i != list.size() - 1) {
                fw.write("\n");
            }
        }
        fw.close();
    }

    /**
     * Counts the number of lines a file has
     *
     * @param file - the file you want to read
     * @return - returns a int with the lines count
     */
    public static int countFileLines(File file) {
        if (!file.exists()) {
            System.out.print("File does not exist");
            return -1;
        }
        int lines = 0;
        try {
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                fileScanner.nextLine();
                lines++;
            }
            fileScanner.close();
        } catch (IOException e) {
            System.out.print("There was an error when handeling the file");
            return -1;
        }
        return lines;
    }

    /* ARRAY HANDELING METHODS */

    /**
     * Searches a String array if it contains target String with ignoring the case
     *
     * @param search   - the string youre searching for
     * @param searched - the array that is being searched
     * @return - returns a boolean, true if found, false if not
     */
    public static boolean laysInArray(String search, String[] searched) {
        for (String element : searched) {
            if (search.equalsIgnoreCase(element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Converts a string array to a String with elements being separated by coma and
     * space (', ')
     *
     * @param array - parameter - its the array that is going to be printed
     * @return return all the strings in the array separated by commas and spaces
     */
    public static String arrayToString(String[] array, String divider, String start) {
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
}
