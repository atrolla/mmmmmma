package org.atrolla.games.characters;

import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.Item;
import org.atrolla.games.items.weapons.MageSpell;
import org.atrolla.games.system.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import static org.atrolla.games.configuration.ConfigurationConstants.MAGE_SPELL_COUNTDOWN_DURATION;
import static org.atrolla.games.configuration.ConfigurationConstants.MAGE_SPELL_OFFSET;

public class Mage extends GameCharacter {

    public Mage(Player player) {
        super(player);
    }

    @Override
    public void coolDownAbility(int time) {
        abilityReadyTime = time + ConfigurationConstants.MAGE_ABILITY_COOLDOWN_DURATION;
    }

    @Override
    public Collection<Item> useAbility(int time) {
        if (!isAlive() || !isPlayer() || abilityIsCoolingDown(time)) {
            return Collections.emptySet();
        }
        coolDownAbility(time);
        final HashSet<Item> items = new HashSet<>();
        items.add(new MageSpell(this.getCenter().translateXandY(-MAGE_SPELL_OFFSET, -MAGE_SPELL_OFFSET), time + MAGE_SPELL_COUNTDOWN_DURATION, this));
        items.add(new MageSpell(this.getCenter().translateXandY(-MAGE_SPELL_OFFSET, MAGE_SPELL_OFFSET), time + MAGE_SPELL_COUNTDOWN_DURATION, this));
        items.add(new MageSpell(this.getCenter().translateXandY(MAGE_SPELL_OFFSET, -MAGE_SPELL_OFFSET), time + MAGE_SPELL_COUNTDOWN_DURATION, this));
        items.add(new MageSpell(this.getCenter().translateXandY(MAGE_SPELL_OFFSET, MAGE_SPELL_OFFSET), time + MAGE_SPELL_COUNTDOWN_DURATION, this));
        return items;
    }

    private boolean abilityIsCoolingDown(int time) {
        return abilityReadyTime >= time;
    }

}
