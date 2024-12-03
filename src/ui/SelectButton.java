package ui;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;

import static utils.Constants.UI.SelectButtons.*;


import utils.StoreImage;

public class SelectButton extends Buttons {
    private BufferedImage[] btnImg;
    private boolean mouseHover, mousePress;
    private int index, level, textOffset = 0;
    private boolean active = false;

    public SelectButton(int x, int y, int width, int height, int level) {
        super(x, y, width, height);
        initImgs();
        this.level = level - 1;
    }
    
    private void initImgs() {
        BufferedImage tmp = StoreImage.GetSpriteAtLas(StoreImage.SELECT_BUTTONS);
        btnImg = new BufferedImage[3];
        for (int i = 0; i < btnImg.length; i++) {
            btnImg[i] = tmp.getSubimage(i * SELECT_DEFAULT_WIDTH, 0, SELECT_DEFAULT_WIDTH, SELECT_DEFAULT_HEIGHT);
        }
    }

    public void update() {
        index = 0;
        textOffset = 0;
        if (mouseHover) {
            index = 1;
        }
        if (mousePress) {
            index = 2;
            textOffset = (int) (4 * Game.SCALE);
        }
    }

    public void resetBooleans() {
        mouseHover = false;
        mousePress = false;
    }

    public void draw(Graphics g) {
        g.drawImage(btnImg[index], x, y, width, height, null);
        g.setFont(new Font("SVN-Rush Hour", Font.BOLD, 30));
        g.drawString(String.valueOf(level + 1), (int) (x + width/2 - 6), (int) (y + height/2 + 3 + textOffset));
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

    public void setMousePress(boolean mousePress) {
        this.mousePress = mousePress;
    }

    public int getLevel() {
        return level;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}