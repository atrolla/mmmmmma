package org.atrolla.games.desktop.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.atrolla.games.characters.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class CharacterSkinManager {

    /**
     * @see <a href="http://www.spriters-resource.com/playstation_2/makaikingdom/">sprite website</a>
     */

    float stateTime;
    float lastSkinAssignTime;
    private final Map<Class, CharacterSkin> classSkins;
    private Collection<BaseSkin> baseSkins;

    public CharacterSkinManager() {
        stateTime = 0f;
        lastSkinAssignTime = 0f;
        classSkins = new HashMap<>();
        initClassSkins();
    }

    private void initClassSkins() {
        baseSkins = new ArrayList<>();
        baseSkins.add(new BaseSkin(Gdx.files.internal("skins/asagi.png")));
        baseSkins.add(new BaseSkin(Gdx.files.internal("skins/etna.png")));
        baseSkins.add(new BaseSkin(Gdx.files.internal("skins/castille.png")));
        baseSkins.add(new BaseSkin(Gdx.files.internal("skins/flonne.png")));
        baseSkins.add(new BaseSkin(Gdx.files.internal("skins/archer.png")));
        baseSkins.add(new BaseSkin(Gdx.files.internal("skins/priest.png")));
        baseSkins.add(new BaseSkin(Gdx.files.internal("skins/warrior.png")));
        baseSkins.add(new BaseSkin(Gdx.files.internal("skins/swordman.png")));
        baseSkins.add(new BaseSkin(Gdx.files.internal("skins/babylon.png")));
        assignSkins();
    }

    public void assignSkins() {
        if (classSkins.isEmpty() || stateTime - lastSkinAssignTime > 3) {
            lastSkinAssignTime = stateTime;
            List<BaseSkin> baseSkinList = new ArrayList<>(baseSkins);
            classSkins.put(Bomber.class, getRandomSkin(baseSkinList));
            classSkins.put(Knight.class, getRandomSkin(baseSkinList));
            classSkins.put(Archer.class, getRandomSkin(baseSkinList));
            classSkins.put(Mage.class, getRandomSkin(baseSkinList));
        }
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
        final CharacterSkin characterSkin = classSkins.get(gameCharacterClass);
        return characterSkin.getFrame(gameCharacter, stateTime);
    }

    public float getStateTime() {
        return stateTime;
    }
}
