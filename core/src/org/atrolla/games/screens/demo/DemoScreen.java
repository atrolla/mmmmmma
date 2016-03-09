package org.atrolla.games.screens.demo;

import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.items.Item;

import java.util.Collection;

public interface DemoScreen {

    Collection<GameCharacter> getCharacters();

    Collection<Item> getItems();

    void update(int time);

}
