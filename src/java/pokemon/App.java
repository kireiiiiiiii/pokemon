/*
 * Author: Matěj Šťastný aka Kirei
 * Date created: 12/17/2023
 * Github link: https://github.com/kireiiiiiiii/pokemon
 */

package pokemon;

import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import pokemon.common.Preset;
import pokemon.common.User;
import pokemon.common.Util;
import pokemon.pokemonlib.Bulbasaur;
import pokemon.pokemonlib.Eevee;
import pokemon.pokemonlib.Flareon;
import pokemon.pokemonlib.Mew;
import pokemon.pokemonlib.Mewtwo;
import pokemon.pokemonlib.Pichu;
import pokemon.pokemonlib.Pikachu;
import pokemon.pokemonlib.Pokemon;
import pokemon.pokemonlib.Raichu;

public class App {

    public static final String COLORRESET = "\u001B[0m";
    public static final String[] POKEMONLIST = { "pichu", "pikachu", "raichu", "bulbasaur", "eevee", "flareon", "mew", "mewtwo" };
    public static final String[] BASE_POKEMONS = { "pichu", "bulbasaur", "eevee", "mew" };
    public static final String[] CLASSLIST = { "Electric", "Seed", "Normal", "Fire" };

    public static void main(String[] args) {
        printBanner();
        Pokemon pokemon = null;
        Scanner console = new Scanner(System.in);
        String path = getGamePath();

        User user = new User(path);
        Preset preset = new Preset(user);

        File userFile = user.getUserFile();
        File presetFile = preset.getPreset();
        int presetIndex = 0;

        // loads preset if preset file exists and user didn't select new pokemon in
        // getPresetIndex
        if (preset.isValidPreset()) {
            presetIndex = getPresetIndex(preset, console);
            if (presetIndex != -1) {
                pokemon = loadPokemon(presetFile, presetIndex);
            }
        }

        // if pokemon is still null, create a new one
        if (pokemon == null) {
            pokemon = newPokemon(console);
            presetIndex = getPresetCount(presetFile) + 1;
        }

        console(pokemon, userFile, presetFile, presetIndex, preset, user);
    }

    /**
     * Prints the game banner ascii art into console
     */
    public static void printBanner() {
        String[] welcomeArray = { "|------------------------------------------------------------------------------------------|", "|                                                                                          |", "| __________       __                                      ________                        |", "| \\______   \\____ |  | __ ____   _____   ____   ____      /  _____/_____    _____   ____   |", "|  |     ___/  _ \\|  |/ // __ \\ /     \\ /  _ \\ /    \\    /   \\  ___\\__  \\  /     \\_/ __ \\  |", "|  |    |  (  <_> )    <\\  ___/|  Y Y  (  <_> )   |  \\   \\    \\_\\  \\/ __ \\|  Y Y  \\  ___/  |", "|  |____|   \\____/|__|_ \\\\___  >__|_|  /\\____/|___|  /    \\______  (____  /__|_|  /\\___  > |", "|                      \\/    \\/      \\/            \\/            \\/     \\/      \\/     \\/  |", "|                                                                                          |",
                "|------------------------------------------------------------------------------------------|", " " };
        System.out.print(Util.arrayToString(welcomeArray, "\n", ""));
    }

    /**
     * Promts user to choose a pokemon type, than compares it with the list of
     * current pokemons, if the pokemon is not in the list, it promts the user again
     *
     * @param console - Scanner
     * @param list    - list of pokemons that the user can select from
     * @return - returns a String of the pokemon type
     */
    public static String getPokemonType(Scanner console, String[] list) {
        System.out.print("Choose your pokemon!: ");
        String pokemonType;
        while (true) {
            pokemonType = console.nextLine();
            if (Util.laysInArray(pokemonType, list)) {
                System.out.println(pokemonType.substring(0, 1).toUpperCase() + pokemonType.substring(1) + " selected!");
                break;
            } else {
                System.out.println("Invalid Pokemon, you can only choose " + Util.arrayToString(list, ", ", "") + "...");
            }
        }
        return pokemonType;
    }

    /**
     * Promts the user to name his pokemon
     *
     * @param console - Scanner with system.in
     * @return - returns a string of the name
     */
    public static String getName(Scanner console) {
        System.out.print("What do you want to name your pokemon?: ");
        String name = console.nextLine();
        while (name.equalsIgnoreCase("") || name == null || name.equalsIgnoreCase(" ")) {
            System.out.println("Sorry, but you can't name your pokemon like this...");
            System.out.print("Try another one: ");
            name = console.nextLine();
        }
        System.out.println("Your pokemon was named " + name + "! What a nice name!");
        return name;
    }

    /**
     * Creates a pokemon object based on the parameters
     *
     * @param name - user defined name of the pokemon
     * @param type - type of the pokemon, determines the type of the object
     * @param hp   - hp of the pokemon
     * @return Created pokemon object reference
     */
    public static Pokemon createPokemon(String name, String type, int hp) {
        type = type.toLowerCase();
        Pokemon pokemon = null;
        switch (type) {
        case "pichu":
            pokemon = new Pichu(name, hp);
            break;
        case "bulbasaur":
            pokemon = new Bulbasaur(name, hp);
            break;
        case "eevee":
            pokemon = new Eevee(name, hp);
            break;
        case "raichu":
            pokemon = new Raichu(name, hp);
            break;
        case "pikachu":
            pokemon = new Pikachu(name, hp);
            break;
        case "flareon":
            pokemon = new Flareon(name, hp);
            break;
        case "mew":
            pokemon = new Mew(name, hp);
            break;
        case "mewtwo":
            pokemon = new Mewtwo(name, hp);
            break;
        default:
            assert (false) : "changePokemon failed - wrong type: " + type + "|";
        }
        return pokemon;
    }

    /**
     * Creates a new base pokemon by asking the user
     *
     * @param console - scanner with system in
     * @return Created pokemon object reference
     */
    public static Pokemon newPokemon(Scanner console) {
        String type = getType(console, BASE_POKEMONS);
        String name = getName(console);
        return createPokemon(name, type, -1);
    }

    /**
     * Prompts user to choose a pokemon from a list
     *
     * @param console - scanner with system.in
     * @param list    - list that he has to choose from
     * @return - string of type in lowercase
     */
    public static String getType(Scanner console, String[] list) {
        System.out.print("Choose your pokemon!: ");
        String type = console.nextLine().toLowerCase();
        while (!Util.laysInArray(type, list)) {
            System.out.println("You can't choose that pokemon...\nPlease choose from " + Util.arrayToString(list, "", ", ").substring(2) + "...");
            type = console.nextLine().toLowerCase();
        }
        System.out.println(type.substring(0, 1).toUpperCase() + type.substring(1) + " selected!");
        return type;
    }

    /**
     * Prompts user to select his user, if valid enter password, if valid and in
     * less than number of attempts, than pass and assign file If user types create
     * new user, that create new user
     *
     * @param console - scanner with system.in
     * @return - returns the file of the user
     */
    public static File setUser(Scanner console, String userPath) {
        File user = null;
        while (true) {
            String[] usersList = getUsers(userPath);
            user = getUser(console, usersList, userPath); // returns null if new user selected
            // new user
            if (user == null) {
                user = createUser(console, usersList);
                if (setPassword(user, console)) {
                    return user; // returns user if succesful
                } else {
                    user.delete(); // delets the file, so no empty files
                }
                // setUser(user, USERPATH);
            }
            // old user
            else {
                if (getPassword(user, console, 5)) {
                    return user; // returns the user if succesful
                } else {
                } // else - try again
            }
        }
    }

    /**
     * User console
     *
     * @param pokemon    - current pokemon
     * @param userFile   - current user file
     * @param presetFile - current preset file
     * @param index      - file preset slot index of the current pokemon
     */
    public static void console(Pokemon pokemon, File userFile, File presetFile, int index, Preset preset, User user) {
        String commandInput;
        Scanner console = new Scanner(System.in);
        Pokemon evolvePokemon;
        while (true) {
            System.out.print("\nWhat sould I do?: ");
            commandInput = console.nextLine();
            if (commandInput.equals("ability 1")) {
                pokemon.ability1();
            } else if (commandInput.equalsIgnoreCase("ability 2")) {
                pokemon.ability2();
            } else if (commandInput.equalsIgnoreCase("image")) {
                pokemon.image();
            } else if (commandInput.equalsIgnoreCase("stats")) {
                pokemon.stats();
            } else if (commandInput.equalsIgnoreCase("exit")) {
                System.out.print("Do you want to save your pokemon? (y/n): ");
                String answer = console.nextLine();
                while (true) {
                    if (answer.equalsIgnoreCase("y")) {
                        savePokemon(presetFile, userFile, pokemon, index);
                        System.out.println("Pokemon saved succesfully!\nBye!");
                        break;
                    } else if (answer.equalsIgnoreCase("n")) {
                        System.out.println("Bye!");
                        break;
                    } else {
                        System.out.println("Answer y/n!: ");
                        answer = console.nextLine();
                    }
                }
                break;
            } else if (commandInput.equalsIgnoreCase("evolve")) {
                evolvePokemon = evolve(pokemon, console);
                if (evolvePokemon != null) {
                    pokemon = evolvePokemon;
                }
            } else if (commandInput.equalsIgnoreCase("delete account")) {
                deleteUser(userFile, presetFile, console);
                break;
            } else if (commandInput.equalsIgnoreCase("save pokemon")) {
                savePokemon(presetFile, userFile, pokemon, index);
                System.out.println("Pokemon saved sucesfully!");
            } else if (commandInput.equalsIgnoreCase("new pokemon")) {
                if (!savePokemon(presetFile, userFile, pokemon, index)) {
                    System.out.print("Pokemon wasn't saved sucsfully... Do you want to continue? (y/n): ");
                    String input = console.nextLine();
                    while (!input.equalsIgnoreCase("y") && !input.equalsIgnoreCase("n")) {
                        System.out.println("Answer 'y' for yes and 'n' for no: ");
                        input = console.nextLine();
                    }
                    if (input.equalsIgnoreCase("y")) {
                        pokemon = newPokemon(console);
                        index++;
                    }
                } else {
                    pokemon = newPokemon(console);
                    index++;
                }
            } else if (commandInput.equalsIgnoreCase("swich pokemon")) {
                savePokemon(presetFile, userFile, pokemon, index);
                index = getPresetIndex(preset, console);
                if (index != -1) {
                    pokemon = loadPokemon(presetFile, index);
                } else {
                    pokemon = newPokemon(console);
                    index = getPresetCount(presetFile) + 1;
                }
            } else if (commandInput.equalsIgnoreCase("delete pokemon")) {
                savePokemon(presetFile, userFile, pokemon, index);
                deletePokemon(presetFile, index, console);
                index = getPresetIndex(preset, console);
                if (index != -1) {
                    pokemon = loadPokemon(presetFile, index);
                } else {
                    pokemon = newPokemon(console);
                    index = getPresetCount(presetFile) + 1;
                }
            } else {
                System.out.println("I didn't understand...");
                System.out.println("You can only use these commands:");
                System.out.println("     -Stats\n     -Ability 1\n     -Ability 2\n     -Evolve\n     -Image\n     -Swich Pokemon\n     -New Pokemon\n     -Save Pokemon\n     -Delete Pokemon\n     -Exit\n     -Delete account");
            }
        }
    }

    /**
     * Evolves the pokemon, asks user for confirmation, if the pokemon has more
     * evolves, it asks the user for which one should be used
     *
     * @param pokemon - current pokemon used
     * @param console - scanner with system.in
     * @return - returns the pokemon that evolved, or null if the user didnt confirm
     *         the evolve
     */
    public static Pokemon evolve(Pokemon pokemon, Scanner console) {
        String[] stageType = pokemon.getStageType();
        Pokemon evolved;
        if (stageType == null) {
            System.out.println("This pokemon doesn't evolve...");
            return null;
        } else if (stageType.length == 1) {
            System.out.print("Do you want to evolve your " + pokemon.getType() + " to " + stageType[0] + "? (y/n):");
            String answer = console.nextLine();
            while (!answer.equalsIgnoreCase("y") && !answer.equalsIgnoreCase("n")) {
                System.out.print("I didn't understand...\nAnswer 'y' or 'n': ");
                answer = console.nextLine();
            }
            if (answer.equalsIgnoreCase("y")) {
                evolved = createPokemon(pokemon.getName(), stageType[0], -1);
                System.out.println(evolved.getName() + " evolved to " + evolved.getType() + "!");
                return evolved;
            } else {
                return null;
            }
        } else {
            System.out.print("This pokemon can evolve to " + Util.arrayToString(stageType, " or ", "") + ".\nWhich one do you want to evolve to?:");
            String answerType = console.nextLine();
            while (!Util.laysInArray(answerType, stageType)) {
                System.out.print("You can only choose from " + Util.arrayToString(stageType, ",", "") + "...\nTry again: ");
                answerType = console.nextLine();
            }
            System.out.print("Do you want to evolve your " + pokemon.getType() + " to " + answerType + "? (y/n): ");
            String answer = console.nextLine();
            while (!answer.equalsIgnoreCase("y") && !answer.equalsIgnoreCase("n")) {
                System.out.print("I didn't understand...\nAnswer 'y' or 'n': ");
                answer = console.nextLine();
            }
            if (answer.equalsIgnoreCase("y")) {
                evolved = createPokemon(pokemon.getName(), answerType, -1);
                System.out.println(evolved.getName() + " evolved to " + evolved.getType() + "!");
                return evolved;
            } else {
                return null;
            }
        }
    }

    /**
     * Sets the user file, according to user, if the file for the preset doesn't
     * exist it will create it
     *
     * @param user - user file of the current user
     * @return - returns the file of the preset of the user given in @param
     */
    public static File setPreset(File user) {
        String username = Util.readFileLine(user, 1);
        File preset = new File(username + "PRESET.txt");
        if (preset.exists()) {
            return preset;
        } else {
            try {
                preset.createNewFile();
            } catch (IOException e) {
                System.out.println("IO error");
            }
            return preset;
        }
    }

    /**
     * Returs a number of preset slots that are in a file
     *
     * @param preset - preset file being scanned
     * @return - returns an int with the number of presets
     */
    public static int getPresetCount(File preset) {
        int lines = Util.countFileLines(preset);
        return (lines) / 4;
    }

    /**
     * Prompts the user to enter a preset number according to a printed list of
     * pokemons from a preset file, the user also has an option to select 'new
     * pokemon'
     *
     * @param preset  - preset file from which the user should select
     * @param console - scanner with system.in
     * @return - returns an int of the index selected, -1 if the user selected 'new
     *         pokemon'
     */
    public static int getPresetIndex(Preset preset, Scanner console) {
        String answer = "";
        if (!preset.isValidPreset()) {
            System.out.print("File does not exist");
            return -1;
        }
        int presetIndex = 0;
        boolean validInput = false;
        System.out.print("What pokemon do you select? ");
        System.out.println("Enter a number according to:");
        preset.printContents();
        System.out.println("NEW POKEMON");
        System.out.print("\n> ");
        do {
            answer = console.nextLine();
            if (answer.equalsIgnoreCase("new pokemon")) {
                return -1;
            }
            try {
                presetIndex = Integer.parseInt(answer);
                System.out.println(preset.getTypeOnIndex(presetIndex) + " selected!");
                return presetIndex;
            } catch (NumberFormatException e) {
                validInput = false;
            }
            if (!validInput) {
                System.out.print("I don't understand... \nEnter a valid preset index number, or 'new pokemon': ");
            }
            // console.next();
        } while (!validInput);
        return -1;
    }

    /**
     * Deletes pokemon data in a preset file according to an index, asks user for
     * confirmation
     *
     * @param preset - target preset file
     * @param index  - target index
     */
    public static void deletePokemon(File preset, int index, Scanner console) {
        assert (index <= getPresetCount(preset)) : "IndexOutOfBounds for deletePokemon";
        System.out.print("Are you sure you want to delete this pokemon? (y/n): ");
        String answer = console.nextLine();
        while (!answer.equalsIgnoreCase("y") && !answer.equalsIgnoreCase("n")) {
            System.out.print("I didn't understand...\nAnswer 'y' for yes or 'n' for no: ");
            answer = console.nextLine();
        }
        if (answer.equalsIgnoreCase("y")) {
            ArrayList<String> contents = Util.fileToList(preset);
            contents.remove((index - 1) * 4);
            contents.remove((index - 1) * 4);
            contents.remove((index - 1) * 4);
            contents.remove((index - 1) * 4);
            try {
                Util.listToFile(contents, preset);
            } catch (IOException e) {
                System.out.println("IOException");
            }
            System.out.println("Pokemon deleted!");
        }
    }

    /**
     * Saves variables of a pokemon according to the 'example.txt' file to the slot
     * given by the index, if the index is greater than the number of presets saved,
     * it will create a new slot and save
     *
     * @param preset  - preset file the pokemon is being saved into
     * @param user    - user file to acces the username of user saving
     * @param pokemon - pokemon thats being saved
     * @param index   - index of the target save slot
     * @return - returns if the save was sucesfull, false when: IOException while
     *         writing to the preset file
     */
    public static boolean savePokemon(File preset, File user, Pokemon pokemon, int index) {
        assert (user.exists()) : "savePokemon - user file does not exist";
        int presetCount = Util.countFileLines(preset) / 4;
        ArrayList<String> contents = Util.fileToList(preset);
        String userName = Util.readFileLine(user, 1);
        String currHp = "" + pokemon.getHp();
        String name = pokemon.getName();
        String type = pokemon.getType();

        // new slot
        if (index > presetCount) {
            contents.add(userName);
            contents.add(name);
            contents.add(currHp);
            contents.add(type);
        }

        // old slot
        else {
            contents.set((index - 1) * 4, userName);
            contents.set((index - 1) * 4 + 1, name);
            contents.set((index - 1) * 4 + 2, currHp);
            contents.set((index - 1) * 4 + 3, type);
        }

        try {
            Util.listToFile(contents, preset);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Converts a preset file to an array according to 'example.txt'
     *
     * @param preset1 - preset file
     * @param index   - index of the pokemon you want to load (starting at one for
     *                the first one)
     * @return - array of String variables, null if the index is invalid
     */
    public static Pokemon loadPokemon(File preset, int index) {
        assert (preset.exists()) : "file does not exist (loadPokemon)";

        // preset index param validity check
        int presetCount = Util.countFileLines(preset) / 4;
        if (index > presetCount) {
            System.out.println("Preset index invalid: " + index + " File Index Count: " + presetCount);
            return null;
        }

        String[] variableArray = new String[4];
        try {
            Scanner fileScanner = new Scanner(preset);
            for (int y = 0; y < 4; y++) {
                int currFileLine = ((index - 1) * 4) + 1 + y;
                variableArray[y] = Util.readFileLine(preset, currFileLine);
            }
            fileScanner.close();
        } catch (IOException e) {
            System.out.print("There was an error when handeling the file");
        }
        String name = variableArray[1];
        int hp = Integer.parseInt(variableArray[2]);
        String type = variableArray[3].toLowerCase();
        return createPokemon(name, type, hp);
    }

    /**
     * Tests, if the file is a preset and not empty, if the file doesnt exist, it
     * will create it and return false
     *
     * @param preset  - file that it scans for
     * @param console - scanner with system.in for answer of the user
     * @return - return false if the file should not load and true if it should load
     */
    public static boolean isValidPreset(File presetFile, File user, Scanner console) {
        if (!presetFile.exists()) {
            try {
                presetFile.createNewFile();
            } catch (IOException e) {
                System.out.println("An error occured when creating the file: " + presetFile.getName());
            }
            return false;
        } else if (presetFile.length() == 0) {
            return false;
        } else if (!Util.readFileLine(presetFile, 1).equalsIgnoreCase(Util.readFileLine(user, 1))) {
            return false;
        }
        return true;
    }

    /**
     * Finds the name of the java file it was executed in
     *
     * @return returns a string "fileName.java"
     */
    public static String getFileName() {
        // Get the name of the class (excluding the package) and append ".java"
        String className = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        String fileName = className + ".java";
        return fileName;
    }

    /**
     * Finds the path of the folder, where the java file this method was executed in
     * is located
     *
     * @return String with path
     */
    public static String getGamePath() {
        String fileName = getFileName();
        int nameLenght = fileName.length();
        File folder = new File(fileName);
        String absolutePath = folder.getAbsolutePath();
        int pathLenght = absolutePath.length();
        return absolutePath.substring(0, pathLenght - nameLenght - 1);
    }

    /**
     * This method lists all users (*USER.txt files) in the path given
     *
     * @param path - path being searched
     * @return - returnes an array of Strings of names of all users
     */
    public static String[] getUsers(String path) {
        File dir = new File(path);
        int userCount = 0;
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
    private static boolean isUser(String file) {
        int lenght = file.length();
        if (lenght < 8) {
            return false;
        }
        return file.substring(lenght - 8).equals("USER.txt");
    }

    /**
     * Prompts user to select an account
     *
     * @param console - Scanner with System.in
     * @param users   - array of names of current user accounts
     * @param path    - path of user files
     * @return - return a file object set to the file of the user selected
     */
    public static File getUser(Scanner console, String[] users, String path) {
        System.out.print("Which user do you select?:\n" + Util.arrayToString(users, "\n", "--") + "\nNEW USER" + "\n\n> ");
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
        File currUser = new File(user + "USER.txt");
        return currUser;
    }

    /**
     * Prompts user to create his new account, it will create a new file named
     * 'accountnameUSER.txt'
     *
     * @param console - scanner with System.in
     * @param users   - array of current user accounts
     * @return - returns a file obj set to the file of the newly created user
     */
    public static File createUser(Scanner console, String[] users) {
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
        File newUser = new File(username + "USER.txt");
        try {
            newUser.createNewFile();
            FileWriter fw = new FileWriter(newUser);
            fw.append(username);
            fw.close();
            System.out.println("User created succesfully!");
        } catch (IOException e) {
            System.out.println("There was an error while creating new user file");
            e.printStackTrace();
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
    public static void deleteUser(File currUser, File currPreset, Scanner console) {
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
    public static String encrypt(String target) {
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
     * @param user        - the file that the user selected
     * @param console     - scanner with system.in
     * @param maxAttempts - maximum ammount of wrong password attempts
     * @return - returns boolean, true if password correct, false if not
     */
    public static boolean getPassword(File user, Scanner console, int maxAttempts) {
        Console passwordReader = System.console();
        String password = Util.readFileLine(user, 2);
        if (password == null) {
            return setPassword(user, console);
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
     * @param user    - the user the method is setting the pasword to
     * @param console - scanner with system.in
     * @return - returns a boolean, true if password settings succesful, false if
     *         not
     */
    public static boolean setPassword(File user, Scanner console) {
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
                FileWriter fw = new FileWriter(user, true);
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
