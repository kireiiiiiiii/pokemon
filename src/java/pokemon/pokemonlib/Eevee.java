/*
 * Author: Matěj Šťastný aka Kirei
 * Date created: 12/17/2023
 * Github link: https://github.com/kireiiiiiiii/pokemon
 */

package pokemon.pokemonlib;

/**
 * Eevee pokemon class
 *
 */
public class Eevee extends Pokemon {
    private static String image = "Im sorry, but Eevee doesn't have a picture yet...";
    private static String element = "Normal";
    private static String type = "Eevee";
    private static int maxHp = 60;
    private static String[] stageType = { "Flareon" };
    private static String[] abilities = { "Be Prepared", "Bite" };
    private static String colour = "\u001b[47m";

    public Eevee(String name, int currHp) {
        super(name, type, element, image, colour, abilities, stageType, currHp, maxHp);
    }

    public Eevee(String name) {
        super(name, type, element, image, colour, abilities, stageType, maxHp, maxHp);
    }
}
