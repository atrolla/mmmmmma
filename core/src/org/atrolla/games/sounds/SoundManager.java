package org.atrolla.games.sounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import org.atrolla.games.items.weapons.Arrow;
import org.atrolla.games.items.weapons.Bomb;
import org.atrolla.games.items.weapons.MageSpell;
import org.atrolla.games.items.weapons.Sword;
import org.atrolla.games.utils.PatternMatching;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by MicroOnde on 08/03/2015.
 */
public class SoundManager {

    private Sound explosion;
    private Sound arrow;
    private Sound sword;
    private Sound spell;
    private final Queue<Sound> sounds;

    public SoundManager() {
        loadSounds();
        sounds = new LinkedList<>();
    }

    //https://www.freesound.org/people/smcameron/sounds/50773/
    //https://www.freesound.org/people/LiamG_SFX/sounds/334238/
    //http://opengameart.org/content/spell-4-fire

    private void loadSounds() {
        if (Gdx.app != null) {
            explosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
            arrow = Gdx.audio.newSound(Gdx.files.internal("sounds/50773__smcameron__arrow-whoosh.wav"));
            sword = Gdx.audio.newSound(Gdx.files.internal("sounds/334238__liamg-sfx__sword-slice.wav"));
            spell = Gdx.audio.newSound(Gdx.files.internal("sounds/foom_0.wav"));
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
                .orWhen(Sword.class::isInstance, o -> sounds.offer(sword))
                .orWhen(Arrow.class::isInstance, o -> sounds.offer(arrow))
                .orWhen(MageSpell.class::isInstance, o -> sounds.offer(spell))
                .orWhen(String.class::isInstance, s -> {
                    System.out.println(s + " is a string !");
                    return true;
                })
                .otherwise(o -> {
//                    System.out.println("no matching found for " + o);
                    return false;
                })
                .matches(object);
    }

    public void disposeSounds() {
        //place all sounds here
        explosion.dispose();
    }
}
