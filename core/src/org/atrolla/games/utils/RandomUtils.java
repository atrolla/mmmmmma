package org.atrolla.games.utils;

import org.apache.commons.lang3.EnumUtils;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Direction;

import java.util.List;
import java.util.Random;

import static org.atrolla.games.configuration.ConfigurationConstants.*;

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

    public static final Coordinates getRandomCoordinates(int objectWidth, int objectHeight) {
        return new Coordinates(RANDOM.nextInt(ConfigurationConstants.STAGE_WIDTH - objectWidth), RANDOM.nextInt(ConfigurationConstants.STAGE_HEIGHT - objectHeight));
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
        } else if (x == ConfigurationConstants.STAGE_WIDTH - GAME_CHARACTER_WIDTH) {
            validDirectionList.remove(Direction.RIGHT);
            validDirectionList.remove(Direction.DOWN_RIGHT);
            validDirectionList.remove(Direction.UP_RIGHT);
        }
        if (y == ConfigurationConstants.STAGE_HEIGHT - GAME_CHARACTER_HEIGHT) {
            validDirectionList.remove(Direction.UP);
            validDirectionList.remove(Direction.UP_RIGHT);
            validDirectionList.remove(Direction.UP_LEFT);
        } else if (y == 0) {
            validDirectionList.remove(Direction.DOWN);
            validDirectionList.remove(Direction.DOWN_RIGHT);
            validDirectionList.remove(Direction.DOWN_LEFT);
        }
        return validDirectionList;
    }

}
