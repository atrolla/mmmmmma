package org.atrolla.games.system;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import org.atrolla.games.characters.CharacterClasses;

import java.util.Optional;

/**
 * Created by MicroOnde on 25/02/2015.
 */
public class Player {

    public final static Player BOT = null;

    private final Optional<Input> keyboardInput;
    private final Optional<Controller> controllerInput;
    private CharacterClasses gameCharacterClass;

    public Player(Optional<Input> keyboardInput, Optional<Controller> controllerInput) {
        this.keyboardInput = keyboardInput;
        this.controllerInput = controllerInput;
        this.gameCharacterClass = CharacterClasses.BOMBER;
    }

    public boolean isSameInput(Input keyboard) {
        return keyboardInput.isPresent();// assume there can only be one keyboard
    }

    public boolean isSameInput(Controller controller) {
        return controllerInput.filter(c -> c.equals(controller)).isPresent();
    }

    public void switchClass(boolean next) {
        this.gameCharacterClass = next ? CharacterClasses.next(gameCharacterClass) : CharacterClasses.previous(gameCharacterClass);
    }

    public Optional<Input> getKeyboardInput() {
        return keyboardInput;
    }

    public Optional<Controller> getControllerInput() {
        return controllerInput;
    }

    public CharacterClasses getGameCharacterClass() {
        return gameCharacterClass;
    }


}
