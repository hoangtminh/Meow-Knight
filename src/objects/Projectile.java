package objects;

import java.awt.geom.Rectangle2D;

import main.Game;

import static utils.Constants.Projectiles.*;

public class Projectile {
    
    private Rectangle2D.Float hitbox;
    private int dir;
    private boolean active = true;

    public Projectile(int x, int y, int direction) {
        int xOffset = (int) (-3 * Game.SCALE);
        int yOffset = (int) (7 * Game.SCALE);

        if (dir == 1) {
            xOffset = (int) (29 * Game.SCALE);
        }

        hitbox = new Rectangle2D.Float(x + xOffset, y + yOffset, ARROW_WIDTH, ARROW_HEIGHT);
        this.dir = direction;
    }

    public void updatePos() {
        hitbox.x += dir * SPEED;
    }

    public void setPos(int x, int y) {
        hitbox.x = x;
        hitbox.y = y;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    public boolean isActive() {
        return active;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public int getDir() {
        return dir;
    }
}
