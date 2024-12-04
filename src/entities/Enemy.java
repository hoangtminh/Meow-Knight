package entities;

import static utils.Constants.EnemyConstant.*;
import static utils.Constants.Direction.*;
import static utils.Constants.*;
import static main.Game.TILES_SIZE;
import static utils.HelpMethod.*;

import java.awt.geom.Rectangle2D;

import main.Game;

public abstract class Enemy extends Entity {

    protected int enemyType;
    protected boolean firstUpdate = true;
    protected float walkSpeedDefault = 0.35f * Game.SCALE;
    protected float walkTowardPlayerSpeed = 0.65f * Game.SCALE;
    protected float walkSpeed = walkSpeedDefault;
    protected int walkDir = LEFT;
    protected int tileY;
    protected float attackDistance = TILES_SIZE;

    protected boolean active = true;
    protected boolean attackChecked = false;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        this.maxHealth = getMaxHealth(enemyType);
        currHealth = maxHealth;
    }
    
    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(enemyType, state)) {
                aniIndex = 0;
                switch (state) {
                    case ATTACK, HIT:
                        state = IDLE;
                        break;
                    case DEAD:
                        active = false;
                    default:
                        break;
                }
                
            }
        }
    } 

    protected void changeWalkDir() {
        if (walkDir == LEFT) {
            walkDir = RIGHT;
        } else {
            walkDir = LEFT;
        }
    }

    protected void firstUpdateCheck(int[][] lvlData) {
        if (!isOnFloor(hitbox, lvlData)) {
            inAir = true;
        }
    }

    protected void updateInAir(int[][] lvlData) {
        if (canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
        } else {
            inAir = false;
            hitbox.y = getEntityYPosHit(hitbox, airSpeed);
            airSpeed = 0;
            tileY = (int) (hitbox.y / TILES_SIZE);
        }
    }

    protected void move(int[][] lvlData) {
        float xSpeed = 0;
        
        if (walkDir == LEFT) {
            xSpeed -= walkSpeed;
        } else {
            xSpeed += walkSpeed;
        }

        if (canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            if (isFloor(hitbox, xSpeed, lvlData)) {
                hitbox.x += xSpeed;
                return;
            }
        }

        changeWalkDir();
    }

    protected void updateState(int state) {
        this.state = state;
        aniIndex = 0;
        aniTick = 0;
    }

    public void hurt(int damage) {
        currHealth -= damage;
        if (currHealth <= 0) {
            updateState(DEAD);
        } else {
            if (state != ATTACK) {
                updateState(HIT);
            }
        }
    }

    protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
        if (attackBox.intersects(player.getHitbox())) {
            player.changeHealth(- getEnemyDamage(enemyType));
        }
        attackChecked = true;
    }

    protected boolean canSeePlayer(int[][] lvlData, Player player) {
        int playerYTile = (int) (player.getHitbox().y / TILES_SIZE);
        if (player.getDodge()) {
            return false;
        }
        if (playerYTile == tileY) {
            if (isPlayerInRange(player)) {
                if (isSightClear(lvlData, hitbox, player.hitbox, tileY)) {
                    walkSpeed = walkTowardPlayerSpeed;
                    return true;
                }
            }
        } 
        walkSpeed = walkSpeedDefault;
        return false;
    }

    protected void turnTowardPlayer(Player player) {
        if (player.hitbox.x > hitbox.x) {
            walkDir = RIGHT;
        } else {
            walkDir = LEFT;
        }
    }

    public void resetEnemy() {
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currHealth = maxHealth;
        updateState(IDLE);
        active = true;
        airSpeed = 0;
    }

    protected boolean isPlayerInRange(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        if (enemyType == ARCHER) {
            return absValue <= attackDistance * 9;
        }
        return absValue <= attackDistance * 5;
    }

    protected boolean isPlayerCloseForAttack(Player player) {
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        if (enemyType == ARCHER) {
            return absValue <= attackDistance * 7;
        }
        return absValue <= attackDistance;
    }

    public int getEnemyState() {
        return state;
    }
    public boolean isActive() {
        return active;
    }
}
