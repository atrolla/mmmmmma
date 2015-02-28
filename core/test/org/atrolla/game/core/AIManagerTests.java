package org.atrolla.game.core;

import org.atrolla.game.ai.AIManager;
import org.atrolla.game.characters.*;
import org.atrolla.game.engine.Coordinates;
import org.atrolla.game.engine.Player;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by MicroOnde on 25/02/2015.
 */
public class AIManagerTests {

    @Test
    public void commandsNumberEqualsBots() throws Exception {
        List<GameCharacter> characters = new ArrayList<>(5);
        characters.add(new Archer(new Coordinates(50, 50), Player.BOT));
        characters.add(new Archer(new Coordinates(500, 500), new Player()));
        characters.add(new Mage(new Coordinates(100, 100), Player.BOT));
        characters.add(new Knight(new Coordinates(150, 150), Player.BOT));
        characters.add(new Bomber(new Coordinates(250, 250), Player.BOT));
        AIManager aiManager = new AIManager(characters.size());
        assertEquals(characters.size(), aiManager.getCommands().size());
    }
}
