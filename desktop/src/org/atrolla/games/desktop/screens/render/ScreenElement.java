package org.atrolla.games.desktop.screens.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ScreenElement {

    public final TextureRegion textureRegion;
    public final float x;
    public final float y;
    public final float width;
    public final float height;

    public ScreenElement(TextureRegion textureRegion, float x, float y, float width, float height) {
        this.textureRegion = new TextureRegion(textureRegion);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public ScreenElement(TextureRegion textureRegion, float x, float y) {
        this.textureRegion = textureRegion;
        this.x = x;
        this.y = y;
        this.width = 0;
        this.height = 0;
    }

    public void draw(final SpriteBatch batch) {
        if (width == 0) {
            batch.draw(textureRegion, x, y);
        }
        batch.draw(textureRegion, x, y, width, height);
    }
}
