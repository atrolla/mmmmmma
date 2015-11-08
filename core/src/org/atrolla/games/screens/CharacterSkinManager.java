package org.atrolla.games.screens;

import org.atrolla.games.characters.Bomber;

/**
 * Created by MicroOnde on 08/11/2015.
 */
public class CharacterSkinManager {

    private final CharacterSkin bomberSkin;

    public CharacterSkinManager() {
        this.bomberSkin = new BomberSkin();
    }

    public CharacterSkin getSkin(Class clazz){
        if(Bomber.class.isAssignableFrom(clazz)){
            return bomberSkin;
        }
        return bomberSkin;
    }
}
