package objects;

import static utils.Constants.ObjectsConstants.*;
import static utils.Constants.Projectiles.*;
import static utils.HelpMethod.IsProjectileHitThings;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.ArcherDoggo;
import entities.Player;
import gameStates.Playing;
import levels.Levels;
import utils.StoreImage;

public class ObjectManager {

    private Playing playing;
    private BufferedImage[] healsImgs, chestImgs;
    private BufferedImage spikeImg, projectileLeftImg, projectileRightImg;
    private BufferedImage[] coinsImgs;

    private ArrayList<Heal> heals;
    private ArrayList<Chest> chests;
    private ArrayList<Spike> spikes;
    private static ArrayList<Projectile> projectiles = new ArrayList<>();
    private ArrayList<Coin> coins;
        
    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImgs();
        heals = new ArrayList<>();
        chests = new ArrayList<>();
        coins = new ArrayList<>();
    }
    
    public void loadObject(Levels newLevel) {
        heals = new ArrayList<>(newLevel.getHeals());
        coins = new ArrayList<>(newLevel.getCoins());
        chests = new ArrayList<>(newLevel.getChests());
        spikes = newLevel.getSpikes();
        projectiles.clear();
    }
    
    private void loadImgs() {
        BufferedImage healsprite = StoreImage.GetSpriteAtLas(StoreImage.HEAL_SPRITE);
        healsImgs = new BufferedImage[4];
    
        for (int i = 0; i < healsImgs.length; i++) {
            healsImgs[i] = healsprite.getSubimage(HEAL_WIDTH_DEFAULT * i, 0, HEAL_WIDTH_DEFAULT, HEAL_WIDTH_DEFAULT);
        }
    
        BufferedImage chestSprite = StoreImage.GetSpriteAtLas(StoreImage.CHEST_SPRITE);
        chestImgs = new BufferedImage[4];
    
        for (int i = 0; i < chestImgs.length; i++) {
            chestImgs[i] = chestSprite.getSubimage(CHEST_WIDTH_DEFAULT * i, 0, CHEST_WIDTH_DEFAULT, CHEST_WIDTH_DEFAULT);
        }
    
        spikeImg = StoreImage.GetSpriteAtLas(StoreImage.TRAP_SPRITE);
        
        projectileLeftImg = StoreImage.GetSpriteAtLas(StoreImage.ARROW_LEFT);
        projectileRightImg = StoreImage.GetSpriteAtLas(StoreImage.ARROW_RIGHT);

        BufferedImage coinSprite = StoreImage.GetSpriteAtLas(StoreImage.COIN_SPRITE);
        coinsImgs = new BufferedImage[4];
        for (int i = 0; i < coinsImgs.length; i++) {
            coinsImgs[i] = coinSprite.getSubimage(i * COIN_WIDTH_DEFAULT, 0, COIN_WIDTH_DEFAULT, COIN_WIDTH_DEFAULT);
        }
    }

    public void update(int[][] lvlData, Player player) {
        for (Heal p : heals) {
            if (p.isActive()) {
                p.update();
            }
        }
    
        for (Chest c : chests) {
            if (c.isActive()) {
                c.update();
            }
        }

        for (Coin c : coins) {
            if (c.isActive()) {
                c.update();
            }
        }
        updateProjectiles(lvlData, player);
    }
        
    private void updateProjectiles(int[][] lvlData, Player player) {
        for (Projectile p: projectiles) {
            if (p.isActive()) {
                p.updatePos();
                if (p.getHitbox().intersects(player.getHitbox()) && !player.getDodge()) {
                    player.changeHealth(-20);
                    p.setActive(false);
                } else if (IsProjectileHitThings(p, lvlData)) {
                    p.setActive(false);
                }
            }
        }
    }
        
    public void checkSpikesTouched(Player player) {
        for (Spike spike: spikes) {
            if (spike.getHitbox().intersects(player.getHitbox())) {
                player.kill();
            }
        }
    }

    public void checkCoinTouched(Player player) {
        for (Coin c : coins) {
            if (c.isActive()) {
                if (player.getHitbox().intersects(c.getHitbox())) {
                    c.setActive(false);
                }
            }
        }
    }
    
    public void checkObjectTouched(Player player) {
        for (Heal p: heals) {
            if (p.isActive()) {
                if (player.getHitbox().intersects(p.getHitbox())) {
                    p.setActive(false);
                    applyEffectToPlayer(p);
                }
            }
        }
    }
    
    public void applyEffectToPlayer(Heal p) {
        playing.getPlayer().changeHealth(HEAL_VALUE);
    }

    public void checkObjectHit(Rectangle2D.Float attackbox) {
        for (Chest c: chests) {
            if (c.isActive() && !c.doAnimation) {
                if (c.getHitbox().intersects(attackbox)) {
                    c.setDoAnimation(true);
                    heals.add(new Heal((int) (c.getHitbox().x + c.getHitbox().width/3), (int) (c.getHitbox().y), HEAL));
                }
            }
        }
    }

    public void resetAllObjects() {

        loadObject(playing.getLevelManager().getCurrLevels());

        for (Heal p: heals) {
            p.resetObject();
        }
        for (Chest c: chests) {
            c.resetObject();
        }

        for (Coin c: coins) {
            c.resetObject();
        }
    }

    public static void shootArcher(ArcherDoggo a, int walkDir) {
        int dir = -1;
        if (walkDir == 2) {
            dir = 1;
        }
        projectiles.add(new Projectile((int) a.getHitbox().x, (int) a.getHitbox().y, dir));
    }

    public void draw(Graphics g, int lvlOffset) {
        drawHeals(g, lvlOffset);
        drawChests(g, lvlOffset);
        drawTraps(g, lvlOffset);
        drawCoins(g, lvlOffset);
        drawProjectile(g, lvlOffset);
    }

    private void drawProjectile(Graphics g, int lvlOffset) {
        for (Projectile p: projectiles) {
            if (p.isActive()) {
                if (p.getDir() == 1) {
                    g.drawImage(projectileRightImg, 
                    (int) (p.getHitbox().x - lvlOffset), 
                    (int) (p.getHitbox().y), 
                    ARROW_WIDTH,
                    ARROW_HEIGHT,
                    null);
                } else {
                    g.drawImage(projectileLeftImg, 
                    (int) (p.getHitbox().x - lvlOffset), 
                    (int) (p.getHitbox().y), 
                    ARROW_WIDTH,
                    ARROW_HEIGHT,
                    null);
                }
            }
        }    
    }

    private void drawTraps(Graphics g, int lvlOffset) {
        for (Spike spike: spikes) {
            g.drawImage(spikeImg, (int) (spike.hitbox.x - lvlOffset), 
            (int) (spike.hitbox.y - spike.yDrawOffset), 
            SPIKE_WIDTH,
            SPIKE_HEIGHT,
            null);
        }
    }

    private void drawChests(Graphics g, int lvlOffset) {
        for (Chest c : chests) {
            if (c.isActive()) {
                g.drawImage(chestImgs[c.getAniIndex()], 
                (int) (c.getHitbox().x - c.getxDrawOffset() - lvlOffset), 
                (int) (c.getHitbox().y - c.getyDrawOffset()),
                CHEST_WIDTH * 2,
                CHEST_WIDTH * 2,
                 null);
                g.setColor(Color.red);
                g.drawRect((int) c.getHitbox().x, (int) c.getHitbox().y, (int) c.getHitbox().width, (int)c.getHitbox().height);
            }
        }
    }

    private void drawHeals(Graphics g, int lvlOffset) {
        for (Heal p : heals) {
            if (p.isActive()) {
                g.drawImage(healsImgs[p.getAniIndex()], 
                (int) (p.getHitbox().x - p.getxDrawOffset() - lvlOffset), 
                (int) (p.getHitbox().y - p.getyDrawOffset()),
                HEAL_WIDTH, HEAL_WIDTH,
                 null);
                 g.setColor(Color.red);
                g.drawRect((int) p.getHitbox().x, (int) p.getHitbox().y, (int) p.getHitbox().width, (int)p.getHitbox().height);
            
            }
        }
    }
    
    private void drawCoins(Graphics g, int lvlOffset) {
        for (Coin c : coins) {
            if (c.isActive()) {
                g.drawImage(coinsImgs[c.getAniIndex()], 
                (int) (c.getHitbox().x- lvlOffset), 
                (int) (c.getHitbox().y - c.getyDrawOffset()), 
                COIN_WIDTH, 
                COIN_WIDTH, 
                null);
            }
        }
    }
}
