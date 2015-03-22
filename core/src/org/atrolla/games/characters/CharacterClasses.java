package org.atrolla.games.characters;

import org.atrolla.games.system.Player;

/**
 * Created by MicroOnde on 22/03/2015.
 */
public enum CharacterClasses {

    ARCHER {
        @Override
        public GameCharacter createCharacter(Player player) {
            return new Archer(player);
        }
    }, BOMBER {
        @Override
        public GameCharacter createCharacter(Player player) {
            return new Bomber(player);
        }
    }, KNIGHT {
        @Override
        public GameCharacter createCharacter(Player player) {
            return new Knight(player);
        }
    }, MAGE {
        @Override
        public GameCharacter createCharacter(Player player) {
            return new Mage(player);
        }
    };

    public abstract GameCharacter createCharacter(Player player);

    public static CharacterClasses next(CharacterClasses current) {
        final CharacterClasses[] values = CharacterClasses.values();
        return values[(current.ordinal() + 1) % values.length];
    }

    public static CharacterClasses previous(CharacterClasses current) {
        final CharacterClasses[] values = CharacterClasses.values();
        final int i = current.ordinal() - 1;
        return values[i == -1 ? values.length - 1 : i];
    }
}
