package org.atrolla.game.stage;

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

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}
}
