package entities;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

import main.Game;

public abstract class Entity {
    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitbox;
    protected int aniTick, aniIndex;
    protected int state;
    protected float airSpeed;
    protected boolean inAir = false;
    protected int maxHealth;
    protected int currHealth;
    protected Rectangle2D.Float attackBox;

    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void drawHitbox(Graphics g, int lvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) (hitbox.x - lvlOffset), (int) (hitbox.y), (int) hitbox.width, (int) hitbox.height);
    }
    
    public void drawAttackBox(Graphics g, int lvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) (attackBox.x - lvlOffset),(int) (attackBox.y), (int) (attackBox.width), (int) (attackBox.height));
    }

    protected void initHitbox(float width, float height) {
        hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public int getAniIndex() {
        return aniIndex;
    }

    public int getCurrentHealth() {
        return currHealth;
    }
}
