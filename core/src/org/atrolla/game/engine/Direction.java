package org.atrolla.game.engine;

import org.atrolla.game.configuration.ConfigurationConstants;

/**
 * Created by MicroOnde on 24/02/2015.
 */
public enum Direction {

    UP {
        @Override
        public Coordinates move(Coordinates coord) {
            return coord.translateY(-ConfigurationConstants.MOVE_STEP);
        }
    }, DOWN {
        @Override
        public Coordinates move(Coordinates coord) {
            return coord.translateY(ConfigurationConstants.MOVE_STEP);
        }
    }, LEFT {
        @Override
        public Coordinates move(Coordinates coord) {
            return coord.translateX(-ConfigurationConstants.MOVE_STEP);
        }
    }, RIGHT {
        @Override
        public Coordinates move(Coordinates coord) {
            return coord.translateX(ConfigurationConstants.MOVE_STEP);
        }
    }, UP_LEFT {
        @Override
        public Coordinates move(Coordinates coord) {
            return coord.translateXandY(-ConfigurationConstants.MOVE_STEP, -ConfigurationConstants.MOVE_STEP);
        }
    }, UP_RIGHT {
        @Override
        public Coordinates move(Coordinates coord) {
            return coord.translateXandY(-ConfigurationConstants.MOVE_STEP, ConfigurationConstants.MOVE_STEP);
        }
    }, DOWN_LEFT {
        @Override
        public Coordinates move(Coordinates coord) {
            return coord.translateXandY(ConfigurationConstants.MOVE_STEP, -ConfigurationConstants.MOVE_STEP);
        }
    }, DOWN_RIGHT {
        @Override
        public Coordinates move(Coordinates coord) {
            return coord.translateXandY(ConfigurationConstants.MOVE_STEP, ConfigurationConstants.MOVE_STEP);
        }
    };

    public abstract Coordinates move(Coordinates coord);


}
