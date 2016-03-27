package org.atrolla.games.characters;

import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.Item;
import org.atrolla.games.items.weapons.MageSpell;
import org.atrolla.games.system.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.atrolla.games.configuration.ConfigurationConstants.MAGE_SPELL_COUNTDOWN_DURATION;
import static org.atrolla.games.configuration.ConfigurationConstants.MAGE_SPELL_OFFSET;

public class Mage extends GameCharacter {

    public Mage(Player player) {
        super(player);
        lives = ConfigurationConstants.MAGE_LIVES;
    }

    @Override
    public void coolDownAbility(int time) {
        abilityReadyTime = time + ConfigurationConstants.MAGE_ABILITY_COOLDOWN_DURATION;
    }

    @Override
    public List<Item> useAbility(int time) {
        if (!isAlive() || !isPlayer() || isAbilityCoolingDown(time)) {
            return Collections.emptyList();
        }
        coolDownAbility(time);
        final List<Item> items = new ArrayList<>();
        items.add(new MageSpell(this.getCenter().translateXandY(-MAGE_SPELL_OFFSET, -MAGE_SPELL_OFFSET), time + MAGE_SPELL_COUNTDOWN_DURATION, this));
        items.add(new MageSpell(this.getCenter().translateXandY(-MAGE_SPELL_OFFSET, MAGE_SPELL_OFFSET), time + MAGE_SPELL_COUNTDOWN_DURATION, this));
        items.add(new MageSpell(this.getCenter().translateXandY(MAGE_SPELL_OFFSET, -MAGE_SPELL_OFFSET), time + MAGE_SPELL_COUNTDOWN_DURATION, this));
        items.add(new MageSpell(this.getCenter().translateXandY(MAGE_SPELL_OFFSET, MAGE_SPELL_OFFSET), time + MAGE_SPELL_COUNTDOWN_DURATION, this));
        return items;
    }

    @Override
    public boolean isAbilityCoolingDown(int time) {
        return abilityReadyTime >= time;
    }

}
