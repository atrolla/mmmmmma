package org.atrolla.game.engine;

import org.apache.commons.lang3.EnumUtils;
import org.atrolla.game.configuration.ConfigurationConstants;

import java.util.List;
import java.util.Random;

/**
 * Created by MicroOnde on 25/02/2015.
 */
public final class RandomUtils {

    private RandomUtils() {
    }

    private static final Random RANDOM = new Random();
    private static final int DIRECTIONS_NUMBER = Direction.values().length;

    public static final int getRandomMoveTime(int time) {
        return RANDOM.nextInt(ConfigurationConstants.MAX_MOVE_COMMAND_TIME) + 1 + time;
    }

    public static final Direction getRandomDirection() {
        return Direction.values()[RANDOM.nextInt(DIRECTIONS_NUMBER)];
    }

    public static final Direction getRandomDirectionFrom(Coordinates coordinates) {
        List<Direction> validDirectionList = buildValidDirectionList(coordinates);
        int randomOrdinal =  RANDOM.nextInt(validDirectionList.size());
        return validDirectionList.get(randomOrdinal);
    }

    private static List<Direction> buildValidDirectionList(Coordinates coordinates) {
        final double x = coordinates.getX();
        final double y = coordinates.getY();
        final List<Direction> validDirectionList = EnumUtils.getEnumList(Direction.class);
        if (x == 0) {
            validDirectionList.remove(Direction.LEFT);
            validDirectionList.remove(Direction.DOWN_LEFT);
            validDirectionList.remove(Direction.UP_LEFT);
        } else if (x == ConfigurationConstants.STAGE_WIDTH) {
            validDirectionList.remove(Direction.RIGHT);
            validDirectionList.remove(Direction.DOWN_RIGHT);
            validDirectionList.remove(Direction.UP_RIGHT);
        }
        if (y == 0) {
            validDirectionList.remove(Direction.UP);
            validDirectionList.remove(Direction.UP_RIGHT);
            validDirectionList.remove(Direction.UP_LEFT);
        } else if (y == ConfigurationConstants.STAGE_HEIGHT) {
            validDirectionList.remove(Direction.DOWN);
            validDirectionList.remove(Direction.DOWN_RIGHT);
            validDirectionList.remove(Direction.DOWN_LEFT);
        }
        return validDirectionList;
    }

}
