package ui;

import gameStates.GameState;
import gameStates.Playing;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import main.Game;
import static utils.Constants.UI.URMButtons.*;
import utils.StoreImage;

public class LevelCompleteOverlay {

    private Playing playing;
    private URMButtons menu, next;
    private BufferedImage img;
    private int bgX, bgY, bgW, bgH;

    private int coinX;
    private int coinY;
    private BufferedImage[] coinImg = new BufferedImage[4];

    public LevelCompleteOverlay(Playing playing) {
        this.playing = playing;
        initImg();
        initButtons();
        initCoin();
    }

    private void initButtons() {
        int menuX = (int) (330 * Game.SCALE);
        int nextX = (int) (445 * Game.SCALE);
        int y = (int) (280 * Game.SCALE);

        next = new URMButtons(nextX, y, URM_SIZE, URM_SIZE, 0);
        menu = new URMButtons(menuX, y, URM_SIZE, URM_SIZE, 2);
    }

    private void initImg() {
        img = StoreImage.GetSpriteAtLas(StoreImage.LEVEL_COMPLETE);
        bgW = (int) (img.getWidth() * Game.SCALE);
        bgH = (int) (img.getHeight() * Game.SCALE);
        bgX = (int) (Game.GAME_WIDTH / 2 - bgW / 2);
        bgY = (int) (75 * Game.SCALE);
    }

    private void initCoin() {
        coinImg[0] = StoreImage.GetSpriteAtLas(StoreImage.COIN_IMAGE0);
        coinImg[1] = StoreImage.GetSpriteAtLas(StoreImage.COIN_IMAGE1);
        coinImg[2] = StoreImage.GetSpriteAtLas(StoreImage.COIN_IMAGE2);
        coinImg[3] = StoreImage.GetSpriteAtLas(StoreImage.COIN_IMAGE3);
        coinY = (int) (190 * Game.SCALE);
        coinX = (int) (Game.GAME_WIDTH/2 - coinImg[3].getWidth() * Game.SCALE / 2);
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        g.drawImage(img, bgX, bgY, bgW, bgH, null);
        next.draw(g);
        menu.draw(g);

        int coinNum = playing.getObjectManager().getCoinNum();
        g.drawImage(coinImg[coinNum], coinX, coinY, 
        (int) (coinImg[coinNum].getWidth() * Game.SCALE),
        (int) (coinImg[coinNum].getHeight() * Game.SCALE),
        null);
    }

    public void update() {
        next.update();
        menu.update();
    }

    private boolean isIn(URMButtons b, MouseEvent e) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        next.setMouseHover(false);
        menu.setMouseHover(false);
        if (isIn(menu, e)) {
            menu.setMouseHover(true);
        }
        if (isIn(next, e)) {
            next.setMouseHover(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(menu, e)) {
            if (menu.isMousePressed()) {
                playing.resetAllPlaying();
                playing.setGameState(GameState.MENU);
            }
        }
        if (isIn(next, e)) {
            if (next.isMousePressed()) {
                playing.setLoading(true);
                playing.getLoadingOverlay().setNextState(GameState.PLAYING);
            }
        }

        menu.resetBooleans();
        next.resetBooleans();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(menu, e)) {
            menu.setMousePressed(true);
        }
        if (isIn(next, e)) {
            next.setMousePressed(true);
        }
    }
}
