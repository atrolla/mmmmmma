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

        @Override
        public Class getClazz() {
            return Archer.class;
        }
    }, BOMBER {
        @Override
        public GameCharacter createCharacter(Player player) {
            return new Bomber(player);
        }

        @Override
        public Class getClazz() {
            return Bomber.class;
        }
    }, KNIGHT {
        @Override
        public GameCharacter createCharacter(Player player) {
            return new Knight(player);
        }

        @Override
        public Class getClazz() {
            return Knight.class;
        }
    }, MAGE { /** @see org.atrolla.games.game.Round#addBotsToCharactersCollection() */
        @Override
        public GameCharacter createCharacter(Player player) {
            return new Mage(player);
        }

        @Override
        public Class getClazz() {
            return Mage.class;
        }
    };

    public abstract GameCharacter createCharacter(Player player);
    public abstract Class getClazz();

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
