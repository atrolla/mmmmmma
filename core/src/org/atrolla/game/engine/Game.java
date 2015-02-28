package org.atrolla.game.engine;

import org.atrolla.game.ai.AIManager;
import org.atrolla.game.characters.*;
import org.atrolla.game.configuration.ConfigurationConstants;
import org.atrolla.game.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.IntStream;

/**
 * Created by MicroOnde on 25/02/2015.
 */
public class Game {
    private final Stage stage;
    private final Collection<GameCharacter> characters;
    private final AIManager aiManager;
    private int time;


    public Game(Stage stage) {
        this.stage = stage;
        this.characters = new ArrayList<>(ConfigurationConstants.GAME_CHARACTERS);
        this.aiManager = new AIManager(ConfigurationConstants.GAME_CHARACTERS);
        this.time = 0;
        initCharacters();
    }

    public Game() {
        this.stage = new Stage();
        this.characters = new ArrayList<>(ConfigurationConstants.GAME_CHARACTERS);
        this.aiManager = new AIManager(ConfigurationConstants.GAME_CHARACTERS);
        this.time = 0;
        initCharacters();
    }

    public Stage getStage() {
        return stage;
    }

    public Collection<GameCharacter> getCharacters() {
        return characters;
    }

    public void initCharacters() {
        final int endInclusive = ConfigurationConstants.GAME_CHARACTERS / 4;
        IntStream
                .rangeClosed(1, endInclusive)
                .forEach(i -> {
                            characters.add(new Archer(new Coordinates(i * 50, 50), Player.BOT));
                            characters.add(new Bomber(new Coordinates(i * 50, 100), Player.BOT));
                            characters.add(new Knight(new Coordinates(i * 50, 150), Player.BOT));
                            characters.add(new Mage(new Coordinates(i * 50, 200), Player.BOT));
                        }
                );
    }

    public void update() {
        time++;
        aiManager.updateBots(characters);
        preventCharacterToBeOutOfBound();
        aiManager.updateCommands(time);
    }

    private void preventCharacterToBeOutOfBound() {
        final double height = stage.getHeight();
        final double width = stage.getWidth();
        int index = 0;
        for (GameCharacter character : characters) {
            // TODO : merge for perf ?
            if (stage.isOutOfBound(character)) {
                double x = character.getCoordinates().getX();
                double y = character.getCoordinates().getY();
                x = x < 0 ? 0 : x > width ? width : x;
                y = y < 0 ? 0 : y > height ? height : y;
                final Coordinates coordinates = new Coordinates(x, y);
                character.teleports(coordinates);
                // TODO : bots that are prevented must choose another valid direction = command.
                aiManager.goAwayFromWall(index, time, coordinates);
            }
            index++;
        }
    }

    public AIManager getAIManager() {
        return aiManager;
    }
}
