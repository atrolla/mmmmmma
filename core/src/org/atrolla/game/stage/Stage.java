package org.atrolla.game.stage;

import org.atrolla.game.characters.GameCharacter;
import org.atrolla.game.configuration.ConfigurationConstants;

/**
 * Created by MicroOnde on 25/02/2015.
 */
public class Stage {

	private final double width;
	private final double height;

	public Stage() {
		this.width = ConfigurationConstants.STAGE_WIDTH;
		this.height = ConfigurationConstants.STAGE_HEIGHT;
	}

	public Stage(double width, double height) {
		this.width = width;
		this.height = height;
	}

    public boolean isOutOfBound(GameCharacter character) {
        //TODO : stricly at the moment
        return (character.getCoordinates().getX() < 0 || character.getCoordinates().getX() > this.width)
                || (character.getCoordinates().getY() < 0 || character.getCoordinates().getY() > this.height);
    }

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}
}
