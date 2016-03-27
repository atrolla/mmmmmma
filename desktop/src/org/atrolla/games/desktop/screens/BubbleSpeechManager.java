package org.atrolla.games.desktop.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.game.Round;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.utils.RandomUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by MicroOnde on 20/03/2016.
 */
public class BubbleSpeechManager {

    public static final int SPEECH_DURATION = 500;
    public static final int TALK_CHANCE = 497;
    private final Round round;
    private final SpriteBatch spriteBatch;
    private final BitmapFont font;
    private final Map<BubbleSpeech, Integer> speechIntegerMap;

    public BubbleSpeechManager(Round round, SpriteBatch spriteBatch) {
        this.round = round;
        this.spriteBatch = spriteBatch;
        font = new BitmapFont();
        speechIntegerMap = new HashMap<>();
    }

    public void updateBubbleSpeech() {
        if (RandomUtils.between0AndExcluded(SPEECH_DURATION) > TALK_CHANCE) {
            final List<GameCharacter> characters = round.getCharacters().characters.stream().filter(GameCharacter::isAlive).collect(Collectors.toList());
            final int randomChar = RandomUtils.between0AndExcluded(characters.size());
            final GameCharacter gameCharacter = characters.get(randomChar);
            final int roundTime = round.getTime();
            final int timeOut = roundTime + SPEECH_DURATION;
            final boolean isAlreadySpeaking = speechIntegerMap.keySet().stream().map(k -> k.gameCharacter).anyMatch(c -> c.equals(gameCharacter));
            if (!isAlreadySpeaking) {
                speechIntegerMap.put(new BubbleSpeech(gameCharacter, "hello world !"), timeOut);
            }
        }
        drawBubbles();
    }

    private void drawBubbles() {
        spriteBatch.begin();
        font.setColor(Color.BLACK);
        final int roundTime = round.getTime();
        speechIntegerMap.entrySet().stream().forEach(bubbleSpeechIntegerEntry -> {
            if (roundTime <= bubbleSpeechIntegerEntry.getValue()) {
                final BubbleSpeech bubbleSpeech = bubbleSpeechIntegerEntry.getKey();
                final Coordinates coordinates = bubbleSpeech.gameCharacter.getCoordinates();
                final float x = (float) coordinates.getX() + ConfigurationConstants.GAME_CHARACTER_WIDTH / 3;
                final float y = (float) coordinates.getY() + ConfigurationConstants.GAME_CHARACTER_HEIGHT + 10;
                font.draw(spriteBatch, bubbleSpeech.text, x, y);
            }
        });
        spriteBatch.end();
    }

    private class BubbleSpeech {
        public final String text;
        public final GameCharacter gameCharacter;

        private BubbleSpeech(GameCharacter gameCharacter, String text) {
            this.text = text;
            this.gameCharacter = gameCharacter;
        }
    }

}
