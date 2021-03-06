package org.atrolla.games.ai;

import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Direction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import static org.atrolla.games.ai.Command.RandomCommand;

/**
 * Created by MicroOnde on 25/02/2015.
 * <p>
 * This class manages bots movements
 * <p>
 * <p>
 * bots are smart enough to not go into walls
 * bots can stop walking
 */
public class AIManager {

    private final List<Command> commands;


    public AIManager(int botNumber) {
        commands = new ArrayList<>(botNumber);
        initCommands(botNumber);
    }

    /**
     * add a random Command to each bot
     *
     * @see Command
     */
    private void initCommands(int botNumber) {
        IntConsumer addRandomCommand = i -> commands.add(RandomCommand(0, Coordinates.NULL));
        IntStream.rangeClosed(1, botNumber)
                .forEachOrdered(addRandomCommand);
    }

    public List<Command> getCommands() {
        return commands;
    }

    /**
     * move bots accordingly to their command
     *
     * @see GameCharacter#moves(int,Direction)
     */
    public void updateBotsMove(int time, final Collection<GameCharacter> bots) {
        int i = 0;
        for (GameCharacter character : bots) {
            if (!character.isPlayer()) {
                final Command command = commands.get(i);
                character.moves(time, command.getDirection());
                i++;
            } else {
                System.err.println("Found a player in bot list for updateBotsMove, please remove in code");
            }

        }
    }

    /**
     * for each finished Command, replace with a new one
     *
     * @see Command#checkIsDone(int)
     */
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

}
