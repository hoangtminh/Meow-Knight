package objects;

import main.Game;

public class Coin extends GameObjects {
    private int maxHoverOffset, hoverDir = 1;
    private float hoverOffset;

    public Coin(int x, int y, int objType) {
        super(x, y, objType);
        doAnimation = true;
        initHitbox(18, 18);
        xDrawOffset = (int) (-8 * Game.SCALE);
        yDrawOffset = (int) (2 * Game.SCALE);
        maxHoverOffset = (int) (3 * Game.SCALE);
        hitbox.x -= xDrawOffset;
    }
    
    public void update() {
        updateAnimationTick();
        updateHover();
    }

    private void updateHover() {
        hoverOffset += (0.03f * Game.SCALE * hoverDir);
        if (hoverOffset >= maxHoverOffset) {
            hoverDir = -1;
        } else if (hoverOffset <= 0) {
            hoverDir = 1;
        }  
        hitbox.y = y + hoverOffset;
    }
}
