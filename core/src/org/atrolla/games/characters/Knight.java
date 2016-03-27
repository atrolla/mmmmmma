package org.atrolla.games.characters;

import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.Item;
import org.atrolla.games.items.weapons.Sword;
import org.atrolla.games.system.Player;

import java.util.Collections;
import java.util.List;

public class Knight extends GameCharacter {
    public Knight(Player player) {
        super(player);
        lives = ConfigurationConstants.KNIGHT_LIVES;
    }

    @Override
    public void coolDownAbility(int time) {
        abilityReadyTime = time + ConfigurationConstants.KNIGHT_ABILITY_COOLDOWN_DURATION;
    }

    @Override
    public List<Item> useAbility(int time) {
        if (!isAlive() || !isPlayer() || isAbilityCoolingDown(time)) {
            return Collections.emptyList();
        }
        coolDownAbility(time);
        return Collections.singletonList(Sword.generates(this, time));
    }

    @Override
    public boolean isAbilityCoolingDown(int time) {
        return abilityReadyTime >= time;
    }
}
