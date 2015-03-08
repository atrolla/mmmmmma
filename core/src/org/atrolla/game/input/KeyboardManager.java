package org.atrolla.game.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Array;
import org.atrolla.game.characters.GameCharacter;
import org.atrolla.game.system.Direction;

import java.util.List;

/**
 * Created by yeamanan on 28/02/15.
 */
public class KeyboardManager {
    private final Array<Input> keyboards;

    public KeyboardManager(Array<Input> keyboards) {
        this.keyboards = new Array<>(keyboards);
    }

    public void updatePlayers(List<GameCharacter> characters) {
        if (keyboards != null && keyboards.size > 0) {
            final GameCharacter gameCharacter = characters.get(0);
            final Input keyboard = keyboards.get(0);
            if (keyboard.isKeyPressed(Keys.UP)) {
                if (keyboard.isKeyPressed(Keys.LEFT)) {
                    gameCharacter.moves(Direction.UP_LEFT);
                } else if (keyboard.isKeyPressed(Keys.RIGHT)) {
                    gameCharacter.moves(Direction.UP_RIGHT);
                } else {
                    gameCharacter.moves(Direction.UP);
                }
            } else if (keyboard.isKeyPressed(Keys.DOWN)) {
                if (keyboard.isKeyPressed(Keys.LEFT)) {
                    gameCharacter.moves(Direction.DOWN_LEFT);
                } else if (keyboard.isKeyPressed(Keys.RIGHT)) {
                    gameCharacter.moves(Direction.DOWN_RIGHT);
                } else {
                    gameCharacter.moves(Direction.DOWN);
                }
            } else if (keyboard.isKeyPressed(Keys.RIGHT)) {
                gameCharacter.moves(Direction.RIGHT);
            } else if (keyboard.isKeyPressed(Keys.LEFT)) {
                gameCharacter.moves(Direction.LEFT);
            }
        }
    }
}
