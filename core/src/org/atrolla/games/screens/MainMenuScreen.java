package org.atrolla.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.game.Mmmmmma;
import org.atrolla.games.input.InputManager;
import org.atrolla.games.items.weapons.Bomb;
import org.atrolla.games.screens.demo.*;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch spriteBatch;
    private ShaderProgram shader;
    private Stage stage;
    private final InputManager inputManager;
    private int currentChosenButtonIndex;
    private final Collection<DemoScreen> demoScreens;
    private int timeElapsed;
    private final ItemSpriteManager itemSpriteManager;

    //TODO : many things in common with RoundScreen -> REFACTO

    public MainMenuScreen(Mmmmmma game) {
        //important since we aren't using some uniforms and attributes that SpriteBatch expects
        ShaderProgram.pedantic = false;

//        shader = new ShaderProgram(GLShaders.VERT, GLShaders.FRAG);
//        if (!shader.isCompiled()) {
//            System.err.println(shader.getLog());
//            System.exit(0);
//        }
//        if (shader.getLog().length() != 0)
//            System.out.println(shader.getLog());
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(game.getCamera().combined);
        spriteBatch = new SpriteBatch(1000, shader);
        spriteBatch.setShader(shader);
        stage = new Stage(new ScalingViewport(Scaling.fit, ConfigurationConstants.STAGE_WIDTH, ConfigurationConstants.STAGE_HEIGHT, game.getCamera()), spriteBatch);
        skinManager = new CharacterSkinManager();

        FileHandle skinFile = Gdx.files.internal("skins/skin.json");
        skin = new Skin(skinFile);
        BitmapFont labelFont = skin.get("default-font", BitmapFont.class);
        labelFont.getData().markupEnabled = true;
//        this.skin.getFont("nolife").getData().setScale(0.6f, 0.6f);
        itemSpriteManager = new ItemSpriteManager();

        this.game = game;
        table = new Table();
        playersTable = new Table();
        buttonPlay = new Label("Play", skin);
//        buttonPlay = new Label("<<[BLUE]M[RED]u[YELLOW]l[GREEN]t[OLIVE]ic[]o[]l[]o[]r[]*[MAROON]Label[][] [Unknown Color]>>", skin);
        setStyle(buttonPlay);
        buttonResetPlayers = new Label("Reset Players", skin);
        setStyle(buttonResetPlayers);
//        buttonExit = new Label("Exit", skin);
        title = new Label(" [BLUE]M[BLACK]ultiplayer [YELLOW]M[BLACK]ulti [BLACK]M[BLACK]ysterious\n" +
                "[RED]M[BLACK]erciless [GREEN]M[BLACK]agic [ROYAL]M[BLACK]ashup [BLACK]Arena", skin);
        inputManager = game.getInputManager();
        currentChosenButtonIndex = -1;
        addElementsToTable();
        addButtonListeners();

        demoScreens = Arrays.asList(new KnightDemo(new Coordinates(180, 320))
                , new ArcherDemo(new Coordinates(470, 380))
                , new BomberDemo(new Coordinates(770, 330))
                , new MageDemo(new Coordinates(1070, 320))
        );
        timeElapsed = 0;
//        stage.setDebugAll(true);


    }

    private void setStyle(Label label) {
        label.setColor(Color.BLACK);
        label.setFontScale(0.6f);
    }

    private void addElementsToTable() {
        //The elements are displayed in the order you add them.
        //The first appear on top, the last at the bottom.
        table.add(title).center().padTop(10).padBottom(20).row();
        table.add(buttonPlay).center().padBottom(20).row();
        table.add(buttonResetPlayers).center().padBottom(20).row();
//        table.add(buttonExit).center().padBottom(20).row();
        table.add(playersTable).expand().padTop(350).row();
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
            String labelText = "Press\nright ctrl\nor\nstart";
            if (i < size) {
                Player player = players.get(i);
                labelText = "Player " + (i + 1) + "\n" + player.getGameCharacterClass().name();
            }
            Label label = new Label(labelText, skin);
            label.setAlignment(Align.center);
            setStyle(label);
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

        renderDemo(timeElapsed++);

        stage.draw();
    }

    private void renderDemo(int timeElapsed) {
        spriteBatch.begin();
        skinManager.updateTime();
        skinManager.assignSkins();
        final List<GameCharacter> gameCharacters = demoScreens.stream()
                .peek(d -> d.update(timeElapsed))
                .map(DemoScreen::getCharacters)
                .flatMap(Collection::stream)
                .sorted((c1, c2) -> Double.compare(c2.getCoordinates().getY(), c1.getCoordinates().getY()))
                .sequential()
                .collect(Collectors.toList());
        gameCharacters.forEach(c -> {
            final Rectangle hitbox = c.getHitbox();
            final TextureRegion frame = skinManager.getFrame(c);
            spriteBatch.draw(frame, hitbox.getX(), hitbox.getY(),
                    (float) ConfigurationConstants.GAME_CHARACTER_WIDTH,
                    (float) ConfigurationConstants.GAME_CHARACTER_HEIGHT);
        });
        spriteBatch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        gameCharacters.forEach(c -> {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0f, 0f, 0f, 0.25f);
            shapeRenderer.ellipse(c.getHitbox().getX() + RoundScreen.PADDING_SHADOW_WIDTH, c.getHitbox().getY() - RoundScreen.PADDING_SHADOW_HEIGHT,
                    RoundScreen.ELLIPSE_WIDTH, RoundScreen.ELLIPSE_HEIGHT);
            shapeRenderer.end();
        });
        Gdx.gl.glDisable(GL20.GL_BLEND);
        spriteBatch.begin();
        demoScreens.stream()
                .map(DemoScreen::getItems)
                .flatMap(Collection::stream)
                .sequential()
                .forEach(item -> {
                    if (item instanceof Bomb) {
                        final Bomb bomb = (Bomb) item;
                        if (bomb.isExploding()) {
                            itemSpriteManager.makeExplosion(bomb, bomb.getCoordinates());
                        }
                    }
                });
        itemSpriteManager.render(spriteBatch);
        spriteBatch.end();
//        if (mlol != null) {
//            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//            shapeRenderer.setColor(Color.LIGHT_GRAY);
//            shapeRenderer.circle((float) mlol.getX(), (float) mlol.getY(), ConfigurationConstants.EXPLOSION_RADIUS_SIZE);
//            shapeRenderer.end();
//        }
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
