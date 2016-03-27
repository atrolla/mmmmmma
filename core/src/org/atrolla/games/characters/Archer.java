package org.atrolla.games.characters;

import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.Item;
import org.atrolla.games.items.weapons.Arrow;
import org.atrolla.games.system.Player;

import java.util.Collections;
import java.util.List;

public class Archer extends GameCharacter {

    public Archer(Player player) {
        super(player);
        lives = ConfigurationConstants.ARCHER_LIVES;
    }

    private boolean abilityIsCoolingDown = false;

//    @Override
//    public void moves(int time, Direction direction) {
//        if (!isAbilityCoolingDown(time)) {
//            super.moves(time, direction);
//        } else {
//            super.moves(time, Direction.STOP);
//        }
//    }

    @Override
    public void coolDownAbility(int time) {
        abilityReadyTime = time + ConfigurationConstants.ARCHER_ABILITY_COOLDOWN_DURATION;
    }

    @Override
    public List<Item> useAbility(int time) {
        if (!isAlive() || !isPlayer() || isAbilityCoolingDown(time)) {
            return Collections.emptyList();
        }
        coolDownAbility(time);
        abilityIsCoolingDown = true;
        return Collections.singletonList(new Arrow(this, time));
    }

    @Override
    public  boolean isAbilityCoolingDown(int time) {
        abilityIsCoolingDown = abilityReadyTime != ConfigurationConstants.GAME_CHARACTER_INITIAL_READY_TIME
                && abilityReadyTime >= time;
        return abilityIsCoolingDown;
    }
}
