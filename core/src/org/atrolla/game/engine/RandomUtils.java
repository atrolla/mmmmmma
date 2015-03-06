package org.atrolla.game.engine;

import org.apache.commons.lang3.EnumUtils;
import org.atrolla.game.configuration.ConfigurationConstants;

import java.util.List;
import java.util.Random;

import static org.atrolla.game.configuration.ConfigurationConstants.*;

/**
 * Created by MicroOnde on 25/02/2015.
 */
public final class RandomUtils {

    private RandomUtils() {
    }

    private static final Random RANDOM = new Random();
    private static final int DIRECTIONS_NUMBER = Direction.values().length - 1;

    public static final int getRandomMoveTime(int time) {
        return RANDOM.nextInt(ConfigurationConstants.BOT_MAX_MOVE_COMMAND_TIME) + 1 + time;
    }

    public static final int getRandomStopTime(int time) {
        return RANDOM.nextInt(ConfigurationConstants.BOT_MAX_STOP_COMMAND_TIME) + 1 + time;
    }

    public static boolean stopRandomChance() {
        return RANDOM.nextInt(RANDOM_UTILS_MAX_PROBABILITY) < BOT_STOP_PROBABILITY;
    }

    public static final Direction getRandomDirectionFrom(Coordinates coordinates) {
        if (Coordinates.NULL == coordinates) {
            return getRandomDirection();
        }
        List<Direction> validDirectionList = buildValidDirectionList(coordinates);
        return validDirectionList.get(RANDOM.nextInt(validDirectionList.size()));
    }

    private static final Direction getRandomDirection() {
        return Direction.values()[RANDOM.nextInt(DIRECTIONS_NUMBER)];
    }

    private static List<Direction> buildValidDirectionList(Coordinates coordinates) {
        final double x = coordinates.getX();
        final double y = coordinates.getY();
        final List<Direction> validDirectionList = EnumUtils.getEnumList(Direction.class);
        validDirectionList.remove(Direction.STOP);
        if (x == 0) {
            validDirectionList.remove(Direction.LEFT);
            validDirectionList.remove(Direction.DOWN_LEFT);
            validDirectionList.remove(Direction.UP_LEFT);
        } else if (x == ConfigurationConstants.STAGE_WIDTH-PLAYER_WIDTH) {
            validDirectionList.remove(Direction.RIGHT);
            validDirectionList.remove(Direction.DOWN_RIGHT);
            validDirectionList.remove(Direction.UP_RIGHT);
        }
        if (y == 0) {
            validDirectionList.remove(Direction.UP);
            validDirectionList.remove(Direction.UP_RIGHT);
            validDirectionList.remove(Direction.UP_LEFT);
        } else if (y == ConfigurationConstants.STAGE_HEIGHT-PLAYER_HEIGHT) {
            validDirectionList.remove(Direction.DOWN);
            validDirectionList.remove(Direction.DOWN_RIGHT);
            validDirectionList.remove(Direction.DOWN_LEFT);
        }
        return validDirectionList;
    }

}
