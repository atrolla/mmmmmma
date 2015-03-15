package org.atrolla.games.ai;

import oracle.jrockit.jfr.jdkevents.ThrowableTracer;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.system.Coordinates;

import java.util.*;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import static org.atrolla.games.ai.Command.RandomCommand;

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


    public AIManager(int botNumber) {
        commands = new ArrayList<>(botNumber);
        knockOutBotsAwakeTime = new HashMap<>();
        initCommands(botNumber);
    }

    private void initCommands(int botNumber) {
        IntConsumer addRandomCommand = i -> commands.add(RandomCommand(0, Coordinates.NULL));
        IntStream.rangeClosed(1, botNumber)
                .forEachOrdered(addRandomCommand);
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void updateBotsMove(final Collection<GameCharacter> bots) {
        int i = 0;
        for (GameCharacter character : bots) {
            if (!character.isPlayer()) {
                final Command command = commands.get(i);
                character.moves(command.getDirection());
                i++;
            } else {
                System.err.println("Found a player in bot list for updateBotsMove, please remove in code");
            }

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
