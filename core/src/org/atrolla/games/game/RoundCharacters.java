package org.atrolla.games.game;

import com.google.common.collect.ImmutableList;
import org.atrolla.games.characters.CharacterClasses;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Player;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class RoundCharacters {

    public final List<GameCharacter> characters;
    public final List<GameCharacter> players;
    public final List<GameCharacter> bots;
    private final Map<GameCharacter, Integer> knockOutAwakeTime;

    public RoundCharacters(int size, List<Player> players) {
        List<GameCharacter> gameCharacters = new ArrayList<>(size);
        players.forEach(p -> gameCharacters.add(p.getGameCharacterClass().createCharacter(p)));
        addBotsToCharactersCollection(gameCharacters, size);
        Collections.shuffle(gameCharacters);
        this.characters = ImmutableList.copyOf(gameCharacters);
        this.bots = characters.stream().filter(c -> !c.isPlayer()).collect(collectingAndThen(toList(), ImmutableList::copyOf));
        this.players = characters.stream().filter(GameCharacter::isPlayer).collect(collectingAndThen(toList(), ImmutableList::copyOf));
        knockOutAwakeTime = new HashMap<>();
    }

    public RoundCharacters(List<GameCharacter> characters) {
        this.characters = ImmutableList.copyOf(characters);
        this.bots = characters.stream().filter(c -> !c.isPlayer()).collect(collectingAndThen(toList(), ImmutableList::copyOf));
        this.players = characters.stream().filter(GameCharacter::isPlayer).collect(collectingAndThen(toList(), ImmutableList::copyOf));
        knockOutAwakeTime = new HashMap<>();
    }

    /**
     * creates bots with same classes as players
     */
    private void addBotsToCharactersCollection(List<GameCharacter> gameCharacters, int size) {
        List<CharacterClasses> visibleClasses = initVisibleClasses(gameCharacters);
        final int players = gameCharacters.size();
        for (int i = players; i < size; i++) {
            gameCharacters.add(visibleClasses.get(i % visibleClasses.size()).createCharacter(Player.BOT));
        }
    }

    /**
     * get which classes players chose
     */
    private List<CharacterClasses> initVisibleClasses(List<GameCharacter> gameCharacters) {
        List<CharacterClasses> visibleClasses = gameCharacters.stream()
                .map(c -> c.getPlayer().getGameCharacterClass())
                .distinct()
                .collect(toList());
        if (visibleClasses.size() == 0) {
            final int i = org.apache.commons.lang3.RandomUtils.nextInt(0, 3);
            /** Mage must be last enum in CharacterClasses */
            visibleClasses.add(CharacterClasses.values()[i]);
        }
        return visibleClasses;
    }

    /**
     * place characters randomly on screen
     */
    public void placeCharactersOnStage() {
        final int endInclusive = characters.size() / 4;
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
     * Each new KO character is put in a map so that after a time, they will awake and move again.
     *
     * @see GameCharacter#isKnockOut()
     * @see ConfigurationConstants#KNOCK_OUT_DURATION
     */
    public void updateKOCharactersState(final int time) {
        characters.stream().filter(GameCharacter::isKnockOut).forEach(character -> {
            if (knockOutAwakeTime.containsKey(character)) {
                if (time >= knockOutAwakeTime.get(character)) {
                    knockOutAwakeTime.remove(character);
                    character.awake();
                }
            } else {
                knockOutAwakeTime.put(character, time + ConfigurationConstants.KNOCK_OUT_DURATION);
            }
        });
    }
}
