package org.atrolla.games.game;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import org.atrolla.games.ai.AIManager;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.input.InputManager;
import org.atrolla.games.items.Item;
import org.atrolla.games.items.neutrals.NeutralItem;
import org.atrolla.games.items.neutrals.NeutralItemManager;
import org.atrolla.games.items.weapons.Arrow;
import org.atrolla.games.items.weapons.Bomb;
import org.atrolla.games.items.weapons.MageSpell;
import org.atrolla.games.items.weapons.Sword;
import org.atrolla.games.sounds.SoundManager;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.world.Stage;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by MicroOnde on 25/02/2015.
 * <p>
 * A round contains every mechanism needed to play
 * </p>
 */
public class Round {
    private final Stage stage;
    private final RoundCharacters characters;
    private final Optional<AIManager> aiManager;
    private final Optional<InputManager> inputManager;
    protected int time;

    protected final GameItems gameItems;
    private final Optional<SoundManager> soundManager;
    private final Optional<NeutralItemManager> neutralItemManager;

    public Round(InputManager inputManager, SoundManager soundManager) {
        this.stage = new Stage();
        this.inputManager = Optional.ofNullable(inputManager);
        this.soundManager = Optional.ofNullable(soundManager);
        this.characters = new RoundCharacters(ConfigurationConstants.GAME_CHARACTERS, inputManager.getPlayers());
        this.gameItems = new GameItems();
        this.time = 0;
        this.aiManager = Optional.of(new AIManager(characters.bots.size()));
        this.neutralItemManager = Optional.of(new NeutralItemManager());
        characters.placeCharactersOnStage();
    }

    public Round(RoundCharacters roundCharacters, AIManager aiManager) {
        this.stage = new Stage();
        this.inputManager = Optional.empty();
        this.soundManager = Optional.empty();
        this.characters = roundCharacters;
        this.gameItems = new GameItems();
        this.time = 0;
        this.aiManager = Optional.ofNullable(aiManager);
        this.neutralItemManager = Optional.empty();
    }

    public Stage getStage() {
        return stage;
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
        playSounds(); //TODO: remove for sound
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
        gameItems.update(time);
        neutralItemManager.ifPresent(ni -> ni.addItem(time).ifPresent(i -> gameItems.registerItem(i, time)));
        getGameItems().stream().filter(NeutralItem.class::isInstance).map(NeutralItem.class::cast)
                .filter(NeutralItem::isUsed)
                .forEach(ni -> ni.applyEffect(this, gameItems));
        final Iterator<Item> iterator = getGameItems().iterator();
        final int time1 = this.time + 1;
        while (iterator.hasNext()) {
            final Item item = iterator.next();
            if (item.update(time)) { //update the item state if needed (ex: Arrow moves)
                iterator.remove();
                soundManager.ifPresent(sm -> sm.register(item));  // play item sound when it disappears.
            } else {
                gameItems.registerItem(item, time1);
            }
        }
    }

    /**
     * <ol>
     * <li>Awake KO characters</li>
     * <li>Move bots</li>
     * </ol>
     */
    private void manageBots() {
        characters.updateKOCharactersState(time);
        aiManager.ifPresent(ai -> ai.updateBotsMove(time, characters.bots));
    }

    /**
     * Update players and add the newly created item to gameItems list for the next time
     */
    private void managePlayers() {
        inputManager.ifPresent(im -> gameItems.registerItem(im.updatePlayers(time, characters.players), time + 1));
    }

    /**
     * <ol>
     * <li>Players picking items</li>
     * <li>Manage GameCharacters going to the walls</li>
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
        soundManager.ifPresent(SoundManager::playAllSounds);
    }

    /**
     * update time
     */
    private void postUpdate() {
        time++;
        aiManager.ifPresent(ai -> ai.updateCommands(time));
    }

    /**
     * For each NeutralItem, the first player to hit it picks it.
     *
     * @see NeutralItem
     * @see NeutralItem#isPicked(GameCharacter)
     */
    private void neutralItemsPick() {
        getGameItems().stream().filter(NeutralItem.class::isInstance).map(NeutralItem.class::cast)
                .filter(item -> !item.getPicker().isPresent()).forEach(
                item -> characters.players.stream()
                        .filter(player -> Intersector.overlaps(item.getHitbox(), player.getHitbox()))
                        .findFirst()
                        .ifPresent(item::isPicked)
        );
    }

    /**
     * Players being hit will die
     * Bots will only be knocked out and awakes later on
     *
     * @see GameCharacter#hit(GameCharacter)
     * @see Sword
     */
    private void knockOutCharactersBeingHitByWeapons() {
        //TODO : sword must not kill now but after a while (considered like poison)
        //manage sword and arrow hit
        getGameItems().stream().filter(i -> Sword.class.isInstance(i) || Arrow.class.isInstance(i))
                .forEach(weaponHitConsumer(false));
        getGameItems().stream().filter(Bomb.class::isInstance).map(Bomb.class::cast)
                .filter(Bomb::isExploding) // only exploding bombs will hit characters
                .forEach(weaponHitConsumer(true));
        getGameItems().stream().filter(MageSpell.class::isInstance).map(MageSpell.class::cast)
                .filter(MageSpell::isTriggered) // only triggered mage spell will hit characters
                .forEach(weaponHitConsumer(true));
    }

    private Consumer<Item> weaponHitConsumer(boolean canKillUser) {
        return weapon -> weaponHitConsumer(canKillUser, weapon, weapon.getUser());
    }

    private void weaponHitConsumer(boolean canKillUser, Item weapon, final GameCharacter user) {
        characters.characters.stream()
                .filter(GameCharacter::isAlive) // never hit a KO chacacter
                .filter(c -> canKillUser || !c.equals(weapon.getUser())) // if canKillUser = false, then must not kill its user
                .filter(c -> Intersector.overlaps(weapon.getHitbox(), c.getHitbox()))
                .forEach(c -> c.hit(user));
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
        for (GameCharacter character : characters.characters) {
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
                final int botIndexz = botIndex;
                aiManager.ifPresent(ai -> {
                    if (!character.isPlayer()) {
                        ai.goAwayFromWall(botIndexz, time, coordinates);
                    }
                });
            }
            if (!character.isPlayer()) {
                botIndex++;
            }
        }
    }

    public boolean isFinished() {
        if (characters.players.size() == 1) {
            return !characters.players.stream().anyMatch(GameCharacter::isNotDead);
        } else {
            final long count = characters.players.stream().filter(GameCharacter::isNotDead).count();
            return count <= 1;
        }
    }

    public List<Item> getGameItems() {
        return gameItems.getEvents(time);
    }

    public int getTime() {
        return time;
    }

    public RoundCharacters getCharacters() {
        return characters;
    }
}
