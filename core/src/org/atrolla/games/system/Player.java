package org.atrolla.games.system;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import org.atrolla.games.characters.CharacterClasses;
import org.atrolla.games.input.PadController;

import java.util.Optional;

public class Player {

    public final static Player BOT = null;

    private final Optional<Input> keyboardInput;
    private final Optional<PadController> padControllerInput;
    private CharacterClasses gameCharacterClass;

    public Player(Optional<Input> keyboardInput, Optional<PadController> padControllerInput) {
        this.keyboardInput = keyboardInput;
        this.padControllerInput = padControllerInput;
        this.gameCharacterClass = CharacterClasses.BOMBER;
    }

    public Player(CharacterClasses gameCharacterClass) {
        this.keyboardInput = Optional.empty();
        this.padControllerInput = Optional.empty();
        this.gameCharacterClass = gameCharacterClass;
    }

    public boolean isSameInput(Input keyboard) {
        return keyboardInput.isPresent();// assume there can only be one keyboard
    }

    public boolean isSameInput(Controller controller) {
        return padControllerInput.filter(pc -> pc.getController().equals(controller)).isPresent();
    }

    public void switchClass(boolean next) {
        this.gameCharacterClass = next ? CharacterClasses.next(gameCharacterClass) : CharacterClasses.previous(gameCharacterClass);
    }

    public Optional<Input> getKeyboardInput() {
        return keyboardInput;
    }

    public Optional<PadController> getPadControllerInput() {
        return padControllerInput;
    }

    public CharacterClasses getGameCharacterClass() {
        return gameCharacterClass;
    }


}
