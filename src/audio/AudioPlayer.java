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
    public static int OPENING = 3;
    public static int ENDING = 4;

    public static int DIE = 0;
    public static int JUMP = 1;
    public static int LVL_COMPLETED = 2;
    public static int GAMEOVER = 3;
    public static int ATTACK_ONE = 4;
    public static int ATTACK_TWO = 5;
    public static int ATTACK_THREE = 6;
    public static int HIT_ONE = 7;
    public static int HIT_TWO = 8;
    public static int HIT_THREE = 9;

    public static int E_ATTACK_1 = 0;
    public static int E_ATTACK_2 = 1;
    public static int E_ATTACK_3 = 2;
    public static int E_DIE = 3;
    public static int E_HIT_1 = 4;
    public static int E_HIT_2 = 5;
    public static int E_HIT_3 = 6;
    public static int E_HÚ = 7;
    public static int E_GẦM_1 = 8;
    public static int E_GẦM_2 = 9;
    public static int E_GẦM_3 = 10;

    private Clip[] songs, effects, e_effects;
    private int currentSongId, currentEffectId;
    private float volume = 0.8f;
    private boolean songMute, effectMute;
    private Random rand = new Random();

    public AudioPlayer() {
        loadSong();
        loadEffect();
        playSong(MENU);
    }

    private void loadSong() {
        String[] names = {"menu", "level1", "level2", "opening", "ending"};
        songs = new Clip[names.length];
        for (int i = 0; i < songs.length; i++) {
            songs[i] = getClip(names[i]);
        }
        updateSongVolume();
    }

    private void loadEffect() {
        String[] names = {"die", "jump", "lvlcompleted", "gameover", "attack1", "attack2", "attack3", "hit1", "hit2", "hit3"};
        String[] enemies = {"dog_attack1", "dog_attack2", "dog_attack3", "dog_die1", "dog_hit1", "dog_hit2", "dog_hit3", "dog_hú", "dog_gầm", "dog_gầm2", "dog_gầm3"};
        effects = new Clip[names.length];
        e_effects = new Clip[enemies.length];
        for (int i = 0; i < effects.length; i++) {
            effects[i] = getClip(names[i]);
        }
        for (int i = 0; i < e_effects.length; i++) {
            e_effects[i] = getClip(enemies[i]);
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

    public void stopEffect() {
        if (effects[currentEffectId].isActive()) {
            effects[currentEffectId].stop(); 
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

    public void playEnemiesAttackSound() {
        int start = 0;
        start += rand.nextInt(3);
        playEnemiesEffect(start);
    }

    public void playEnemiesHitSound() {
        int start = 4;
        start += rand.nextInt(3);
        playEnemiesEffect(start);
    }

    public void playEnemiesEffect(int effect) {
        stopEnemiesEffect();

        currentEffectId = effect;
        e_effects[currentEffectId].setMicrosecondPosition(0);
        e_effects[currentEffectId].start();
    }

    public void stopEnemiesEffect() {
        if (effects[currentEffectId].isActive()) {
            effects[currentEffectId].stop(); 
        }
    }

    public void playAttackSound() {
        int start = 4;
        start += rand.nextInt(3);
        playEffect(start);
    }

    public void playHit() {
        int start = 7;
        start += rand.nextInt(3);
        playEffect(start);
    }

    public void playEffect(int effect) {
        stopEffect();

        currentEffectId = effect;
        if (effect == DIE || effect == GAMEOVER) {
            stopSong();
        }
        effects[currentEffectId].setMicrosecondPosition(0);
        effects[currentEffectId].start();
    }

    public void playSong(int song) {
        stopSong();

        currentSongId = song;
        updateSongVolume();
        songs[currentSongId].setMicrosecondPosition(0);
        songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);
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
        for (Clip c: e_effects) {
            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }
}
