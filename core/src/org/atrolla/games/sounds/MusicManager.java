package org.atrolla.games.sounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import org.atrolla.games.utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by MicroOnde on 16/03/2016.
 */
public class MusicManager implements Music.OnCompletionListener {

    private Optional<Music> mainMenuMusic;
    private Optional<Music> current;
    private final List<Music> musics;

    public MusicManager() {
        musics = new ArrayList<>();
        extractMusics();
    }

    private void extractMusics() {
        //Any MP3 files in conf/music/menu
        final List<Music> musicList = Stream.of(Gdx.files.local("conf/music/ingame"))
                .filter(FileHandle::exists)
                .map(fh -> fh.list(".mp3"))
                .flatMap(Stream::of)
                .filter(fh -> !fh.name().contains("menu"))
                .map(fh -> Gdx.audio.newMusic(fh))
                .collect(Collectors.toList());
        musics.addAll(musicList);
        //First MP3 file in conf/music/menu
        mainMenuMusic = Stream.of(Gdx.files.local("conf/music/menu"))
                .filter(FileHandle::exists)
                .map(fh -> fh.list(".mp3"))
                .flatMap(Stream::of)
                .findFirst()
                .map(fh -> Gdx.audio.newMusic(fh));
//        FileHandle[] files = Gdx.files.local("conf/music/").list();
//        for (FileHandle file : files) {
//            // do something interesting here
//            System.out.println(file.name());
//            logFile.writeString(file.name(), true);
//            logFile.writeString("\n", true);
//        }
    }

    public void playMenuMusic() {
        play(mainMenuMusic, true);
    }

    public void playRoundMusic() {
        if (!musics.isEmpty()) {
            final int i = RandomUtils.between0AndExcluded(musics.size());
            play(Optional.of(musics.get(i)), false);
        }
    }

    private void play(Optional<Music> music, boolean loop) {
        current = music;
        music.ifPresent(m -> {
            m.setLooping(loop);
            m.play();
            m.setVolume(0.5f);
            if (!loop) {
                m.setOnCompletionListener(this);
            }
        });
    }

    public void stop() {
        current.ifPresent(Music::stop);
    }

    public void dispose() {
        mainMenuMusic.ifPresent(Music::dispose);
        musics.forEach(Music::dispose);
    }

    @Override
    public void onCompletion(Music music) {
        System.out.println("playing another music");
        playRoundMusic();
    }
}
