package org.atrolla.game.sounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import org.atrolla.game.items.weapons.Bomb;
import org.atrolla.game.utils.PatternMatching;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by MicroOnde on 08/03/2015.
 */
public class SoundManager {

    private Sound explosion;
    private final Queue<Sound> sounds;

    public SoundManager() {
        loadSounds();
        sounds = new LinkedList<>();
    }

    private void loadSounds() {
        if (Gdx.app != null) {
            explosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        }
    }

    public void playAllSounds() {
        Sound poll = sounds.poll();
        while (poll != null) {
            poll.play();
            poll = sounds.poll();
        }
    }

    public void register(Object object) {
        PatternMatching.when(Bomb.class::isInstance, o -> sounds.offer(explosion))
                .orWhen(String.class::isInstance, s -> {
                    System.out.println(s + " is a string !");
                    return true;
                })
                .otherwise(o -> {
                    System.out.println("no matching found for " + o);
                    return false;
                })
                .matches(object);
    }
}
