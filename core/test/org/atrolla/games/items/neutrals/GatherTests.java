package org.atrolla.games.items.neutrals;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.utils.Array;
import org.atrolla.games.characters.Archer;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.game.Round;
import org.atrolla.games.input.InputManager;
import org.atrolla.games.mocks.MockController;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Player;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.TestCase.assertFalse;
import static org.atrolla.games.configuration.ConfigurationConstants.ITEM_NEUTRAL_SIZE;
import static org.atrolla.games.utils.RandomUtils.getRandomCoordinates;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by MicroOnde on 15/03/2015.
 */
public class GatherTests {

    private Gather gather;

    @Before
    public void setUp() throws Exception {
        gather = new Gather(getRandomCoordinates(ITEM_NEUTRAL_SIZE, ITEM_NEUTRAL_SIZE));
    }

    @Test
    public void gatherCanOnlyBePickedByAPlayer() throws Exception {
        assertFalse(gather.isPicked(new Archer(Player.BOT)));
        assertTrue(gather.isPicked(new Archer(new Player(Optional.empty(), Optional.empty()))));
    }

    @Test
    public void pickerPlayerCanUseGather() throws Exception {
        final Archer player = new Archer(new Player(Optional.empty(), Optional.empty()));
        gather.isPicked(player);
        assertTrue(player.useNeutralItem(42).get() instanceof Gather);
    }

    @Test
    @Ignore
    public void usedGatherTeleportsAllSamePickerClassAtTheSameArea() throws Exception {
        final Array<Controller> controllers = new Array<>();
        controllers.add(new MockController());
        final InputManager inputManager = new InputManager(controllers, null);
        inputManager.assignPlayers();
        Round defaultRound = new Round(inputManager, null);
        GameCharacter firstPlayer = defaultRound.getCharacters().stream().filter(GameCharacter::isPlayer).findFirst().get();
        defaultRound.update();
        final NeutralItem item = (NeutralItem) defaultRound.getGameItems().stream().filter(Gather.class::isInstance).findFirst().get();
        item.isPicked(firstPlayer);
        final Coordinates initCoordinates = firstPlayer.getCoordinates();
        //first is picked
        defaultRound.update();
        //direcly used but workflow add one tick
        defaultRound.update();
        final Coordinates coordinates = firstPlayer.getCoordinates();
//        System.out.println("COORD : x=" + coordinates.getX() + " - y=" + coordinates.getY());
        assertNotEquals(initCoordinates, coordinates);
        assertTrue(defaultRound.getCharacters().stream().filter(firstPlayer.getClass()::isInstance).allMatch(c -> {
            final double x = c.getCoordinates().getX();
            final double y = c.getCoordinates().getY();
            if ((x > coordinates.getX() - 5) && (x < coordinates.getX() + 5) && (y > coordinates.getY() - 5) && (y < coordinates.getY() + 5)) {
                return true;
            }
//            System.out.println("NOT IN SAME AREA :" + c + "\n x=" + x + " - y=" + y);
            return false;
        }));
        assertTrue(defaultRound.getGameItems().stream().noneMatch(Gather.class::isInstance));

    }
}
