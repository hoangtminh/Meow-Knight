package objects;

import main.Game;

public class Heal extends GameObjects {

    private int maxHoverOffset, hoverDir = 1;
    private float hoverOffset;

    public Heal(int x, int y, int objType) {
        super(x, y, objType);
        doAnimation = true;
        initHitbox(16, 16);
        maxHoverOffset = (int) (10 * Game.SCALE);
    }
    
    public void update() {
        updateAnimationTick();
        updateHover();
    }

    private void updateHover() {
        hoverOffset += (0.065f * Game.SCALE * hoverDir);
        if (hoverOffset >= maxHoverOffset) {
            hoverDir = -1;
        } else if (hoverOffset <= 0) {
            hoverDir = 1;
        }  
        hitbox.y = y + hoverOffset;
    }   
}
