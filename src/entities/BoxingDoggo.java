package entities;

import static main.Game.SCALE;
import static utils.Constants.Direction.RIGHT;
import static utils.Constants.EnemyConstant.*;

import java.awt.geom.Rectangle2D;

public class BoxingDoggo extends Enemy{

    private int attackBoxOffsetX;

    public BoxingDoggo(float x, float y) {
        super(x, y, CalculateDoggoWidth(BOXING), DOGGO_HEIGHT, BOXING);
        initHitbox(32, 30);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x,y,(int)(48 * SCALE), (int) (48 * SCALE));
        attackBoxOffsetX = (int) (15 * SCALE);
    }

    public void update(int[][] lvlData, Player player) {
        updateBehaviour(lvlData, player);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;
    }
    
    private void updateBehaviour(int[][] lvlData, Player player) {
        if (firstUpdate) {
            firstUpdateCheck(lvlData);
        }
        if (inAir) {
            updateInAir(lvlData);
        } else {
            switch (state) {
                case IDLE:
                    updateState(RUNNING);
                    break;
                case RUNNING:
                    if (canSeePlayer(lvlData, player)) {
                        turnTowardPlayer(player);
                        if (isPlayerCloseForAttack(player)) {
                            updateState(ATTACK);
                        }
                    }
                    
                    move(lvlData);
                    break;
                case ATTACK:
                    if (aniIndex == 0) {
                        attackChecked = false;
                    }
                    if (aniIndex == 7 && !attackChecked) {
                        checkPlayerHit(attackBox, player);
                    }    
                    break;
                case HIT:
            }
        }
    }

    public int flipX() {
        if (walkDir == RIGHT) {
            return 0;
        } else {
            return width;
        }
    }

    public int flipW() {
        if (walkDir == RIGHT) {
            return 1;
        } else {
            return -1;
        }
    }

}
