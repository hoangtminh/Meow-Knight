package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gameStates.GameState;
import gameStates.Playing;
import main.Game;
import utils.StoreImage;

import static utils.Constants.UI.URMButtons.URM_SIZE;

public class GameOverOverlay {
    
    private Playing playing;
    private BufferedImage img;
    private int imgX, imgY, imgW, imgH;
    private URMButtons menu, play;

    public GameOverOverlay(Playing playing) {
        this.playing = playing;
        createImg();
        createButtons();
    }

    private void createButtons() {
        int menuX = (int) (330 * Game.SCALE);
        int playX = (int) (445 * Game.SCALE);
        int y = (int) (195 * Game.SCALE);
        play = new URMButtons(playX, y, URM_SIZE, URM_SIZE , 0);    
        menu = new URMButtons(menuX, y, URM_SIZE, URM_SIZE , 2);    
    }

    private void createImg() {
        img = StoreImage.GetSpriteAtLas(StoreImage.DEATH_SCREEN);
        imgW = (int) (img.getWidth() * Game.SCALE);
        imgH = (int) (img.getHeight() * Game.SCALE);
        imgX = (int) (Game.GAME_WIDTH/2 - imgW/2);
        imgY = (int) (100 * Game.SCALE);
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        g.drawImage(img, imgX, imgY, imgW, imgH, null);
        menu.draw(g);
        play.draw(g);
    }

    public void update() {
        menu.update();
        play.update();
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                playing.setGameState(GameState.MENU);
                break;
            case KeyEvent.VK_R:
                playing.resetAllPlaying();
                playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLvlIndex());
                break;
            default:
                break;
        }
    }

    private boolean isIn(URMButtons b, MouseEvent e) {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e) {
        play.setMouseHover(false);
        menu.setMouseHover(false);
        if (isIn(menu, e)) {
            menu.setMouseHover(true);
        }
        if (isIn(play, e)) {
            play.setMouseHover(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(menu, e)) {
            if (menu.isMousePressed()) {
                playing.resetAllPlaying();
                playing.setGameState(GameState.MENU);
            }
        }
        if (isIn(play, e)) {
            if (play.isMousePressed()) {
                playing.resetAllPlaying();
                playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLvlIndex());
            }
        }

        menu.resetBooleans();
        play.resetBooleans();
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(menu, e)) {
            menu.setMousePressed(true);
        }
        if (isIn(play, e)) {
            play.setMousePressed(true);
        }
    }
}
