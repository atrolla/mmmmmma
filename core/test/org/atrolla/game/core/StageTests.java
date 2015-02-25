package org.atrolla.game.core;

import org.atrolla.game.stage.Stage;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by MicroOnde on 25/02/2015.
 */
public class StageTests {

	public static final double STAGE_WIDTH = 720d;
	private static final double STAGE_HEIGHT = 1280d;

	@Test
	public void stageHasWidth() throws Exception {
		Stage stage = new Stage(STAGE_WIDTH,STAGE_HEIGHT);
		assertEquals(STAGE_WIDTH, stage.getWidth());
	}

	@Test
	public void stageHasHeight() throws Exception {
		Stage stage = new Stage(STAGE_WIDTH,STAGE_HEIGHT);
		assertEquals(STAGE_HEIGHT, stage.getHeight());
	}
}
