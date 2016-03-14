package org.atrolla.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.game.Mmmmmma;
import org.atrolla.games.game.Round;
import org.atrolla.games.items.Item;
import org.atrolla.games.items.neutrals.Locator;
import org.atrolla.games.items.neutrals.NeutralItem;
import org.atrolla.games.items.weapons.Bomb;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoundScreen implements Screen {

    public static final float ELLIPSE_HEIGHT = (float) ConfigurationConstants.GAME_CHARACTER_HEIGHT / 5;
    public static final float PADDING_SHADOW_WIDTH = 4f;
    public static final float PADDING_SHADOW_HEIGHT = 1f;
    public static final float ELLIPSE_WIDTH = (float) ConfigurationConstants.GAME_CHARACTER_WIDTH - PADDING_SHADOW_WIDTH - PADDING_SHADOW_WIDTH;
    public static final float WINNER_AURA = 50f;

    private final Mmmmmma game;
    private final Round round;

    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch spriteBatch;
    private final CharacterSkinManager skinManager;
    private final ItemSpriteManager itemSpriteManager;
    private final Stage stage;

    private Label topLeftText;
    private float opacity;
    private Label winnerText;
    private float winDelay;

    private Set<Item> itemsToHide;

    private final Sprite backgroundSprite;
    private final List<Sprite> trees;

    private ShaderProgram shader;

    private float startGameDelay = 3;

    private List<ScreenElement> screenElements;

    public RoundScreen(Mmmmmma game) {
        this.game = game;
        round = new Round(game.getInputManager(), game.getSoundManager());

        //important since we aren't using some uniforms and attributes that SpriteBatch expects
        ShaderProgram.pedantic = false;

        shader = new ShaderProgram(GLShaders.VERT, GLShaders.FRAG);
        if (!shader.isCompiled()) {
            System.err.println(shader.getLog());
            System.exit(0);
        }
        if (shader.getLog().length() != 0)
            System.out.println(shader.getLog());
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(game.getCamera().combined);
        spriteBatch = new SpriteBatch(1000, shader);
        spriteBatch.setShader(shader);
        stage = new Stage(new ScalingViewport(Scaling.fit, ConfigurationConstants.STAGE_WIDTH, ConfigurationConstants.STAGE_HEIGHT, game.getCamera()),
                spriteBatch);
        skinManager = new CharacterSkinManager();
        FileHandle skinFile = Gdx.files.internal("skins/skin.json");
        Skin skin = new Skin(skinFile);
//        stage.setDebugAll(true);
        itemSpriteManager = new ItemSpriteManager();
        final int playersNumber = round.getCharacters().players.size();
        if (playersNumber == 1) {
            topLeftText = new Label("(ALONE)", skin);
        } else if (playersNumber == 0) {
            topLeftText = new Label("(DEMO)", skin);
        }
        winnerText = new Label("", skin);
        opacity = 0f;
        winDelay = 2f;
        itemsToHide = new HashSet<>();
        backgroundSprite = new Sprite(new Texture("background/level.png"));
        trees = new ArrayList<>();
        trees.add(new Sprite(new Texture("trees/tree1.png")));
        trees.add(new Sprite(new Texture("trees/tree1s.png")));
    }

    @Override
    public void show() {
        if (round.getCharacters().players.size() < 2) {
            topLeftText.setColor(new Color(0.75f, 0.75f, 0.75f, 0.3f));
            topLeftText.setFontScale(0.5f);
            stage.addActor(topLeftText);
        }
        Gdx.input.setInputProcessor(stage);
    }

    private void renderTrees() {
        spriteBatch.begin();
        backgroundSprite.draw(spriteBatch);
        screenElements.add(new ScreenElement(trees.get(0), 800f, 150f));
        spriteBatch.draw(trees.get(1), 800f, 150f);
        screenElements.add(new ScreenElement(trees.get(0), 350f, 220f));
        spriteBatch.draw(trees.get(1), 350f, 220f);
        screenElements.add(new ScreenElement(trees.get(0), 1100f, 600f));
        spriteBatch.draw(trees.get(1), 1100f, 600f);

        spriteBatch.end();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        screenElements = new ArrayList<>();

        delayRoundStart(delta);

        //draw objects...
        renderTrees();
        renderCharacters();
        renderItems();
        renderEndGame(delta);

        stage.draw();
        if (winDelay < 0) {
            game.switchToMenuScreen();
        }

    }

    private void delayRoundStart(float delta) {
        if (startGameDelay >= 0) {
            startGameDelay -= delta;
        } else {
            round.update();
        }
    }

    private void renderEndGame(float delta) {
        if (round.isFinished()) {
            if (opacity < 0.8f) {
                opacity += 0.005f;
            } else {
                winDelay -= delta;
            }
            //winDelay
            int index = getWinningPlayerIndex();
            if (index > 0) {
                winnerText.setText("Player " + index + " wins !");
            } else {
                winnerText.setText("Well... All that for a draw !?");
            }
            winnerText.setColor(new Color(0.75f, 0.75f, 0.75f, 0.2f + opacity));
            winnerText.setAlignment(Align.center);
            winnerText.setWidth(stage.getWidth());
            winnerText.setY(stage.getHeight() / 2);
            stage.addActor(winnerText);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            final GameCharacter winner = round.getCharacters().players.stream().filter(GameCharacter::isAlive).findFirst().get();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0f, 1f, 0f, 0.5f);
            shapeRenderer.circle((float) winner.getCenter().getX(), (float) winner.getCenter().getY(), WINNER_AURA);
            shapeRenderer.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0.9f, 0.9f, 0.9f, opacity);
            shapeRenderer.rect(0, 0, stage.getWidth(), stage.getHeight());
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    private int getWinningPlayerIndex() {
        final Player player = round.getCharacters().players.stream().filter(GameCharacter::isAlive).map(GameCharacter::getPlayer).findFirst().get();
        final List<Player> playerList = game.getInputManager().getPlayers();
        for (int i = 0; i < playerList.size(); i++) {
            final Player playerI = playerList.get(i);
            if (player.equals(playerI)) {
                return i + 1;
            }
        }
        return 0;
    }

    private void renderItems() {
        spriteBatch.begin();
        final List<Item> gameObjects = round.getGameItems();
        for (Item item : gameObjects) {
            final Coordinates coordinates = item.getCoordinates();
//            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            showItem(item, coordinates);
//            shapeRenderer.end();
        }
        gameObjects.stream().filter(item -> item instanceof NeutralItem).forEach(item -> {
            if (item instanceof Locator && ((Locator) item).mustRegister()) {
                itemSpriteManager.registerAnimation(item, ((Locator) item).getVictimCoordinates());
            } else if (!(item instanceof Locator && ((Locator) item).getPicker().isPresent())) {
                itemSpriteManager.registerItem(item);
            }
        });
        itemSpriteManager.render(spriteBatch);
        spriteBatch.end();
//        gameObjects.stream().filter(item -> item instanceof NeutralItem).forEach(item -> {
//            if (!(item instanceof Locator && ((Locator) item).getPicker().isPresent())) {
//                final Coordinates coordinates = item.getCoordinates();
//                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//                shapeRenderer.setColor(Color.MAGENTA);
//                shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), ConfigurationConstants.NEUTRAL_ITEM_SIZE);
//                shapeRenderer.end();
//            }
//        });
    }

    private void showItem(Item item, Coordinates coordinates) {
        if (item instanceof Bomb) {
//            if (!itemsToHide.contains(item)) {
//                shapeRenderer.setColor(Color.RED);
//                shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), 2);

//                itemsToHide.add(item);
//            }
            final Bomb bomb = (Bomb) item;
            if (bomb.isExploding()) {
//                shapeRenderer.setColor(Color.DARK_GRAY);
//                shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), ConfigurationConstants.BOMB_EXPLOSION_RADIUS_SIZE);

                itemSpriteManager.registerAnimation(bomb, coordinates);
            }
        }
//        else if (item instanceof Arrow) {
//            // Arrow
//            shapeRenderer.setColor(Color.PINK);
//            shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), 1);
//        } else if (item instanceof Sword) {
//            // Sword
//            shapeRenderer.setColor(Color.TEAL);
//            shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), 1);
//        else if (item instanceof NeutralItem) {
//            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//            shapeRenderer.setColor(Color.MAGENTA);
//            shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), ConfigurationConstants.NEUTRAL_ITEM_SIZE);
//            shapeRenderer.end();
//        }
//        else if (item instanceof MageSpell) {
//            shapeRenderer.setColor(Color.MAGENTA);
//            shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), ConfigurationConstants.MAGE_SPELL_SIZE);
//        }
    }

    private void renderCharacters() {
        skinManager.updateTime();
        final List<GameCharacter> characters = round.getCharacters().characters;
        renderCharactersShadow(characters);
        spriteBatch.begin();
        characters.stream().forEach(this::renderCharacter);
        renderScreenElements();
        spriteBatch.end();
    }

    private void renderScreenElements() {
        screenElements.stream()
                .sorted((s1, s2) -> Float.compare(s2.y, s1.y))
                .sequential()
                .forEach(s -> s.draw(spriteBatch));
    }

    private void renderCharactersShadow(List<GameCharacter> characters) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        characters.stream()
                .sorted((c1, c2) -> Double.compare(c2.getCoordinates().getY(), c1.getCoordinates().getY()))
                .sequential()
                .forEach(c -> {
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    shapeRenderer.setColor(0f, 0f, 0f, 0.25f);
                    shapeRenderer.ellipse(c.getHitbox().getX() + PADDING_SHADOW_WIDTH, c.getHitbox().getY() - PADDING_SHADOW_HEIGHT, ELLIPSE_WIDTH, ELLIPSE_HEIGHT);
                    shapeRenderer.end();
                });
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void renderCharacter(GameCharacter gameCharacter) {
        final Rectangle hitbox = gameCharacter.getHitbox();
        final TextureRegion frame = skinManager.getFrame(gameCharacter);
        screenElements.add(new ScreenElement(frame, hitbox.getX(), hitbox.getY(),
                (float) ConfigurationConstants.GAME_CHARACTER_WIDTH,
                (float) ConfigurationConstants.GAME_CHARACTER_HEIGHT));
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
        //bind the shader, then set the uniform, then unbind the shader
        shader.begin();
        shader.setUniformf("resolution", width, height);
        shader.end();
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
        spriteBatch.dispose();
        shapeRenderer.dispose();
        shader.dispose();
    }
}
