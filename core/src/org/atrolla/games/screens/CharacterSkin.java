package org.atrolla.games.screens;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.atrolla.games.system.Direction;

/**
 * Created by MicroOnde on 08/11/2015.
 */
public interface CharacterSkin {

    TextureRegion getFrame(Direction direction, boolean isMoving);
}
