package org.atrolla.games.system;

import org.atrolla.games.configuration.ConfigurationConstants;

import static org.atrolla.games.configuration.ConfigurationConstants.DIAGONAL_COEFFICIENT_COMPENSATION;

/**
 * Created by MicroOnde on 24/02/2015.
 */
public enum Direction {

    UP {
        @Override
        public Coordinates move(Coordinates coord, double StepDistance) {
            return coord.translateY(StepDistance);
        }
    }, DOWN {
        @Override
        public Coordinates move(Coordinates coord, double StepDistance) {
            return coord.translateY(-StepDistance);
        }
    }, LEFT {
        @Override
        public Coordinates move(Coordinates coord, double StepDistance) {
            return coord.translateX(-StepDistance);
        }
    }, RIGHT {
        @Override
        public Coordinates move(Coordinates coord, double StepDistance) {
            return coord.translateX(StepDistance);
        }
    }, UP_LEFT {
        @Override
        public Coordinates move(Coordinates coord, double StepDistance) {
            return coord.translateXandY(-StepDistance * DIAGONAL_COEFFICIENT_COMPENSATION, StepDistance * DIAGONAL_COEFFICIENT_COMPENSATION);
        }
    }, UP_RIGHT {
        @Override
        public Coordinates move(Coordinates coord, double StepDistance) {
            return coord.translateXandY(StepDistance * DIAGONAL_COEFFICIENT_COMPENSATION, StepDistance * DIAGONAL_COEFFICIENT_COMPENSATION);
        }
    }, DOWN_LEFT {
        @Override
        public Coordinates move(Coordinates coord, double StepDistance) {
            return coord.translateXandY(-StepDistance * DIAGONAL_COEFFICIENT_COMPENSATION, -StepDistance * DIAGONAL_COEFFICIENT_COMPENSATION);
        }
    }, DOWN_RIGHT {
        @Override
        public Coordinates move(Coordinates coord, double StepDistance) {
            return coord.translateXandY(StepDistance * DIAGONAL_COEFFICIENT_COMPENSATION, -StepDistance * DIAGONAL_COEFFICIENT_COMPENSATION);
        }
    }, STOP {
        @Override
        public Coordinates move(Coordinates coord, double StepDistance) {
            return coord;
        }
    };

    public abstract Coordinates move(Coordinates coord, double StepDistance);

    public Coordinates move(Coordinates coord) {
        return move(coord, ConfigurationConstants.MOVE_STEP);
    }


}
