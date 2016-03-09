package org.atrolla.games.screens.demo;

import org.atrolla.games.characters.*;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.Item;
import org.atrolla.games.items.weapons.MageSpell;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class MageDemo implements DemoScreen {

    private final Knight victim1;
    private final Archer victim2;
    private final Bomber victim3;
    private final Mage victim4;
    private final Mage mage;
    private float lastUpdate;
    private MageSpell spell;

    public MageDemo(Coordinates coordinates) {
        this.victim1 = new Knight(Player.BOT);
        this.victim2 = new Archer(Player.BOT);
        this.victim3 = new Bomber(Player.BOT);
        this.victim4 = new Mage(Player.BOT);
        this.mage = new Mage(new Player(Optional.empty(), Optional.empty()));
        mage.teleports(coordinates);
        victim1.teleports(coordinates.translateXandY(ConfigurationConstants.ITEM_MAGE_SPELL_OFFSET, ConfigurationConstants.ITEM_MAGE_SPELL_OFFSET));
        victim2.teleports(coordinates.translateXandY(ConfigurationConstants.ITEM_MAGE_SPELL_OFFSET, -ConfigurationConstants.ITEM_MAGE_SPELL_OFFSET));
        victim3.teleports(coordinates.translateXandY(-ConfigurationConstants.ITEM_MAGE_SPELL_OFFSET, ConfigurationConstants.ITEM_MAGE_SPELL_OFFSET));
        victim4.teleports(coordinates.translateXandY(-ConfigurationConstants.ITEM_MAGE_SPELL_OFFSET, -ConfigurationConstants.ITEM_MAGE_SPELL_OFFSET));
    }


    @Override
    public Collection<GameCharacter> getCharacters() {
        return Arrays.asList(mage, victim1, victim2, victim3, victim4);
    }

    @Override
    public Collection<Item> getItems() {
        return spell == null ? Collections.emptyList() : Arrays.asList(spell);
    }

    @Override
    public void update(int time) {
        if (victim1.isAlive()) {
            if (time - lastUpdate > ConfigurationConstants.MAGE_ABILITY_COOLDOWN_DURATION) {
                victim1.hit(mage);
                victim2.hit(mage);
                victim3.hit(mage);
                victim4.hit(mage);
                lastUpdate = time;
            }
        } else {
            if (isCooldownReached(time, ConfigurationConstants.KNOCK_OUT_DURATION)) {
                victim1.awake();
                victim2.awake();
                victim3.awake();
                victim4.awake();
                lastUpdate = time;
            }
        }
    }

    private boolean isCooldownReached(int time, int timeOut) {
        return time - lastUpdate > timeOut;
    }
}
