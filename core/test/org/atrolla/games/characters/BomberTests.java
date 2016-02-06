package org.atrolla.games.characters;

import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.weapons.Bomb;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by MicroOnde on 04/03/2015.
 */
public class BomberTests {

    public static final int ABILITY_USE_TIME = 0;
    private Bomber bomberPlayer;

    @Before
    public void setUp() throws Exception {
        bomberPlayer = new Bomber(new Player(Optional.empty(), Optional.empty()));
    }

    @Test
    public void bomberBotCannotUseHisAbility() throws Exception {
        Bomber bomberBot = new Bomber(Player.BOT);
        assertThat(bomberBot.useAbility(ABILITY_USE_TIME)).isEmpty();
    }

    @Test
    public void bomberAbilityIsToCreateBomb() throws Exception {
        assertThat(bomberPlayer.useAbility(ABILITY_USE_TIME).get()).isInstanceOf(Bomb.class);
    }

    @Test
    public void bombIsCreatedAtCurrentBomberCenter() throws Exception {
        final Coordinates coordinates = new Coordinates(42, 1337);
        bomberPlayer.teleports(coordinates);
        final Bomb bomb = (Bomb) bomberPlayer.useAbility(ABILITY_USE_TIME).get();
        assertThat(bomb.getCoordinates()).isEqualTo(bomberPlayer.getCenter());
    }

    @Test
    public void bomberCanOnlyPlaceOneBomb() throws Exception {
        assertThat(bomberPlayer.useAbility(ABILITY_USE_TIME).get()).isInstanceOf(Bomb.class);
        assertThat(bomberPlayer.useAbility(ABILITY_USE_TIME).isPresent()).isFalse();
    }

    @Test
    public void bomberCanReplaceBombOnlyAfterItsCooldownTime() throws Exception {
        assertThat(bomberPlayer.useAbility(ABILITY_USE_TIME).get()).isInstanceOf(Bomb.class);
        assertThat(bomberPlayer.useAbility(ABILITY_USE_TIME + ConfigurationConstants.BOMBER_ABILITY_COOLDOWN_DURATION).isPresent()).isFalse();
        assertThat(bomberPlayer.useAbility(ABILITY_USE_TIME +1 + ConfigurationConstants.BOMBER_ABILITY_COOLDOWN_DURATION).get()).isInstanceOf(Bomb.class);
    }
}
