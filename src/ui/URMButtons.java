package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import static utils.Constants.UI.URMButtons.*;

import utils.StoreImage;

public class URMButtons extends Buttons {

    private BufferedImage[] img;
    private int rowIndex, index;
    private boolean mouseHover, mousePressed;

    public URMButtons(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImgs();
    }
    
    private void loadImgs() {
        BufferedImage tmp = StoreImage.GetSpriteAtLas(StoreImage.URM_BUTTONS);
        img = new BufferedImage[3];
        for (int i = 0; i < img.length; i++) {
            img[i] = tmp.getSubimage(i * URM_DEFAULT_SIZE, rowIndex * URM_DEFAULT_SIZE, URM_DEFAULT_SIZE, URM_DEFAULT_SIZE);
        }
    }

    
    public void update() {
        index = 0;
        if (mouseHover) {
            index = 1;
        }
        if (mousePressed) {
            index = 2;
        }
    }
    
    public void draw(Graphics g) {
        g.drawImage(img[index], x, y, URM_SIZE, URM_SIZE, null);
    }

    public void resetBooleans() {
        mouseHover = false;
        mousePressed = false;
    }

    public boolean isMouseHover() {
        return mouseHover;
    }
    
    public void setMouseHover(boolean mouseHover) {
        this.mouseHover = mouseHover;
    }
    
    public boolean isMousePressed() {
        return mousePressed;
    }
    
    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }
}
