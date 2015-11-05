package org.atrolla.games.game;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.utils.Array;
import org.atrolla.games.ai.AIManager;
import org.atrolla.games.ai.Command;
import org.atrolla.games.characters.*;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.input.InputManager;
import org.atrolla.games.items.weapons.Bomb;
import org.atrolla.games.mocks.MockController;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Direction;
import org.atrolla.games.world.Stage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Predicate;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by MicroOnde on 25/02/2015.
 */
public class RoundTests {

    private Round defaultRound;
    private GameCharacter firstBot;
    private int firstBotIndex;
    private InputManager inputManager;

    @Before
    public void setUp() throws Exception {
        final Array<Controller> controllers = new Array<>();
        controllers.add(new MockController());
        inputManager = new InputManager(controllers, null);
        inputManager.assignPlayers();
        defaultRound = new Round(inputManager, null);
        firstBot = defaultRound.getCharacters().stream().filter(c -> !c.isPlayer()).findFirst().get();
        firstBotIndex = 0;
    }

    @Test
    public void gameHasStage() throws Exception {
        Stage stage = new Stage();
        Round round = new Round(stage, inputManager, null);
        assertEquals(stage, round.getStage());
    }

    /*@Test // NO MORE TRUE
    public void gameHasEveryCharacters() throws Exception {
        //defaultGame.initCharacters();
        assertTrue(defaultRound.getCharacters().parallelStream().anyMatch(c -> c instanceof Mage));
        assertTrue(defaultRound.getCharacters().parallelStream().anyMatch(c -> c instanceof Archer));
        assertTrue(defaultRound.getCharacters().parallelStream().anyMatch(c -> c instanceof Knight));
        assertTrue(defaultRound.getCharacters().parallelStream().anyMatch(c -> c instanceof Bomber));
    }*/

    @Test
    public void gameHasAIManager() throws Exception {
        assertTrue(defaultRound.getAIManager() instanceof AIManager);
    }

    @Test
    public void gameUpdateMovesEachBotWithCommands() throws Exception {
        defaultRound.update();
        // Down is default direction, so statistically, at least one should not be down after first update
        Predicate<GameCharacter> directionIsNotDown = gameCharacter -> !Direction.DOWN.equals(gameCharacter.getDirection());
//		defaultGame.getCharacters().parallelStream().forEach(c -> System.out.println(c.getDirection()));
        assertTrue(defaultRound.getCharacters().parallelStream().anyMatch(directionIsNotDown));
    }

    @Test
    public void outOfBoundsCharStaysInBound() throws Exception {
        for (int i = 0; i < 10000; i++) {
            firstBot.moves(Direction.DOWN);
        }
        assertTrue(defaultRound.getStage().isOutOfBound(firstBot));
        defaultRound.update();
        assertFalse(defaultRound.getStage().isOutOfBound(firstBot));
    }

    @Test
    public void anotherCommandIsDoneWhenACommandIsComplete() throws Exception {
        Command command = new Command(Direction.RIGHT, 2);
        defaultRound.getAIManager().getCommands().set(firstBotIndex, command);
        // t = 0
        defaultRound.update();
        assertEquals(command, defaultRound.getAIManager().getCommands().get(firstBotIndex));
        // t = 1
        defaultRound.update();
        assertEquals(command, defaultRound.getAIManager().getCommands().get(firstBotIndex));
        // t = 2
        defaultRound.update();
        assertNotEquals(command, defaultRound.getAIManager().getCommands().get(firstBotIndex));
    }

    @Test
    public void botIsNotStuckOnBounds() throws Exception {
        final Coordinates resetCoord = firstBot.getCoordinates();
        for (int j = 0; j < 250; j++) {
            firstBot.teleports(resetCoord);
            // put char out of bound
            for (int i = 0; i < 10000; i++) {
                firstBot.moves(Direction.UP);
            }
            // put direction that makes it still out of bound
            Command command = new Command(Direction.UP_RIGHT, 500);
            defaultRound.getAIManager().getCommands().set(firstBotIndex, command);
            defaultRound.update();
            assertFalse(defaultRound.getStage().isOutOfBound(firstBot));
            final Command generatedCommand = defaultRound.getAIManager().getCommands().get(firstBotIndex);
            assertNotEquals("bad direction found during iteration " + j, Direction.UP, generatedCommand.getDirection());
            assertNotEquals("bad direction found during iteration " + j, Direction.UP_RIGHT, generatedCommand.getDirection());
            assertNotEquals("bad direction found during iteration " + j, Direction.UP_LEFT, generatedCommand.getDirection());
        }
    }

    @Test
    public void characterHitStopMove() throws Exception {
        final Coordinates baseCoordinates = firstBot.getCoordinates();
        Command command = new Command(Direction.UP_RIGHT, 500);
        defaultRound.getAIManager().getCommands().set(firstBotIndex, command);
        defaultRound.update();
        final Coordinates coordinates2 = firstBot.getCoordinates();
        assertNotEquals(baseCoordinates, coordinates2);
        firstBot.hit();
        defaultRound.update();
        assertEquals(coordinates2, firstBot.getCoordinates());
    }

    @Test
    public void knockOutBotMovesAfterSomeTime() throws Exception {
        Command command = new Command(Direction.DOWN_RIGHT, 500);
        defaultRound.getAIManager().getCommands().set(firstBotIndex, command);
        defaultRound.update();
        final Coordinates baseCoordinates = firstBot.getCoordinates();
        Assert.assertTrue(firstBot.isAlive());
        firstBot.hit();
        assertFalse(firstBot.isAlive());
        int i = 0;
        while (i++ < ConfigurationConstants.KNOCK_OUT_DURATION) {
            defaultRound.update();
            assertFalse(firstBot.isAlive());
            assertEquals(baseCoordinates, firstBot.getCoordinates());
        }
        defaultRound.update();
        assertTrue(firstBot.isAlive());
        assertNotEquals(baseCoordinates, firstBot.getCoordinates());
    }

    @Test
    public void gameObjectsContainsBombWhenBomberUsesHisAbility() throws Exception {
        // considering There is a Bomber player...
        defaultRound.update();
        Assert.assertTrue(defaultRound.getGameItems().size() > 0);
        Assert.assertTrue(defaultRound.getGameItems().stream().anyMatch(Bomb.class::isInstance));
    }
}
