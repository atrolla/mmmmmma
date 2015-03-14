package org.atrolla.games;

import org.atrolla.games.sounds.SoundManager;
import org.junit.Test;

/**
 * Created by MicroOnde on 08/03/2015.
 */
public class SoundsTests {

    @Test
    public void soundIsPlayed() throws Exception {
//        final HeadlessApplicationConfiguration cfg = new HeadlessApplicationConfiguration();
//        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
//        final ApplicationAdapter games = new ApplicationAdapter() {
//
//            private Sound sound;
//
//            @Override
//            public void create() {
//                super.create();
//        Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
//                sound.play(1.0f);
//            }
//        };
//        new LwjglApplication(games, cfg);
//        new HeadlessApplication(games, cfg);

        final SoundManager soundManager = new SoundManager();
        Object lol = "toot";
        soundManager.register(lol);

    }
}
