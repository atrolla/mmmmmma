package org.atrolla.game.input;

import com.badlogic.gdx.Input.Keys;
import org.atrolla.game.characters.GameCharacter;
import org.atrolla.game.engine.Direction;

/**
 * Created by yeamanan on 28/02/15.
 */
public class KeyboardManager {

    public KeyboardManager() {
    }

    public void moveCharacter(GameCharacter character, int keyPressed) {
        if (keyPressed == Keys.UP) {
            character.moves(Direction.UP);
        } else if (keyPressed == Keys.RIGHT) {
            character.moves(Direction.RIGHT);
        } else if (keyPressed == Keys.DOWN) {
            character.moves(Direction.DOWN);
        } else if (keyPressed == Keys.LEFT) {
            character.moves(Direction.LEFT);
        }
    }
}
