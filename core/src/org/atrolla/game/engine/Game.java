package org.atrolla.game.engine;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.utils.Array;
import org.atrolla.game.ai.AIManager;
import org.atrolla.game.characters.*;
import org.atrolla.game.configuration.ConfigurationConstants;
import org.atrolla.game.input.ControllerManager;
import org.atrolla.game.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by MicroOnde on 25/02/2015.
 */
public class Game {
    private final Stage stage;
    private final List<GameCharacter> characters;
    private final AIManager aiManager;
    private ControllerManager controllerManager;
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

    public List<GameCharacter> getCharacters() {
        return characters;
    }

    public void initCharacters() {
        /*
         TODO : create char from players classes & bots
         and then shuffle the list before putting them on stage
          */
        characters.add(new Archer(new Player()));
        characters.add(new Knight(Player.BOT));
        characters.add(new Bomber(Player.BOT));
        characters.add(new Mage(Player.BOT));

        for (int i = 0; i < ConfigurationConstants.GAME_CHARACTERS - 4; i++) {
            characters.add(new Bomber(Player.BOT));
        }
        Collections.shuffle(characters);
        placeCharactersOnStage();
    }

    private void placeCharactersOnStage() {
        final int endInclusive = ConfigurationConstants.GAME_CHARACTERS / 4;
        final double xStep = ConfigurationConstants.STAGE_WIDTH / 5;
        final double yStep = ConfigurationConstants.STAGE_HEIGHT / (endInclusive + 1);
        IntStream
                .rangeClosed(1, endInclusive)
                .forEach(i ->
                                IntStream
                                        .rangeClosed(1, 4)
                                        .forEach(j -> {
                                                    final GameCharacter gameCharacter = characters.get((i-1)*4+j-1);
                                                    gameCharacter.teleports(new Coordinates(xStep * j, yStep * i));
                                                }
                                        )
//                            final Archer e = new Archer(Player.BOT);
//                            e.teleports(new Coordinates(xStep, yStep * i));
//                            characters.add(e);
//                            final Bomber e1 = new Bomber(Player.BOT);
//                            e1.teleports(new Coordinates(2 * xStep, yStep * i));
//                            characters.add(e1);
//                            final Knight e2 = new Knight(Player.BOT);
//                            e2.teleports(new Coordinates(3 * xStep, yStep * i));
//                            characters.add(e2);
//                            final Mage e3 = new Mage(Player.BOT);
//                            e3.teleports(new Coordinates(4 * xStep, yStep * i));
//                            characters.add(e3);
                );
    }

    public void update() {
        aiManager.updateBotsState(characters, time);
        aiManager.updateBotsMove(characters);
        // TODO : update Players - only pass playerList
        controllerManager.updatePlayers(characters.stream().filter(c -> c.isPlayer()).collect(Collectors.toList()));
        preventCharactersFromBeingOutOfBound();
        time++;
        aiManager.updateCommands(time);
    }

    private void preventCharactersFromBeingOutOfBound() {
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
                // bots that are prevented must choose another valid direction = command.
                aiManager.goAwayFromWall(index, time, coordinates);
            }
            index++;
        }
    }

    public AIManager getAIManager() {
        return aiManager;
    }


    public void setControllers(Array<Controller> controllers) {
        controllerManager = new ControllerManager(controllers);
    }
}
