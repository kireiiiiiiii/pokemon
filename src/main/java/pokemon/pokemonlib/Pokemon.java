/*
 * Author: Matěj Šťastný
 * Date created: 12/17/2023
 * Github link: https://github.com/kireiiiiiiii/pokemon
 */

package pokemon.pokemonlib;

/**
 * Absract class for the specific pokemon classes.
 */
public abstract class Pokemon implements PokemonInterface {

    protected final String COLOR_RESET = "\u001B[0m";
    protected String name;
    protected String image;
    protected String type;
    protected String element;
    protected String colour;
    protected String[] abilities = new String[1];
    protected String[] stageType;
    protected int currHp;
    protected int maxHp;

    public Pokemon(String name, String type, String element, String image, String colour, String[] abilities, String[] stageType, int currHp, int maxHp) {
        assert (abilities.length == 2) : "pokemonAbilities array doesn't have two elements (constructor)";
        this.name = name;
        this.image = image;
        this.type = type;
        this.element = element;
        this.colour = colour;
        this.abilities = abilities;
        this.stageType = stageType;
        if (currHp == -1) {
            this.currHp = maxHp;
        } else {
            this.currHp = currHp;
        }
        this.maxHp = maxHp;
    }

    public void stats() {
        System.out.println("Hi, I am a " + type + ", my name is " + name + " and I am a " + colour + element + COLOR_RESET + " type Pokemon!\nI currently have " + currHp + " HP!" + "\nMy abilities are:\n\t--" + colour + abilities[0] + COLOR_RESET + "\n\t--" + colour + abilities[1] + COLOR_RESET);
    }

    public void image() {
        System.out.println(image);
    }

    public void ability1() {
        System.out.println(type + " " + name + " uses " + colour + abilities[0] + COLOR_RESET + "!");
    }

    public void ability2() {
        System.out.println(type + " " + name + " uses " + colour + abilities[1] + COLOR_RESET + "!");
    }

    public String getName() {
        return name;
    }

    public int getHp() {
        return currHp;
    }

    public String getType() {
        return type;
    }

    public String[] getStageType() {
        return stageType;
    }

    protected static String saveImage(String[] imageField) {
        StringBuilder sb = new StringBuilder();
        int lenght = imageField.length;
        int counter = 0;
        for (String s : imageField) {
            sb.append(s);
            counter++;
            if (counter < lenght) {
                sb.append("\n");
            }
        }
        String output = sb.toString();
        sb.setLength(0);
        return output;
    }
}
