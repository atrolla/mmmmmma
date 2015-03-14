package org.atrolla.games.mocks;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by MicroOnde on 08/03/2015.
 */
public class MockController implements Controller {

    //Button is always pressed down
    @Override
    public boolean getButton(int buttonCode) {
        return true;
    }

    @Override
    public float getAxis(int axisCode) {
        return 0;
    }

    //Does not move
    @Override
    public PovDirection getPov(int povCode) {
        return PovDirection.center;
    }

    @Override
    public boolean getSliderX(int sliderCode) {
        return false;
    }

    @Override
    public boolean getSliderY(int sliderCode) {
        return false;
    }

    @Override
    public Vector3 getAccelerometer(int accelerometerCode) {
        return null;
    }

    @Override
    public void setAccelerometerSensitivity(float sensitivity) {

    }

    @Override
    public String getName() {
        return "mockController";
    }

    @Override
    public void addListener(ControllerListener listener) {

    }

    @Override
    public void removeListener(ControllerListener listener) {

    }
}
