package org.atrolla.games.sounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

/**
 * Created by MicroOnde on 16/03/2016.
 */
public class MusicManager {

    private Music mainMenuMusic;
    private Music roundMusic;
    private Music current;

    public MusicManager() {
        mainMenuMusic = newMusicFrom("sounds/Welcome.mp3");
        roundMusic = newMusicFrom("sounds/05-eric_prydz-last_dragon-43bf64.mp3");
    }

    private Music newMusicFrom(String path) {
        final FileHandle internal = Gdx.files.internal(path);
        if (internal.exists()) {
            return Gdx.audio.newMusic(internal);
        }
        return null;
    }

    public void playMenuMusic() {
        current = mainMenuMusic;
        play(current);
    }

    public void playRoundMusic() {
        current = roundMusic;
        play(current);
    }

    private void play(Music music) {
        if (music != null) {
            music.setLooping(true);
            music.play();
            music.setVolume(0.5f);
        }
    }

    public void stop() {
        if (current != null) {
            current.stop();
        }
    }

    public void dispose() {
        dispose(mainMenuMusic);
        dispose(roundMusic);
    }

    private void dispose(Music music) {
        if (music != null) {
            music.dispose();
        }
    }
}
