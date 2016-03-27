package org.atrolla.games.desktop.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.desktop.game.Demo;
import org.atrolla.games.desktop.game.Mmmmmma;
import org.atrolla.games.game.Round;
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
    private final CharacterSkinManager skinManager;
    private final RoundRender roundRender;
    private Stage stage;
    private final InputManager inputManager;
    private int currentChosenButtonIndex;
    private Round demo;

    //TODO : many things in common with RoundScreen -> REFACTO

    public MainMenuScreen(Mmmmmma game) {
        //important since we aren't using some uniforms and attributes that SpriteBatch expects
        ShaderProgram.pedantic = false;

        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(game.getCamera().combined);
        SpriteBatch spriteBatch = new SpriteBatch(1000);
        stage = new Stage(new ScalingViewport(Scaling.fit, ConfigurationConstants.STAGE_WIDTH, ConfigurationConstants.STAGE_HEIGHT, game.getCamera()), spriteBatch);
        skinManager = new CharacterSkinManager();

        FileHandle skinFile = Gdx.files.internal("skins/skin.json");
        skin = new Skin(skinFile);
        BitmapFont labelFont = skin.get("default-font", BitmapFont.class);
        labelFont.getData().markupEnabled = true;

        this.game = game;
        table = new Table();
        playersTable = new Table();
        buttonPlay = new Label("Play", skin);
        setStyle(buttonPlay);
        buttonResetPlayers = new Label("Reset Players", skin);
        setStyle(buttonResetPlayers);
//        buttonExit = new Label("Exit", skin);
        title = new Label("[BLUE]M[WHITE]ultiplayer [ORANGE]M[WHITE]ulti [GREEN]M[WHITE]ysterious\n" +
                "[SCARLET]M[WHITE]erciless [OLIVE]M[WHITE]agic [ROYAL]M[WHITE]ashup [YELLOW]A[WHITE]rena", skin);
        inputManager = game.getInputManager();
        currentChosenButtonIndex = -1;
        addElementsToTable();
        addButtonListeners();

        demo = new Demo.Builder().build();

        roundRender = new RoundRender(shapeRenderer, spriteBatch, demo, new ItemSpriteManager(), skinManager);
        roundRender.setDemo(true);

//        stage.setDebugAll(true);
    }

    private void setStyle(Label label) {
        label.setColor(Color.WHITE);
        label.setFontScale(2f);
        label.setAlignment(Align.center);
    }

    private void addElementsToTable() {
        //The elements are displayed in the order you add them.
        //The first appear on top, the last at the bottom.
        final int colspan = 5;
        title.setAlignment(Align.center);
        title.setFontScale(3f);
//        title.setText("[BLUE]D[WHITE]evoxx [YELLOW]D[WHITE]eadline [OLIVE]D[WHITE]riven [RED]D[WHITE]evelopment\n" +
//                "[SALMON]D[WHITE]irected & [ROYAL]D[WHITE]esigned by [GREEN]A[WHITE]rolla");
        table.add(title).center().padTop(40).padBottom(20).colspan(colspan).row();
        table.add(buttonPlay).center().padBottom(20).colspan(colspan).row();
        table.add(buttonResetPlayers).center().padBottom(20).colspan(colspan).row();
//        table.add(buttonExit).center().padBottom(20).row();
        final String Color = "[#FFFFFF88]";
        final Label knight = new Label(Color+"Knight\n(" + ConfigurationConstants.KNIGHT_LIVES + " lives)", skin);
        setStyle(knight);
        table.add(knight);
        final Label archer = new Label(Color + "Archer\n" +
                "(" + ConfigurationConstants.ARCHER_LIVES + " life)", skin);
        setStyle(archer);
        table.add(archer).spaceBottom(120);
        final Label weapon = new Label(Color+"Demo Class has\ngreen shadow\n\n\nWeapons \n(Magenta Dots)\n" +
                " are invisible\n in game", skin);
        setStyle(weapon);
        table.add(weapon).spaceTop(70);
        final Label bomber = new Label(Color+"Bomber\n" +
                "(" + ConfigurationConstants.BOMBER_LIVES + " lives)", skin);
        setStyle(bomber);
        table.add(bomber).spaceBottom(30);
        final Label mage = new Label(Color+"Mage\n" +
                "(" + ConfigurationConstants.MAGE_LIVES + " lives)", skin);
        setStyle(mage);
        table.add(mage).spaceBottom(120);

        table.row();
        table.add(playersTable).expand().padTop(100).colspan(colspan).row();
        table.setFillParent(true);
    }

    @Override
    public void show() {
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
        game.getMusicManager().playMenuMusic();
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
        game.getMusicManager().stop();
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

        stage.act(delta);
        // clear the screen with the given RGB color (black)
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
//        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // draw the actors
        inputManager.assignPlayers();

        playersTable.clear();
        List<Player> players = inputManager.getPlayers();
        int size = players.size();
        for (int i = 0; i < 4; i++) {
            String labelText = "Press\nstart";
            if (i < size) {
                Player player = players.get(i);
                labelText = "Player " + (i + 1) + "\n" + player.getGameCharacterClass().name();
            }
            Label label = new Label(labelText, skin);
            label.setAlignment(Align.center);
            setStyle(label);
            playersTable.add(label).width(ConfigurationConstants.STAGE_WIDTH / 4).expand().uniform();
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

        demo.update();
        roundRender.clearScreenElements();
        skinManager.assignSkins();
        roundRender.renderRound();

        stage.draw();
    }

    private void focusChosenButton() {
        buttonPlay.setColor(Color.WHITE);
        buttonResetPlayers.setColor(Color.WHITE);
//        buttonExit.setFontScale(1);
        getChoosenTextButton().setColor(Color.ROYAL);
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
