package org.atrolla.games.game;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import org.atrolla.games.ai.AIManager;
import org.atrolla.games.characters.CharacterClasses;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.characters.Mage;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.input.InputManager;
import org.atrolla.games.items.Item;
import org.atrolla.games.items.neutrals.NeutralItem;
import org.atrolla.games.items.neutrals.NeutralItemManager;
import org.atrolla.games.items.weapons.Arrow;
import org.atrolla.games.items.weapons.Bomb;
import org.atrolla.games.items.weapons.Sword;
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
 * <p>
 * A round contains every mechanism needed to play
 * </p>
 */
public class Round {
    private final Stage stage;
    private final List<GameCharacter> characters;
    private final List<GameCharacter> players;
    private final List<GameCharacter> bots;
    private List<CharacterClasses> visiblesClasses;
    private final AIManager aiManager;
    private final InputManager inputManager;
    private int time;
    /**
     * all items that are currently in the round
     */
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

    /**
     * creates bots with same classes as players and place characters randomly on screen
     */
    private void initCharacters() {
        inputManager.getPlayers().forEach(
                p -> characters.add(p.getGameCharacterClass().createCharacter(p))
        );
        addBotsToCharactersCollection();
        Collections.shuffle(characters);
        placeCharactersOnStage();
    }

    private void addBotsToCharactersCollection() {
        visiblesClasses = characters.stream()
                .map(c -> c.getPlayer().getGameCharacterClass())
                .filter(c -> c != CharacterClasses.MAGE)
                .distinct()
                .collect(Collectors.toList());// Get all playing classes that are not mage
        if(visiblesClasses.size()==0){ // there are only mages... get a random class
            final int i = org.apache.commons.lang3.RandomUtils.nextInt(0, 3);
            /** Mage must be last enum in CharacterClasses */
            visiblesClasses.add(CharacterClasses.values()[i]);
        }
        final int players = characters.size();
        for (int i = players; i <  ConfigurationConstants.GAME_CHARACTERS; i++) {
            characters.add(visiblesClasses.get(i % visiblesClasses.size()).createCharacter(Player.BOT));
        }
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

    /**
     * Update game state
     * <p>
     * This method should be called each time the game renders
     */
    public void update() {
        manageItems();
        manageBots();
        managePlayers();
        manageHitBoxes();
        playSounds();
        postUpdate();
    }

    /**
     * <ol>
     * <li>check if it is time to add a neutral item</li>
     * <li>check if it is time to remove used items</li>
     * </ol>
     *
     * @see NeutralItem
     */
    private void manageItems() {
        neutralItemManager.addItem(time).ifPresent(gameItems::add);
        //apply effect of any neutral item in gameItems
        // TODO : should not exists, neutral items effect is triggered when the player uses it
        gameItems.stream().filter(NeutralItem.class::isInstance).map(NeutralItem.class::cast)
                .forEach(
                        ni -> ni.applyEffect(this)
                );
        //
        final Iterator<Item> iterator = gameItems.iterator();
        while (iterator.hasNext()) {
            final Item item = iterator.next();
            if (item.update(time)) { //update the item state if needed (ex: Arrow moves)
                iterator.remove();
                if (soundManager != null) { // play item sound when it disappears.
                    //TODO: i think this limits the sound to only 1 per item...
                    soundManager.register(item);
                }
            }
        }
    }

    /**
     * <ol>
     * <li>Awake KO bots</li>
     * <li>Move bots</li>
     * </ol>
     */
    private void manageBots() {
        aiManager.updateBotsState(bots, time);
        aiManager.updateBotsMove(bots);
    }

    /**
     * Update players and add the newly created item to gameItems list
     */
    private void managePlayers() {
        //Make mage disguise if not already disguised
        players.stream()
                .filter(p -> Mage.class.equals(p.getClass()))
                .map(Mage.class::cast)
                .filter(m -> !m.getCharacterClass().isPresent())
                .forEach(m -> {
                    final int i = org.apache.commons.lang3.RandomUtils.nextInt(0, visiblesClasses.size());
                    final CharacterClasses characterClasses = visiblesClasses.get(i);
                    m.turnsInto(characterClasses);
                });
        gameItems.addAll(inputManager.updatePlayers(time, players));
    }

    /**
     * <ol>
     * <li>Players picking items</li>
     * <li>Manage GameCharacters goinng to the walls</li>
     * </ol>
     */
    private void manageHitBoxes() {
        neutralItemsPick();
        knockOutCharactersBeingHitByWeapons();
        preventCharactersFromBeingOutOfBound();
    }

    /**
     * Play sounds if a soundManager is present
     */
    private void playSounds() {
        if (soundManager != null) {
            soundManager.playAllSounds();
        }
    }

    /**
     * update time
     */
    private void postUpdate() {
        time++;
        aiManager.updateCommands(time);
    }

    /**
     * For each NeutralItem, the first player to hit it picks it.
     *
     * @see NeutralItem
     * @see NeutralItem#isPicked(GameCharacter)
     */
    private void neutralItemsPick() {
        gameItems.stream().filter(NeutralItem.class::isInstance).map(NeutralItem.class::cast).forEach(
                item -> players.stream()
                        .filter(player -> Intersector.overlaps(item.getHitbox(), player.getHitbox()))
                        .findFirst()
                        .ifPresent(item::isPicked)
        );
    }

    /**
     * Players being hit will die
     * Bots will only be knocked out and awakes later on
     *
     * @see GameCharacter#hit()
     * @see Sword
     */
    private void knockOutCharactersBeingHitByWeapons() {
        //TODO : sword must not kill now but after a while (considered like poison)
        /**
         * @see Mage#equals(java.lang.Object)
         */
        gameItems.stream().filter(Sword.class::isInstance).map(Sword.class::cast)
                .forEach(
                        sword -> characters.stream()
                                .filter(GameCharacter::canMove) // never hit a KO chacacter
                                .filter(c -> !c.equals(sword.getUser())) // sword must not kill its user
                                .filter(c -> Intersector.overlaps(sword.getHitbox(), c.getHitbox())) //sword must hit every character it overlaps
                                .forEach(GameCharacter::hit)
                );
        gameItems.stream().filter(Bomb.class::isInstance).map(Bomb.class::cast)
                .filter(Bomb::isExploding) // only exploding bombs will hit characters
                .forEach(
                        bomb -> characters.stream()
                                .filter(GameCharacter::canMove) // never hit a KO chacacter
                                .filter(player -> Intersector.overlaps(bomb.getHitbox(), player.getHitbox()))
                                .forEach(GameCharacter::hit)
                );
        gameItems.stream().filter(Arrow.class::isInstance).map(Arrow.class::cast)
                .forEach(
                        arrow -> characters.stream()
                                .filter(GameCharacter::canMove) // never hit a KO chacacter
                                .filter(c -> !c.equals(arrow.getUser())) // arrow must not kill its user
                                .filter(player -> Intersector.overlaps(arrow.getHitbox(), player.getHitbox()))
                                .forEach(GameCharacter::hit)
                );
        //TODO : manage others weapons hit here
    }

    /**
     * Players will not move when their hitbox are on bounds
     * Bots will also have to go away from the wall by having another command/direction
     *
     * @see GameCharacter#teleports(Coordinates)
     * @see AIManager#goAwayFromWall(int, int, Coordinates)
     */
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
