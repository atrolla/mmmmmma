package org.atrolla.games.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.screens.MainMenuScreen;
import org.atrolla.games.sounds.SoundManager;

public class Mmmmmma extends Game {
    //    private Texture img;
    private OrthographicCamera camera;
    private SoundManager soundManager;
    private MainMenuScreen mainMenuScreen;
    private float stageWidth;
    private float stageHeight;

    @Override
    public void create() {
//        img = new Texture("badlogic.jpg");
        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        stageWidth = (float) ConfigurationConstants.STAGE_WIDTH;
        stageHeight = (float) ConfigurationConstants.STAGE_HEIGHT;
        camera.setToOrtho(false, stageWidth, stageHeight);
        soundManager = new SoundManager();
        mainMenuScreen = new MainMenuScreen(this);
        setScreen(mainMenuScreen);
    }

    /*@Override
    public void render() {
        // clear the screen with a dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.

        Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();
    }*/

    @Override
    public void dispose() {
        // dispose of all the native resources
        soundManager.disposeSounds();
    }


    public MainMenuScreen getMainMenuScreen() {
        return mainMenuScreen;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public float getStageWidth() {
        return stageWidth;
    }

    public float getStageHeight() {
        return stageHeight;
    }
}
