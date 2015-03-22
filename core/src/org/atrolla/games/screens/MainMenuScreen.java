package org.atrolla.games.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import org.atrolla.games.game.Mmmmmma;
import org.atrolla.games.input.InputManager;
import org.atrolla.games.system.Player;

import java.util.List;

/**
 * Created by MicroOnde on 21/03/2015.
 */
public class MainMenuScreen implements Screen {

    private final Mmmmmma game;
    private final Table table;
    private final Table playersTable;
    private final Skin skin;
    private final TextButton buttonExit;
    private final TextButton buttonPlay;
    private final TextButton buttonResetPlayers;
    private final Label title;
    private final Stage stage;
    private final InputManager inputManager;

    public MainMenuScreen(Mmmmmma game) {
        this.game = game;
        table = new Table();
        playersTable = new Table();
        FileHandle skinFile = Gdx.files.internal("skins/skin.json");
        skin = new Skin(skinFile);
        buttonPlay = new TextButton("Play", skin);
        buttonResetPlayers = new TextButton("Reset Players", skin);
        buttonExit = new TextButton("Exit", skin);
        title = new Label("Multiplayer Multi Mortal Mix Melee Mashup Arena", skin);
        stage = new Stage();
        inputManager = game.getInputManager();
    }

    @Override
    public void show() {
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new RoundScreen(game));
            }
        });
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        buttonResetPlayers.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                inputManager.unAssignPlayers();
            }
        });
        //The elements are displayed in the order you add them.
        //The first appear on top, the last at the bottom.
        table.add(title).center().padTop(40).padBottom(40).row();
        table.add(buttonPlay).size(150, 60).padBottom(20).row();
        table.add(buttonResetPlayers).size(400, 60).padBottom(20).row();
        table.add(buttonExit).size(150, 60).padBottom(20).row();
        table.add(playersTable).expand().padBottom(20).row();
        table.setFillParent(true);
        //table.debug();
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // (1) process the game logic
        // update the actors
        stage.act(delta);
        // (2) draw the result
        // clear the screen with the given RGB color (black)
        Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1);
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

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
