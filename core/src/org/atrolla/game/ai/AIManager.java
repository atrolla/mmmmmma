package org.atrolla.game.ai;

import org.atrolla.game.characters.GameCharacter;
import org.atrolla.game.engine.Command;
import org.atrolla.game.engine.Coordinates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import static org.atrolla.game.engine.Command.RandomCommand;

/**
 * Created by MicroOnde on 25/02/2015.
 */
public class AIManager {

    //TODO : AI must be smart enough to not go into walls...

    //TODO : AI can pause !

    private List<Command> commands;

    //TODO : replace characterNumber with  botNumber...
    public AIManager(int characterNumber) {
        commands = new ArrayList<>(characterNumber);
        initCommands(characterNumber);
    }

    private void initCommands(int characterNumber) {
        IntConsumer addRandomCommand = i -> commands.add(RandomCommand());
        IntStream.rangeClosed(1, characterNumber)
                .forEachOrdered(addRandomCommand);
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void updateBots(final Collection<GameCharacter> characters) {
        //TODO : should filter on bots...
        int i = 0;
        for (GameCharacter character : characters) {
            final Command command = commands.get(i);
            character.moves(command.getDirection());
            i++;
        }
    }

    public void updateCommands(int time) {
        final ListIterator<Command> commandListIterator = commands.listIterator();
        while (commandListIterator.hasNext()) {
            final Command command = commandListIterator.next();
            if (command.checkIsDone(time)) {
                commandListIterator.set(RandomCommand());
            }
        }
    }

    public void goAwayFromWall(int index, Coordinates coordinates) {
        commands.set(index,RandomCommand(coordinates));
    }
}
