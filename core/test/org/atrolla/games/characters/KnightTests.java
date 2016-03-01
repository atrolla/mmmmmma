package org.atrolla.games.characters;

import org.atrolla.games.items.weapons.Sword;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by MicroOnde on 15/03/2015.
 */
public class KnightTests {

    private static final int ABILITY_USE_TIME = 0;
    private Knight knightPlayer;

    @Before
    public void setUp() throws Exception {
        this.knightPlayer = new Knight(new Player(Optional.empty(), Optional.empty()));
        knightPlayer.teleports(new Coordinates(42,1337));
    }

    @Test
    public void knightBotCannotUseHisAbility() throws Exception {
        Knight knightBot = new Knight(Player.BOT);
        assertThat(knightBot.useAbility(ABILITY_USE_TIME)).isEmpty();
    }

    @Test
    public void knightAbilityIsToUseHisSword() throws Exception {
        assertThat(knightPlayer.useAbility(ABILITY_USE_TIME).iterator().next()).isInstanceOf(Sword.class);
    }
}
