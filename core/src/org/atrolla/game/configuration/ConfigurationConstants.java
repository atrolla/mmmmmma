package org.atrolla.game.configuration;

/**
 * Created by MicroOnde on 24/02/2015.
 */
public final class ConfigurationConstants {

    private ConfigurationConstants() {
    }

    public final static double MOVE_STEP = 1.5d;

    public final static double STAGE_WIDTH = 1280d;
    public final static double STAGE_HEIGHT = 720d;

    public final static int GAME_CHARACTERS = 40; // must be multiple of game character classes number

    public final static int RANDOM_UTILS_MAX_PROBABILITY = 100;
    public final static int BOT_MAX_MOVE_COMMAND_TIME = 100;
    public final static int BOT_STOP_PROBABILITY = 75;
    public final static int BOT_MAX_STOP_COMMAND_TIME = 20;

}
