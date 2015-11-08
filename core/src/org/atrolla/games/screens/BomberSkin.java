package org.atrolla.games.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.atrolla.games.system.Direction;

public class BomberSkin implements CharacterSkin {

    private static final int FRAME_COLS = 4;
    private static final int FRAME_ROWS = 8;
    public static final float FRAME_DURATION = 1f;

    Animation walkDown;
    Animation walkDownLeft;
    Animation walkLeft;
    Animation walkUpLeft;
    Animation walkUp;
    Animation walkDownRight;
    Animation walkRight;
    Animation walkUpRight;

    float stateTime;

    public BomberSkin() {
        Texture walkSheet = new Texture(Gdx.files.internal("skins/perso.png"));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);
        walkDown = buildAnimation(tmp, 0);
        walkDownLeft = buildAnimation(tmp, 1);
        walkLeft = buildAnimation(tmp, 2);
        walkUpLeft = buildAnimation(tmp, 3);
        walkUp = buildAnimation(tmp, 4);
        walkDownRight = buildAnimation(tmp, 5);
        walkRight = buildAnimation(tmp, 6);
        walkUpRight = buildAnimation(tmp, 7);
        stateTime = 0f;
    }

    private Animation buildAnimation(TextureRegion[][] tmp, int row) {
        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS];
        int index = 0;
        for (int j = 0; j < FRAME_COLS; j++) {
            walkFrames[index++] = tmp[row][j];
        }
        return new Animation(FRAME_DURATION, walkFrames);
    }

    @Override
    public TextureRegion getFrame(Direction direction, boolean isMoving) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        //FIXME : this make the animation buggy
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion result = null;
        switch (direction) {
            case UP:
                result = isMoving ? walkUp.getKeyFrame(stateTime, true) : walkUp.getKeyFrame(0);
                break;
            case DOWN:
                result = isMoving ? walkDown.getKeyFrame(stateTime, true) : walkDown.getKeyFrame(0);
                break;
            case LEFT:
                result = isMoving ? walkLeft.getKeyFrame(stateTime, true) : walkLeft.getKeyFrame(0);
                break;
            case RIGHT:
                result = isMoving ? walkRight.getKeyFrame(stateTime, true) : walkRight.getKeyFrame(0);
                break;
            case UP_LEFT:
                result = isMoving ? walkUpLeft.getKeyFrame(stateTime, true) : walkUpLeft.getKeyFrame(0);
                break;
            case UP_RIGHT:
                result = isMoving ? walkUpRight.getKeyFrame(stateTime, true) : walkUpRight.getKeyFrame(0);
                break;
            case DOWN_LEFT:
                result = isMoving ? walkDownLeft.getKeyFrame(stateTime, true) : walkDownLeft.getKeyFrame(0);
                break;
            case DOWN_RIGHT:
                result = isMoving ? walkDownRight.getKeyFrame(stateTime, true) : walkDownRight.getKeyFrame(0);
                break;
            case STOP:
                //should never happen
                break;
        }
        return (result == null) ? null : new TextureRegion(result);
    }
}
