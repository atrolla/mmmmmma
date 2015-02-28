package org.atrolla.game.core;

import org.atrolla.game.ai.AIManager;
import org.atrolla.game.characters.*;
import org.atrolla.game.engine.Direction;
import org.atrolla.game.engine.Game;
import org.atrolla.game.stage.Stage;
import org.junit.Test;

import java.util.function.Predicate;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by MicroOnde on 25/02/2015.
 */
public class GameTests {

	private final Game defaultGame = new Game();

	@Test
	public void gameHasStage() throws Exception {
		Stage stage = new Stage();
		Game game = new Game(stage);
		assertEquals(stage,game.getStage());
	}

	@Test
	public void gameHasEveryCharacters() throws Exception {
		//defaultGame.initCharacters();
		assertTrue(defaultGame.getCharacters().parallelStream().anyMatch(c -> c instanceof Mage));
		assertTrue(defaultGame.getCharacters().parallelStream().anyMatch(c -> c instanceof Archer));
		assertTrue(defaultGame.getCharacters().parallelStream().anyMatch(c -> c instanceof Knight));
		assertTrue(defaultGame.getCharacters().parallelStream().anyMatch(c -> c instanceof Bomber));
	}

	@Test
	public void gameHasAIManager() throws Exception {
		assertTrue(defaultGame.getAIManager() instanceof AIManager);
	}

	@Test
	public void gameUpdateMovesEachBotWithCommands() throws Exception {
		defaultGame.update();
		// Down is default direction, so statistically, at least one should not be down after first update
		Predicate<GameCharacter> directionIsNotDown = gameCharacter -> !Direction.DOWN.equals(gameCharacter.getDirection());
//		defaultGame.getCharacters().parallelStream().forEach(c -> System.out.println(c.getDirection()));
		assertTrue(defaultGame.getCharacters().parallelStream().anyMatch(directionIsNotDown));
	}

    @Test
    public void outOfBoundsCharStaysInBound() throws Exception {
        final GameCharacter gameCharacter = (GameCharacter) defaultGame.getCharacters().toArray()[0];
        for (int i = 0; i < 10000; i++) {
            gameCharacter.moves(Direction.DOWN);
        }
        assertTrue(defaultGame.getStage().isOutOfBound(gameCharacter));
        defaultGame.update();
        assertFalse(defaultGame.getStage().isOutOfBound(gameCharacter));
    }
}
