package org.atrolla.games.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.utils.Array;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.items.Item;
import org.atrolla.games.system.Direction;

import java.util.List;

/**
 * Created by MicroOnde on 01/03/2015.
 */
public class ControllerManager {
    private final Array<Controller> controllers;

    public ControllerManager(Array<Controller> controllers) {
        this.controllers = new Array<>(controllers);
    }

    //TODO : remove
    public void test() {
        System.out.println("Controllers: " + controllers.size);
        int i = 0;
        for (Controller controller : controllers) {
            System.out.println("#" + i++ + ": " + controller.getName());
        }
        if (controllers.size == 0) System.out.println("No controllers attached");
    }

    public Item updatePlayers(int time, List<GameCharacter> characters) {
        Item gameObject = null;
        if (controllers != null && controllers.size > 0) {
            final GameCharacter gameCharacter = characters.get(0);
            final Controller controller = controllers.get(0);
            moveGameCharacter(gameCharacter, controller);
            if (controller.getButton(Xbox360PadInput.BUTTON_A)) {
                gameObject = gameCharacter.useAbility(time);
            }
        }
        return gameObject;
    }

    private void moveGameCharacter(GameCharacter gameCharacter, Controller controller) {
        final PovDirection pov = controller.getPov(Xbox360PadInput.POV_INDEX);
        switch (pov) {
            case east:
                gameCharacter.moves(Direction.RIGHT);
                break;
            case north:
                gameCharacter.moves(Direction.UP);
                break;
            case south:
                gameCharacter.moves(Direction.DOWN);
                break;
            case west:
                gameCharacter.moves(Direction.LEFT);
                break;
            case northWest:
                gameCharacter.moves(Direction.UP_LEFT);
                break;
            case southWest:
                gameCharacter.moves(Direction.DOWN_LEFT);
                break;
            case northEast:
                gameCharacter.moves(Direction.UP_RIGHT);
                break;
            case southEast:
                gameCharacter.moves(Direction.DOWN_RIGHT);
                break;
            default:
                break;
        }
    }
}
