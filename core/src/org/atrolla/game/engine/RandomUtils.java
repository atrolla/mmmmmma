package org.atrolla.game.engine;

import org.atrolla.game.configuration.ConfigurationConstants;

import java.util.Random;

/**
 * Created by MicroOnde on 25/02/2015.
 */
public final class RandomUtils {

    private RandomUtils() {
    }

    private static final Random RANDOM = new Random();
    private static final int DIRECTIONS_NUMBER = Direction.values().length;

    public static final int getRandomMoveTime() {
        return RANDOM.nextInt(ConfigurationConstants.MAX_MOVE_COMMAND_TIME) + 1;
    }

    public static final Direction getRandomDirection() {
        return Direction.values()[RANDOM.nextInt(DIRECTIONS_NUMBER)];
    }

}
