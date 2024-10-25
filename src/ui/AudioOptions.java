package ui;

import static utils.Constants.UI.PauseButton.SOUND_SIZE;
import static utils.Constants.UI.VolumeButtons.*;

import java.awt.Graphics;

import java.awt.event.MouseEvent;

import main.Game;

public class AudioOptions {

    private VolumeButton volumeButton;
    private SoundButton musicButton, sfxButton;

    public AudioOptions() {
        createSoundButton();
        createVolumeButton();
    }

    private void createVolumeButton() {
        int vX = (int) (309 * Game.SCALE);
        int vY = (int) (278 * Game.SCALE);
        volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    private void createSoundButton() {
        int soundX = (int) (450 * Game.SCALE);
        int musicY = (int) (140 * Game.SCALE);
        int sfxY = (int) (186 * Game.SCALE);
        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }

    public void update() {
        musicButton.update();
        sfxButton.update();
        volumeButton.update();
    }

    public void draw(Graphics g) {
        sfxButton.draw(g);
        musicButton.draw(g);
        volumeButton.draw(g);
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.resetBooleans();
        sfxButton.resetBooleans();
        volumeButton.resetBooleans();
        System.out.println("hover");
        if (isIn(e, musicButton)) {
            musicButton.setMouseHover(true);
        } else if (isIn(e, sfxButton)) {
            sfxButton.setMouseHover(true);
        } else if (isIn(e, volumeButton)) {
            volumeButton.setMouseHover(true);
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (volumeButton.isMousePressed()) {
            volumeButton.changeX(e.getX());
        }
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, musicButton)) {
            musicButton.setMousePress(true);
        } else if (isIn(e, sfxButton)) {
            sfxButton.setMousePress(true);
        } else if (isIn(e, volumeButton)) {
            volumeButton.setMousePressed(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, musicButton)) {
            if (musicButton.isMousePress()) {
                musicButton.setMuted(!musicButton.isMuted());
            }
        } else if (isIn(e, sfxButton)) {
            if (sfxButton.isMousePress()) {
                sfxButton.setMuted(!sfxButton.isMuted());
            }
        } 

        musicButton.resetBooleans();
        sfxButton.resetBooleans();
        volumeButton.resetBooleans();
    }

    private boolean isIn(MouseEvent e, PauseButton b) {
        if (b.getBounds().contains(e.getX(), e.getY())) {
            return true;
        }
        return false;
    }
}
