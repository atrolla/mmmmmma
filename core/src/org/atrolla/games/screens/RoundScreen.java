package org.atrolla.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.game.Mmmmmma;
import org.atrolla.games.game.Round;
import org.atrolla.games.items.Item;
import org.atrolla.games.items.neutrals.NeutralItem;
import org.atrolla.games.items.weapons.Arrow;
import org.atrolla.games.items.weapons.Bomb;
import org.atrolla.games.items.weapons.MageWeaponWrapper;
import org.atrolla.games.items.weapons.Sword;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Player;

import java.util.List;

public class RoundScreen implements Screen {

    private final Mmmmmma game;
    private final Round round;

    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch spriteBatch;
    private final CharacterSkinManager skinManager;
    private final Stage stage;
    private final Skin skin;

    private Label topLeftText;
    private float opacity;
    private Label winnerText;
    private float winDelay;


    public RoundScreen(Mmmmmma game) {
        this.game = game;
        round = new Round(game.getInputManager(), game.getSoundManager());
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        stage = new Stage(new ScalingViewport(Scaling.stretch, ConfigurationConstants.STAGE_WIDTH, ConfigurationConstants.STAGE_HEIGHT, game.getCamera()),
                spriteBatch);
//        stage.setDebugAll(true);
        skinManager = new CharacterSkinManager();
        FileHandle skinFile = Gdx.files.internal("skins/skin.json");
        this.skin = new Skin(skinFile);
        final int playersNumber = round.getPlayers().size();
        if (playersNumber == 1) {
            topLeftText = new Label("(ALONE)", skin);
        } else if (playersNumber == 0) {
            topLeftText = new Label("(DEMO)", skin);
        }
        winnerText = new Label("", skin);
        opacity = 0f;
        winDelay = 2f;
    }

    @Override
    public void show() {
        System.out.println("game :" + Gdx.graphics.getWidth() + "/" + Gdx.graphics.getHeight());
        if (round.getPlayers().size() < 2) {
            topLeftText.setColor(new Color(0.75f, 0.75f, 0.75f, 0.3f));
            topLeftText.setFontScale(0.5f);
            stage.addActor(topLeftText);
        }
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        round.update();

        //draw objects...
        renderItems();
        renderCharacters();
//        if (round.isFinished()) { TODO: uncomment when mainScreen bug is fixed
            if (opacity < 0.8f) {
                opacity += 0.005f;
            } else {
                winDelay -= delta;
            }
            //winDelay
            int index = getWinningPlayerIndex();
            winnerText.setText("Player " + index + " wins !");
            winnerText.setColor(new Color(0.75f, 0.75f, 0.75f, 0.2f + opacity));
            winnerText.setAlignment(Align.center);
            winnerText.setWidth(stage.getWidth());
            winnerText.setY(stage.getHeight() / 2);
            stage.addActor(winnerText);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0.9f, 0.9f, 0.9f, opacity);
            shapeRenderer.rect(0, 0, stage.getWidth(), stage.getHeight());
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
//        }

        stage.draw();
        if (winDelay < 0) {
            game.switchToMenuScreen();
        }
    }

    private int getWinningPlayerIndex() {
        final Player player = round.getPlayers().stream().filter(GameCharacter::isAlive).map(GameCharacter::getPlayer).findFirst().get();
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
        final List<Item> gameObjects = round.getGameItems();
        for (Item item : gameObjects) {
            final Coordinates coordinates = item.getCoordinates();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            showItem(item, coordinates);
            if (item instanceof MageWeaponWrapper) {
                final Item weapon = ((MageWeaponWrapper) item).getWeapon();
                showItem(weapon, weapon.getCoordinates());
            }
            shapeRenderer.end();
        }
    }

    private void showItem(Item item, Coordinates coordinates) {
        if (item instanceof Bomb) {
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), 2);
            shapeRenderer.end();
//            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//            shapeRenderer.setColor(Color.RED);
//            shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), ConfigurationConstants.EXPLOSION_RADIUS_SIZE);
        } else if (item instanceof Arrow) {
            // Arrow
            shapeRenderer.setColor(Color.PINK);
            shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), 2);
        } else if (item instanceof Sword) {
            // Sword
            shapeRenderer.setColor(Color.TEAL);
            shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), 2);
        } else if (item instanceof NeutralItem) {
            shapeRenderer.setColor(Color.MAGENTA);
            shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), ConfigurationConstants.ITEM_NEUTRAL_SIZE);
        }
    }

    private void renderCharacters() {
        skinManager.updateTime();
        final List<GameCharacter> characters = round.getCharacters();
//        characters.stream().forEach(
//                c -> {
//                    final Rectangle hitbox = c.getHitbox();
//                    switch (c.getState()) {
//                        case DEAD:
//                            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//                            shapeRenderer.setColor(Color.BLACK);
//                            shapeRenderer.rect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
//                            shapeRenderer.end();
//                            break;
////                        case KNOCK_OUT:
////                            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
////                            shapeRenderer.setColor(Color.LIGHT_GRAY);
////                            shapeRenderer.rect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
////                            shapeRenderer.end();
////                            break;
//                    }
//                }
//        );
        spriteBatch.begin();
        characters.stream()
//                .filter(c -> CharacterState.DEAD != c.getState())
                .sorted((c1, c2) -> Double.compare(c2.getCoordinates().getY(), c1.getCoordinates().getY()))
                .sequential()
                .forEach(this::renderCharacter);
        spriteBatch.end();

    }

    private void renderCharacter(GameCharacter gameCharacter) {
        final Rectangle hitbox = gameCharacter.getHitbox();
        final TextureRegion frame = skinManager.getFrame(gameCharacter);
        spriteBatch.draw(frame, hitbox.getX(), hitbox.getY(),
                (float) (frame.getRegionWidth() * 1.5), (float) (frame.getRegionHeight() * 1.5));
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
        dispose();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        shapeRenderer.dispose();
    }
}
