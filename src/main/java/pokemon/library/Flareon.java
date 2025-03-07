/*
 * Author: Matěj Šťastný aka Kirei
 * Date created: 12/17/2023
 * Github link: https://github.com/kireiiiiiiii/pokemon
 */

package pokemon.library;

import pokemon.abstracts.Pokemon;

public class Flareon extends Pokemon {
    private static String[] imageArray = { //
            "⠐⣶⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣷⠀", //
            "⠀⠸⡏⠙⠦⣤⡀⠀⠀⠀⠀⠀⠀⠀⠀⢠⣠⠖⠋⣸⠃⠀", //
            "⠀⠀⢻⠀⠀⠈⢳⡀⠀⠀⠀⢨⢢⠀⢠⠞⠁⠀⢠⡏⠀⠀", //
            "⠀⠀⠈⣧⠀⠀⠀⢻⡀⡠⠔⠊⠀⢣⡏⠀⠀⠀⡾⠀⠀⠀", //
            "⠀⠀⠀⠈⠓⠦⣴⣼⡏⠀⠀⠀⢠⣾⣤⣤⠶⠛⠁⢀⣿⠀", //
            "⠀⠀⠀⠀⠀⣶⠟⢻⣿⣦⣀⣤⣾⣿⡏⠙⢲⠀⠀⣾⣿⡇", //
            "⠀⠀⠀⠀⣸⠃⠀⢀⠀⢹⣿⣿⠃⢀⡇⠀⠘⣶⣶⣿⣿⠇", //
            "⠀⠀⠀⠀⢰⠀⠀⠈⠻⣿⣯⣿⡿⠟⠁⠀⠀⣿⣿⣿⡿⠀", //
            "⠀⠀⠀⠀⠙⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣽⣿⣿⣄⠀", //
            "⠀⠀⠀⠀⠀⢺⡀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣿⣿⣿⡟⠁⠀", //
            "⠀⠀⠀⠀⠀⠀⠈⣿⣦⣴⣴⣤⣶⣤⣾⣿⣿⣿⣿⡁⠀⠀", //
            "⠀⠀⠀⠀⠀⠀⠀⢹⣿⣿⣿⣿⣿⣿⡿⣿⠟⠋⠉⠀⠀⠀", //
            "⠀⠀⠀⠀⠀⠀⠺⠿⢿⣿⠏⠻⣿⡿⠿⠇⠀⠀⠀⠀⠀⠀"//
    };
    private static String image = saveImage(imageArray);
    private static String element = "Fire";
    private static String type = "Flareon";
    private static int maxHp = 110;
    private static String[] stageType = null;
    private static String[] abilities = { "Bite", "Fire Spin" };
    private static String colour = "\u001b[41m";

    public Flareon(String name, int currHp) {
        super(name, type, element, image, colour, abilities, stageType, currHp, maxHp);
    }

    public Flareon(String name) {
        super(name, type, element, image, colour, abilities, stageType, maxHp, maxHp);
    }
}
