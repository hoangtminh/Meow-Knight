package ui;

import static utils.Constants.UI.PauseButton.SOUND_SIZE;
import static utils.Constants.UI.URMButtons.URM_SIZE;
import static utils.Constants.UI.VolumeButtons.*;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gameStates.GameState;
import gameStates.Playing;
import main.Game;
import utils.LoadSave;

public class PauseOverlay {

    private Playing playing;
    private BufferedImage background;
    private int bgX, bgY, bgWidth, bgHeight;
    private SoundButton musicButton, sfxButton;
    private URMButtons menuB, replayB, unpauseB;
    private VolumeButton volumeButton;

    
    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        createSoundButton();
        createUrmButtons();
        createVolumeButton();
    }

    private void createVolumeButton() {
        int vX = (int) (309 * Game.SCALE);
        int vY = (int) (278 * Game.SCALE);
        volumeButton = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    private void createUrmButtons() {
        int menuX = (int) (313 * Game.SCALE);
        int replayX = (int) (387 * Game.SCALE);
        int unpauseX = (int) (462 * Game.SCALE);
        int bY = (int) (325 * Game.SCALE);

        menuB = new URMButtons(menuX, bY, URM_SIZE, URM_SIZE, 2);
        replayB = new URMButtons(replayX, bY, URM_SIZE, URM_SIZE, 1);
        unpauseB = new URMButtons(unpauseX, bY, URM_SIZE, URM_SIZE, 0);
    }

    private void createSoundButton() {
        int soundX = (int) (450 * Game.SCALE);
        int musicY = (int) (140 * Game.SCALE);
        int sfxY = (int) (186 * Game.SCALE);
        musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
    }

    private void loadBackground() {
        background = LoadSave.GetSpriteAtLas(LoadSave.PAUSE_BACKGROUND);
        bgWidth = (int) (background.getWidth() * Game.SCALE);
        bgHeight = (int) (background.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgWidth / 2;
        bgY = (int) (25 * Game.SCALE);

    }

    public void update() {
        musicButton.update();
        sfxButton.update();
        menuB.update();
        replayB.update();
        unpauseB.update();
        volumeButton.update();
    }

    public void draw(Graphics g) {
        g.drawImage(background, bgX, bgY, bgWidth, bgHeight, null);
        sfxButton.draw(g);
        musicButton.draw(g);
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);
        volumeButton.draw(g);
    }

    public void mouseMoved(MouseEvent e) {

        musicButton.resetBooleans();
        sfxButton.resetBooleans();
        menuB.resetBooleans();
        replayB.resetBooleans();
        unpauseB.resetBooleans();
        volumeButton.resetBooleans();

        if (isIn(e, musicButton)) {
            musicButton.setMouseHover(true);
        }
        if (isIn(e, sfxButton)) {
            sfxButton.setMouseHover(true);
        }
        if (isIn(e, menuB)) {
            menuB.setMouseHover(true);
        }
        if (isIn(e, replayB)) {
            replayB.setMouseHover(true);
        }
        if (isIn(e, unpauseB)) {
            unpauseB.setMouseHover(true);
        }
        if (isIn(e, volumeButton)) {
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
        } else if (isIn(e, menuB)) {
            menuB.setMousePressed(true);
        } else if (isIn(e, replayB)) {
            replayB.setMousePressed(true);
        } else if (isIn(e, unpauseB)) {
            unpauseB.setMousePressed(true);
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
        } else if (isIn(e, menuB)) {
            if (menuB.isMousePressed()) {
                GameState.state = GameState.MENU;
                playing.unPauseGame(); 
            }
        } else if (isIn(e, replayB)) {
            if (replayB.isMousePressed()) {
                playing.resetAllPlaying();
                playing.unPauseGame();
            }
        } else if (isIn(e, unpauseB)) {
            if (unpauseB.isMousePressed()) {
                playing.unPauseGame();
            }
        } 

        musicButton.resetBooleans();
        sfxButton.resetBooleans();
        menuB.resetBooleans();
        replayB.resetBooleans();
        unpauseB.resetBooleans();
        volumeButton.resetBooleans();
    }

    private boolean isIn(MouseEvent e, PauseButton b) {
        if (b.getBounds().contains(e.getX(), e.getY())) {
            return true;
        }
        return false;
    }
}
