package org.atrolla.games.desktop.screens.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.game.Round;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.utils.RandomUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by MicroOnde on 20/03/2016.
 */
public class BubbleSpeechManager {

    public static final int SPEECH_DURATION = 700;
    public static final int GLOBAL_CHANCE = 200;
    public static final int TALK_CHANCE = 199;
    private final Round round;
    private final SpriteBatch spriteBatch;
    private final Map<BubbleSpeech, Integer> speechIntegerMap;
    private final List<String> texts;
    private final FreeTypeFontGenerator generator;
    private final BitmapFont font;

    public BubbleSpeechManager(Round round, SpriteBatch spriteBatch) {
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/DejaVuSans.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = Color.BLACK;
        parameter.characters = getCharacters();
        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.size = 14;
        font = generator.generateFont(parameter);
        this.round = round;
        this.spriteBatch = spriteBatch;
        speechIntegerMap = new HashMap<>();
        texts = new ArrayList<>();
        initTexts();
    }

    private String getCharacters() {
        return FreeTypeFontGenerator.DEFAULT_CHARS + "❤⚆⚇⚈⚉☺☻◕‿♫♪♺⚅⚃⚁";
    }

    private void initTexts() {
        FileHandle file = Gdx.files.local("conf/bubblespeech.txt");
        if (file.exists()) {
            final List<String> strings = file.reader(1024, StandardCharsets.UTF_8.name()).lines().filter(s -> s.length() < 40).collect(Collectors.toList());
            texts.addAll(strings);
        }
        texts.add("Hello world !");
    }

    public void updateBubbleSpeech() {
        if (RandomUtils.between0AndExcluded(GLOBAL_CHANCE) >= TALK_CHANCE) {
            final List<GameCharacter> characters = round.getCharacters().characters.stream().filter(GameCharacter::isAlive).collect(Collectors.toList());
            final int randomChar = RandomUtils.between0AndExcluded(characters.size());
            final GameCharacter gameCharacter = characters.get(randomChar);
            final int roundTime = round.getTime();
            final int timeOut = roundTime + SPEECH_DURATION;
            final boolean isAlreadySpeaking = speechIntegerMap.entrySet().stream().anyMatch(kv -> kv.getKey().gameCharacter.equals(gameCharacter) && kv.getValue() > roundTime);
            if (!isAlreadySpeaking) {
                speechIntegerMap.put(new BubbleSpeech(gameCharacter, getText()), timeOut);
            }
        }
        drawBubbles();
    }

    private String getText() {
        return texts.get(RandomUtils.between0AndExcluded(texts.size()));
    }

    private void drawBubbles() {
        spriteBatch.begin();
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

    public void dispose() {
        generator.dispose();
    }

}
