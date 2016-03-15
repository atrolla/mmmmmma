package org.atrolla.games.desktop.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.OrthographicCamera;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.desktop.screens.MainMenuScreen;
import org.atrolla.games.input.InputManager;
import org.atrolla.games.sounds.SoundManager;

public class Mmmmmma extends Game {

    //    private Texture img;
    private OrthographicCamera camera;
    private SoundManager soundManager;
    private InputManager inputManager;
    private MainMenuScreen mainMenuScreen;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, (float) ConfigurationConstants.STAGE_WIDTH, (float) ConfigurationConstants.STAGE_HEIGHT);
        soundManager = new SoundManager();
        inputManager = new InputManager(Controllers.getControllers(), Gdx.input);
        mainMenuScreen = new MainMenuScreen(this);
        switchToMenuScreen();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public void switchToMenuScreen() {
        setScreen(mainMenuScreen);
    }

    @Override
    public void dispose() {
        // dispose of all the native resources
        soundManager.disposeSounds();
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public InputManager getInputManager() {
        return inputManager;
    }
}
