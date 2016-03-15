package org.atrolla.games.characters;

import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.Item;
import org.atrolla.games.items.weapons.Arrow;
import org.atrolla.games.system.Direction;
import org.atrolla.games.system.Player;

import java.util.Collection;
import java.util.Collections;

public class Archer extends GameCharacter {

    public Archer(Player player) {
        super(player);
        lives = ConfigurationConstants.ARCHER_LIVES;
    }

    private boolean abilityIsCoolingDown = false;

    @Override
    public void moves(int time, Direction direction) {
        abilityIsCoolingDown = abilityReadyTime >= time;
        if(!abilityIsCoolingDown) {
            super.moves(time, direction);
        }
    }

    @Override
    public void coolDownAbility(int time) {
        abilityReadyTime = time + ConfigurationConstants.ARCHER_ABILITY_COOLDOWN_DURATION;
    }

    @Override
    public Collection<Item> useAbility(int time) {
        if (!isAlive() || !isPlayer() || abilityIsCoolingDown(time)) {
            return Collections.emptySet();
        }
        coolDownAbility(time);
        abilityIsCoolingDown = true;
        return Collections.singleton(new Arrow(this, time));
    }

    private boolean abilityIsCoolingDown(int time){
        return abilityIsCoolingDown;
    }
}
