package org.atrolla.games.items.weapons;

import com.badlogic.gdx.math.Circle;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.items.Item;

public class MageWeaponWrapper extends Item {
    private final Item weapon;

    public MageWeaponWrapper(Item weapon, GameCharacter user) {
        super(weapon.getCoordinates(), weapon.getTimeout(), user);
        this.weapon = weapon;
    }

    @Override
    public boolean update(int timeTick) {
        return weapon.update(timeTick);
    }

    public Item getWeapon() {
        return weapon;
    }

    @Override
    public Circle getHitbox() {
        return weapon.getHitbox();
    }
}
