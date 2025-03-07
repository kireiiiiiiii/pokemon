/*
 * Author: Matěj Šťastný aka Kirei
 * Date created: 12/17/2023
 * Github link: https://github.com/kireiiiiiiii/pokemon
 */

package pokemon.common;

import java.io.File;
import java.io.IOException;

/**
 * Represents a preset of a user. Handles the creation and management of the
 * preset file.
 */
public class CharacterProfile {

    private final User owner;
    private final File presetFile;

    // Constructor --------------------------------------------------------------

    public CharacterProfile(User owner) {
        this.owner = owner;
        this.presetFile = initializePresetFile();
    }

    // Accessors ----------------------------------------------------------------

    public File getPresetFile() {
        return presetFile;
    }

    public User getOwner() {
        return owner;
    }

    public boolean isValidPreset() {
        if (!presetFile.exists() || presetFile.length() == 0) {
            createFileIfNotExists(presetFile);
            return false;
        }
        return Util.readFileLine(presetFile, 1).equalsIgnoreCase(owner.getUsername());
    }

    public void printContents() {
        int presetCount = getPresetCount();
        for (int i = 1; i <= presetCount; i++) {
            int currFileLine = i * 4;
            String name = Util.readFileLine(presetFile, currFileLine - 2);
            String type = Util.readFileLine(presetFile, currFileLine);
            String hp = Util.readFileLine(presetFile, currFileLine - 1);
            System.out.printf("     %d. %s [3m%s[0m %sHP%n", i, capitalizeFirstLetter(name), type, hp);
        }
    }

    public String getTypeOnIndex(int index) {
        return Util.readFileLine(presetFile, index * 4);
    }

    // Private ------------------------------------------------------------------

    private File initializePresetFile() {
        File presetFile = new File(getPresetPath());
        createFileIfNotExists(presetFile);
        return presetFile;
    }

    private String getPresetPath() {
        return owner.getUserFile().getParent() + File.separator + owner.getUsername() + "PRESET.txt";
    }

    private void createFileIfNotExists(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating preset file: " + file.getAbsolutePath());
                e.printStackTrace();
            }
        }
    }

    private int getPresetCount() {
        return Util.countFileLines(presetFile) / 4;
    }

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}
