package org.atrolla.games.characters;

import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.Item;
import org.atrolla.games.items.weapons.Bomb;
import org.atrolla.games.system.Player;

import java.util.Collections;
import java.util.List;

public class Bomber extends GameCharacter {

    public Bomber(Player player) {
        super(player);
        lives = ConfigurationConstants.BOMBER_LIVES;
    }

    @Override
    public void coolDownAbility(int time) {
        abilityReadyTime = time + ConfigurationConstants.BOMBER_ABILITY_COOLDOWN_DURATION;
    }

    @Override
    public List<Item> useAbility(int currentTime) {
        if (!isAlive() || !isPlayer() || isAbilityCoolingDown(currentTime)) {
            return Collections.emptyList();
        }
        coolDownAbility(currentTime);
        return Collections.singletonList(new Bomb(this, currentTime));
    }

    @Override
    public boolean isAbilityCoolingDown(int time){
        return abilityReadyTime >= time;
    }
}
