package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utils.StoreImage;
import static utils.Constants.UI.PauseButton.*;

public class SoundButton extends Buttons {
    private BufferedImage[][] soundImgs;
    private boolean mouseHover, mousePress;
    private boolean muted;
    private int rowIndex, colIndex;

    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);
        loadSoundImgs();
    }

    private void loadSoundImgs() {
        BufferedImage tmp = StoreImage.GetSpriteAtLas(StoreImage.SOUND_BUTTON);
        soundImgs = new BufferedImage[2][3];
        for (int  j = 0; j < soundImgs.length; j++) {
            for (int i = 0; i < soundImgs[j].length; i++) {
                soundImgs[j][i] = tmp.getSubimage(i * SOUND_SIZE_DEFAULT, j * SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT);
            }
        }
    }
    
    public void update() {
        if (muted) {
            rowIndex = 1;
        } else {
            rowIndex = 0;
        }

        colIndex = 0;
        if (mouseHover) {
            colIndex = 1;
        }
        if (mousePress) {
            colIndex = 2;
        }
    }

    public void resetBooleans() {
        mouseHover = false;
        mousePress = false;
    }

    public void draw(Graphics g) {
        g.drawImage(soundImgs[rowIndex][colIndex], x, y, width, height, null);
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

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }
}
