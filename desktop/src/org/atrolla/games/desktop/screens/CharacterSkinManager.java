package org.atrolla.games.desktop.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.atrolla.games.characters.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

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
        List<BaseSkin> baseSkinList = new ArrayList<>();
        baseSkinList.add(new BaseSkin(Gdx.files.internal("skins/asagi.png")));
        baseSkinList.add(new BaseSkin(Gdx.files.internal("skins/etna.png")));
        baseSkinList.add(new BaseSkin(Gdx.files.internal("skins/castille.png")));
        baseSkinList.add(new BaseSkin(Gdx.files.internal("skins/flonne.png")));
        baseSkinList.add(new BaseSkin(Gdx.files.internal("skins/archer.png")));
        baseSkinList.add(new BaseSkin(Gdx.files.internal("skins/priest.png")));
        baseSkinList.add(new BaseSkin(Gdx.files.internal("skins/warrior.png")));
        baseSkinList.add(new BaseSkin(Gdx.files.internal("skins/swordman.png")));
        baseSkinList.add(new BaseSkin(Gdx.files.internal("skins/babylon.png")));

        classSkins.put(Bomber.class, getRandomSkin(baseSkinList));
        classSkins.put(Knight.class, getRandomSkin(baseSkinList));
        classSkins.put(Archer.class, getRandomSkin(baseSkinList));

    }

    private CharacterSkin getRandomSkin(List<BaseSkin> baseSkinList) {
        final int i = ThreadLocalRandom.current().nextInt(0, baseSkinList.size());
        return baseSkinList.remove(i);
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
        return characterSkin.getFrame(gameCharacter, stateTime);
    }

    public float getStateTime() {
        return stateTime;
    }
}
