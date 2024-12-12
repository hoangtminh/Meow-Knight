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
import utils.StoreImage;

public class Player extends Entity {
    private BufferedImage animations[][];
    private boolean left,right, jump;
    private boolean moving = false;
    private float playerSpeed = 1.5f;
    private boolean attacking = false;
    private boolean hit = false;
    private boolean dodge = false;
    
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

    private boolean powerAttackActive;
    private int powerAttackTick;
    private int powerGrowSpeed = 15;
    private int powerGrowTick;

    private int powerBarWidth = (int) (104 * SCALE);
    private int powerBarHeight = (int) (4 * SCALE);
    private int powerBarXStart = (int) (44 * SCALE);
    private int powerBarYStart = (int) (34 * SCALE);
    private int powerWidth = powerBarWidth;
    private int powerMaxValue = 200;
    private int powerValue = powerMaxValue;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        this.state = IDLE;
        this.maxHealth = 160;
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
        attackBox = new Rectangle2D.Float(x, y, (int) (25 * SCALE), (int)(22 * SCALE));
    }
    
    public void update() {
        updateHealthBar();
        updatePowerBar();

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
        }else if (powerAttackActive) {
            checkAttack();
            updatePos();
        } else {
            updatePos();
        }

        if (moving) {
            checkPotionTouched();
            checkCoinTouched();
            checkSpikesTouched();
            tileY = (int) (hitbox.y / Game.TILES_SIZE);
        }

        if (powerAttackActive) {
            powerAttackTick++;
            if (powerAttackTick >= 35) {
                powerAttackTick = 0;
                powerAttackActive = false;
            }
        }

        setAnimation();
        updateAnimationTick();
    }

    private void checkSpikesTouched() {
        playing.checkSpikesTouched(this);
    }

    private void checkCoinTouched() {
        playing.checkCoinTouched(this);
    }

    private void checkPotionTouched() {
        playing.checkPotionTouched(this);
    }

    private void checkAttack() {
        if (attackChecked || aniIndex != 1) {
            return;
        }
        attackChecked = true;

        if (powerAttackActive) {
            attackChecked = false;
        }

        playing.checkEnemyHit(attackBox);
        playing.checkObjectHit(attackBox);
        playing.getGame().getAudioPlayer().playAttackSound();
    }

    private void updateAttackBox() {
        if (flipW == 1 ) {
            attackBox.x = hitbox.x + hitbox.width + (int) (5 * SCALE); 
        } else if (flipW == -1) {
            attackBox.x = hitbox.x - hitbox.width - (int) (5 * SCALE); 
        }
        attackBox.y = hitbox.y + (3 * SCALE);
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currHealth /  (float) maxHealth) * healthBarWidth);
    }

    private void updatePowerBar() {
        powerWidth = (int) ((powerValue / (float) powerMaxValue) * powerBarWidth);

        powerGrowTick++;
        if (powerGrowTick >= powerGrowSpeed) {
            powerGrowTick = 0;
            changePower(1);
        }
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
        drawAttackBox(g, lvlOffset);
    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBar, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);

        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);

        g.setColor(Color.yellow);
        g.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight);
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
                dodge = false;
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

        if (hit) {
            state = HIT;
        }

        if (dodge) {
            state = DODGE;
            return;
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

        if (powerAttackActive) {
            state = ATTACK_1;
            aniIndex = 1;
            aniTick = 0;
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
            if (!powerAttackActive) {    
                if (!dodge) {
                    if ((!left && !right) || (left && right)) {
                        return;
                    }
                }
            }
        }

        if (!inAir) {
            if (!isOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
        }

        float xSpeed = 0;
        if (left && !right) {
            xSpeed -= playerSpeed;
            flipX = (int) ((width - 13) * 1.5);
            flipW = -1; 
        } else if (right && !left) {
            xSpeed += playerSpeed;
            flipX = 0;
            flipW = 1;
        }

        if (powerAttackActive) {
            if (flipW == -1) {
                xSpeed = -playerSpeed;
            } else {
                xSpeed = playerSpeed;
            }
            xSpeed *= 3;
        }

        if (dodge) {
            if (flipW == -1) {
                xSpeed = -playerSpeed;
            } else {
                xSpeed = playerSpeed;
            }
            xSpeed *= 1;
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

    public void powerAttack() {
        if (powerAttackActive || inAir || dodge || attacking) {
            return;
        }
        if (powerValue >= 60) {
            powerAttackActive = true;
            changePower(-60);
        }
    }

    public void dodging() {
        if (dodge || powerAttackActive || inAir || attacking) {
            return;
        }
        if (powerValue >= 30) {
            dodge = true;
            changePower(-30);
        }
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }
    
    private void updateXPos(float xSpeed) {
        if (canMoveHere(hitbox.x+xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
            if (IsWater(hitbox, lvlData)) {
                kill();
                hitbox.y += Game.TILES_SIZE /3;
            }
        } else {
            hitbox.x = getEntityXPosNextToWall(hitbox, xSpeed);
            if (powerAttackActive) {
                powerAttackActive = false;
                powerAttackTick = 0;
            }
        }
    }

    public void changeHealth(int value) {
        if (dodge) {
            return;
        }
        currHealth += value;

        if (value < 0) {
            hit = true;
            playing.getGame().getAudioPlayer().playHit();
            playing.getGame().getAudioPlayer().playEnemiesAttackSound();
        }

        if (currHealth <= 0) {
            currHealth = 0;
        }
        if (currHealth >= maxHealth) {
            currHealth = maxHealth;
        }
    }

    public void changePower(int value) {
        powerValue += value;
        if (powerValue >= powerMaxValue) {
            powerValue = powerMaxValue;
        } else if (powerValue <= 0) {
            powerValue = 0;
        }
    }

    public void kill() {
        currHealth = 0;
    }

    private void loadAnimation() {

        BufferedImage img = StoreImage.GetSpriteAtLas(StoreImage.PLAYER_ATLAS);

        animations = new BufferedImage[9][8];
        for (int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i*49, j*33, 49, 33);
            }
        }

        statusBar = StoreImage.GetSpriteAtLas(StoreImage.STATUS_BAR);
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
        dodge = false;
        currHealth = maxHealth;

        state = IDLE;

        hitbox.x = x;
        hitbox.y = y;

        updateAttackBox();

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

    public boolean getDodge() {
        return dodge;
    }

    public boolean getPowerAttack() {
        return powerAttackActive;
    }
}