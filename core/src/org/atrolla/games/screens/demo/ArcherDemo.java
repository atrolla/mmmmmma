package org.atrolla.games.screens.demo;

import org.atrolla.games.characters.Archer;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.characters.Knight;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.Item;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class ArcherDemo implements DemoScreen {

    private final Knight victim;
    private final Archer archer;
    private float lastUpdate;

    public ArcherDemo(Coordinates coordinates) {
        this.victim = new Knight(Player.BOT);
        this.archer = new Archer(new Player(Optional.empty(), Optional.empty()));
        archer.teleports(coordinates);
        victim.teleports(coordinates.translateXandY(0, -ConfigurationConstants.ITEM_ARROW_RANGE_TIME_OUT * ConfigurationConstants.ITEM_ARROW_MOVE_STEP + 1));
    }


    @Override
    public Collection<GameCharacter> getCharacters() {
        return Arrays.asList(archer, victim);
    }

    @Override
    public Collection<Item> getItems() {
        return Collections.emptyList();
    }

    @Override
    public void update(int time) {
        if (victim.isAlive()) {
            if (time - lastUpdate > ConfigurationConstants.ARCHER_ABILITY_COOLDOWN_DURATION) {
                victim.hit(archer);
                lastUpdate = time;
            }
        } else {
            if (time - lastUpdate > ConfigurationConstants.KNOCK_OUT_DURATION) {
                victim.awake();
                lastUpdate = time;
            }
        }
    }
}
