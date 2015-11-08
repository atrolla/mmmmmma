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
    private final List<Boolean> controllerPovState;

    public InputManager(Array<Controller> controllers, Input keyboard) {
        this.keyboard = keyboard;
        this.controllers = new Array<>(controllers);
        this.controllerPovState = new ArrayList<>();
        for (int i = 0; i < controllers.size; i++) {
            this.controllerPovState.add(Boolean.FALSE);
        }
        this.players = new ArrayList<>();
        this.keyboardArrrowState = false;
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
            if (controller.getButton(Xbox360PadInput.BUTTON_START)) {
                addPlayerIfNotPresent(controller);
            }
            if (PovDirection.west == controller.getPov(Xbox360PadInput.POV_INDEX)) {
                if (!controllerPovState.get(controllerIndex)) {
                    controllerPovState.set(controllerIndex, Boolean.TRUE);
                    switchClassIfPresent(controller, false);
                }
            } else if (PovDirection.east == controller.getPov(Xbox360PadInput.POV_INDEX)) {
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
            players.add(new Player(Optional.empty(), Optional.of(controller)));
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    /**
     *
     * For each player, accordingly to their controller input, <br/>
     * <ul>
     *     <li>move the player</li>
     *     <li>use player's ability</li>
     *     <li>use player's neutral item</li>
     * </ul>
     *
     * @return list of Items that players may create
     *
     * @see Player#getKeyboardInput()
     * @see Player#getControllerInput()
     * @see GameCharacter#useAbility(int)
     * @see GameCharacter#useNeutralItem(int)
     * @see Item
     */
    public List<Item> updatePlayers(int time, List<GameCharacter> playerCharacterList) {
        List<Item> items = new ArrayList<>();
        for (GameCharacter character : playerCharacterList) {
            final Player player = character.getPlayer();
            if (player.getKeyboardInput().isPresent()) {
                character.moves(Direction.STOP);
                final Input input = player.getKeyboardInput().get();
                moveGameCharacter(character, input);
                if (keyboard.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
                    character.useAbility(time).ifPresent(items::add);
                }
                if (keyboard.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
                    character.useNeutralItem(time).ifPresent(items::add);
                }
            } else if (player.getControllerInput().isPresent()) {
                final Controller controller = player.getControllerInput().get();
                moveGameCharacter(character, controller);
                //TODO : must release
                if (controller.getButton(Xbox360PadInput.BUTTON_A)) {
                    character.useAbility(time).ifPresent(items::add);
                }
                if (controller.getButton(Xbox360PadInput.BUTTON_X)) {
                    character.useNeutralItem(time).ifPresent(items::add);
                }
            }
        }
        return items;
    }

    private void moveGameCharacter(GameCharacter gameCharacter, Input keyboard) {
        if (keyboard.isKeyPressed(Input.Keys.UP)) {
            if (keyboard.isKeyPressed(Input.Keys.LEFT)) {
                gameCharacter.moves(Direction.UP_LEFT);
            } else if (keyboard.isKeyPressed(Input.Keys.RIGHT)) {
                gameCharacter.moves(Direction.UP_RIGHT);
            } else {
                gameCharacter.moves(Direction.UP);
            }
        } else if (keyboard.isKeyPressed(Input.Keys.DOWN)) {
            if (keyboard.isKeyPressed(Input.Keys.LEFT)) {
                gameCharacter.moves(Direction.DOWN_LEFT);
            } else if (keyboard.isKeyPressed(Input.Keys.RIGHT)) {
                gameCharacter.moves(Direction.DOWN_RIGHT);
            } else {
                gameCharacter.moves(Direction.DOWN);
            }
        } else if (keyboard.isKeyPressed(Input.Keys.RIGHT)) {
            gameCharacter.moves(Direction.RIGHT);
        } else if (keyboard.isKeyPressed(Input.Keys.LEFT)) {
            gameCharacter.moves(Direction.LEFT);
        }
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
                gameCharacter.moves(Direction.STOP);
                break;
        }
    }

}
