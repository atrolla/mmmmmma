package org.atrolla.game.core;

import org.atrolla.game.characters.*;
import org.atrolla.game.engine.Coordinates;
import org.atrolla.game.engine.Direction;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by MicroOnde on 24/02/2015.
 */
public class GameCharacterTests {


	@Test
	public void thisAlwaysPasses() {
		assertTrue(true);
	}

	@Test
	public void archerIsAGameCharacter() throws Exception {
		Archer archer = new Archer(new Coordinates(1.0d,1.0d));
		assertTrue(archer instanceof GameCharacter);
	}

	@Test
	public void bomberIsAGameCharacter() throws Exception {
		Bomber bomber = new Bomber(new Coordinates(1.0d,1.0d));
		assertTrue(bomber instanceof GameCharacter);
	}

	@Test
	public void knightIsAGameCharacter() throws Exception {
		Knight knight = new Knight(new Coordinates(1.0d,1.0d));
		assertTrue(knight instanceof GameCharacter);
	}

	@Test
	public void mageIsAGameCharacter() throws Exception {
		Mage mage = new Mage(new Coordinates(1.0d,1.0d));
		assertTrue(mage instanceof GameCharacter);
	}

	@Test
	 public void moveUpChangesDirection() throws Exception {
		Archer archer = new Archer(new Coordinates(1.0d,1.0d));
		archer.moves(Direction.UP);
		assertEquals(Direction.UP,archer.getDirection());
	}

	@Test
	public void moveUpLeftChangesDirection() throws Exception {
		Archer archer = new Archer(new Coordinates(1.0d,1.0d));
		archer.moves(Direction.UP_LEFT);
		assertEquals(Direction.UP_LEFT,archer.getDirection());
	}

	@Test
	public void moveUpRightChangesDirection() throws Exception {
		Archer archer = new Archer(new Coordinates(1.0d,1.0d));
		archer.moves(Direction.UP_RIGHT);
		assertEquals(Direction.UP_RIGHT,archer.getDirection());
	}

	@Test
	public void moveLeftChangesDirection() throws Exception {
		Archer archer = new Archer(new Coordinates(1.0d,1.0d));
		archer.moves(Direction.LEFT);
		assertEquals(Direction.LEFT,archer.getDirection());
	}

	@Test
	public void moveRightChangesDirection() throws Exception {
		Archer archer = new Archer(new Coordinates(1.0d,1.0d));
		archer.moves(Direction.RIGHT);
		assertEquals(Direction.RIGHT,archer.getDirection());
	}

	@Test
	public void moveDownLeftChangesDirection() throws Exception {
		Archer archer = new Archer(new Coordinates(1.0d,1.0d));
		archer.moves(Direction.DOWN_LEFT);
		assertEquals(Direction.DOWN_LEFT,archer.getDirection());
	}

	@Test
	public void moveDownChangesDirection() throws Exception {
		Archer archer = new Archer(new Coordinates(1.0d,1.0d));
		archer.moves(Direction.DOWN);
		assertEquals(Direction.DOWN,archer.getDirection());
	}

	@Test
	public void moveDownRightChangesDirection() throws Exception {
		Archer archer = new Archer(new Coordinates(1.0d,1.0d));
		archer.moves(Direction.DOWN_RIGHT);
		assertEquals(Direction.DOWN_RIGHT,archer.getDirection());
	}

	@Test
	public void changeDirectionRotatesWithoutChangingCoordinates() throws Exception {
		Archer archer = new Archer(new Coordinates(1.0d,1.0d));
		Coordinates c1 = archer.getCoordinates();
		archer.moves(Direction.UP);
		assertEquals(c1,archer.getCoordinates());
	}

	@Test
	public void moveSameDirectionChangesCoordinates() throws Exception {
		Archer archer = new Archer(new Coordinates(1.0d,1.0d));
		archer.moves(Direction.UP);
		Coordinates c1 = archer.getCoordinates();
		archer.moves(Direction.UP);
		assertNotEquals(c1, archer.getCoordinates());
	}
}
