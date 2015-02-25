package org.atrolla.game.core;

import org.atrolla.game.engine.Command;
import org.atrolla.game.engine.Direction;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by MicroOnde on 25/02/2015.
 */
public class CommandTests {

	private final Command command = new Command(Direction.UP,5);

	@Test
	public void commandHasDirection() throws Exception {
		assertEquals(Direction.UP, command.getDirection());
	}

	@Test
	public void commandHasTimeoutForTimeout() throws Exception {
		assertFalse(command.checkIsDone(1));
		assertFalse(command.checkIsDone(4));
		assertFalse(command.checkIsDone(5));
		assertTrue(command.checkIsDone(6));
	}
}
