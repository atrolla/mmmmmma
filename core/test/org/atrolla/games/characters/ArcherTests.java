package org.atrolla.games.characters;

import org.atrolla.games.items.Item;
import org.atrolla.games.items.weapons.Arrow;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Direction;
import org.atrolla.games.system.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by MicroOnde on 14/03/2015.
 */
public class ArcherTests {

    public static final int ABILITY_USE_TIME = 0;
    private Archer archerPlayer;

    @Before
    public void setUp() throws Exception {
        this.archerPlayer = new Archer(new Player(Optional.empty(), Optional.empty()));
    }

    @Test
    public void archerBotCannotUseHisAbility() throws Exception {
        Archer archerBot = new Archer(Player.BOT);
        assertThat(archerBot.useAbility(ABILITY_USE_TIME)).isEmpty();
    }

    @Test
    public void archerAbilityIsToThrowArrow() throws Exception {
        assertThat(archerPlayer.useAbility(ABILITY_USE_TIME).iterator().next()).isInstanceOf(Arrow.class);
    }

    @Test
    public void arrowSpawnsAtPlayerCenter() throws Exception {
        final Coordinates coordinates = new Coordinates(42, 1337);
        archerPlayer.teleports(coordinates);
        final Item arrow = archerPlayer.useAbility(ABILITY_USE_TIME).iterator().next();
        assertThat(arrow.getCoordinates()).isEqualTo(archerPlayer.getCenter());
    }

    @Test
    public void archerCannotMoveAfterThrowingArrow() throws Exception {
        final Coordinates coordinates = new Coordinates(42, 1337);
        archerPlayer.teleports(coordinates);
        archerPlayer.useAbility(ABILITY_USE_TIME).iterator().next();
        archerPlayer.moves(1, Direction.DOWN);
        assertThat(archerPlayer.getCoordinates()).isEqualTo(coordinates);
    }
}
