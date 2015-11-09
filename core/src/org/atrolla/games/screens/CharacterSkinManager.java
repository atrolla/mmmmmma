package org.atrolla.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.atrolla.games.characters.*;

import java.util.HashMap;
import java.util.Map;

public class CharacterSkinManager {

    /**
     * @see <a href="http://www.spriters-resource.com/playstation_2/makaikingdom/">sprite website</a>
     */

    float stateTime;
    private final Map<Class, CharacterSkin> classSkins;

    public CharacterSkinManager() {
        stateTime = 0f;
        classSkins = new HashMap<>();
        initClassSkins();
    }

    private void initClassSkins() {
        // randomly affect a skin to a class
        classSkins.put(Bomber.class, new BaseSkin(Gdx.files.internal("skins/asagi.png")));
        classSkins.put(Knight.class, new BaseSkin(Gdx.files.internal("skins/etna.png")));
        classSkins.put(Archer.class, new BaseSkin(Gdx.files.internal("skins/blond.png")));
    }

    public void updateTime() {
        stateTime += Gdx.graphics.getDeltaTime();
    }

    public TextureRegion getFrame(GameCharacter gameCharacter) {
        Class<? extends GameCharacter> gameCharacterClass = gameCharacter.getClass();
        if (gameCharacter instanceof Mage) {
            gameCharacterClass = ((Mage) gameCharacter).getDisguisedCharacter().get().getClass();
        }
        final CharacterSkin characterSkin = classSkins.get(gameCharacterClass);
        return characterSkin.getFrame(gameCharacter.getDirection(), gameCharacter.isMoving(), stateTime);
    }

    public TextureRegion getFrame(GameCharacter gameCharacter, Class clazz) {
//        Class<? extends GameCharacter> gameCharacterClass = gameCharacter.getClass();
//        if (gameCharacter instanceof Mage) {
//            gameCharacterClass = ((Mage) gameCharacter).getDisguisedCharacter().get().getClass();
//        }
        final CharacterSkin characterSkin = classSkins.get(clazz);
        return characterSkin.getFrame(gameCharacter.getDirection(), gameCharacter.isMoving(), stateTime);
    }

    public float getStateTime() {
        return stateTime;
    }
}
