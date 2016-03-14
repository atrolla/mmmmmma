package org.atrolla.games.items.neutrals;

import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.game.GameItems;
import org.atrolla.games.game.Round;
import org.atrolla.games.system.Coordinates;

import java.util.Random;

/**
 * Created by MicroOnde on 12/03/2016.
 */
public class Switch extends NeutralItem {

    public Switch(Coordinates coordinates, int timeout) {
        super(coordinates, timeout);
    }

    @Override
    public void applyEffect(Round round, GameItems gameItems) {
        if (isUsed() && getPicker().isPresent()) {
            final GameCharacter picker = getPicker().get();
            final Class<? extends GameCharacter> pickerClass = picker.getClass();
            Random r = new Random();
            final long count = round.getCharacters().characters.stream().filter(pickerClass::isInstance).filter(c -> !c.equals(picker)).count();//skip();
            final int randomSkip = r.nextInt((int) count);
            final GameCharacter switcher = round.getCharacters().characters.stream().filter(pickerClass::isInstance).filter(c -> !c.equals(picker)).skip(randomSkip).findFirst().get();
            final Coordinates coordinates = picker.getCoordinates();
            picker.teleports(switcher.getCoordinates());
            switcher.teleports(coordinates);
        }
    }

}
