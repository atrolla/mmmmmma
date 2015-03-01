package org.atrolla.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.atrolla.game.characters.GameCharacter;
import org.atrolla.game.configuration.ConfigurationConstants;
import org.atrolla.game.engine.Coordinates;
import org.atrolla.game.engine.Game;

import java.util.Collection;

public class MmmmmmaGame extends ApplicationAdapter {
    private SpriteBatch batch;
//    private Texture img;
    private OrthographicCamera camera;
    private Game game;
    private ShapeRenderer shapeRenderer;


    @Override
	public void create () {
//		img = new Texture("badlogic.jpg");

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, (float)ConfigurationConstants.STAGE_WIDTH, (float)ConfigurationConstants.STAGE_HEIGHT);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        game = new Game();
        game.setControllers(Controllers.getControllers());
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

        game.update();

        //draw objects...
        final Collection<GameCharacter> gameCharacters = game.getCharacters();
        for (GameCharacter gameCharacter : gameCharacters) {
            final Coordinates coordinates = gameCharacter.getCoordinates();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle((float) coordinates.getX(), (float)coordinates.getY(), 5);
            // TODO : why is it not me ???
            if(gameCharacter.isPlayer()){
                shapeRenderer.setColor(Color.GREEN);
            }else {
                shapeRenderer.setColor(Color.BLACK);
            }
            shapeRenderer.end();
        }

        batch.end();

        // process user input


	}

    @Override
    public void dispose() {
        // dispose of all the native resources
        batch.dispose();
        shapeRenderer.dispose();
    }
}
