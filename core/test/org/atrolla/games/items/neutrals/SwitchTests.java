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
import static org.atrolla.games.configuration.ConfigurationConstants.NEUTRAL_ITEM_SIZE;
import static org.atrolla.games.utils.RandomUtils.getRandomCoordinates;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by MicroOnde on 15/03/2015.
 */
public class SwitchTests {

    private Switch aSwitch;

    @Before
    public void setUp() throws Exception {
        aSwitch = new Switch(getRandomCoordinates(NEUTRAL_ITEM_SIZE, NEUTRAL_ITEM_SIZE),Integer.MAX_VALUE);
    }

    @Test
    public void aSwitchCanOnlyBePickedByAPlayer() throws Exception {
        assertFalse(aSwitch.isPicked(new Archer(Player.BOT)));
        assertTrue(aSwitch.isPicked(new Archer(new Player(Optional.empty(), Optional.empty()))));
    }

    @Test
    public void pickerPlayerCanUseaSwitch() throws Exception {
        final Archer player = new Archer(new Player(Optional.empty(), Optional.empty()));
        aSwitch.isPicked(player);
        assertTrue(player.useNeutralItem(42).get() instanceof Switch);
    }

    @Test
    @Ignore
    public void usedSwitchSwitchesCoordinatesBetweenAPlayerAndABotOfSameClass() throws Exception {
        final Array<Controller> controllers = new Array<>();
        controllers.add(new MockController());
        final InputManager inputManager = new InputManager(controllers, null);
        inputManager.assignPlayers();
        Round defaultRound = new Round(inputManager, null);
        GameCharacter firstPlayer = defaultRound.getCharacters().characters.stream().filter(GameCharacter::isPlayer).findFirst().get();
        defaultRound.update();
        final NeutralItem item = (NeutralItem) defaultRound.getGameItems().stream().filter(Switch.class::isInstance).findFirst().get();
        item.isPicked(firstPlayer);
        final Coordinates initCoordinates = firstPlayer.getCoordinates();
        //first is picked
        defaultRound.update();
        //direcly used but workflow add one tick
        defaultRound.update();
        final Coordinates coordinates = firstPlayer.getCoordinates();
//        System.out.println("COORD : x=" + coordinates.getX() + " - y=" + coordinates.getY());
        assertNotEquals(initCoordinates, coordinates);
    }
}
