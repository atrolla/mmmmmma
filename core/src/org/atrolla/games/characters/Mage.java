package org.atrolla.games.characters;

import org.atrolla.games.items.Item;
import org.atrolla.games.items.weapons.MageWeaponWrapper;
import org.atrolla.games.system.Player;

import java.util.Optional;

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
            gameCharacter.teleports(this.getCoordinates(), getDirection()); //so we use the ability at the mage place
            final Optional<Item> weapon = gameCharacter.useAbility(time);
            return weapon.map(w -> new MageWeaponWrapper(w,this));
        }
        return super.useAbility(time); //empty
    }

    public void turnsInto(CharacterClasses characterClass) {
        this.disguisedCharacter = Optional.of(characterClass.createCharacter(getPlayer()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        /**
         * Hack to be able to have equality between a GameCharacter and the disguisedCharacter of mage
         */
        if(getClass() != o.getClass()) {
            return GameCharacter.class.isAssignableFrom(o.getClass())
                    && disguisedCharacter.isPresent()
                    && disguisedCharacter.get().getClass() == o.getClass()
                    && disguisedCharacter.get().equals(o);
        }
        if (!super.equals(o)) return false;

        Mage mage = (Mage) o;

        return !(disguisedCharacter != null ? !disguisedCharacter.equals(mage.disguisedCharacter) : mage.disguisedCharacter != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (disguisedCharacter != null ? disguisedCharacter.hashCode() : 0);
        return result;
    }

    public Optional<Class> getCharacterClass() {
        return disguisedCharacter.map(Object::getClass);
    }
}
