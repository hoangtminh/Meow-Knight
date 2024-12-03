package gameStates;

import static utils.Constants.UI.URMButtons.URM_SIZE;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.AudioOptions;
import ui.Buttons;
import ui.URMButtons;
import utils.StoreImage;

public class GameOption extends State {

    private AudioOptions audioOptions;
    private BufferedImage backgroundImg, optionBackgroundImg;
    private int bgX, bgY, bgW, bgH;
    private URMButtons menuB;

    public GameOption(Game game) {
        super(game);
        loadImgs();
        loadButtons();    
        audioOptions = game.getAudioOptions();
    }

    private void loadButtons() {
        int menuX = (int) (387 * Game.SCALE);
        int menuY = (int) (325 * Game.SCALE);

        menuB = new URMButtons(menuX, menuY, URM_SIZE, URM_SIZE, 2);
    }

    private void loadImgs() {
        backgroundImg = StoreImage.GetSpriteAtLas(StoreImage.BACKGROUND_MENU);
        optionBackgroundImg = StoreImage.GetSpriteAtLas(StoreImage.OPTIONS_BACKGROUND);

        bgW = (int) (optionBackgroundImg.getWidth() * Game.SCALE);
        bgH = (int) (optionBackgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH/2 - bgW/2;
        bgY = (int) (33 * Game.SCALE);
    }

    public void update() {
        menuB.update();
        audioOptions.update();
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(optionBackgroundImg, bgX, bgY, bgW, bgH, null);

        menuB.draw(g);
        audioOptions.draw(g);
    }

    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuB)) {
            menuB.setMousePressed(true);
        } else {
            audioOptions.mousePressed(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, menuB)) {
            if (menuB.isMousePressed()) {
                GameState.state = GameState.MENU;
            }
        } else {
            audioOptions.mouseReleased(e);
        }
        menuB.resetBooleans();
    }

    public void mouseMoved(MouseEvent e) {
        menuB.setMouseHover(false);
        if (isIn(e, menuB)) {
            menuB.setMouseHover(true);
        } else {
            audioOptions.mouseMoved(e);
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            GameState.state = GameState.MENU;
        }
    }

    public void keyReleased(KeyEvent e) {
    
    }

    public void keyTyped(KeyEvent e) {
    
    }
    
    public void mouseClicked(MouseEvent e) {
    
    }
    
    private boolean isIn(MouseEvent e, Buttons b) {
        if (b.getBounds().contains(e.getX(), e.getY())) {
            return true;
        }
        return false;
    }
}
