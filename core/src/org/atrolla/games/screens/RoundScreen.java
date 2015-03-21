package org.atrolla.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.game.Mmmmmma;
import org.atrolla.games.game.Round;
import org.atrolla.games.items.Item;
import org.atrolla.games.items.neutrals.NeutralItem;
import org.atrolla.games.items.weapons.Arrow;
import org.atrolla.games.items.weapons.Bomb;
import org.atrolla.games.items.weapons.Sword;
import org.atrolla.games.system.Coordinates;

import java.util.Collection;
import java.util.List;

/**
 * Created by MicroOnde on 21/03/2015.
 */
public class RoundScreen implements Screen {

    private final Mmmmmma game;
    private Round round;

    private ShapeRenderer shapeRenderer;

    public RoundScreen(Mmmmmma game) {
        this.game = game;
        round = new Round();
        round.setControllers(Controllers.getControllers());
        Array<Input> keyboards = new Array<>(1);
        keyboards.add(Gdx.input);
        round.setKeyboards(keyboards);
        round.setSoundManager(game.getSoundManager());
        shapeRenderer = new ShapeRenderer();
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
        //render bombs...
        final List<Item> gameObjects = round.getGameItems();
        for (Item item : gameObjects) {
            final Coordinates coordinates = item.getCoordinates();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            if (item instanceof Bomb) {
                shapeRenderer.setColor(Color.RED);
                shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), 3);
            } else if (item instanceof Arrow) {
                // Arrow
                shapeRenderer.setColor(Color.PINK);
                shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), 2);
            } else if (item instanceof Sword) {
                // Arrow
                shapeRenderer.setColor(Color.TEAL);
                shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), 4);
            } else if (item instanceof NeutralItem) {
                shapeRenderer.setColor(Color.MAGENTA);
                shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), ConfigurationConstants.ITEM_NEUTRAL_SIZE);
            }
            shapeRenderer.end();
        }
    }

    private void renderCharacters() {
        final Collection<GameCharacter> gameCharacters = round.getCharacters();
        for (GameCharacter gameCharacter : gameCharacters) {
            final Coordinates coordinates = gameCharacter.getCoordinates();
            final Rectangle hitbox = gameCharacter.getHitbox();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            if (gameCharacter.isPlayer()) {
                shapeRenderer.setColor(Color.BLACK);
            } else {
                shapeRenderer.setColor(Color.LIGHT_GRAY);
            }
            shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), 5);
            shapeRenderer.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.rect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
            shapeRenderer.end();
        }
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
        shapeRenderer.dispose();
    }
}
