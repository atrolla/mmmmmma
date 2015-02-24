package org.atrolla.game.characters;

import org.atrolla.game.engine.Coordinates;
import org.atrolla.game.engine.Direction;

/**
 * Created by MicroOnde on 24/02/2015.
 */
public abstract class GameCharacter {


	private Direction direction;
	private Coordinates coordinates;

	protected GameCharacter(Coordinates coordinates) {
		this.coordinates = coordinates;
		direction = Direction.DOWN;
	}

	public void moves(Direction direction) {
		if (direction != this.direction) {
			this.direction = direction;
		} else {
			coordinates = direction.move(coordinates);
		}
	}

	public Direction getDirection() {
		return direction;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}
}
