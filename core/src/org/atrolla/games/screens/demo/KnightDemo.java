package org.atrolla.games.screens.demo;

import org.atrolla.games.characters.Archer;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.characters.Knight;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class KnightDemo implements DemoScreen {

    private final Archer victim;
    private final Knight knight;
    private float lastUpdate;

    public KnightDemo(Coordinates coordinates) {
        this.victim = new Archer(Player.BOT);
        this.knight = new Knight(new Player(Optional.empty(), Optional.empty()));
        knight.teleports(coordinates);
        victim.teleports(coordinates.translateXandY(18, -8));
    }


    @Override
    public Collection<GameCharacter> getCharacters() {
        return Arrays.asList(knight, victim);
    }

    @Override
    public void update(int time) {
        if (time - lastUpdate > ConfigurationConstants.KNIGHT_ABILITY_COOLDOWN_DURATION) {
            if (victim.isAlive()) {
                victim.hit(knight);
            } else {
                victim.awake();
            }
            lastUpdate = time;
        }
    }
}
