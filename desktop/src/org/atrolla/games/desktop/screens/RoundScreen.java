package org.atrolla.games.desktop.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.desktop.game.Mmmmmma;
import org.atrolla.games.desktop.screens.render.BubbleSpeechManager;
import org.atrolla.games.desktop.screens.render.ItemSpriteManager;
import org.atrolla.games.desktop.screens.render.RoundRender;
import org.atrolla.games.desktop.screens.render.ScreenElement;
import org.atrolla.games.desktop.screens.render.skins.CharacterSkinManager;
import org.atrolla.games.game.Round;
import org.atrolla.games.system.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoundScreen implements Screen {

    public static final float WINNER_AURA = 50f;

    private final Mmmmmma game;
    private final Round round;
    private final RoundRender roundRender;

    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch spriteBatch;
    private final Stage stage;
    private final BubbleSpeechManager bubbleSpeechManager;

    private Label topLeftText;
    private float opacity;
    private Label winnerText;
    private float winDelay;

    private final Sprite backgroundSprite;
    private final List<Sprite> trees;

    private ShaderProgram shader;

    private float startGameDelay = 4;

    public RoundScreen(Mmmmmma game) {
        this.game = game;
        round = new Round(game.getInputManager(), game.getSoundManager());

        //important since we aren't using some uniforms and attributes that SpriteBatch expects
        ShaderProgram.pedantic = false;

        shader = new ShaderProgram(GLShaders.VERT, GLShaders.FRAG);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(game.getCamera().combined);
        spriteBatch = new SpriteBatch(1000, shader);
        spriteBatch.setShader(shader);
        stage = new Stage(new ScalingViewport(Scaling.fit, ConfigurationConstants.STAGE_WIDTH, ConfigurationConstants.STAGE_HEIGHT, game.getCamera()),
                spriteBatch);
        FileHandle skinFile = Gdx.files.internal("skins/skin.json");
        Skin skin = new Skin(skinFile);
//        stage.setDebugAll(true);

        roundRender = new RoundRender(shapeRenderer, spriteBatch, round, new ItemSpriteManager(), new CharacterSkinManager());

        final int playersNumber = round.getCharacters().players.size();
        if (playersNumber == 1) {
            topLeftText = new Label("(ALONE)", skin);
        } else if (playersNumber == 0) {
            topLeftText = new Label("(DEMO)", skin);
        }
        winnerText = new Label("", skin);
        opacity = 0f;
        winDelay = 3f;
        backgroundSprite = new Sprite(new Texture("background/level.png"));
        trees = new ArrayList<>();
        trees.add(new Sprite(new Texture("trees/tree1.png")));
        trees.add(new Sprite(new Texture("trees/tree1s.png")));

        bubbleSpeechManager = new BubbleSpeechManager(round, spriteBatch);
    }

    @Override
    public void show() {
        if (round.getCharacters().players.size() < 2) {
            topLeftText.setColor(new Color(0.75f, 0.75f, 0.75f, 0.3f));
            stage.addActor(topLeftText);
        }
        Gdx.input.setInputProcessor(stage);
        game.getMusicManager().playRoundMusic();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        delayRoundStart(delta);

        //draw objects...
        roundRender.clearScreenElements();
        renderTrees();
        roundRender.renderRound();
        renderEndGame(delta);
        bubbleSpeechManager.updateBubbleSpeech();

        stage.draw();
        if (winDelay < 0) {
            game.getMusicManager().stop();
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

    private void renderTrees() {
        spriteBatch.begin();
        backgroundSprite.draw(spriteBatch);
        roundRender.addScreenElement(new ScreenElement(trees.get(0), 800f, 150f));
        spriteBatch.draw(trees.get(1), 800f, 150f);
        roundRender.addScreenElement(new ScreenElement(trees.get(0), 350f, 220f));
        spriteBatch.draw(trees.get(1), 350f, 220f);
        roundRender.addScreenElement(new ScreenElement(trees.get(0), 1100f, 600f));
        spriteBatch.draw(trees.get(1), 1100f, 600f);
        spriteBatch.end();
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
            winnerText.setColor(new Color(0.85f, 0.85f, 0.85f, 0.2f + opacity));
            winnerText.setAlignment(Align.center);
            winnerText.setWidth(stage.getWidth());
            winnerText.setFontScale(4f);
            winnerText.setY(stage.getHeight() / 2);
            stage.addActor(winnerText);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            if (index > 0) {
                final GameCharacter winner = round.getCharacters().players.stream().filter(GameCharacter::isAlive).findFirst().get();
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(0f, 1f, 0f, 0.5f);
                shapeRenderer.circle((float) winner.getCenter().getX(), (float) winner.getCenter().getY(), WINNER_AURA);
                shapeRenderer.end();
            }
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0.9f, 0.9f, 0.9f, opacity);
            shapeRenderer.rect(0, 0, stage.getWidth(), stage.getHeight());
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    private int getWinningPlayerIndex() {
        final Optional<Player> winner = round.getCharacters().players.stream().filter(GameCharacter::isAlive).map(GameCharacter::getPlayer).findFirst();
        if (winner.isPresent()) {
            final Player player = winner.get();
            final List<Player> playerList = game.getInputManager().getPlayers();
            for (int i = 0; i < playerList.size(); i++) {
                final Player playerI = playerList.get(i);
                if (player.equals(playerI)) {
                    return i + 1;
                }
            }
        }
        return 0;
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
        bubbleSpeechManager.dispose();
        spriteBatch.dispose();
        shapeRenderer.dispose();
        shader.dispose();
    }
}
