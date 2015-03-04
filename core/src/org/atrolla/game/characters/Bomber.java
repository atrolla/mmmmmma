package org.atrolla.game.characters;

import org.atrolla.game.engine.Player;
import org.atrolla.game.weapons.Bomb;

/**
 * Created by MicroOnde on 24/02/2015.
 */
public class Bomber extends GameCharacter {

    boolean abilityIsCoolingDown;

    public Bomber(Player player) {
        super(player);
        abilityIsCoolingDown = false;
    }

    public Bomb useAbility() {
        if(!isPlayer() || abilityIsCoolingDown){
            return null;
        }
        abilityIsCoolingDown = true;
        return new Bomb(getCoordinates());
    }


    public void setReadyToUseAbility() {
        abilityIsCoolingDown = false;
    }
}
