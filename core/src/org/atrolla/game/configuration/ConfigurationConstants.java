package org.atrolla.game.configuration;

/**
 * Created by MicroOnde on 24/02/2015.
 */
public final class ConfigurationConstants {

    private ConfigurationConstants() {
    }

    public static final double MOVE_STEP = 1.5d;

    public static final double STAGE_WIDTH = 1280d;
    public static final double STAGE_HEIGHT = 720d;

    public static final int PLAYER_WIDTH = 30;
    public static final int PLAYER_HEIGHT = 40;

    public static final int GAME_CHARACTERS = 40; // must be multiple of game character classes number

    public static final int RANDOM_UTILS_MAX_PROBABILITY = 100;

    public static final int BOT_MAX_MOVE_COMMAND_TIME = 100;
    public static final int BOT_STOP_PROBABILITY = 75;
    public static final int BOT_MAX_STOP_COMMAND_TIME = 20;
    public static final int BOT_KNOCK_OUT_DURATION = 100;

    public static final int MAX_PLAYERS = 4;

    public static final int BOMBER_ABILITY_COOLDOWN_DURATION = 60;

    public static final int ITEM_BOMB_COUNTDOWN_DURATION = 60;

}
