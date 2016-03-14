package org.atrolla.games.screens.demo;

import org.atrolla.games.characters.Bomber;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.characters.Knight;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.Item;
import org.atrolla.games.items.weapons.Bomb;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Direction;
import org.atrolla.games.system.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class BomberDemo implements DemoScreen {

    private final Knight victim;
    private final Bomber bomber;
    private float lastUpdate;
    private Bomb bomb;
    private boolean mustGoDown = true;

    public BomberDemo(Coordinates coordinates) {
        this.victim = new Knight(Player.BOT);
        this.bomber = new Bomber(new Player(Optional.empty(), Optional.empty()));
        bomber.teleports(coordinates);
        victim.teleports(coordinates.translateXandY(0, -100));
    }


    @Override
    public Collection<GameCharacter> getCharacters() {
        return Arrays.asList(bomber, victim);
    }

    @Override
    public Collection<Item> getItems() {
        return bomb == null ? Collections.emptyList() : Arrays.asList(bomb);
    }

    @Override
    public void update(int time) {
        if (victim.isAlive()) {
            if (canExplodeVictim() && mustGoDown) {
                bomber.moves(time, Direction.DOWN);
                lastUpdate = time;
            } else {
                if (isCooldownReached(time, ConfigurationConstants.BOMB_COUNTDOWN_DURATION)) {
                    victim.hit(bomber);
                    lastUpdate = time;
                    bomb.update(time);
                } else {
                    if (isOutsideExplosionRadius()) {
                        if (bomb == null) {
                            bomb = new Bomb(bomber, time - ConfigurationConstants.BOMB_COUNTDOWN_DURATION - 10);
                        }
                        bomber.moves(time, Direction.UP);
                        mustGoDown = false;
                    } else {
                        bomber.moves(time, Direction.STOP);
                    }
                }
            }
        } else {
            bomb = null;
            if (isCooldownReached(time, ConfigurationConstants.BOMBER_ABILITY_COOLDOWN_DURATION)) {
                victim.awake();
                mustGoDown = true;
            }
        }
    }

    private boolean isOutsideExplosionRadius() {
        return bomber.getCenter().getY() - victim.getCenter().getY() - ConfigurationConstants.GAME_CHARACTER_HEIGHT <= ConfigurationConstants.BOMB_EXPLOSION_RADIUS_SIZE * 2 + 1;
    }


    private boolean isCooldownReached(int time, int timeOut) {
        return time - lastUpdate > timeOut;
    }

    private boolean canExplodeVictim() {
        return bomber.getCoordinates().getY() - victim.getCoordinates().getY() > ConfigurationConstants.BOMB_EXPLOSION_RADIUS_SIZE;
    }
}
