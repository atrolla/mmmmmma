package org.atrolla.game.characters;

import org.atrolla.game.engine.CharacterState;
import org.atrolla.game.engine.Coordinates;
import org.atrolla.game.engine.Direction;
import org.atrolla.game.engine.Player;

/**
 * Created by MicroOnde on 24/02/2015.
 */
public abstract class GameCharacter {

	private Direction direction;
	private Coordinates coordinates;
	private final Player player;
	private CharacterState state;

	protected GameCharacter(Coordinates coordinates, Player player) {
		this.coordinates = coordinates;
		this.player = player;
		this.state = CharacterState.ALIVE;
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

	public boolean isPlayer() {
		return player != Player.BOT;
	}

	public Player getPlayer() {
		return player;
	}

	public CharacterState getState() {
		return state;
	}

	public void hit() {
		state = isPlayer() ? CharacterState.DEAD : CharacterState.KNOCK_OUT;
	}

	public void awake() {
		state = isPlayer() ? CharacterState.DEAD : CharacterState.ALIVE;
	}
}
