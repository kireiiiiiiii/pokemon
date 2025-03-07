/*
 * Author: Matěj Šťastný aka Kirei
 * Date created: 12/17/2023
 * Github link: https://github.com/kireiiiiiiii/pokemon
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package pokemon.common;

import java.io.File;
import java.io.IOException;

public class Preset {

    private User owner;
    private File presetFile;

    /* CONSTRUCTORS */

    /**
     * Preset object constructor
     *
     * @param owner - User object of the owner of this preset
     */
    public Preset(User owner) {
        this.owner = owner;
        String path = getPresetPath(owner);
        presetFile = setPreset(path);
    }

    /* ACCESORS */

    /**
     * Returns the preset file
     *
     * @return File object of the preset file
     */
    public File getPreset() {
        return presetFile;
    }

    /**
     * Returns the user object of the owner of this preset
     *
     * @return Owner user object
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Tests, if the file is a preset and not empty, if the file doesnt exist, it
     * will create it and return false
     *
     * @return - return false if the file should not load and true if it should load
     */
    public boolean isValidPreset() {
        if (!presetFile.exists()) {
            try {
                presetFile.createNewFile();
            } catch (IOException e) {
                System.out.println("An error occured when creating the file: " + presetFile.getName());
            }
            return false;
        } else if (presetFile.length() == 0) {
            return false;
        } else if (!Util.readFileLine(presetFile, 1).equalsIgnoreCase(owner.getUsername())) {
            return false;
        }
        return true;
    }

    /**
     * Prints the contents of the preset file in a list into console
     */
    public void printContents() {
        for (int i = 1; i <= getPresetCount(); i++) {
            int currFileLine = i * 4;
            System.out.println("     " + i + ". " + Util.readFileLine(presetFile, currFileLine - 2).substring(0, 1).toUpperCase() + Util.readFileLine(presetFile, currFileLine - 2).substring(1) + " \033[3m" + Util.readFileLine(presetFile, currFileLine) + "\033[0m " + Util.readFileLine(presetFile, currFileLine - 1) + "HP");
        }
    }

    /**
     * Reades the type of the pokemon, on the given preset index (NOT the file line)
     *
     * @param index - Preset index
     * @return String of the type on the index
     */
    public String getTypeOnIndex(int index) {
        return Util.readFileLine(presetFile, index * 4);
    }

    /* PRIVATE METHODS */

    /**
     * Returns the path to this preset file
     *
     * @param owner - Owner of the preset file
     * @return String of the preset path
     */
    private String getPresetPath(User owner) {
        String path = owner.getPath();
        String username = owner.getUsername();
        return path + "/" + username + "PRESET.txt";
    }

    /**
     * Sets the user file, according to user, if the file for the preset doesn't
     * exist it will create it
     *
     * @param user - user file of the current user
     * @return - returns the file of the preset of the user given in @param
     */
    private File setPreset(String path) {
        File preset = new File(path);
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
     * Counts the number of presets, that are saved in the preset file
     *
     * @return A number of pokemon presets in the file
     */
    private int getPresetCount() {
        return Util.countFileLines(presetFile) / 4;
    }
}
