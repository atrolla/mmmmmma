package org.atrolla.games.desktop.screens.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.desktop.screens.render.skins.CharacterSkinManager;
import org.atrolla.games.game.Round;
import org.atrolla.games.items.Item;
import org.atrolla.games.items.neutrals.Locator;
import org.atrolla.games.items.neutrals.NeutralItem;
import org.atrolla.games.items.weapons.Arrow;
import org.atrolla.games.items.weapons.Bomb;
import org.atrolla.games.items.weapons.MageSpell;
import org.atrolla.games.items.weapons.Sword;
import org.atrolla.games.system.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class RoundRender {

    private static final float ELLIPSE_HEIGHT = (float) ConfigurationConstants.GAME_CHARACTER_HEIGHT / 5;
    private static final float PADDING_SHADOW_WIDTH = 4f;
    private static final float PADDING_SHADOW_HEIGHT = 1f;
    private static final float ELLIPSE_WIDTH = (float) ConfigurationConstants.GAME_CHARACTER_WIDTH - PADDING_SHADOW_WIDTH - PADDING_SHADOW_WIDTH;

    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch spriteBatch;
    private final Round round;
    private final ItemSpriteManager itemSpriteManager;
    private final CharacterSkinManager skinManager;
    private List<ScreenElement> screenElements;
    private boolean demo;


    public RoundRender(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, Round round, ItemSpriteManager itemSpriteManager, CharacterSkinManager skinManager) {
        this.shapeRenderer = shapeRenderer;
        this.spriteBatch = spriteBatch;
        this.round = round;
        this.itemSpriteManager = itemSpriteManager;
        this.skinManager = skinManager;
        this.screenElements = new ArrayList<>();
    }

    public void clearScreenElements() {
        screenElements.clear();
    }

    public void addScreenElement(ScreenElement screenElement) {
        screenElements.add(screenElement);
    }

    public void renderRound() {
        renderCharacters();
        renderItems();
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
            } else if (((NeutralItem) item).mustDisplay()) {
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
        if (demo) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.MAGENTA);
            if (item instanceof Arrow) {
                shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), 3);
            } else if (item instanceof Bomb && !((Bomb) item).isExploding()) {
                shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), 3);
            } else if (item instanceof Sword) {
                shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), 3);
            } else if (item instanceof MageSpell) {
                shapeRenderer.circle((float) coordinates.getX(), (float) coordinates.getY(), ConfigurationConstants.MAGE_SPELL_SIZE);
            }
            shapeRenderer.end();
        }
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
                    if (demo && c.isPlayer()) {
                        shapeRenderer.setColor(0f, 50f, 0f, 0.75f);
                    } else {
                        shapeRenderer.setColor(0f, 0f, 0f, 0.25f);
                    }
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

    public void setDemo(boolean demo) {
        this.demo = demo;
    }

}
