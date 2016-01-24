package org.atrolla.games.screens;


import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.system.Direction;

public class BaseSkin implements CharacterSkin {

    public static final float FRAME_DURATION = 0.2f;

    private Animation walkDown;
    private Animation walkDownLeft;
    private Animation walkLeft;
    private Animation walkUpLeft;
    private Animation walkUp;
    private TextureRegion hit;
    private TextureRegion dead;

    public BaseSkin(FileHandle textureFile) {
        Texture walkSheet = new Texture(textureFile);
        TextureRegion[] regions = new TextureRegion[50];
        int k = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                regions[k++] = new TextureRegion(walkSheet, (32 * j), (40 * i) - 1, 32, 40);
            }
        }
        //TODO : dies
//        regions[0] = new TextureRegion(walkSheet, 0, 0, 32, 40);
//        regions[1] = new TextureRegion(walkSheet, 0.5f, 0f, 1f, 0.5f);
//        regions[2] = new TextureRegion(walkSheet, 0, 63, 64, 64);
//        regions[3] = new TextureRegion(walkSheet, 0.5f, 0.5f, 1f, 1f);
        walkDown = buildAnimation(regions, 0);
        walkDownLeft = buildAnimation(regions, 1);
        walkLeft = buildAnimation(regions, 2);
        walkUpLeft = buildAnimation(regions, 3);
        walkUp = buildAnimation(regions, 4);
        hit = new TextureRegion(walkSheet, 224, 632 - 1, 32, 40);
        dead = new TextureRegion(walkSheet, 224, 672 - 1, 32, 40);
    }

    private Animation buildAnimation(TextureRegion[] tmp, int row) {
        TextureRegion[] walkFrames = new TextureRegion[4];
        System.arraycopy(tmp, (4 * row), walkFrames, 0, 4);
        return new Animation(FRAME_DURATION, walkFrames);
    }

    @Override
    public TextureRegion getFrame(GameCharacter gameCharacter, float stateTime) {
        Direction direction = gameCharacter.getDirection();
        boolean isMoving = gameCharacter.isMoving();
        TextureRegion result = null;
        if (gameCharacter.isKnockOut()) {
            result = hit;
        } else if (!gameCharacter.isAlive()) {
            result = dead;
        } else {
            switch (direction) {
                case UP:
                    result = isMoving ? walkUp.getKeyFrame(stateTime, true) : walkUp.getKeyFrame(0);
                    break;
                case DOWN:
                    result = isMoving ? walkDown.getKeyFrame(stateTime, true) : walkDown.getKeyFrame(0);
                    break;
                case LEFT:
                    result = isMoving ? walkLeft.getKeyFrame(stateTime, true) : walkLeft.getKeyFrame(0);
                    if (result.isFlipX())
                        result.flip(true, false);
                    break;
                case RIGHT:
                    result = isMoving ? walkLeft.getKeyFrame(stateTime, true) : walkLeft.getKeyFrame(0);
                    if (!result.isFlipX())
                        result.flip(true, false);
                    break;
                case UP_LEFT:
                    result = isMoving ? walkUpLeft.getKeyFrame(stateTime, true) : walkUpLeft.getKeyFrame(0);
                    if (result.isFlipX())
                        result.flip(true, false);
                    break;
                case UP_RIGHT:
                    result = isMoving ? walkUpLeft.getKeyFrame(stateTime, true) : walkUpLeft.getKeyFrame(0);
                    if (!result.isFlipX())
                        result.flip(true, false);
                    break;
                case DOWN_LEFT:
                    result = isMoving ? walkDownLeft.getKeyFrame(stateTime, true) : walkDownLeft.getKeyFrame(0);
                    if (result.isFlipX())
                        result.flip(true, false);
                    break;
                case DOWN_RIGHT:
                    result = isMoving ? walkDownLeft.getKeyFrame(stateTime, true) : walkDownLeft.getKeyFrame(0);
                    if (!result.isFlipX())
                        result.flip(true, false);
                    break;
                case STOP:
                    //should never happen
                    break;
            }
        }
        return (result == null) ? null : result;
    }
}
