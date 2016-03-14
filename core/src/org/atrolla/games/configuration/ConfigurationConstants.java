package org.atrolla.games.configuration;

public final class ConfigurationConstants {

    private ConfigurationConstants() {
    }

    public static final double MOVE_STEP = 1.5d;

    public static final double DIAGONAL_COEFFICIENT_COMPENSATION = 0.7d;

    public static final int STAGE_WIDTH = 1280;
    public static final int STAGE_HEIGHT = 720;

//    public static final int GAME_CHARACTER_WIDTH = 32;
//    public static final int GAME_CHARACTER_HEIGHT = 40;

    public static final int GAME_CHARACTER_WIDTH = 40;
    public static final int GAME_CHARACTER_HEIGHT = 50;

    public static final int GAME_CHARACTER_INITIAL_READY_TIME = -1;

    public static final int GAME_CHARACTERS = 48; // must be multiple of games character classes number

    public static final int RANDOM_UTILS_MAX_PROBABILITY = 100;

    public static final int BOT_MAX_MOVE_COMMAND_TIME = 100;
    public static final int BOT_STOP_PROBABILITY = 75;
    public static final int BOT_MAX_STOP_COMMAND_TIME = 20;
    public static final int KNOCK_OUT_DURATION = 100;

    public static final int MAX_PLAYERS = 4;

    public static final double ARROW_MOVE_STEP = 4d;
    public static final int ARROW_RANGE_TIME_OUT = 30;
    public static final int ARROW_HITBOX_SIZE = 1;
    public static final int SWORD_ACTION_TIME_OUT = 1;
    public static final int SWORD_SIZE = 10;
    public static final int BOMB_COUNTDOWN_DURATION = 150;
    public static final int BOMB_EXPLOSION_RADIUS_SIZE = 50;
    public static final int MAGE_SPELL_COUNTDOWN_DURATION = 10;
    public static final double MAGE_SPELL_OFFSET = 60;
    public static final int MAGE_SPELL_SIZE = 5;

    public static final int ARCHER_ABILITY_COOLDOWN_DURATION = 150;
    public static final int BOMBER_ABILITY_COOLDOWN_DURATION = BOMB_COUNTDOWN_DURATION + 10;
    public static final int KNIGHT_ABILITY_COOLDOWN_DURATION = 50;
    public static final int MAGE_ABILITY_COOLDOWN_DURATION = 70;

    public static final int NEUTRAL_ITEM_SPAWN_FREQUENCY_TIME = 750;
    public static final int NEUTRAL_ITEM_SIZE = 32;
    public static final int NEUTRAL_ITEM_PICK_SHOW_DELAY = 350;
    public static final int NEUTRAL_ITEM_DESPAWN = 1000;
    public static final int NEUTRAL_ITEM_LOCATOR_DELAY_BEGIN = 100;
}
