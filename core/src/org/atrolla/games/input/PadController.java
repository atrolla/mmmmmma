package org.atrolla.games.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class PadController {

    private static final Long REPEAT_NANO_DELAY = 200000000L;
    private final Controller controller;
    private final Map<Integer, Long> buttonsPressedTime = new HashMap<>();
    private final Map<PovDirection, Long> povDirectionPressedTime = new HashMap<>();

    public PadController(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }

    public boolean hasJustPressed(PovDirection povDirection, PadInput padInput) {
        final PovDirection pov = padInput.getPovDirection(controller);
        if (pov == povDirection) {
            final Long lastTimePressed = povDirectionPressedTime.get(pov);
            final Long nowNano = getNowNano();
            if (lastTimePressed == null || lastTimePressed + REPEAT_NANO_DELAY < nowNano) {
                povDirectionPressedTime.put(pov, nowNano);
                return true;
            }
        }
        return false;
    }

    private Long getNowNano() {
        final Instant now = Instant.now();
        long time = now.getEpochSecond();
        time *= 1000000000L; //convert to nanoseconds
        time += now.getNano();
        return time;
    }

    public boolean hasJustPressed(int button) {
        if (controller.getButton(button)) {
            final Long lastTimePressed = buttonsPressedTime.get(button);
            final Long nowNano = getNowNano();
            if (lastTimePressed == null || lastTimePressed + REPEAT_NANO_DELAY < nowNano) {
                buttonsPressedTime.put(button, nowNano);
                return true;
            }
        }
        return false;
    }
}
