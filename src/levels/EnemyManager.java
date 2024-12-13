package levels;

import static utils.Constants.EnemyConstant.*;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.*;
import gameStates.Playing;
import utils.StoreImage;

public class EnemyManager {

    private Playing playing;
    private BufferedImage[][] archerArr, swordArr, boxingArr, axeArr;
    private ArrayList<ArcherDoggo> archers = new ArrayList<>();
    private ArrayList<SwordDoggo> swords = new ArrayList<>();
    private ArrayList<BoxingDoggo> boxings = new ArrayList<>();
    private ArrayList<AxeDoggo> axes = new ArrayList<>();
    
    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImgs();
    }

    public void loadEnemies(Levels level) {
        archers = level.getArchers();
        swords = level.getSwords();
        boxings = level.getBoxings();
        axes = level.getAxes();
    }

    public void update(int[][] lvlData, Player player) {
        boolean isAnyActive = false;
        for (ArcherDoggo a : archers) {
            if (a.isActive()){
                a.update(lvlData, player);
                isAnyActive = true;
            }
        }
        for (SwordDoggo s : swords) {
            if (s.isActive()){
                s.update(lvlData, player);
                isAnyActive = true;
            }
        }
        for (BoxingDoggo b : boxings) {
            if (b.isActive()){
                b.update(lvlData, player);
                isAnyActive = true;
            }
        }
        for (AxeDoggo ax : axes) {
            if (ax.isActive()){
                ax.update(lvlData, player);
                isAnyActive = true;
            }
        }

        if (!isAnyActive) {
            playing.setLevelComplete(true);
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawArchers(g, xLvlOffset);
        drawSwords(g, xLvlOffset);
        drawBoxings(g, xLvlOffset);
        drawAxes(g, xLvlOffset);
    }

    private void drawArchers(Graphics g, int xLvlOffset) {
        for (ArcherDoggo a : archers) {
            if (a.isActive()) {
                g.drawImage(archerArr[a.getEnemyState()][a.getAniIndex()],
                (int) (a.getHitbox().x - xLvlOffset - DOGGO_DRAWOFFSET_X + a.flipX()), 
                (int) (a.getHitbox().y - DOGGO_DRAWOFFSET_Y),
                CalculateDoggoWidth(ARCHER) * a.flipW(), DOGGO_HEIGHT, null);
                // c.drawAttackBox(g, xLvlOffset);
                a.drawHitbox(g, xLvlOffset);
            }
        }
    }

    private void drawSwords(Graphics g, int xLvlOffset) {
        for (SwordDoggo s : swords) {
            if (s.isActive()) {
                g.drawImage(swordArr[s.getEnemyState()][s.getAniIndex()],
                (int) (s.getHitbox().x - xLvlOffset - DOGGO_DRAWOFFSET_X + s.flipX()), 
                (int) (s.getHitbox().y - DOGGO_DRAWOFFSET_Y),
                CalculateDoggoWidth(SWORD) * s.flipW(), DOGGO_HEIGHT, null);
                // c.drawAttackBox(g, xLvlOffset);
                // s.drawHitbox(g, xLvlOffset);
            }
        }
    }
    private void drawBoxings(Graphics g, int xLvlOffset) {
        for (BoxingDoggo b : boxings) {
            if (b.isActive()) {
                g.drawImage(boxingArr[b.getEnemyState()][b.getAniIndex()],
                (int) (b.getHitbox().x - xLvlOffset - DOGGO_DRAWOFFSET_X + b.flipX()), 
                (int) (b.getHitbox().y - DOGGO_DRAWOFFSET_Y),
                CalculateDoggoWidth(BOXING) * b.flipW(), DOGGO_HEIGHT, null);
                // c.drawAttackBox(g, xLvlOffset);
                // b.drawHitbox(g, xLvlOffset);
            }
        }
    }

    private void drawAxes(Graphics g, int xLvlOffset) {
        for (AxeDoggo ax : axes) {
            if (ax.isActive()) {
                g.drawImage(axeArr[ax.getEnemyState()][ax.getAniIndex()],
                (int) (ax.getHitbox().x - xLvlOffset - DOGGO_DRAWOFFSET_X + ax.flipX()), 
                (int) (ax.getHitbox().y - DOGGO_DRAWOFFSET_Y),
                CalculateDoggoWidth(AXE) * ax.flipW(), DOGGO_HEIGHT, null);
                // c.drawAttackBox(g, xLvlOffset);
                // ax.drawHitbox(g, xLvlOffset);
            }
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        for (ArcherDoggo a : archers) {
            if (a.isActive()) {
                if (a.getCurrentHealth() > 0) {
                    if (attackBox.intersects(a.getHitbox())) {
                        if (playing.getPlayer().getPowerAttack()) {
                            a.hurt(1);
                        } else {
                            a.hurt(8);
                        }
                        playing.getGame().getAudioPlayer().playEnemiesHitSound();
                        return;
                    }
                }
            }
        }

        for (AxeDoggo ax : axes) {
            if (ax.isActive()) {
                if (ax.getCurrentHealth() > 0) {
                    if (attackBox.intersects(ax.getHitbox())) {
                        if (playing.getPlayer().getPowerAttack()) {
                            ax.hurt(1);
                        } else {
                            ax.hurt(8);
                        }
                        playing.getGame().getAudioPlayer().playEnemiesHitSound();
                        return;
                    }
                }
            }
        }

        for (BoxingDoggo b : boxings) {
            if (b.isActive()) {
                if (b.getCurrentHealth() > 0) {
                    if (attackBox.intersects(b.getHitbox())) {
                        if (playing.getPlayer().getPowerAttack()) {
                            b.hurt(1);
                        } else {
                            b.hurt(8);
                        }
                        playing.getGame().getAudioPlayer().playEnemiesHitSound();
                        return;
                    }
                }
            }
        }

        for (SwordDoggo s : swords) {
            if (s.isActive()) {
                if (s.getCurrentHealth() > 0) {
                    if (attackBox.intersects(s.getHitbox())) {
                        if (playing.getPlayer().getPowerAttack()) {
                            s.hurt(1);
                        } else {
                            s.hurt(8);
                        }
                        playing.getGame().getAudioPlayer().playEnemiesHitSound();
                        return;
                    }
                }
            }
        }
    }

    private void loadEnemyImgs() {
        BufferedImage tmp = StoreImage.GetSpriteAtLas(StoreImage.ARCHER_SPRITE);

        archerArr = new BufferedImage[5][10];
        for (int i = 0; i < archerArr.length; i++ ){
            for (int j = 0; j < archerArr[i].length; j++) {
                archerArr[i][j] = tmp.getSubimage(j* GetDoggoWidth(ARCHER), i* DOGGO_HEIGHT_DEFAULT, GetDoggoWidth(ARCHER), DOGGO_HEIGHT_DEFAULT);
            }
        }

        tmp = StoreImage.GetSpriteAtLas(StoreImage.SWORD_SPRITE);
        swordArr = new BufferedImage[5][10];
        for (int i = 0; i < swordArr.length; i++ ){
            for (int j = 0; j < swordArr[i].length; j++) {
                swordArr[i][j] = tmp.getSubimage(j* GetDoggoWidth(SWORD), i* DOGGO_HEIGHT_DEFAULT, GetDoggoWidth(SWORD), DOGGO_HEIGHT_DEFAULT);
            }
        }

        tmp = StoreImage.GetSpriteAtLas(StoreImage.BOXING_SPRITE);
        boxingArr = new BufferedImage[5][11];
        for (int i = 0; i < boxingArr.length; i++ ){
            for (int j = 0; j < boxingArr[i].length; j++) {
                boxingArr[i][j] = tmp.getSubimage(j* GetDoggoWidth(BOXING), i* DOGGO_HEIGHT_DEFAULT, GetDoggoWidth(BOXING), DOGGO_HEIGHT_DEFAULT);
            }
        }

        tmp = StoreImage.GetSpriteAtLas(StoreImage.AXE_SPRITE);
        axeArr = new BufferedImage[5][10];
        for (int i = 0; i < axeArr.length; i++ ){
            for (int j = 0; j < axeArr[i].length; j++) {
                axeArr[i][j] = tmp.getSubimage(j* GetDoggoWidth(AXE), i* DOGGO_HEIGHT_DEFAULT, GetDoggoWidth(AXE), DOGGO_HEIGHT_DEFAULT);
            }
        }
    }

    public void resetAllEnemies() {
        for (ArcherDoggo a: archers) {
            a.resetEnemy();
        }
        for (SwordDoggo s: swords) {
            s.resetEnemy();
        }
        for (BoxingDoggo b: boxings) {
            b.resetEnemy();
        }
        for (AxeDoggo ax: axes) {
            ax.resetEnemy();
        }
    }
}
