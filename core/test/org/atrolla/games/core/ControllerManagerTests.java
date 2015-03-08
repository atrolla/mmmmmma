package org.atrolla.games.core;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.utils.Array;
import org.atrolla.games.characters.Archer;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.core.mocks.MockController;
import org.atrolla.games.input.ControllerManager;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by MicroOnde on 08/03/2015.
 */
public class ControllerManagerTests {

    private ControllerManager controllerManager;

    @Before
    public void setUp() throws Exception {
        final Array<Controller> controllers = new Array<>();
        controllers.add(new MockController());
        controllerManager = new ControllerManager(controllers);
    }

    @Test
    public void padWithPovCenteredDoesNotMoveCharacter() throws Exception {
        final Archer archer = new Archer(new Player());
        final Coordinates coordinates = new Coordinates(42, 1337);
        archer.teleports(coordinates);
        final List<GameCharacter> gameCharacters = Stream.of(archer).collect(Collectors.toList());
        controllerManager.updatePlayers(0, gameCharacters);
        assertEquals(coordinates, archer.getCoordinates());
    }
}
