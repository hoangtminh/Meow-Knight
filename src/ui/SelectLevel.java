package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;

import gameStates.GameState;
import gameStates.Playing;
import main.Game;
import utils.StoreImage;

import static utils.Constants.UI.SelectButtons.*;

public class SelectLevel {

    private Playing playing;
    private SelectButton[] btnLevel;
    private BufferedImage[] coinNumImg = new BufferedImage[4];
    private BufferedImage bgImg;
    private boolean active = false;
    private int bgX, bgY, bgWidth, bgHeight;
    private int coinW, coinH;

    public SelectLevel(Playing playing) {
        this.playing = playing;
        initImgs();
        initButtons();
        btnLevel[0].setActive(true);
        btnLevel[4].setActive(true);
    }

    private void initImgs() {
        bgImg = StoreImage.GetSpriteAtLas(StoreImage.LEVEL_SELECT);
        bgX = (int) (Game.GAME_WIDTH/2 - bgImg.getWidth() * Game.SCALE/2);
        bgY = (int) (50 * Game.SCALE);
        bgWidth = (int) (bgImg.getWidth() * Game.SCALE);
        bgHeight = (int) (bgImg.getHeight() * Game.SCALE);
        
        coinNumImg[0] = StoreImage.GetSpriteAtLas(StoreImage.COIN_IMAGE0);
        coinNumImg[1] = StoreImage.GetSpriteAtLas(StoreImage.COIN_IMAGE1);
        coinNumImg[2] = StoreImage.GetSpriteAtLas(StoreImage.COIN_IMAGE2);
        coinNumImg[3] = StoreImage.GetSpriteAtLas(StoreImage.COIN_IMAGE3);

        coinW = (int) (coinNumImg[0].getWidth() / 1.5);
        coinH = (int) (coinNumImg[0].getHeight() / 1.5);
    }

    private void initButtons() {
        btnLevel = new SelectButton[6];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                btnLevel[i * 3 + j] = new SelectButton(
                (int) (bgX + bgWidth/2 -25*Game.SCALE + (SELECT_WIDTH + 45) * (j-1)), 
                (int) (bgY + bgHeight/2 - 30*Game.SCALE + (SELECT_HEIGHT + 55) * (i)), 
                SELECT_WIDTH, SELECT_HEIGHT, i * 3 + j + 1);
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
        for (int i = 0; i < btnLevel.length; i++) {
            if (btnLevel[i].isActive()) {
                btnLevel[i].draw(g);
                int coinNum = playing.getLevelManager().getCoinNum(i);
                g.drawImage(coinNumImg[coinNum], 
                (int) (bgX + bgWidth/2 -25*Game.SCALE + (SELECT_WIDTH + 45) * (i%3-1)), 
                (int) (bgY + bgHeight/2 - 60*Game.SCALE + (SELECT_HEIGHT + 55) * (i/3)), 
                coinW, coinH, null);
            }
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
                playing.getLevelManager().setLvlIndex(b.getLevel() - 1);
                playing.loadNextLevel();
                GameState.state = GameState.PLAYING;
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

    public void setButtonActive(int index) {
        btnLevel[index].setActive(true);
    }
}