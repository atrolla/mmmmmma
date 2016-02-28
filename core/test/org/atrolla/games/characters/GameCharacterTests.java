package org.atrolla.games.characters;

import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Direction;
import org.atrolla.games.system.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by MicroOnde on 24/02/2015.
 */
public class GameCharacterTests {


    private Archer defaultBotArcher;

    @Before
    public void setUp() throws Exception {
        defaultBotArcher = new Archer(Player.BOT);
        defaultBotArcher.teleports(new Coordinates(1.0d, 1.0d));
    }

    @Test
    public void thisAlwaysPasses() {
        assertThat(true);
    }

    @Test
    public void archerIsAGameCharacter() throws Exception {
        assertThat(defaultBotArcher instanceof GameCharacter);
    }

    @Test
    public void bomberIsAGameCharacter() throws Exception {
        Bomber bomber = new Bomber(Player.BOT);
        assertThat(bomber instanceof GameCharacter);
    }

    @Test
    public void knightIsAGameCharacter() throws Exception {
        Knight knight = new Knight(Player.BOT);
        assertThat(knight instanceof GameCharacter);
    }

    @Test
    public void mageIsAGameCharacter() throws Exception {
        Mage mage = new Mage(Player.BOT);
        assertThat(mage instanceof GameCharacter);
    }

    @Test
    public void moveUpChangesDirection() throws Exception {
        defaultBotArcher.moves(0, Direction.UP);
        assertThat(Direction.UP).isEqualTo(defaultBotArcher.getDirection());
    }

    @Test
    public void moveUpLeftChangesDirection() throws Exception {
        defaultBotArcher.moves(0, Direction.UP_LEFT);
        assertThat(Direction.UP_LEFT).isEqualTo(defaultBotArcher.getDirection());
    }

    @Test
    public void moveUpRightChangesDirection() throws Exception {
        defaultBotArcher.moves(0, Direction.UP_RIGHT);
        assertThat(Direction.UP_RIGHT).isEqualTo(defaultBotArcher.getDirection());
    }

    @Test
    public void moveLeftChangesDirection() throws Exception {
        defaultBotArcher.moves(0, Direction.LEFT);
        assertThat(Direction.LEFT).isEqualTo(defaultBotArcher.getDirection());
    }

    @Test
    public void moveRightChangesDirection() throws Exception {
        defaultBotArcher.moves(0, Direction.RIGHT);
        assertThat(Direction.RIGHT).isEqualTo(defaultBotArcher.getDirection());
    }

    @Test
    public void moveDownLeftChangesDirection() throws Exception {
        defaultBotArcher.moves(0, Direction.DOWN_LEFT);
        assertThat(Direction.DOWN_LEFT).isEqualTo(defaultBotArcher.getDirection());
    }

    @Test
    public void moveDownChangesDirection() throws Exception {
        defaultBotArcher.moves(0, Direction.DOWN);
        assertThat(Direction.DOWN).isEqualTo(defaultBotArcher.getDirection());
    }

    @Test
    public void moveDownRightChangesDirection() throws Exception {
        defaultBotArcher.moves(0, Direction.DOWN_RIGHT);
        assertThat(Direction.DOWN_RIGHT).isEqualTo(defaultBotArcher.getDirection());
    }

    @Test
    public void moveChangesCoordinates() throws Exception {
        defaultBotArcher.moves(0, Direction.UP);
        Coordinates c1 = defaultBotArcher.getCoordinates();
        defaultBotArcher.moves(0, Direction.UP);
        assertThat(c1).isNotEqualTo(defaultBotArcher.getCoordinates());
    }

    @Test
    public void characterIsBotByDefault() throws Exception {
        assertThat(defaultBotArcher.isPlayer()).isFalse();
    }

    @Test
    public void characterIsPlayedByPlayer() throws Exception {
        Player player = new Player(Optional.empty(), Optional.empty());
        Archer archer = new Archer(player);
        assertThat(archer.isPlayer());
        assertThat(player).isEqualTo(archer.getPlayer());
    }

    @Test
    public void characterIsInitiallyAlive() throws Exception {
        assertThat(CharacterState.ALIVE).isEqualTo(defaultBotArcher.getState());
    }

    @Test
    public void playedCharacterAfterHitIsDead() throws Exception {
        Player player = new Player(Optional.empty(), Optional.empty());
        Archer archer = new Archer(player);
        archer.hit(new Knight(Player.BOT));
        assertThat(CharacterState.DEAD).isEqualTo(archer.getState());
    }

    @Test
    public void botCharacterAfterHitIsKnockOut() throws Exception {
        defaultBotArcher.hit(new Knight(Player.BOT));
        assertThat(CharacterState.KNOCK_OUT).isEqualTo(defaultBotArcher.getState());
    }

    @Test
    public void knockOutCharacterAfterAwakeIsAlive() throws Exception {
        defaultBotArcher.hit(new Knight(Player.BOT));
        assertThat(CharacterState.KNOCK_OUT).isEqualTo(defaultBotArcher.getState());
        defaultBotArcher.awake();
        assertThat(CharacterState.ALIVE).isEqualTo(defaultBotArcher.getState());
    }

    @Test
    public void deadCharacterAfterAwakeIsStillDead() throws Exception {
        Player player = new Player(Optional.empty(), Optional.empty());
        Archer archer = new Archer(player);
        archer.hit(new Knight(Player.BOT));
        assertThat(CharacterState.DEAD).isEqualTo(archer.getState());
        archer.awake();
        assertThat(CharacterState.DEAD).isEqualTo(archer.getState());
    }

    @Test
    public void onlyAliveCharacterCanMove() throws Exception {
        assertThat(defaultBotArcher.isAlive());
        defaultBotArcher.hit(new Knight(Player.BOT));
        assertThat(CharacterState.KNOCK_OUT).isEqualTo(defaultBotArcher.getState());
        assertThat(defaultBotArcher.isAlive()).isFalse();
        Player player = new Player(Optional.empty(), Optional.empty());
        Archer archer = new Archer(player);
        assertThat(archer.isAlive());
        archer.hit(new Knight(Player.BOT));
        assertThat(CharacterState.DEAD).isEqualTo(archer.getState());
        assertThat(archer.isAlive()).isFalse();
    }

    @Test
    public void nextCharacter() throws Exception {
        assertThat(CharacterClasses.next(CharacterClasses.ARCHER)).isEqualTo(CharacterClasses.BOMBER);
        assertThat(CharacterClasses.next(CharacterClasses.BOMBER)).isEqualTo(CharacterClasses.KNIGHT);
        assertThat(CharacterClasses.next(CharacterClasses.KNIGHT)).isEqualTo(CharacterClasses.MAGE);
        assertThat(CharacterClasses.next(CharacterClasses.MAGE)).isEqualTo(CharacterClasses.ARCHER);
    }

    @Test
    public void previousCharacter() throws Exception {
        assertThat(CharacterClasses.previous(CharacterClasses.ARCHER)).isEqualTo(CharacterClasses.MAGE);
        assertThat(CharacterClasses.previous(CharacterClasses.BOMBER)).isEqualTo(CharacterClasses.ARCHER);
        assertThat(CharacterClasses.previous(CharacterClasses.KNIGHT)).isEqualTo(CharacterClasses.BOMBER);
        assertThat(CharacterClasses.previous(CharacterClasses.MAGE)).isEqualTo(CharacterClasses.KNIGHT);
    }
}
