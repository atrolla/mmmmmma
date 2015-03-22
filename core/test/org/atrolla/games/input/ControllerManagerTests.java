package org.atrolla.games.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.utils.Array;
import org.atrolla.games.characters.Archer;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.mocks.MockController;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by MicroOnde on 08/03/2015.
 */
public class ControllerManagerTests {

    private InputManager inputManager;

    @Before
    public void setUp() throws Exception {
        final Array<Controller> controllers = new Array<>();
        controllers.add(new MockController());
        inputManager = new InputManager(controllers, null);
    }

    @Test
    public void padWithPovCenteredDoesNotMoveCharacter() throws Exception {
        final Archer archer = new Archer(new Player(Optional.empty(), Optional.empty()));
        final Coordinates coordinates = new Coordinates(42, 1337);
        archer.teleports(coordinates);
        final List<GameCharacter> gameCharacters = Stream.of(archer).collect(Collectors.toList());
        inputManager.updatePlayers(0, gameCharacters);
        assertEquals(coordinates, archer.getCoordinates());
    }
}
