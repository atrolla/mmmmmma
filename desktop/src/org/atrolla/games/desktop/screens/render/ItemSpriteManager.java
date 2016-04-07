package org.atrolla.games.desktop.screens.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.Item;
import org.atrolla.games.items.neutrals.Locator;
import org.atrolla.games.items.neutrals.Switch;
import org.atrolla.games.items.weapons.Bomb;
import org.atrolla.games.system.Coordinates;

import java.util.*;

/**
 * Created by MicroOnde on 06/02/2016.
 */
public class ItemSpriteManager {

    public static final int RADIUS_SIZE = ConfigurationConstants.BOMB_EXPLOSION_RADIUS_SIZE * 4;
    public static final int EXPLOSION_SIZE = 128;
    public static final int OFFSET_EXPLOSION = RADIUS_SIZE / 2;
    private Animation explosion;
    private Animation locator;
    public static final float EXPLOSION_FRAME_DURATION = 0.02f;
    private final Map<ItemWithCoordinates, Float> itemAnimationProgress;
    private final Set<Item> itemsToPick;
    private Sprite locatorItem;
    private Sprite switchItem;

    public ItemSpriteManager() {
        itemAnimationProgress = new HashMap<>();
        itemsToPick = new HashSet<>();
        explosion = buildAnimation("effects/Explosion03.png", EXPLOSION_FRAME_DURATION);
        locator = buildAnimation("effects/Effect95.png", 0.1f);
        switchItem = new Sprite(new Texture(Gdx.files.internal("effects/switch.png")));
        locatorItem = new Sprite(new Texture(Gdx.files.internal("effects/locator.png")));
    }

    private Animation buildAnimation(String path, float duration) {
        Texture walkSheet = new Texture(Gdx.files.internal(path));
        TextureRegion[] regions = new TextureRegion[16];
        int k = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                regions[k++] = new TextureRegion(walkSheet, (EXPLOSION_SIZE * j), (EXPLOSION_SIZE * i) - 1, EXPLOSION_SIZE, EXPLOSION_SIZE);
            }
        }
        return new Animation(duration, regions);
    }

    public void render(SpriteBatch spriteBatch) {
        List<ItemWithCoordinates> itemToRemove = new ArrayList<>();
        itemAnimationProgress.entrySet().stream().forEach(
                itemFloatEntry -> {
                    final ItemWithCoordinates key = itemFloatEntry.getKey();
                    if (key.isType(Bomb.class)) {
                        renderBombExplosion(spriteBatch, itemToRemove, itemFloatEntry, key);
                    } else if (key.isType(Locator.class)) {
                        renderLocatorExplosion(spriteBatch, itemToRemove, itemFloatEntry, key);
                    }
                }
        );
        itemsToPick.forEach(i -> {
            Sprite sprite = null;
            final Coordinates coordinates = i.getCoordinates();
            if (i instanceof Switch) {
                sprite = switchItem;
            } else if (i instanceof Locator) {
                sprite = locatorItem;
            }
            sprite.setPosition((float) coordinates.getX() - 32, (float) coordinates.getY() - 32);
            sprite.setScale(0.75f);
            sprite.setColor(Color.MAGENTA);
            sprite.draw(spriteBatch);
        });
        itemsToPick.clear();
        itemToRemove.forEach(itemAnimationProgress::remove);
    }

    private void renderLocatorExplosion(SpriteBatch spriteBatch, List<ItemWithCoordinates> itemToRemove, Map.Entry<ItemWithCoordinates, Float> itemFloatEntry, ItemWithCoordinates key) {
        final Coordinates coordinates = key.coordinates;
        final Float value = itemFloatEntry.getValue();
        final TextureRegion keyFrame = locator.getKeyFrame(value, false);
        if (keyFrame != null) {
            spriteBatch.draw(keyFrame,
                    (float) coordinates.getX() - EXPLOSION_SIZE / 2, (float) coordinates.getY() - EXPLOSION_SIZE / 2);
            itemFloatEntry.setValue(value + Gdx.graphics.getDeltaTime());
        } else {
            itemToRemove.add(key);
        }
    }

    private void renderBombExplosion(SpriteBatch spriteBatch, List<ItemWithCoordinates> itemToRemove, Map.Entry<ItemWithCoordinates, Float> itemFloatEntry, ItemWithCoordinates key) {
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

    public void registerAnimation(Item item, Coordinates coordinates) {
        if (coordinates != null) {
            itemAnimationProgress.put(new ItemWithCoordinates(item, coordinates), 0f);
        }
    }

    public void registerItem(Item item) {
        itemsToPick.add(item);
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
