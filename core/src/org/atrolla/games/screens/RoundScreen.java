package org.atrolla.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import org.atrolla.games.characters.CharacterState;
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

import java.util.List;

/**
 * Created by MicroOnde on 21/03/2015.
 */
public class RoundScreen implements Screen {

    private final Mmmmmma game;
    private final Round round;

    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch spriteBatch;
    private final CharacterSkinManager skinManager;


    public RoundScreen(Mmmmmma game) {
        this.game = game;
        round = new Round(game.getInputManager(), game.getSoundManager());
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        skinManager = new CharacterSkinManager();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        round.update();

        //draw objects...
        renderCharacters();
        //render items...
        renderItems();

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
            shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), 3);
            shapeRenderer.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), ConfigurationConstants.EXPLOSION_RADIUS_SIZE);
        } else if (item instanceof Arrow) {
            // Arrow
            shapeRenderer.setColor(Color.PINK);
            shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), 2);
        } else if (item instanceof Sword) {
            // Sword
            shapeRenderer.setColor(Color.TEAL);
            shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), 4);
        } else if (item instanceof NeutralItem) {
            shapeRenderer.setColor(Color.MAGENTA);
            shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), ConfigurationConstants.ITEM_NEUTRAL_SIZE);
        }
    }

    private void renderCharacters() {
        skinManager.updateTime();
        final List<GameCharacter> characters = round.getCharacters();
        spriteBatch.begin();
        characters.stream()
                .filter(c -> CharacterState.ALIVE == c.getState())
                .sorted((c1, c2) -> Double.compare(c2.getCoordinates().getY(), c1.getCoordinates().getY()))
                .sequential()
                .forEach(this::renderCharacter);
        spriteBatch.end();
        characters.stream().forEach(
                c -> {
                    final Rectangle hitbox = c.getHitbox();
                    switch (c.getState()) {
                        case DEAD:
                            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                            shapeRenderer.setColor(Color.BLACK);
                            shapeRenderer.rect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
                            shapeRenderer.end();
                            break;
                        case KNOCK_OUT:
                            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                            shapeRenderer.setColor(Color.LIGHT_GRAY);
                            shapeRenderer.rect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
                            shapeRenderer.end();
                            break;
                    }
                }
        );
    }

    private void renderCharacter(GameCharacter gameCharacter) {
        /*debug mode*/
//        final Coordinates coordinates = gameCharacter.getCoordinates();
        final Rectangle hitbox = gameCharacter.getHitbox();

//        if (gameCharacter instanceof Archer) {
//            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//            if (gameCharacter.isPlayer()) {
//                shapeRenderer.setColor(Color.BLACK);
//            } else {
//                shapeRenderer.setColor(Color.LIGHT_GRAY);
//            }
//            shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), 5);
//            shapeRenderer.end();
//            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//            shapeRenderer.rect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
//            shapeRenderer.end();
//        } else if (gameCharacter instanceof Bomber) {
////            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
////            if (gameCharacter.isPlayer()) {
////                shapeRenderer.setColor(Color.BLACK);
////            } else {
////                shapeRenderer.setColor(Color.LIGHT_GRAY);
////            }
////            shapeRenderer.rect((float) coordinates.getX(), (float) coordinates.getY(), 10, 10);
////            shapeRenderer.end();
////            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
////            shapeRenderer.rect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
////            shapeRenderer.end();
//            final TextureRegion frame = skinManager.getFrame(gameCharacter);
//            spriteBatch.draw(frame, hitbox.getX(), hitbox.getY());
//        } else if (gameCharacter instanceof Knight) {
//            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//            if (gameCharacter.isPlayer()) {
//                shapeRenderer.setColor(Color.BLACK);
//            } else {
//                shapeRenderer.setColor(Color.LIGHT_GRAY);
//            }
//            shapeRenderer.triangle((float) coordinates.getX(), (float) coordinates.getY(),
//                    (float) coordinates.getX() + 10, (float) coordinates.getY(),
//                    (float) coordinates.getX() + 5, (float) coordinates.getY() + 10);
//            shapeRenderer.end();
//            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//            shapeRenderer.rect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
//            shapeRenderer.end();
//
//        } else
//        if (gameCharacter instanceof Mage) {
        final TextureRegion frame = skinManager.getFrame(gameCharacter);
        spriteBatch.draw(frame, hitbox.getX(), hitbox.getY(),
                (float) (frame.getRegionWidth() * 1.2), (float) (frame.getRegionHeight() * 1.2));
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

    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        shapeRenderer.dispose();
    }
}
