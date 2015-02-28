package org.atrolla.game.engine;

import static org.atrolla.game.engine.RandomUtils.getRandomDirection;
import static org.atrolla.game.engine.RandomUtils.getRandomDirectionFrom;
import static org.atrolla.game.engine.RandomUtils.getRandomMoveTime;

/**
 * Created by MicroOnde on 25/02/2015.
 */
public class Command {

    private final Direction direction;
    private final int timeout;

    /**
     * @param direction
     * @param timeout   creates a command with a direction and the timeout "timetick" when it must be evicted from AIManager
     */
    public Command(Direction direction, int timeout) {
        this.direction = direction;
        this.timeout = timeout;
    }

    public static Command RandomCommand(int time) {
        return new Command(getRandomDirection(), getRandomMoveTime(time));
    }

    public static Command RandomCommand(int time,Coordinates coordinates) {
        return new Command(getRandomDirectionFrom(coordinates), getRandomMoveTime(time));
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean checkIsDone(int timeTick) {
        return timeout < timeTick;
    }
}
