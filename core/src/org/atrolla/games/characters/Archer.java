package org.atrolla.games.characters;

import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.Item;
import org.atrolla.games.items.weapons.Arrow;
import org.atrolla.games.system.Direction;
import org.atrolla.games.system.Player;

import java.util.Optional;

public class Archer extends GameCharacter {

    public Archer(Player player) {
        super(player);
    }

    private boolean abilityIsCoolingDown = false;

    @Override
    public void moves(int time, Direction direction) {
        if (!abilityIsCoolingDown(time)) {
            super.moves(time, direction);
        } else {
            super.moves(time, Direction.STOP);
        }
    }

    @Override
    public void coolDownAbility(int time) {
        abilityReadyTime = time + ConfigurationConstants.ARCHER_ABILITY_COOLDOWN_DURATION;
    }

    @Override
    public Optional<Item> useAbility(int time) {
        if (!isAlive() || !isPlayer() || abilityIsCoolingDown(time)) {
            return Optional.empty();
        }
        coolDownAbility(time);
        abilityIsCoolingDown = true;
        return Optional.of(new Arrow(this, time));
    }

    private boolean abilityIsCoolingDown(int time) {
        abilityIsCoolingDown = abilityReadyTime != ConfigurationConstants.GAME_CHARACTER_INITIAL_READY_TIME
                && abilityReadyTime >= time;
        return abilityIsCoolingDown;
    }
}
