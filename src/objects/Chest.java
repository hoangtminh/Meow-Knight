package objects;

import static utils.Constants.ObjectsConstants.*;

public class Chest extends GameObjects {

    public Chest(int x, int y, int objType) {
        super(x, y, objType);
        createHitbox();
    }
    
    private void createHitbox() {
        initHitbox(CHEST_WIDTH_DEFAULT, CHEST_WIDTH_DEFAULT);
    }

    public void update() {
        if (doAnimation) {
            updateAnimationTick();
        }
    }
}
