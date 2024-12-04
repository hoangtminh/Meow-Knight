package objects;

import static utils.Constants.ANI_SPEED;
import static utils.Constants.OBJECT_ANI_SPEED;
import static utils.Constants.ObjectsConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;

public class GameObjects {
    
    protected int x, y, objType;
    protected Rectangle2D.Float hitbox;
    protected boolean doAnimation, active = true;
    protected int aniTick, aniIndex;
    protected int xDrawOffset, yDrawOffset;

    public GameObjects(int x, int y, int objType) {
        this.x = x;
        this.y = y;
        this.objType = objType;
    }

    protected void initHitbox(float width, float height) {
        hitbox = new Rectangle2D.Float(x, y, width * Game.SCALE, height * Game.SCALE);
    }

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= OBJECT_ANI_SPEED) {
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(objType)) {
                aniIndex = 0;
                if (objType == CHEST) {
                    doAnimation = false;
                    active = false;
                }
            }
            aniTick = 0;
        }
    }

    public void resetObject() {
        aniIndex = 0;
        aniTick = 0;
        active = true;

        doAnimation = true;
        if (objType == CHEST) {
            doAnimation = false;
        } else {
            doAnimation = true;
        }
    }

    public void drawHitbox(Graphics g) {
        g.setColor(Color.red);
        g.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    public void drawHitbox(Graphics g, int lvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) (hitbox.x - lvlOffset), (int) (hitbox.y), (int) hitbox.width, (int) hitbox.height);
    }

    public int getObjType() {
        return objType;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public boolean isActive() {
        return active;
    }

    public int getxDrawOffset() {
        return xDrawOffset;
    }

    public int getyDrawOffset() {
        return yDrawOffset;
    }

    public int getAniIndex() {
        return aniIndex;
    }
    public int getAniTick() {
        return aniTick;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setDoAnimation(boolean doAnimation) {
        this.doAnimation = doAnimation;
    }
}