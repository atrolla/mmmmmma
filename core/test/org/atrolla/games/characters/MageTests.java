package org.atrolla.games.characters;

import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.Item;
import org.atrolla.games.items.weapons.Arrow;
import org.atrolla.games.items.weapons.Bomb;
import org.atrolla.games.items.weapons.MageWeaponWrapper;
import org.atrolla.games.items.weapons.Sword;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by MicroOnde on 22/03/2015.
 */
public class MageTests {


    private static final int ABILITY_USE_TIME = 0;
    private Mage mage;

    @Before
    public void setUp() throws Exception {
        mage = new Mage(new Player(Optional.empty(), Optional.empty()));
        mage.teleports(new Coordinates(42, 1337));
    }

    @Test
    public void mageBotCannotUseHisAbility() throws Exception {
        final Mage magebot = new Mage(Player.BOT);
        assertThat(magebot.useAbility(ABILITY_USE_TIME).isPresent()).isFalse();
    }

    @Test
     public void mageTurnsIntoAnotherClass() throws Exception {
        mage.turnsInto(CharacterClasses.BOMBER);
        assertThat(mage.getDisguisedCharacter()).isPresent();
        assertThat(mage.getDisguisedCharacter().get() instanceof Bomber);
        assertThat(mage.getCharacterClass().isPresent()).isTrue();
        assertThat(mage.getCharacterClass().get()).isEqualTo(Bomber.class);
    }

    @Test
    public void mageTurnedIntoKnightAbilityIsToUseHisSword() throws Exception {
        mage.turnsInto(CharacterClasses.KNIGHT);
        Item ability = mage.useAbility(ABILITY_USE_TIME).get();
        assertThat(ability).isInstanceOf(MageWeaponWrapper.class);
        assertThat(((MageWeaponWrapper)ability).getWeapon()).isInstanceOf(Sword.class);

    }

    @Test
    public void mageTurnedIntoArcherAbilityIsToThrowArrow() throws Exception {
        mage.turnsInto(CharacterClasses.ARCHER);
        Item ability = mage.useAbility(ABILITY_USE_TIME).get();
        assertThat(ability).isInstanceOf(MageWeaponWrapper.class);
        assertThat(((MageWeaponWrapper)ability).getWeapon()).isInstanceOf(Arrow.class);
    }

    @Test
    public void mageTurnedIntoBomberAbilityIsToCreateBomb() throws Exception {
        mage.turnsInto(CharacterClasses.BOMBER);
        Item ability = mage.useAbility(ABILITY_USE_TIME).get();
        assertThat(ability).isInstanceOf(MageWeaponWrapper.class);
        assertThat(((MageWeaponWrapper)ability).getWeapon()).isInstanceOf(Bomb.class);

    }

    @Test
    public void mageTurnedIntoBomberGetsBomberCooldown() throws Exception {
        mage.turnsInto(CharacterClasses.BOMBER);
        assertThat(mage.useAbility(ABILITY_USE_TIME)).isPresent();
        assertThat(mage.useAbility(ABILITY_USE_TIME)).isEmpty(); //cooldown
        assertThat(mage.useAbility(ABILITY_USE_TIME+ ConfigurationConstants.BOMBER_ABILITY_COOLDOWN_DURATION)).isEmpty();
        assertThat(mage.useAbility(ABILITY_USE_TIME+ ConfigurationConstants.BOMBER_ABILITY_COOLDOWN_DURATION + 1)).isPresent();

    }

    @Test
    public void mageTurnedIntoArcherGetsArcherCooldown() throws Exception {
        mage.turnsInto(CharacterClasses.ARCHER);
        assertThat(mage.useAbility(ABILITY_USE_TIME)).isPresent();
        assertThat(mage.useAbility(ABILITY_USE_TIME)).isEmpty(); //cooldown
        assertThat(mage.useAbility(ABILITY_USE_TIME+ ConfigurationConstants.ARCHER_ABILITY_COOLDOWN_DURATION)).isEmpty();
        assertThat(mage.useAbility(ABILITY_USE_TIME+ ConfigurationConstants.ARCHER_ABILITY_COOLDOWN_DURATION + 1)).isPresent();

    }

    @Test
    public void mageTurnedIntoKnightGetsKnightCooldown() throws Exception {
        mage.turnsInto(CharacterClasses.KNIGHT);
        assertThat(mage.useAbility(ABILITY_USE_TIME)).isPresent();
        assertThat(mage.useAbility(ABILITY_USE_TIME)).isEmpty(); //cooldown
        assertThat(mage.useAbility(ABILITY_USE_TIME+ ConfigurationConstants.KNIGHT_ABILITY_COOLDOWN_DURATION)).isEmpty();
        assertThat(mage.useAbility(ABILITY_USE_TIME+ ConfigurationConstants.KNIGHT_ABILITY_COOLDOWN_DURATION + 1)).isPresent();

    }

    @Test
    public void mageCanOnlyKillSamePlayerClass() throws Exception {
        mage.turnsInto(CharacterClasses.BOMBER);

    }
}
