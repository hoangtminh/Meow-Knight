package entities;

import static main.Game.SCALE;
import static utils.Constants.PlayerConstants.*;
import static utils.Constants.*;
import static utils.HelpMethod.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gameStates.Playing;
import main.Game;
import utils.LoadSave;

public class Player extends Entity {
    private BufferedImage animations[][];
    private boolean left,right, jump;
    private boolean moving = false;
    private float playerSpeed = 1.5f;
    private boolean attacking = false;
    private boolean hit = false;
    
    public int[][] lvlData;
    private float xDrawOffset = 20 * SCALE * 1.5f;
    private float yDrawOffset = 16 * SCALE * 1.5f;

    private float jumpSpeed = -2.25f *SCALE;
    private float fallSpeedAfterCollision = 0.5f *SCALE;

    private BufferedImage statusBar;
    private int statusBarWidth = (int) (192 * SCALE);
    private int statusBarHeight = (int) (58 * SCALE);
    private int statusBarX = (int) (10 * SCALE);
    private int statusBarY = (int) (10 * SCALE);

    private int healthBarWidth = (int) (150 * SCALE);
    private int healthBarHeight = (int) (4 * SCALE);
    private int healthBarXStart = (int) (34 * SCALE);
    private int healthBarYStart = (int) (14 * SCALE);

    
    private int healthWidth = healthBarWidth;

    private int flipX = 0;
    private int flipW = 1;

    private boolean attackChecked = false;
    private Playing playing;

    private int tileY;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        this.state = IDLE;
        this.maxHealth = 100;
        this.currHealth = maxHealth;

        loadAnimation();
        initHitbox((int) (15 * 1.5), (int) (17 * 1.5));
        initAttackBox();

        tileY = (int) (hitbox.y / Game.TILES_SIZE);
    }

    public void setSpawn(Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (25 * SCALE), (int)(25 * SCALE));
    }
    
    public void update() {
        updateHealthBar();
        if (currHealth <= 0) {
            if (state != DEAD) {
                state = DEAD;
                aniIndex = 0;
                aniTick = 0;
                playing.setPlayerDying(true);
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
            } else if (aniIndex == GetSpriteAmount(DEAD) -1 && aniTick >= ANI_SPEED - 1) {
                playing.setGameOver(true);
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
            } else {
                updateAnimationTick();
            }

            return;
        }
        updateAttackBox();
        if (inAir) {
            updatePos();
        } else if (attacking) {
            checkAttack();
        } else {
            updatePos();
        }

        if (moving) {
            checkPotionTouched();
            checkSpikesTouched();
            tileY = (int) (hitbox.y / Game.TILES_SIZE);
        }

        setAnimation();
        updateAnimationTick();
    }

    private void checkSpikesTouched() {
        playing.checkSpikesTouched(this);
    }

    private void checkPotionTouched() {
        playing.checkPotionTouched(hitbox);
    }

    private void checkAttack() {
        if (attackChecked || aniIndex != 1) {
            return;
        }
        attackChecked = true;
        playing.checkEnemyHit(attackBox);
        playing.checkObjectHit(attackBox);
        playing.getGame().getAudioPlayer().playAttackSound();
    }

    private void updateAttackBox() {
        if (right) {
            attackBox.x = hitbox.x + hitbox.width + (int) (5 * SCALE); 
        } else if (left) {
            attackBox.x = hitbox.x - hitbox.width - (int) (5 * SCALE); 
        }
        attackBox.y = hitbox.y + (10 * SCALE);
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currHealth /  (float) maxHealth) * healthBarWidth);
    }
    
    public void render(Graphics g, int lvlOffset) {
        g.drawImage(animations[state][aniIndex],
        (int) (hitbox.x - xDrawOffset) - lvlOffset + flipX,
        (int) (hitbox.y - yDrawOffset),
        (int) (width * flipW * 1.5), (int) (height * 1.5), null);
        drawUI(g);
        // g.setColor(Color.red);
        // g.drawRect((int) (hitbox.x - lvlOffset), (int) (hitbox.y), (int) (width * 1.5), (int) (height * 1.5));
        // drawHitbox(g, lvlOffset);
        // drawAttackBox(g, lvlOffset);
    }

    private void drawAttackBox(Graphics g, int lvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) (attackBox.x - lvlOffset),(int) (attackBox.y), (int) (attackBox.width), (int) (attackBox.height));
    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBar, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(state)) {
                aniIndex = 0;
                attacking = false;
                attackChecked = false;
                hit = false;
            }
            aniTick = 0;
        }
    }

    private void setAnimation() {
        int startAnimation = state;

        if (moving) {
            state = RUNNING;
        } else {
            state = IDLE;
        }

        if (attacking) {
            state = ATTACK_1;
        }

        if (inAir) {
            if (airSpeed < 0) {
                state = JUMP;
            } else {
                state = FALLING;
            }
        }

        if (hit) {
            state = HIT;
        }

        if (startAnimation != state) {
            resetAnimation();
        }
    }

    private void resetAnimation() {
        aniIndex = 0;
        aniTick = 0;
    }

    private void updatePos() {
        moving = false;
        
        if (jump) {
            jumping();
        }
        if (!inAir) {
            if ((!left && !right) || (left && right)) {
                return;
            }
        }
        if (!inAir) {
            if (!isOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
        }

        float xSpeed = 0;
        if (left) {
            xSpeed -= playerSpeed;
            flipX = (int) ((width - 13) * 1.5);
            flipW = -1; 
        }
        if (right) {
            xSpeed += playerSpeed;
            flipX = 0;
            flipW = 1;
        }

        if (inAir) {   
            if (canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPos(xSpeed);
            } else {
                hitbox.y = getEntityYPosHit(hitbox, airSpeed);
                if (airSpeed > 0) {
                    resetInAir();
                } else {
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPos(xSpeed);
            }

        } else {
            updateXPos(xSpeed);
        }

        moving = true;
    }

    private void jumping() {
        if (inAir) {
            return;
        }
        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }
    
    private void updateXPos(float xSpeed) {
        if (canMoveHere(hitbox.x+xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = getEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    public void changeHealth(int value) {
        currHealth += value;

        if (value < 0) {
            hit = true;
        }

        if (currHealth <= 0) {
            currHealth = 0;
        }
        if (currHealth >= maxHealth) {
            currHealth = maxHealth;
        }
    }

    public void kill() {
        currHealth = 0;
    }

    public void changePower(int value) {
        // currPower += value;
        // if (currHealth <= 0) {
        //     currHealth = 0;
        // }
        // if (currHealth >= maxHealth) {
        //     currHealth = maxHealth;
        // }
    }

    private void loadAnimation() {

        BufferedImage img = LoadSave.GetSpriteAtLas(LoadSave.PLAYER_ATLAS);

        animations = new BufferedImage[9][8];
        for (int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i*49, j*33, 49, 33);
            }
        }

        statusBar = LoadSave.GetSpriteAtLas(LoadSave.STATUS_BAR);
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!isOnFloor(hitbox, lvlData)) {
            inAir = true;
        }
    }

    public void resetDirection() {
        right = false;
        left = false;
        jump = false;
    }

    public void resetAll() {
        resetDirection();
        inAir = false;
        attacking = false;
        moving = false;
        hit = false;
        currHealth = maxHealth;

        state = IDLE;

        hitbox.x = x;
        hitbox.y = y;

        if (!isOnFloor(hitbox, lvlData)) {
            inAir = true;
        }
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public int getTileY() {
        return tileY;
    }
}
