package org.atrolla.games.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.Item;
import org.atrolla.games.items.weapons.Arrow;
import org.atrolla.games.items.weapons.Bomb;
import org.atrolla.games.sounds.SoundManager;
import org.atrolla.games.system.Coordinates;

import java.util.Collection;
import java.util.List;

public class Game extends ApplicationAdapter {
    private SpriteBatch batch;
//    private Texture img;
    private OrthographicCamera camera;
    private Round round;
    private ShapeRenderer shapeRenderer;
    private SoundManager soundManager;

    @Override
    public void create () {
//        img = new Texture("badlogic.jpg");
        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, (float)ConfigurationConstants.STAGE_WIDTH, (float)ConfigurationConstants.STAGE_HEIGHT);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        round = new Round();
        round.setControllers(Controllers.getControllers());
        Array<Input> keyboards = new Array<>(1);
        keyboards.add(Gdx.input);
        round.setKeyboards(keyboards);
        soundManager = new SoundManager();
        round.setSoundManager(soundManager);
    }

    @Override
    public void render () {
        // clear the screen with a dark blue color. The
        // arguments to glClearColor are the red, green
        // blue and alpha component in the range [0,1]
        // of the color to be used to clear the screen.

        Gdx.gl.glClearColor(0.9f, 0.9f, 0.9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the
        // coordinate system specified by the camera.
        batch.setProjectionMatrix(camera.combined);

        // begin a new batch and draw the bucket and
        // all drops
        batch.begin();

        round.update();

        //draw objects...
        renderCharacters();
        //render bombs...
        final List<Item> gameObjects = round.getGameItems();
        for (Item item : gameObjects) {
            final Coordinates coordinates = item.getCoordinates();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            if(item instanceof Bomb){
                shapeRenderer.setColor(Color.RED);
                shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), 3);
            }else if(item instanceof Arrow){
                // Arrow
                shapeRenderer.setColor(Color.PINK);
                shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), 2);
            }else {//if(item instanceof Sword){
                // Arrow
                shapeRenderer.setColor(Color.TEAL);
                shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), 4);
            }
            shapeRenderer.end();
        }

        batch.end();
    }

    private void renderCharacters() {
        final Collection<GameCharacter> gameCharacters = round.getCharacters();
        for (GameCharacter gameCharacter : gameCharacters) {
            final Coordinates coordinates = gameCharacter.getCoordinates();
            final Rectangle hitbox = gameCharacter.getHitbox();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            if(gameCharacter.isPlayer()){
                shapeRenderer.setColor(Color.BLACK);
            }else {
                shapeRenderer.setColor(Color.LIGHT_GRAY);
            }
            shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), 5);
            shapeRenderer.end();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.rect(hitbox.getX(),hitbox.getY(),hitbox.getWidth(),hitbox.getHeight());
            shapeRenderer.end();
        }
    }

    @Override
    public void dispose() {
        // dispose of all the native resources
        batch.dispose();
        shapeRenderer.dispose();
        soundManager.disposeSounds();
    }
}
