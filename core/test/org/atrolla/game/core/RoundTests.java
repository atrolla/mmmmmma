package org.atrolla.game.core;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.utils.Array;
import org.atrolla.game.ai.AIManager;
import org.atrolla.game.ai.Command;
import org.atrolla.game.characters.*;
import org.atrolla.game.configuration.ConfigurationConstants;
import org.atrolla.game.core.mocks.MockController;
import org.atrolla.game.game.Round;
import org.atrolla.game.system.Coordinates;
import org.atrolla.game.system.Direction;
import org.atrolla.game.items.weapons.Bomb;
import org.atrolla.game.world.Stage;
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

    @Before
    public void setUp() throws Exception {
        defaultRound = new Round();
        final Array<Controller> controllers = new Array<>();
        controllers.add(new MockController());
        defaultRound.setControllers(controllers);
        defaultRound.setKeyboards(new Array<>());
        firstBot = defaultRound.getCharacters().stream().filter(c -> !c.isPlayer()).findFirst().get();
    }

    @Test
    public void gameHasStage() throws Exception {
        Stage stage = new Stage();
        Round round = new Round(stage);
        assertEquals(stage, round.getStage());
    }

    @Test
    public void gameHasEveryCharacters() throws Exception {
        //defaultGame.initCharacters();
        assertTrue(defaultRound.getCharacters().parallelStream().anyMatch(c -> c instanceof Mage));
        assertTrue(defaultRound.getCharacters().parallelStream().anyMatch(c -> c instanceof Archer));
        assertTrue(defaultRound.getCharacters().parallelStream().anyMatch(c -> c instanceof Knight));
        assertTrue(defaultRound.getCharacters().parallelStream().anyMatch(c -> c instanceof Bomber));
    }

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
        Command command = new Command(Direction.RIGHT,2);
        defaultRound.getAIManager().getCommands().set(0,command);
        // t = 0
        defaultRound.update();
        assertEquals(command, defaultRound.getAIManager().getCommands().get(0));
        // t = 1
        defaultRound.update();
        assertEquals(command, defaultRound.getAIManager().getCommands().get(0));
        // t = 2
        defaultRound.update();
        assertNotEquals(command, defaultRound.getAIManager().getCommands().get(0));
    }

    @Test
    public void botIsNotStuckOnBounds() throws Exception {
        // put char out of bound
        for (int i = 0; i < 10000; i++) {
            firstBot.moves(Direction.UP);
        }
        // put direction that makes it still out of bound
        Command command = new Command(Direction.UP_RIGHT,500);
        defaultRound.getAIManager().getCommands().set(0,command);
        defaultRound.update();
        assertFalse(defaultRound.getStage().isOutOfBound(firstBot));
        final Command generatedCommand = defaultRound.getAIManager().getCommands().get(0);
        assertNotEquals(Direction.UP, generatedCommand.getDirection());
        assertNotEquals(Direction.UP_RIGHT, generatedCommand.getDirection());
        assertNotEquals(Direction.UP_LEFT, generatedCommand.getDirection());
    }

    @Test
    public void characterHitStopMove() throws Exception {
        final Coordinates baseCoordinates = firstBot.getCoordinates();
        Command command = new Command(Direction.UP_RIGHT,500);
        defaultRound.getAIManager().getCommands().set(0, command);
        defaultRound.update();
        final Coordinates coordinates2 = firstBot.getCoordinates();
        assertNotEquals(baseCoordinates, coordinates2);
        firstBot.hit();
        defaultRound.update();
        assertEquals(coordinates2, firstBot.getCoordinates());
    }

    @Test
    public void knockOutBotMovesAfterSomeTime() throws Exception {
        Command command = new Command(Direction.DOWN_RIGHT,500);
        defaultRound.getAIManager().getCommands().set(0,command);
        defaultRound.update();
        final Coordinates baseCoordinates = firstBot.getCoordinates();
        Assert.assertTrue(firstBot.canMove());
        firstBot.hit();
        assertFalse(firstBot.canMove());
        int i = 0;
        while (i++ < ConfigurationConstants.BOT_KNOCK_OUT_DURATION) {
            defaultRound.update();
            assertFalse(firstBot.canMove());
            assertEquals(baseCoordinates, firstBot.getCoordinates());
        }
        defaultRound.update();
        assertTrue(firstBot.canMove());
        assertNotEquals(baseCoordinates, firstBot.getCoordinates());
    }

    @Test
    public void gameObjectsContainsBombWhenBomberUsesHisAbility() throws Exception {
        // considering There is a Bomber player...
        defaultRound.update();
        Assert.assertTrue(defaultRound.getGameItems().size() > 0);
        Assert.assertTrue(defaultRound.getGameItems().get(0) instanceof Bomb);
    }
}
