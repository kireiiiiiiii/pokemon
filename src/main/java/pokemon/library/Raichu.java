/*
 * Author: Matěj Šťastný aka Kirei
 * Date created: 12/17/2023
 * Github link: https://github.com/kireiiiiiiii/pokemon
 */

package pokemon.library;

import pokemon.abstracts.Pokemon;

public class Raichu extends Pokemon {
    private static String[] imageArray = { //
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⣶⠀⠀⠀⠀⠀⠀⠀⠀⠀", //
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⠛⢿⠀⠀⠀⠀⠀⠀⠀⠀⠀", //
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⡏⠀⢸⠀⠀⠀⠀⠀⠀⠀⠀⠀", //
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣰⠃⠀⠼⣄⡀⠀⠀⠀⠀⠀⠀⠀", //
            "⠀⠀⠀⡴⠋⠳⣄⠀⠀⢀⢎⣔⠀⠀⠀⠀⠉⠳⣄⠀⠀⠀⠀⠀", //
            "⠀⢠⠞⠁⠀⠀⠀⠹⣶⢛⠘⡋⠀⢠⣎⣦⠀⠀⠈⠙⠲⢤⡀⠀", //
            "⣰⠏⠀⠀⠀⠀⠀⠀⢰⠀⠰⡤⡀⠀⢛⣋⠀⠀⠰⣄⣀⣠⣿⣆", //
            "⠛⠒⠶⠶⢶⡶⠄⢀⣨⢦⡀⠢⠃⠀⠸⣿⠇⠀⢰⠃⠉⠉⠉⠉", //
            "⠀⠀⠀⠰⠿⣤⣀⠛⢧⣌⡇⠀⠀⠀⠀⠀⠰⠉⠙⢳⠀⠀⠀⠀", //
            "⠀⠀⠀⠀⠀⠀⣼⣣⣞⣽⣇⠀⠈⠑⢄⠀⠐⢄⣀⣸⠀⠀⠀⠀", //
            "⠀⠀⠀⠀⠀⠈⠛⣿⣿⠁⠉⠠⡀⠀⠀⡆⠀⠀⠀⢹⡀⠀⠀⠀", //
            "⠀⠀⠀⠀⠀⠀⠀⠹⡿⠇⠀⠀⢈⠁⠊⠀⠀⠀⠀⠈⣇⠀⠀⠀", //
            "⠀⠀⠀⠀⠀⠀⠀⠀⣧⠀⠀⠀⢸⠀⠀⠀⠀⠀⠀⠀⢸⠀⠀⠀", //
            "⠀⠀⠀⠀⠀⠀⠀⠀⣸⠗⠀⣀⣦⣀⣤⣤⠴⠾⣄⡀⠺⡄⠀⠀", //
            "⠀⠀⠀⠀⠀⠀⠀⠀⠈⠉⠉⠁⠀⠀⠀⠀⠀⠀⠀⠉⠉⠁⠀⠀" //
    };
    private static String image = saveImage(imageArray);
    private static String element = "Electric";
    private static String type = "Raichu";
    private static int maxHp = 120;
    private static String[] stageType = null;
    private static String[] abilities = { "Thunder Shock", "Ace Spark" };
    private static String colour = "\u001b[43m"; // yellow

    public Raichu(String name, int currHp) {
        super(name, type, element, image, colour, abilities, stageType, currHp, maxHp);
    }

    public Raichu(String name) {
        super(name, type, element, image, colour, abilities, stageType, maxHp, maxHp);
    }
}
