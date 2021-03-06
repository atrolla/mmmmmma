package org.atrolla.games.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.utils.Array;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.items.Item;
import org.atrolla.games.system.Direction;
import org.atrolla.games.system.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InputManager {
    private final Array<Controller> controllers;
    private final Input keyboard;
    private final List<Player> players;
    private boolean keyboardArrrowState;
    //TODO : refactor ?
    private final List<Boolean> controllerPovState;
    private PadInput padInput;

    public InputManager(Array<Controller> controllers, Input keyboard) {
        this.keyboard = keyboard;
        this.controllers = new Array<>(controllers);
        this.controllerPovState = new ArrayList<>();
        for (int i = 0; i < controllers.size; i++) {
            this.controllerPovState.add(Boolean.FALSE);
        }
        this.players = new ArrayList<>();
        this.keyboardArrrowState = false;
        this.padInput = PadInput.getPadInput();
    }

    public void unAssignPlayers() {
        players.clear();
    }

    public void assignPlayers() {
        if (keyboard != null) {
            if (keyboard.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
                addPlayerIfNotPresent(keyboard);
            }
            if (keyboard.isKeyPressed(Input.Keys.LEFT)) {
                if (!keyboardArrrowState) {
                    keyboardArrrowState = true;
                    switchClassIfPresent(keyboard, false);
                }
            } else if (keyboard.isKeyPressed(Input.Keys.RIGHT)) {
                if (!keyboardArrrowState) {
                    keyboardArrrowState = true;
                    switchClassIfPresent(keyboard, true);
                }
            } else {
                keyboardArrrowState = false;
            }
        }
        int controllerIndex = 0;
        for (Controller controller : controllers) {
            if (controller.getButton(padInput.buttonStart())) {
                addPlayerIfNotPresent(controller);
            }
            if (PovDirection.west == padInput.getPovDirection(controller)) {
                if (!controllerPovState.get(controllerIndex)) {
                    controllerPovState.set(controllerIndex, Boolean.TRUE);
                    switchClassIfPresent(controller, false);
                }
            } else if (PovDirection.east == padInput.getPovDirection(controller)) {
                if (!controllerPovState.get(controllerIndex)) {
                    controllerPovState.set(controllerIndex, Boolean.TRUE);
                    switchClassIfPresent(controller, true);
                }
            } else {
                controllerPovState.set(controllerIndex, Boolean.FALSE);
            }
            controllerIndex++;
        }
    }

    private void switchClassIfPresent(Input keyboard, boolean next) {
        players.stream().filter(p -> p.isSameInput(keyboard)).findFirst().ifPresent(p -> p.switchClass(next));
    }

    private void switchClassIfPresent(Controller controller, boolean next) {
        players.stream().filter(p -> p.isSameInput(controller)).findFirst().ifPresent(p -> p.switchClass(next));
    }

    private void addPlayerIfNotPresent(Input keyboard) {
        Optional<Player> playerOptional = players.stream().filter(p -> p.isSameInput(keyboard)).findFirst();
        if (!playerOptional.isPresent()) {
            players.add(new Player(Optional.of(keyboard), Optional.empty()));
        }
    }

    private void addPlayerIfNotPresent(Controller controller) {
        Optional<Player> playerOptional = players.stream().filter(p -> p.isSameInput(controller)).findFirst();
        if (!playerOptional.isPresent()) {
            players.add(new Player(Optional.empty(), Optional.of(new PadController(controller))));
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    /**
     * For each player, accordingly to their controller input, <br/>
     * <ul>
     * <li>move the player</li>
     * <li>use player's ability</li>
     * <li>use player's neutral item</li>
     * </ul>
     *
     * @return list of Items that players may create
     * @see Player#getKeyboardInput()
     * @see Player#getPadControllerInput()
     * @see GameCharacter#useAbility(int)
     * @see GameCharacter#useNeutralItem(int)
     * @see Item
     */
    public List<Item> updatePlayers(int time, List<GameCharacter> playerCharacterList) {
        List<Item> items = new ArrayList<>();
        for (GameCharacter character : playerCharacterList) {
            final Player player = character.getPlayer();
            if (player.getKeyboardInput().isPresent()) {
                character.moves(time, Direction.STOP);
                final Input input = player.getKeyboardInput().get();
                moveGameCharacter(time, character, input);
                if (keyboard.isKeyJustPressed(Input.Keys.CONTROL_RIGHT)) {
                    character.useAbility(time).forEach(items::add);
                }
                if (keyboard.isKeyJustPressed(Input.Keys.SHIFT_RIGHT)) {
                    character.useNeutralItem(time).ifPresent(items::add);
                }
                if (keyboard.isKeyJustPressed(Input.Keys.ESCAPE)) {
                    character.rageQuit();
                }
            } else if (player.getPadControllerInput().isPresent()) {
                final PadController padController = player.getPadControllerInput().get();
                final Controller controller = padController.getController();
                moveGameCharacter(time, character, controller);
                if (padController.hasJustPressed(padInput.buttonA())) {
                    character.useAbility(time).forEach(items::add);
                }
                if (padController.hasJustPressed(padInput.buttonX())) {
                    character.useNeutralItem(time).ifPresent(items::add);
                }
                if (controller.getButton(padInput.buttonBack()) && controller.getButton(padInput.buttonStart())) {
                    character.rageQuit();
                }
            }
        }
        return items;
    }

    private void moveGameCharacter(int time, GameCharacter gameCharacter, Input keyboard) {
        if (keyboard.isKeyPressed(Input.Keys.UP)) {
            if (keyboard.isKeyPressed(Input.Keys.LEFT)) {
                gameCharacter.moves(time, Direction.UP_LEFT);
            } else if (keyboard.isKeyPressed(Input.Keys.RIGHT)) {
                gameCharacter.moves(time, Direction.UP_RIGHT);
            } else {
                gameCharacter.moves(time, Direction.UP);
            }
        } else if (keyboard.isKeyPressed(Input.Keys.DOWN)) {
            if (keyboard.isKeyPressed(Input.Keys.LEFT)) {
                gameCharacter.moves(time, Direction.DOWN_LEFT);
            } else if (keyboard.isKeyPressed(Input.Keys.RIGHT)) {
                gameCharacter.moves(time, Direction.DOWN_RIGHT);
            } else {
                gameCharacter.moves(time, Direction.DOWN);
            }
        } else if (keyboard.isKeyPressed(Input.Keys.RIGHT)) {
            gameCharacter.moves(time, Direction.RIGHT);
        } else if (keyboard.isKeyPressed(Input.Keys.LEFT)) {
            gameCharacter.moves(time, Direction.LEFT);
        }
    }

    private void moveGameCharacter(int time, GameCharacter gameCharacter, Controller controller) {
        final PovDirection pov = padInput.getPovDirection(controller);
        switch (pov) {
            case east:
                gameCharacter.moves(time, Direction.RIGHT);
                break;
            case north:
                gameCharacter.moves(time, Direction.UP);
                break;
            case south:
                gameCharacter.moves(time, Direction.DOWN);
                break;
            case west:
                gameCharacter.moves(time, Direction.LEFT);
                break;
            case northWest:
                gameCharacter.moves(time, Direction.UP_LEFT);
                break;
            case southWest:
                gameCharacter.moves(time, Direction.DOWN_LEFT);
                break;
            case northEast:
                gameCharacter.moves(time, Direction.UP_RIGHT);
                break;
            case southEast:
                gameCharacter.moves(time, Direction.DOWN_RIGHT);
                break;
            default:
                gameCharacter.moves(time, Direction.STOP);
                break;
        }
    }

    public int watchChangeMenu() {
        for (Player p : players) {
            if (p.getPadControllerInput().isPresent()) {
                final PadController padController = p.getPadControllerInput().get();
                if (padController.hasJustPressed(PovDirection.north, padInput)) {
                    return -1;
                }
                if (padController.hasJustPressed(PovDirection.south, padInput)) {
                    return 1;
                }
            } else {
                if (keyboard.isKeyJustPressed(Input.Keys.UP)) {
                    return -1;
                }
                if (keyboard.isKeyJustPressed(Input.Keys.DOWN)) {
                    return 1;
                }
            }
        }
        return 0;
    }

    public boolean watchChooseMenu() {
        for (Player p : players) {
            if (p.getPadControllerInput().isPresent()) {
                final PadController padController = p.getPadControllerInput().get();
                if (padController.hasJustPressed(padInput.buttonA())) {
                    return true;
                }
            } else {
                if (keyboard.isKeyJustPressed(Input.Keys.ENTER)) {
                    return true;
                }
            }
        }
        return false;
    }
}
