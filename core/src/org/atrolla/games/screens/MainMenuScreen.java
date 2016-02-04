package org.atrolla.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.game.Mmmmmma;
import org.atrolla.games.input.InputManager;
import org.atrolla.games.system.Player;

import java.util.List;

public class MainMenuScreen implements Screen {

    public static final float PAD_BOTTOM = 12f;
    private final Mmmmmma game;
    private final Table table;
    private final Table playersTable;
    private final Skin skin;
//    private final Label buttonExit;
    private final Label buttonPlay;
    private final Label buttonResetPlayers;
    private final Label title;
    private Stage stage;
    private final InputManager inputManager;
    private int currentChosenButtonIndex;

    public MainMenuScreen(Mmmmmma game) {
        this.game = game;
        table = new Table();
        playersTable = new Table();
        FileHandle skinFile = Gdx.files.internal("skins/skin.json");
        skin = new Skin(skinFile);
        buttonPlay = new Label("Play", skin);
        buttonResetPlayers = new Label("Reset Players", skin);
//        buttonExit = new Label("Exit", skin);
        title = new Label("Multiplayer Multi Mortal Mix Melee Mashup Arena", skin);
        inputManager = game.getInputManager();
        currentChosenButtonIndex = -1;
        stage = new Stage(new ScalingViewport(Scaling.fit, ConfigurationConstants.STAGE_WIDTH, ConfigurationConstants.STAGE_HEIGHT, game.getCamera()));
        addElementsToTable();
        addButtonListeners();
    }

    private void addElementsToTable() {
        //The elements are displayed in the order you add them.
        //The first appear on top, the last at the bottom.
        table.add(title).center().padTop(40).padBottom(40).row();
        table.add(buttonPlay).center().padBottom(20).row();
        table.add(buttonResetPlayers).center().padBottom(20).row();
//        table.add(buttonExit).center().padBottom(20).row();
        table.add(playersTable).expand().padBottom(20).row();
        table.setFillParent(true);
    }

    @Override
    public void show() {
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    private void addButtonListeners() {
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startNewRound();
            }
        });
//        buttonExit.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                exitGame();
//            }
//        });
        buttonResetPlayers.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                unAssignPlayers();
            }
        });
    }

    private void startNewRound() {
        game.setScreen(new RoundScreen(game));
    }

    private void exitGame() {
        Gdx.app.exit();
    }

    private void unAssignPlayers() {
        inputManager.unAssignPlayers();
    }

    @Override
    public void render(float delta) {

        // (1) process the game logic
        // update the actors
        stage.act(delta);
        // (2) draw the result
        // clear the screen with the given RGB color (black)
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // draw the actors
        inputManager.assignPlayers();

        playersTable.clear();
        List<Player> players = inputManager.getPlayers();
        int size = players.size();
        for (int i = 0; i < 4; i++) {
            String labelText = "Press\nright ctrl\nor\nstart";
            if (i < size) {
                Player player = players.get(i);
                labelText = "Player " + (i + 1) + "\n" + player.getGameCharacterClass().name();
            }
            Label label = new Label(labelText, skin);
            label.setAlignment(Align.center);
            playersTable.add(label).width(300).expand().uniform();
        }
        if (size > 0) {
            if (currentChosenButtonIndex == -1 && size > 0) {
                currentChosenButtonIndex = 0;
            } else {
                final int i = inputManager.watchChangeMenu();
                currentChosenButtonIndex += i;
                currentChosenButtonIndex = currentChosenButtonIndex == -1 ? 0 : currentChosenButtonIndex == 3 ? 2 : currentChosenButtonIndex;
                focusChosenButton();
            }
            if (inputManager.watchChooseMenu()) {
                switch (currentChosenButtonIndex) {
                    case 0:
                        startNewRound();
                        break;
                    case 1:
                        unAssignPlayers();
                        break;
                    case 2:
                        exitGame();
                        break;
                    default:
                        startNewRound();
                }
            }
        }
        stage.draw();
    }

    private void focusChosenButton() {
        buttonPlay.setFontScale(1);
        buttonResetPlayers.setFontScale(1);
//        buttonExit.setFontScale(1);
        getChoosenTextButton().setFontScale(1.5f);
    }

    private Label getChoosenTextButton() {
        switch (currentChosenButtonIndex) {
            case 0:
                return buttonPlay;
            case 1:
                return buttonResetPlayers;
//            case 2:
//                return buttonExit;
            default:
                return buttonPlay;
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
//        System.out.println("MainMenuScreen hidden");
    }

    @Override
    public void dispose() {
//        System.out.println("MainMenuScreen disposed");
        stage.dispose();
        skin.dispose();
    }
}
