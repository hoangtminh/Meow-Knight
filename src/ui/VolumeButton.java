package ui;

import static utils.Constants.UI.VolumeButtons.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utils.StoreImage;

public class VolumeButton extends Buttons {

    private BufferedImage[] imgs;
    private BufferedImage slider;
    private int index;
    private boolean mouseHover, mousePressed;
    private int buttonX, minX, maxX;
    private float floatValue = 0f;

    public VolumeButton(int x, int y, int width, int height) {
        super(x + width/2, y, VOLUME_WIDTH, height);
        bounds.x -= VOLUME_WIDTH / 2;
        buttonX = x + width/2;
        this.x = x;
        this.width = width;
        minX = x;
        maxX = x + width;
        loadImgs();
    }
    
    private void loadImgs() {
        BufferedImage tmp = StoreImage.GetSpriteAtLas(StoreImage.VOLUME_BUTTONS);
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = tmp.getSubimage(i*VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
        }
        slider = tmp.getSubimage(3*VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
    }

    
    public void update() {
        index = 0;
        if (mouseHover) {
            index  = 1;
        }
        if (mousePressed) {
            index = 2;
        }
    }
    
    public void draw(Graphics g) {
        g.drawImage(slider, x, y,width, height, null);
        g.drawImage(imgs[index], buttonX - VOLUME_WIDTH/2, y, VOLUME_WIDTH, VOLUME_HEIGHT, null);
    }

    public void changeX(int x) {
        if (x < minX) {
            buttonX = minX;
        } else if (x > maxX) {
            buttonX = maxX;
        } else {
            buttonX = x;
        }
        updateFloatValue();
        bounds.x = buttonX - VOLUME_WIDTH/2;
    }

    private void updateFloatValue() {
        float range = maxX - minX;
        float value = buttonX - minX;
        floatValue = value / range;
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

    public float getFloatValue() {
        return floatValue;
    }
}
