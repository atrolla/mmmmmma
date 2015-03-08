package org.atrolla.game.ai;

import org.atrolla.game.characters.GameCharacter;
import org.atrolla.game.configuration.ConfigurationConstants;
import org.atrolla.game.system.Coordinates;

import java.util.*;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import static org.atrolla.game.ai.Command.RandomCommand;

/**
 * Created by MicroOnde on 25/02/2015.
 */
public class AIManager {

    /**
     * AI are smart enough to not go into walls
     * AI can stop walking
     */

    private List<Command> commands;
    private Map<GameCharacter, Integer> knockOutBotsAwakeTime;

    //TODO : replace characterNumber with  botNumber...
    public AIManager(int characterNumber) {
        commands = new ArrayList<>(characterNumber);
        knockOutBotsAwakeTime = new HashMap<>();
        initCommands(characterNumber);
    }

    private void initCommands(int characterNumber) {
        IntConsumer addRandomCommand = i -> commands.add(RandomCommand(0, Coordinates.NULL));
        IntStream.rangeClosed(1, characterNumber)
                .forEachOrdered(addRandomCommand);
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void updateBotsMove(final Collection<GameCharacter> characters) {
        int i = 0;
        for (GameCharacter character : characters) {
            if (!character.isPlayer()) {
                final Command command = commands.get(i);
                character.moves(command.getDirection());
            }
            i++;
        }
    }

    public void updateCommands(int time) {
        final ListIterator<Command> commandListIterator = commands.listIterator();
        while (commandListIterator.hasNext()) {
            final Command command = commandListIterator.next();
            if (command.checkIsDone(time)) {
                commandListIterator.set(RandomCommand(time, Coordinates.NULL));
            }
        }
    }

    public void goAwayFromWall(int index, int time, Coordinates coordinates) {
        commands.set(index, RandomCommand(time, coordinates));
    }

    public void updateBotsState(final Collection<GameCharacter> characters,final int time) {
        characters.stream().filter(character -> character.isKnockOut()).forEach(character -> {
            if (knockOutBotsAwakeTime.containsKey(character)) {
                if (time >= knockOutBotsAwakeTime.get(character)) {
                    knockOutBotsAwakeTime.remove(character);
                    character.awake();
                }
            } else {
                knockOutBotsAwakeTime.put(character, time + ConfigurationConstants.BOT_KNOCK_OUT_DURATION);
            }
        });
    }
}
