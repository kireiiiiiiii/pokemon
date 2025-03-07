/*
 * Author: Matěj Šťastný aka Kirei
 * Date created: 12/17/2023
 * Github link: https://github.com/kireiiiiiiii/pokemon
 */

package pokemon.pokemonlib;

/**
 * Interface for the pokemon classes
 */
interface PokemonInterface {

    void stats();

    void image();

    void ability1();

    void ability2();

    String getName();

    int getHp();
}
