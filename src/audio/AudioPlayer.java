package audio;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {

    public static int MENU = 0;
    public static int LEVEL_1 = 1;
    public static int LEVEL_2 = 2;

    public static int DIE = 0;
    public static int DEAD = 1;
    public static int JUMP = 2;
    public static int LVL_COMPLETED = 3;
    public static int GAMEOVER = 4;
    public static int ATTACK_ONE = 5;
    public static int ATTACK_TWO = 6;
    public static int ATTACK_THREE = 7;

    private Clip[] songs, effects;
    private int currentSongId;
    private float volume = 0.8f;
    private boolean songMute, effectMute;
    private Random rand = new Random();

    public AudioPlayer() {
        loadSong();
        loadEffect();
        playSong(MENU);
    }

    private void loadSong() {
        String[] names = {"menu", "level1", "level2"};
        songs = new Clip[names.length];
        for (int i = 0; i < songs.length; i++) {
            songs[i] = getClip(names[i]);
        }
        updateSongVolume();
    }

    private void loadEffect() {
        String[] names = {"die", "dead", "jump", "lvlcompleted", "gameover", "attack1", "attack2", "attack3"};
        effects = new Clip[names.length];
        for (int i = 0; i < effects.length; i++) {
            effects[i] = getClip(names[i]);
        }
        updateEffectsVolume();
    }

    private Clip getClip(String name) {
        URL url = getClass().getResource("/audio/"+ name+".wav");
        AudioInputStream audio;

        try {
            audio = AudioSystem.getAudioInputStream(url);
            Clip c = AudioSystem.getClip();
            c.open(audio);
    
            return c;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setVolume(float volume) {
        this.volume = volume;
        updateSongVolume();
        updateEffectsVolume();
    }

    public void stopSong() {
        if (songs[currentSongId].isActive()) {
            songs[currentSongId].stop(); 
        }
    }

    public void setLevelSong(int lvlIndex) {
        if (lvlIndex % 2 == 0) {
            playSong(LEVEL_1);
        } else {
            playSong(LEVEL_2);
        }
    }

    public void lvlCompleted() {
        stopSong();
        playEffect(LVL_COMPLETED);
    }

    public void playAttackSound() {
        int start = 5;
        start += rand.nextInt(3);
        playEffect(start);
    }

    public void playEffect(int effect) {
        effects[effect].setMicrosecondPosition(0);
        effects[effect].start();
    }

    public void playSong(int song) {
        stopSong();

        currentSongId = song;
        updateSongVolume();
        songs[currentSongId].setMicrosecondPosition(0);
        songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);
        songs[currentSongId].start();
        System.out.println("play");
    }

    public void toggleSongMute() {
        this.songMute = !songMute;
        for (Clip c: songs) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(songMute);
        }
    }

    public void toggleEffectMute() {
        this.effectMute = !effectMute;
        for (Clip c: effects) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(songMute);
        }
        if (!effectMute) {
            playEffect(JUMP);
        }

    }

    private void updateSongVolume() {
        FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        // float gain = gainControl.getMaximum();
        gainControl.setValue(gain);
    }

    private void updateEffectsVolume() {
        for (Clip c: effects) {
            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }
}
