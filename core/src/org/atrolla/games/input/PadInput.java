package org.atrolla.games.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import org.apache.commons.lang3.SystemUtils;

/**
 * Created by CopyCat on 11/11/2015.
 */
public abstract class PadInput {
    public static PadInput getPadInput() {
        if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX) {
            return new OsxXboxPadInput();
        }
        return new Xbox360PadInput();
    }

    public abstract int buttonX();
    public abstract int buttonY();
    public abstract int buttonA();
    public abstract int buttonB();
    public abstract int buttonBack();
    public abstract int buttonStart();
    public abstract int buttonLB();
    public abstract int buttonLS();
    public abstract int buttonRB();
    public abstract int buttonRS();
    public abstract int axisLeftX();
    public abstract int axisLeftY();
    public abstract int axisLeftTrigger();
    public abstract int axisRightX();
    public abstract int axisRightY();
    public abstract int axisRightTrigger();
    public abstract PovDirection getPovDirection(Controller controller);
}
