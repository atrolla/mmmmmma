package org.atrolla.games.game;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import org.atrolla.games.ai.AIManager;
import org.atrolla.games.characters.Bomber;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.input.InputManager;
import org.atrolla.games.items.Item;
import org.atrolla.games.items.neutrals.NeutralItem;
import org.atrolla.games.items.neutrals.NeutralItemManager;
import org.atrolla.games.sounds.SoundManager;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Player;
import org.atrolla.games.world.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by MicroOnde on 25/02/2015.
 */
public class Round {
    private final Stage stage;
    private final List<GameCharacter> characters;
    private final List<GameCharacter> players;
    private final List<GameCharacter> bots;
    private final AIManager aiManager;
    private final InputManager inputManager;
    private int time;
    private final List<Item> gameItems;
    private final SoundManager soundManager;
    private final NeutralItemManager neutralItemManager;

    public Round(Stage stage, InputManager inputManager, SoundManager soundManager) {
        this.stage = stage;
        this.inputManager = inputManager;
        this.soundManager = soundManager;
        this.characters = new ArrayList<>(ConfigurationConstants.GAME_CHARACTERS);
        this.time = 0;
        initCharacters();
        this.bots = characters.stream().filter(c -> !c.isPlayer()).collect(Collectors.toList());
        this.players = characters.stream().filter(GameCharacter::isPlayer).collect(Collectors.toList());
        this.aiManager = new AIManager(bots.size());
        this.gameItems = new ArrayList<>();
        this.neutralItemManager = new NeutralItemManager();
    }

    public Round(InputManager inputManager, SoundManager soundManager) {
        this(new Stage(), inputManager, soundManager);
    }

    public Stage getStage() {
        return stage;
    }

    public List<GameCharacter> getCharacters() {
        return characters;
    }

    public void initCharacters() {
        inputManager.getPlayers()
                .stream()
                .map(p -> p.getGameCharacterClass().createCharacter(p))
                .collect(Collectors.toCollection(() -> characters));
        for (int i = 0; i < ConfigurationConstants.GAME_CHARACTERS - inputManager.getPlayers().size(); i++) {
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
                .forEach(i -> IntStream
                                .rangeClosed(1, 4)
                                .forEach(j -> {
                                            final GameCharacter gameCharacter = characters.get((i - 1) * 4 + j - 1);
                                            gameCharacter.teleports(new Coordinates(xStep * j, yStep * i));
                                        }
                                )
                );
    }

    public void update() {
        manageItems();
        manageBots();
        managePlayers();
        manageHitBoxes();
        playSounds();
        postUpdate();
    }

    private void manageItems() {
        neutralItemManager.addItem(time).ifPresent(gameItems::add);
        gameItems.stream().filter(NeutralItem.class::isInstance).map(NeutralItem.class::cast).forEach(ni -> ni.applyEffect(this));
        final Iterator<Item> iterator = gameItems.iterator();
        while (iterator.hasNext()) {
            final Item item = iterator.next();
            if (item.update(time)) {
                iterator.remove();
                if (soundManager != null) {
                    soundManager.register(item);
                }
            }
        }
    }

    private void manageBots() {
        aiManager.updateBotsState(bots, time);
        aiManager.updateBotsMove(bots);
    }

    private void managePlayers() {
        gameItems.addAll(inputManager.updatePlayers(time, players));
    }

    private void manageHitBoxes() {
        neutralItemsPick();
        preventCharactersFromBeingOutOfBound();
    }

    private void playSounds() {
        if (soundManager != null) {
            soundManager.playAllSounds();
        }
    }

    private void postUpdate() {
        time++;
        aiManager.updateCommands(time);
    }

    private void neutralItemsPick() {
        gameItems.stream().filter(NeutralItem.class::isInstance).map(NeutralItem.class::cast).forEach(
                item -> players.stream()
                        .filter(player -> Intersector.overlaps(item.getHitbox(), player.getHitbox()))
                        .findFirst()
                        .ifPresent(item::isPicked)
        );
    }

    private void preventCharactersFromBeingOutOfBound() {
        final double height = stage.getHeight();
        final double width = stage.getWidth();
        int botIndex = 0;
        for (GameCharacter character : characters) {
            // TODO : merge for perf ?
            if (stage.isOutOfBound(character)) {
                Rectangle hitbox = character.getHitbox();
                double x = character.getCoordinates().getX();
                double y = character.getCoordinates().getY();
                x = hitbox.getX() < 0 ? 0 : hitbox.getX() + hitbox.getWidth() > width ? width - hitbox.getWidth() : x;
                y = hitbox.getY() < 0 ? 0 : hitbox.getY() + hitbox.getHeight() > height ? height - hitbox.getHeight() : y;
                final Coordinates coordinates = new Coordinates(x, y);
                character.teleports(coordinates);
                // bots that are prevented must choose another valid direction = command.
                if (!character.isPlayer()) {
                    aiManager.goAwayFromWall(botIndex, time, coordinates);

                }
            }
            if (!character.isPlayer()) {
                botIndex++;
            }
        }
    }

    public AIManager getAIManager() {
        return aiManager;
    }

    public List<Item> getGameItems() {
        return gameItems;
    }

    public int getTime() {
        return time;
    }

    public List<GameCharacter> getPlayers() {
        return players;
    }
}
