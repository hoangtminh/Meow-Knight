package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;

import gameStates.Playing;
import main.Game;
import utils.StoreImage;

import static utils.Constants.UI.SelectButtons.*;

public class SelectLevel {

    private Playing playing;
    private SelectButton[] btnLevel;
    private BufferedImage bgImg;
    private boolean active = false;
    private int bgX, bgY, bgWidth, bgHeight;

    public SelectLevel(Playing playing) {
        this.playing = playing;
        initImgs();
        initButtons();
    }

    private void initImgs() {
        bgImg = StoreImage.GetSpriteAtLas(StoreImage.LEVEL_SELECT);
        bgX = (int) (Game.GAME_WIDTH/2 - bgImg.getWidth() * Game.SCALE/2);
        bgY = (int) (50 * Game.SCALE);
        bgWidth = (int) (bgImg.getWidth() * Game.SCALE);
        bgHeight = (int) (bgImg.getHeight() * Game.SCALE);
    }

    private void initButtons() {
        btnLevel = new SelectButton[6];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                btnLevel[i * 2 + j] = new SelectButton(
                (int) (bgX + bgWidth/2 -25*Game.SCALE + (SELECT_WIDTH + 45) * (i-1)), 
                (int) (bgY + bgHeight/2 + 40*Game.SCALE + (SELECT_HEIGHT + 40) * (j-1)), 
                SELECT_WIDTH, SELECT_HEIGHT, i * 2 + j);
            }
        }
    }

    public void update() {
        for (SelectButton b : btnLevel) {
            b.update();
        }
    }

    public void draw(Graphics g) {
        g.drawImage(bgImg, bgX, bgY, bgWidth, bgHeight, null);
        for (SelectButton b : btnLevel) {
            b.draw(g);
        }
    }

    public void mouseMoved(MouseEvent e) {
        for (SelectButton b : btnLevel) {
            b.resetBooleans();
        }

        for (SelectButton b : btnLevel) {
            if (isIn(e, b)) {
                b.setMouseHover(true);
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        for (SelectButton b: btnLevel) {
            if (isIn(e, b)) {
                b.setMousePress(true);
                break;
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        for (SelectButton b: btnLevel) {
            if (isIn(e, b)) {
                active = false;
                playing.getLevelManager().setLvlIndex(b.getLevel());
                playing.getLevelManager().nextLevel();
                break;
            }
            b.resetBooleans();
        }
    }

    private boolean isIn(MouseEvent e, Buttons b) {
        if (b.getBounds().contains(e.getX(), e.getY())) {
            return true;
        }
        return false;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
}