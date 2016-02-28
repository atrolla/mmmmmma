package org.atrolla.games.desktop.screens;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.atrolla.games.characters.GameCharacter;

public interface CharacterSkin {

    TextureRegion getFrame(GameCharacter gameCharacter, float stateTime);
}
