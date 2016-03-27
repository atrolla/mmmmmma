package org.atrolla.games.desktop.game;

import org.atrolla.games.characters.*;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.game.Round;
import org.atrolla.games.game.RoundCharacters;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Direction;
import org.atrolla.games.system.Player;

import java.util.ArrayList;
import java.util.List;

public class Demo extends Round {

    private final GameCharacter knightP;
    private final GameCharacter archerP;
    private final GameCharacter bomberP;
    private final GameCharacter mageP;

    private Demo(RoundCharacters roundCharacters, GameCharacter knightP, GameCharacter archerP, GameCharacter bomberP, GameCharacter mageP) {
        super(roundCharacters, null);
        this.knightP = knightP;
        this.archerP = archerP;
        this.bomberP = bomberP;
        this.mageP = mageP;
    }

    public static class Builder {
        private List<GameCharacter> characters;
        private GameCharacter knightP;
        private GameCharacter archerP;
        private GameCharacter bomberP;
        private GameCharacter mageP;

        public Builder() {
            characters = new ArrayList<>(18);
            characters.add(new Archer(Player.BOT));
            characters.add(new Bomber(Player.BOT));
            characters.add(new Knight(Player.BOT));
            characters.add(new Bomber(Player.BOT));
            characters.add(new Mage(Player.BOT));
            characters.add(new Knight(Player.BOT));
            characters.add(new Archer(Player.BOT));
            characters.add(new Mage(Player.BOT));
            characters.add(new Bomber(Player.BOT));
            characters.add(new Knight(Player.BOT));
            characters.add(new Archer(Player.BOT));
            characters.add(new Mage(Player.BOT));
            characters.add(new Bomber(Player.BOT));
            characters.add(new Knight(Player.BOT));
            characters.add(new Archer(Player.BOT));
            knightP = new Knight(new Player(CharacterClasses.KNIGHT));
            characters.add(knightP);
            archerP = new Archer(new Player(CharacterClasses.ARCHER));
            characters.add(archerP);
            bomberP = new Bomber(new Player(CharacterClasses.BOMBER));
            characters.add(bomberP);
            mageP = new Mage(new Player(CharacterClasses.MAGE));
            characters.add(mageP);
            placeCharacters();
        }

        public Demo build() {
            return new Demo(new RoundCharacters(characters), knightP, archerP, bomberP, mageP);
        }

        private void placeCharacters() {
            knightP.teleports(new Coordinates(100, 265));
            characters.get(0).teleports(new Coordinates(118, 257));
            characters.get(1).teleports(new Coordinates(100, 220));
            //
            archerP.teleports(new Coordinates(330, 320));
            characters.get(2).teleports(new Coordinates(350, 270));
            characters.get(3).teleports(new Coordinates(315, 225));
            characters.get(4).teleports(new Coordinates(330, 180));
            characters.get(5).teleports(new Coordinates(370, 225));
            bomberP.teleports(new Coordinates(910, 380));
            characters.get(6).teleports(new Coordinates(930, 275));
            characters.get(7).teleports(new Coordinates(880, 230));
            characters.get(8).teleports(new Coordinates(910, 180));
            mageP.teleports(new Coordinates(1150, 260));
            characters.get(9).teleports(mageP.getCoordinates().translateXandY(ConfigurationConstants.MAGE_SPELL_OFFSET, ConfigurationConstants.MAGE_SPELL_OFFSET));
            characters.get(10).teleports(mageP.getCoordinates().translateXandY(ConfigurationConstants.MAGE_SPELL_OFFSET, -ConfigurationConstants.MAGE_SPELL_OFFSET));
            characters.get(11).teleports(mageP.getCoordinates().translateXandY(-ConfigurationConstants.MAGE_SPELL_OFFSET, ConfigurationConstants.MAGE_SPELL_OFFSET));
            characters.get(12).teleports(mageP.getCoordinates().translateXandY(-ConfigurationConstants.MAGE_SPELL_OFFSET, -ConfigurationConstants.MAGE_SPELL_OFFSET));
            characters.get(13).teleports(mageP.getCoordinates().translateXandY(-ConfigurationConstants.MAGE_SPELL_OFFSET/2, -ConfigurationConstants.MAGE_SPELL_OFFSET));
            characters.get(14).teleports(mageP.getCoordinates().translateXandY(ConfigurationConstants.MAGE_SPELL_OFFSET, 0));
        }
    }

    @Override
    public void update() {
        super.update();
        gameItems.registerItem(knightP.useAbility(time), time + 1);
        gameItems.registerItem(archerP.useAbility(time), time + 1);
        if (bomberP.isAbilityCoolingDown(time)) {
            if (bomberP.getCoordinates().getY() <= 380) {
                bomberP.moves(time, Direction.UP);
            } else {
                bomberP.moves(time, Direction.STOP);
            }
        } else {
            if (bomberP.getCoordinates().getY() <= 200) {
                gameItems.registerItem(bomberP.useAbility(time), time + 1);
            } else {
                bomberP.moves(time, Direction.DOWN);
            }
        }
//        gameItems.registerItem(bomberP.useAbility(time), time + 1);
        gameItems.registerItem(mageP.useAbility(time), time + 1);
    }
}
