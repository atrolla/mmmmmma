package org.atrolla.games.game;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.utils.Array;
import org.atrolla.games.ai.AIManager;
import org.atrolla.games.ai.Command;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.characters.Knight;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.input.InputManager;
import org.atrolla.games.items.weapons.Bomb;
import org.atrolla.games.mocks.MockController;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Direction;
import org.atrolla.games.system.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by MicroOnde on 25/02/2015.
 */
public class RoundTests {

    private Round defaultRound;
    private GameCharacter firstBot;
    private int firstBotIndex;
    //    private InputManager inputManager;
    private AIManager aiManager;

    @Before
    public void setUp() throws Exception {
        final Array<Controller> controllers = new Array<>();
        controllers.add(new MockController());
        InputManager inputManager = new InputManager(controllers, null);
        inputManager.assignPlayers();
        final RoundCharacters roundCharacters = new RoundCharacters(ConfigurationConstants.GAME_CHARACTERS, inputManager.getPlayers());
        aiManager = new AIManager(roundCharacters.bots.size());
        defaultRound = new Round(roundCharacters, aiManager);
        firstBot = defaultRound.getCharacters().characters.stream().filter(c -> !c.isPlayer()).findFirst().get();
        firstBotIndex = 0;
    }

    @Test
    public void gameHasStage() throws Exception {
        assertThat(defaultRound.getStage() != null);
    }

    @Test
    public void gameUpdateMovesEachBotWithCommands() throws Exception {
        defaultRound.update();
        // Down is default direction, so statistically, at least one should not be down after first update
        Predicate<GameCharacter> directionIsNotDown = gameCharacter -> !Direction.DOWN.equals(gameCharacter.getDirection());
//		defaultGame.getCharacters().parallelStream().forEach(c -> System.out.println(c.getDirection()));
        assertThat(defaultRound.getCharacters().characters.parallelStream().anyMatch(directionIsNotDown));
    }

    @Test
    public void outOfBoundsCharStaysInBound() throws Exception {
        for (int i = 0; i < 10000; i++) {
            firstBot.moves(0, Direction.DOWN);
        }
        assertThat(defaultRound.getStage().isOutOfBound(firstBot));
        defaultRound.update();
        assertThat(defaultRound.getStage().isOutOfBound(firstBot)).isFalse();
    }

    @Test
    public void anotherCommandIsDoneWhenACommandIsComplete() throws Exception {
        Command command = new Command(Direction.RIGHT, 2);
        aiManager.getCommands().set(firstBotIndex, command);
        // t = 0
        defaultRound.update();
        assertThat(command).isEqualTo(aiManager.getCommands().get(firstBotIndex));
        // t = 1
        defaultRound.update();
        assertThat(command).isEqualTo(aiManager.getCommands().get(firstBotIndex));
        // t = 2
        defaultRound.update();
        assertThat(command).isNotEqualTo(aiManager.getCommands().get(firstBotIndex));
    }

    @Test
    public void botIsNotStuckOnBounds() throws Exception {
        final Coordinates resetCoord = firstBot.getCoordinates();
        for (int j = 0; j < 250; j++) {
            firstBot.teleports(resetCoord);
            // put char out of bound
            for (int i = 0; i < 10000; i++) {
                firstBot.moves(0, Direction.UP);
            }
            // put direction that makes it still out of bound
            Command command = new Command(Direction.UP_RIGHT, 500);
            aiManager.getCommands().set(firstBotIndex, command);
            defaultRound.update();
            assertThat(defaultRound.getStage().isOutOfBound(firstBot)).isFalse();
            final Command generatedCommand = aiManager.getCommands().get(firstBotIndex);
            assertThat(Direction.UP).isNotEqualTo(generatedCommand.getDirection()).as("bad direction found during iteration " + j);
            assertThat(Direction.UP_RIGHT).isNotEqualTo(generatedCommand.getDirection()).as("bad direction found during iteration " + j);
            assertThat(Direction.UP_LEFT).isNotEqualTo(generatedCommand.getDirection()).as("bad direction found during iteration " + j);
        }
    }

    @Test
    public void characterHitStopMove() throws Exception {
        final Coordinates baseCoordinates = firstBot.getCoordinates();
        Command command = new Command(Direction.UP_RIGHT, 500);
        aiManager.getCommands().set(firstBotIndex, command);
        defaultRound.update();
        final Coordinates coordinates2 = firstBot.getCoordinates();
        assertThat(baseCoordinates).isNotEqualTo(coordinates2);
        firstBot.hit(new Knight(Player.BOT));
        defaultRound.update();
        assertThat(coordinates2).isEqualTo(firstBot.getCoordinates());
    }

    @Test
    public void knockOutBotMovesAfterSomeTime() throws Exception {
        Command command = new Command(Direction.DOWN_RIGHT, 500);
        aiManager.getCommands().set(firstBotIndex, command);
        defaultRound.update();
        final Coordinates baseCoordinates = firstBot.getCoordinates();
        Assert.assertTrue(firstBot.isAlive());
        firstBot.hit(new Knight(Player.BOT));
        assertThat(firstBot.isAlive()).isFalse();
        int i = 0;
        while (i++ < ConfigurationConstants.KNOCK_OUT_DURATION) {
            defaultRound.update();
            assertThat(firstBot.isAlive()).isFalse();
            assertThat(baseCoordinates).isEqualTo(firstBot.getCoordinates());
        }
        defaultRound.update();
        assertThat(firstBot.isAlive()).isTrue();
        assertThat(baseCoordinates).isNotEqualTo(firstBot.getCoordinates());
    }

    @Test
    public void gameObjectsContainsBombWhenBomberUsesHisAbility() throws Exception {
        final Array<Controller> controllers = new Array<>();
        controllers.add(new MockController());
        InputManager inputManager = new InputManager(controllers, null);
        inputManager.assignPlayers();
        Round round = new Round(inputManager, null);
        round.update();
        Assert.assertTrue(round.getGameItems().size() > 0);
        Assert.assertTrue(round.getGameItems().stream().anyMatch(Bomb.class::isInstance));
    }

    @Test
    public void roundIsFinishedWhenLessThan2PlayersAlive() throws Exception {
        final Array<Controller> controllers = new Array<>();
        controllers.add(new MockController());
        controllers.add(new MockController());
        InputManager inputManager = new InputManager(controllers, null);
        inputManager.assignPlayers();
        Round round = new Round(inputManager, null);
        Assert.assertEquals(round.getCharacters().players.size(), 2);
        Assert.assertFalse(round.isFinished());
        round.getCharacters().players.get(0).hit(new Knight(null));
        round.getCharacters().players.get(0).hit(new Knight(null));
        round.getCharacters().players.get(0).hit(new Knight(null));
        Assert.assertTrue(round.isFinished());
    }

    @Test
    public void roundNeverFinishesWhenOnly1PlayerIsPLaying() throws Exception {
        final Array<Controller> controllers = new Array<>();
        controllers.add(new MockController());
        InputManager inputManager = new InputManager(controllers, null);
        inputManager.assignPlayers();
        Round round = new Round(inputManager, null);
        Assert.assertEquals(round.getCharacters().players.size(), 1);
        Assert.assertFalse(round.isFinished());
    }
}
