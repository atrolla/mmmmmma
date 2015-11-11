package org.atrolla.games.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import org.atrolla.games.system.Direction;

/**
 * Created by CopyCat on 11/11/2015.
 */
public class OsxXboxPadInput extends PadInput {
    /*
     * It seems there are different versions of gamepads with different ID Strings.
     * Therefore its IMO a better bet to check for:
     * if (controller.getName().toLowerCase().contains("xbox") &&
                   controller.getName().contains("360"))
     *
     * Controller (Gamepad for Xbox 360)
       Controller (XBOX 360 For Windows)
       Controller (Xbox 360 Wireless Receiver for Windows)
       Controller (Xbox wireless receiver for windows)
       XBOX 360 For Windows (Controller)
       Xbox 360 Wireless Receiver
       Xbox Receiver for Windows (Wireless Controller)
       Xbox wireless receiver for windows (Controller)
     */
//    public final String ID = "XBOX 360 For Windows (Controller)";
    public int buttonX(){ return 13; }
    public int buttonY(){ return 14; }
    public int buttonA(){ return 11; }
    public int buttonB(){ return 12; }
    public int buttonBack(){ return 5; }
    public int buttonStart(){ return 4; }
    public int povIndex(){ return 0; } // getPov()
    public int buttonLB(){ return 8; }
    public int buttonLS(){ return 6; }
    public int buttonRB(){ return 9; }
    public int buttonRS(){ return 7; }
    public int axisLeftX(){ return 2; } //-1 is left | +1 is right
    public int axisLeftY(){ return 3; } //-1 is up | +1 is down
    public int axisLeftTrigger(){ return 0; } //value 0 to 1f
    public int axisRightX(){ return 4; } //-1 is left | +1 is right
    public int axisRightY(){ return 5; } //-1 is up | +1 is down
    public int axisRightTrigger(){ return 1; } //value 0 to -1f

    @Override
    public PovDirection getPovDirection(Controller controller) {
        boolean up = controller.getButton(0);
        boolean down = controller.getButton(1);
        boolean left = controller.getButton(2);
        boolean right = controller.getButton(3);
        if (up && left) return PovDirection.northWest;
        if (left && down) return PovDirection.southWest;
        if (down && right) return PovDirection.southEast;
        if (right && up) return PovDirection.northEast;
        if (up) return PovDirection.north;
        if (down) return PovDirection.south;
        if (left) return PovDirection.west;
        if (right) return PovDirection.east;
        return leftStickDirection(controller);
    }

}
