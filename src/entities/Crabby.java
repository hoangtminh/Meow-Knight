package entities;

import static main.Game.SCALE;
import static utils.Constants.Direction.RIGHT;
import static utils.Constants.EnemyConstant.*;

import java.awt.geom.Rectangle2D;

public class Crabby extends Enemy{

    private int attackBoxOffsetX;

    public Crabby(float x, float y) {
        super(x, y,CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(22, 19);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x,y,(int)(82 * SCALE), (int) (19 * SCALE));
        attackBoxOffsetX = (int) (30* SCALE);
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
                    if (aniIndex == 2 && !attackChecked) {
                        checkPlayerHit(attackBox, player);
                    }    

                    break;
                case HIT:
            }
        }
    }

    public int flipX() {
        if (walkDir == RIGHT) {
            return width;
        } else {
            return 0;
        }
    }

    public int flipW() {
        if (walkDir == RIGHT) {
            return -1;
        } else {
            return 1;
        }
    }

}
