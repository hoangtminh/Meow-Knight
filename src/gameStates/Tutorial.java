package gameStates;

import static utils.Constants.UI.Buttons.*;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.MenuButton;
import utils.StoreImage;

public class Tutorial {
    
    private boolean active = false;
    private static int bgIndex = 0;
    private BufferedImage[] background = new BufferedImage[2];

    private MenuButton nextBtn, quitButton;
    private int nextXPos = Game.GAME_WIDTH - BUTTON_WIDTH/2;
    private int quitXPos = BUTTON_WIDTH/2;
    private int buttonYPos = Game.GAME_HEIGHT - BUTTON_HEIGHT;

    public Tutorial() {
        initBackground();
        initButton();
    }

    private void initBackground() {
        background[0] = StoreImage.GetSpriteAtLas(StoreImage.TURORIAL_0);
        background[1] = StoreImage.GetSpriteAtLas(StoreImage.TURORIAL_1);
    }

    private void initButton() {
        nextBtn = new MenuButton(nextXPos, buttonYPos, 4, null);
        quitButton = new MenuButton(quitXPos, buttonYPos, 2, GameState.MENU);
    }
    
    public void update() {
        nextBtn.update();
        quitButton.update();
    }

    public void draw(Graphics g) {
        g.drawImage(background[bgIndex], 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        nextBtn.draw(g);
        quitButton.draw(g);
    }

    private void updatebgIndex() {
        bgIndex = bgIndex + 1;
        if (bgIndex >= background.length) {
            bgIndex = 0;
        }
        if (bgIndex < 0) {
            bgIndex = background.length -1;
        }
    }

    public void mouseMoved(MouseEvent e) {
        nextBtn.resetBooleans();
        quitButton.resetBooleans();
        if (isIn(e, nextBtn)) {
            nextBtn.setMouseHover(true);
        } else if (isIn(e, quitButton)) {
            quitButton.setMouseHover(true);
        }
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, nextBtn)) {
            nextBtn.setMousePress(true);
        } else if (isIn(e, quitButton)) {
            quitButton.setMousePress(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, nextBtn)) {
            nextBtn.resetBooleans();
            updatebgIndex();
        } else  if (isIn(e, quitButton)) {
            quitButton.resetBooleans();
            active = false;
        }
    }

    public boolean isIn(MouseEvent e, MenuButton b) {
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
