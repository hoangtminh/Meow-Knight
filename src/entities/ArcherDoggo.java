package entities;

import static main.Game.SCALE;
import static utils.Constants.Direction.RIGHT;
import static utils.Constants.EnemyConstant.*;
import static objects.ObjectManager.shootArcher;

import java.awt.geom.Rectangle2D;

public class ArcherDoggo extends Enemy{

    private int attackBoxOffsetX;
    private int cooldownMax = 80;
    private int cooldown = cooldownMax;
    private boolean shooting = false;

    public ArcherDoggo(float x, float y) {
        super(x, y, CalculateDoggoWidth(ARCHER), DOGGO_HEIGHT, ARCHER);
        initHitbox(32, 30);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x,y,(int)(48 * SCALE), (int) (48 * SCALE));
        attackBoxOffsetX = 0;
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
        cooldown--;
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
                            if (cooldown <= 0) {
                                updateState(ATTACK);
                                shooting = true;
                            }
                        } else {
                            shooting = false;
                        }
                    } else {
                        shooting = false;
                    }
                    if (!shooting) {
                        move(lvlData);
                    }
                    break;

                case ATTACK:
                    if (aniIndex == 0) {
                        attackChecked = false;
                    }
                    if (aniIndex == 5 && !attackChecked && cooldown <= 0) {
                        shootArcher(this, walkDir);
                        cooldown = cooldownMax;
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
