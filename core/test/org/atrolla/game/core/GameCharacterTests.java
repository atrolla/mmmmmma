package org.atrolla.game.core;

import org.atrolla.game.characters.*;
import org.atrolla.game.engine.CharacterState;
import org.atrolla.game.engine.Coordinates;
import org.atrolla.game.engine.Direction;
import org.atrolla.game.engine.Player;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by MicroOnde on 24/02/2015.
 */
public class GameCharacterTests {


	private Archer defaultBotArcher  = new Archer(new Coordinates(1.0d,1.0d), Player.BOT);

	@Test
	public void thisAlwaysPasses() {
		assertTrue(true);
	}

	@Test
	public void archerIsAGameCharacter() throws Exception {
		assertTrue(defaultBotArcher instanceof GameCharacter);
	}

	@Test
	public void bomberIsAGameCharacter() throws Exception {
		Bomber bomber = new Bomber(new Coordinates(1.0d,1.0d), Player.BOT);
		assertTrue(bomber instanceof GameCharacter);
	}

	@Test
	public void knightIsAGameCharacter() throws Exception {
		Knight knight = new Knight(new Coordinates(1.0d,1.0d), Player.BOT);
		assertTrue(knight instanceof GameCharacter);
	}

	@Test
	public void mageIsAGameCharacter() throws Exception {
		Mage mage = new Mage(new Coordinates(1.0d,1.0d), Player.BOT);
		assertTrue(mage instanceof GameCharacter);
	}

	@Test
	 public void moveUpChangesDirection() throws Exception {
		defaultBotArcher.moves(Direction.UP);
		assertEquals(Direction.UP,defaultBotArcher.getDirection());
	}

	@Test
	public void moveUpLeftChangesDirection() throws Exception {
		defaultBotArcher.moves(Direction.UP_LEFT);
		assertEquals(Direction.UP_LEFT,defaultBotArcher.getDirection());
	}

	@Test
	public void moveUpRightChangesDirection() throws Exception {
		defaultBotArcher.moves(Direction.UP_RIGHT);
		assertEquals(Direction.UP_RIGHT,defaultBotArcher.getDirection());
	}

	@Test
	public void moveLeftChangesDirection() throws Exception {
		defaultBotArcher.moves(Direction.LEFT);
		assertEquals(Direction.LEFT,defaultBotArcher.getDirection());
	}

	@Test
	public void moveRightChangesDirection() throws Exception {
		defaultBotArcher.moves(Direction.RIGHT);
		assertEquals(Direction.RIGHT,defaultBotArcher.getDirection());
	}

	@Test
	public void moveDownLeftChangesDirection() throws Exception {
		defaultBotArcher.moves(Direction.DOWN_LEFT);
		assertEquals(Direction.DOWN_LEFT,defaultBotArcher.getDirection());
	}

	@Test
	public void moveDownChangesDirection() throws Exception {
		defaultBotArcher.moves(Direction.DOWN);
		assertEquals(Direction.DOWN,defaultBotArcher.getDirection());
	}

	@Test
	public void moveDownRightChangesDirection() throws Exception {
		defaultBotArcher.moves(Direction.DOWN_RIGHT);
		assertEquals(Direction.DOWN_RIGHT,defaultBotArcher.getDirection());
	}

	@Test
	public void changeDirectionRotatesWithoutChangingCoordinates() throws Exception {
		defaultBotArcher.moves(Direction.DOWN);
		Coordinates c1 = defaultBotArcher.getCoordinates();
		defaultBotArcher.moves(Direction.UP);
		assertEquals(c1,defaultBotArcher.getCoordinates());
	}

	@Test
	public void moveSameDirectionChangesCoordinates() throws Exception {
		defaultBotArcher.moves(Direction.UP);
		Coordinates c1 = defaultBotArcher.getCoordinates();
		defaultBotArcher.moves(Direction.UP);
		assertNotEquals(c1, defaultBotArcher.getCoordinates());
	}

	@Test
	public void characterIsBotByDefault() throws Exception {
		assertFalse(defaultBotArcher.isPlayer());
	}

	@Test
	public void characterIsPlayedByPlayer() throws Exception {
		Player player = new Player();
		Archer archer = new Archer(new Coordinates(1.0d,1.0d), player);
		assertTrue(archer.isPlayer());
		assertEquals(player, archer.getPlayer());
	}

	@Test
	public void characterIsInitiallyAlive() throws Exception {
		assertEquals(CharacterState.ALIVE, defaultBotArcher.getState());
	}

	@Test
	public void playedCharacterAfterHitIsDead() throws Exception {
		Player player = new Player();
		Archer archer = new Archer(new Coordinates(1.0d,1.0d), player);
		archer.hit();
		assertEquals(CharacterState.DEAD, archer.getState());
	}

	@Test
	public void botCharacterAfterHitIsKnockOut() throws Exception {
		defaultBotArcher.hit();
		assertEquals(CharacterState.KNOCK_OUT, defaultBotArcher.getState());
	}

	@Test
	public void knockOutCharacterAfterAwakeIsAlive() throws Exception {
		defaultBotArcher.hit();
		assertEquals(CharacterState.KNOCK_OUT, defaultBotArcher.getState());
		defaultBotArcher.awake();
		assertEquals(CharacterState.ALIVE, defaultBotArcher.getState());
	}

	@Test
	public void deadCharacterAfterAwakeIsStillDead() throws Exception {
		Player player = new Player();
		Archer archer = new Archer(new Coordinates(1.0d,1.0d), player);
		archer.hit();
		assertEquals(CharacterState.DEAD, archer.getState());
		archer.awake();
		assertEquals(CharacterState.DEAD, archer.getState());
	}
}
