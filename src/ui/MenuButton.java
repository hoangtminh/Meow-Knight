package ui;

import gameStates.GameState;
import utils.StoreImage;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Rectangle;
import static utils.Constants.UI.Buttons.*;

public class MenuButton {

    private int xPos, yPos, index;
    private int xOffsetCenter = BUTTON_WIDTH / 2;
    private int rowIndex;
    private GameState state;
    BufferedImage[] imgs;
    private boolean mouseHover, mousePress;
    private Rectangle bounds;

    public MenuButton(int xPos, int yPos, int rowIndex, GameState state) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImgs();
        initBounds();
    }

    private void initBounds() {
        bounds = new Rectangle(xPos - xOffsetCenter, yPos, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    private void loadImgs() {
        imgs = new BufferedImage[3];
        BufferedImage tmp = StoreImage.GetSpriteAtLas(StoreImage.MENU_BUTTONS);
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = tmp.getSubimage(i * BUTTON_WIDTH_DEFAULT, rowIndex * BUTTON_HEIGHT_DEFAULT, BUTTON_WIDTH_DEFAULT, BUTTON_HEIGHT_DEFAULT);
        }
    }

    public void draw(Graphics g) {
        g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, BUTTON_WIDTH, BUTTON_HEIGHT, null);
    }

    public void update() {
        index = 0;
        if (mouseHover) {
            index = 1;
        }
        if (mousePress) {
            index = 2;
        }
    }

    public boolean isMouseHover() {
        return mouseHover;
    }

    public void setMouseHover(boolean mouseHover) {
        this.mouseHover = mouseHover;
    }

    public boolean isMousePress() {
        return mousePress;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setMousePress(boolean mousePress) {
        this.mousePress = mousePress;
    }

    public void applyGameState() {
        GameState.state = state;
    }

    public void resetBooleans() {
        mouseHover = false;
        mousePress = false;
    }

    public GameState getState() {
        return state;
    }
    
    public int getRowIndex() {
        return rowIndex;
    }
}
