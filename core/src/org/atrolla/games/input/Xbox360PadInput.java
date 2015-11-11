package org.atrolla.games.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;

/**
 * Created by MicroOnde on 01/03/2015.
 */
public class Xbox360PadInput extends PadInput {
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
//    public static final String ID = "XBOX 360 For Windows (Controller)";
    public int buttonX(){ return 2; }
    public int buttonY(){ return 3; }
    public int buttonA(){ return 0; }
    public int buttonB(){ return 1; }
    public int buttonBack(){ return 6; }
    public int buttonStart(){ return 7; }
    public int povIndex(){ return 0; } // getPov()
    public int buttonLB(){ return 4; }
    public int buttonLS(){ return 8; }
    public int buttonRB(){ return 5; }
    public int buttonRS(){ return 9; }
    public int axisLeftX(){ return 1; } //-1 is left | +1 is right
    public int axisLeftY(){ return 0; } //-1 is up | +1 is down
    public int axisLeftTrigger(){ return 4; } //value 0 to 1f
    public int axisRightX(){ return 3; } //-1 is left | +1 is right
    public int axisRightY(){ return 2; } //-1 is up | +1 is down
    public int axisRightTrigger(){ return 4; } //value 0 to -1f

    @Override
    public PovDirection getPovDirection(Controller controller) {
        PovDirection direction = controller.getPov(povIndex());
        if (PovDirection.center == direction) {
            return leftStickDirection(controller);
        }
        return direction;
    }

}
