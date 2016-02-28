package org.atrolla.games.desktop.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.Item;
import org.atrolla.games.items.weapons.Bomb;
import org.atrolla.games.system.Coordinates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MicroOnde on 06/02/2016.
 */
public class ItemSpriteManager {

    public static final int RADIUS_SIZE = ConfigurationConstants.EXPLOSION_RADIUS_SIZE * 4;
    public static final int EXPLOSION_SIZE = 128;
    public static final int OFFSET_EXPLOSION = RADIUS_SIZE / 2;
    private Animation explosion;
    public static final float EXPLOSION_FRAME_DURATION = 0.02f;
    private final Map<ItemWithCoordinates, Float> itemAnimationProgress;

    public ItemSpriteManager() {
        itemAnimationProgress = new HashMap<>();
        buildExplosionAnimation();
    }

    private void buildExplosionAnimation() {
        Texture walkSheet = new Texture(Gdx.files.internal("effects/explosion03.png"));
        TextureRegion[] regions = new TextureRegion[16];
        int k = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                regions[k++] = new TextureRegion(walkSheet, (EXPLOSION_SIZE * j), (EXPLOSION_SIZE * i) - 1, EXPLOSION_SIZE, EXPLOSION_SIZE);
            }
        }
        explosion = new Animation(EXPLOSION_FRAME_DURATION, regions);
    }

    public void render(SpriteBatch spriteBatch) {
        List<ItemWithCoordinates> itemToRemove = new ArrayList<>();
        itemAnimationProgress.entrySet().stream().forEach(
                itemFloatEntry -> {
                    final ItemWithCoordinates key = itemFloatEntry.getKey();
                    if (key.isType(Bomb.class)) {
                        final Coordinates coordinates = key.coordinates;
                        final Float value = itemFloatEntry.getValue();
                        final TextureRegion keyFrame = explosion.getKeyFrame(value, false);
                        if (keyFrame != null) {
                            spriteBatch.draw(keyFrame,
                                    (float) coordinates.getX() - OFFSET_EXPLOSION, (float) coordinates.getY() - OFFSET_EXPLOSION,
                                    RADIUS_SIZE, RADIUS_SIZE);
                            itemFloatEntry.setValue(value + Gdx.graphics.getDeltaTime());
                        } else {
                            itemToRemove.add(key);
                        }
                    }
                }
        );
        itemToRemove.forEach(itemAnimationProgress::remove);

    }

    public void makeExplosion(Bomb bomb, Coordinates coordinates) {
        itemAnimationProgress.put(new ItemWithCoordinates(bomb, coordinates), 0f);
    }

    private static class ItemWithCoordinates {
        Item item;
        Coordinates coordinates;

        public ItemWithCoordinates(Item item, Coordinates coordinates) {
            this.item = item;
            this.coordinates = coordinates;
        }

        boolean isType(Class c) {
            return c.isAssignableFrom(item.getClass());
        }
    }
}
