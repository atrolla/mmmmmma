package org.atrolla.games.screens.demo;

import org.atrolla.games.characters.GameCharacter;

import java.util.Collection;

public interface DemoScreen {

    Collection<GameCharacter> getCharacters();

    void update(int time);

}
