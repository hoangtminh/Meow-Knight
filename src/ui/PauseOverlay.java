package ui;

import static utils.Constants.UI.URMButtons.URM_SIZE;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gameStates.GameState;
import gameStates.Playing;
import main.Game;
import utils.StoreImage;

public class PauseOverlay {

    private Playing playing;
    private BufferedImage background;
    private int bgX, bgY, bgWidth, bgHeight;
    private URMButtons menuB, replayB, unpauseB;
    private AudioOptions audioOptions;
    
    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        audioOptions = playing.getGame().getAudioOptions();
        createUrmButtons();
    }

    private void createUrmButtons() {
        int menuX = (int) (313 * Game.SCALE);
        int replayX = (int) (387 * Game.SCALE);
        int unpauseX = (int) (462 * Game.SCALE);
        int bY = (int) (332 * Game.SCALE);

        menuB = new URMButtons(menuX, bY, URM_SIZE, URM_SIZE, 2);
        replayB = new URMButtons(replayX, bY, URM_SIZE, URM_SIZE, 1);
        unpauseB = new URMButtons(unpauseX, bY, URM_SIZE, URM_SIZE, 0);
    }
    
    private void loadBackground() {
        background = StoreImage.GetSpriteAtLas(StoreImage.PAUSE_BACKGROUND);
        bgWidth = (int) (background.getWidth() * Game.SCALE);
        bgHeight = (int) (background.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgWidth / 2;
        bgY = (int) (25 * Game.SCALE);
    }

    public void update() {
        menuB.update();
        replayB.update();
        unpauseB.update();
        audioOptions.update();
    }

    public void draw(Graphics g) {
        g.drawImage(background, bgX, bgY, bgWidth, bgHeight, null);
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);
        audioOptions.draw(g);
    }

    public void mouseMoved(MouseEvent e) {
        menuB.resetBooleans();
        replayB.resetBooleans();
        unpauseB.resetBooleans();

        if (isIn(e, menuB)) {
            menuB.setMouseHover(true);
        } else if (isIn(e, replayB)) {
            replayB.setMouseHover(true);
        } else if (isIn(e, unpauseB)) {
            unpauseB.setMouseHover(true);
        } else {
            audioOptions.mouseMoved(e);
        }
    }

    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuB)) {
            menuB.setMousePressed(true);
        } else if (isIn(e, replayB)) {
            replayB.setMousePressed(true);
        } else if (isIn(e, unpauseB)) {
            unpauseB.setMousePressed(true);
        } else {
            audioOptions.mousePressed(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, menuB)) {
            if (menuB.isMousePressed()) {
                playing.setLoading(true);
                playing.getLoadingOverlay().setNextState(GameState.MENU);
                playing.unPauseGame(); 
            }
        } else if (isIn(e, replayB)) {
            if (replayB.isMousePressed()) {
                playing.resetAllPlaying();
                playing.unPauseGame();
                playing.getGame().getAudioPlayer().playEnemiesEffect(AudioPlayer.E_HÚ);
                // playing.setLoading(true);
                // playing.getLoadingOverlay().setNextState(GameState.PLAYING);
            }
        } else if (isIn(e, unpauseB)) {
            if (unpauseB.isMousePressed()) {
                playing.unPauseGame();
            }
        }  else {
            audioOptions.mouseReleased(e);
        }

        menuB.resetBooleans();
        replayB.resetBooleans();
        unpauseB.resetBooleans();
    }

    private boolean isIn(MouseEvent e, Buttons b) {
        if (b.getBounds().contains(e.getX(), e.getY())) {
            return true;
        }
        return false;
    }
}
