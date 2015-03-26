package org.atrolla.games.characters;

import org.atrolla.games.items.Item;
import org.atrolla.games.system.Player;

import java.util.Optional;

/**
 * Created by MicroOnde on 24/02/2015.
 */
public class Mage extends GameCharacter {
    private Optional<GameCharacter> disguisedCharacter;

    public Mage(Player player) {
        super(player);
        disguisedCharacter = Optional.empty();
    }

    @Override
    public void coolDownAbility(int time) {
        // nocooldown
    }

    @Override
    public Optional<Item> useAbility(int time) {
        if (disguisedCharacter.isPresent()) {
            final GameCharacter gameCharacter = disguisedCharacter.get();
            gameCharacter.teleports(this.getCoordinates(), getDirection());
            return gameCharacter.useAbility(time);
        }
        return super.useAbility(time);
    }

    public void turnsInto(CharacterClasses characterClass) {
        this.disguisedCharacter = Optional.of(characterClass.createCharacter(getPlayer()));
    }

    public Optional<Class> getCharacterClass() {
        return disguisedCharacter.map(d -> d.getClass());
    }
}
