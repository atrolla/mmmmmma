package org.atrolla.games.core;

import org.atrolla.games.ai.AIManager;
import org.atrolla.games.characters.*;
import org.atrolla.games.system.Player;
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
        characters.add(new Archer(Player.BOT));
        characters.add(new Archer(new Player()));
        characters.add(new Mage(Player.BOT));
        characters.add(new Knight(Player.BOT));
        characters.add(new Bomber(Player.BOT));
        AIManager aiManager = new AIManager(characters.size());
        assertEquals(characters.size(), aiManager.getCommands().size());
    }
}
