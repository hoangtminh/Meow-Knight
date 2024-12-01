package objects;

import static utils.Constants.ObjectsConstants.*;
import static utils.Constants.Projectiles.*;
import static utils.HelpMethod.IsProjectileHitThings;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.ArcherDoggo;
import entities.Player;
import gameStates.Playing;
import levels.Levels;
import utils.LoadSave;

public class ObjectManager {

    private Playing playing;
    private BufferedImage[][] potionsImgs, containerImgs;
    private BufferedImage spikeImg, projectileLeftImg, projectileRightImg;

    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;
    private static ArrayList<Projectile> projectiles = new ArrayList<>();
        
        public ObjectManager(Playing playing) {
            this.playing = playing;
            loadImgs();
            potions = new ArrayList<>();
            containers = new ArrayList<>();
        }
    
        public void checkSpikesTouched(Player player) {
            for (Spike spike: spikes) {
                if (spike.getHitbox().intersects(player.getHitbox())) {
                    player.kill();
                }
            }
        }
    
        public void checkObjectTouched(Rectangle2D.Float hitbox) {
            for (Potion p: potions) {
                if (p.isActive()) {
                    if (hitbox.intersects(p.getHitbox())) {
                        p.setActive(false);
                        applyEffectToPlayer(p);
                    }
                }
            }
        }
    
        public void applyEffectToPlayer(Potion p) {
            if (p.getObjType() == RED_POTION) {
                playing.getPlayer().changeHealth(RED_POTION_VALUE);
            } else {
                playing.getPlayer().changePower(BLUE_POTION_VALUE);
            }
        }
    
        public void checkObjectHit(Rectangle2D.Float attackbox) {
            for (GameContainer gc: containers) {
                if (gc.isActive() && !gc.doAnimation) {
                    if (gc.getHitbox().intersects(attackbox)) {
                        gc.setDoAnimation(true);
                        
                        int type = 0;
                        if (gc.objType == BARREL) {
                            type = 1;
                        }
                        potions.add(new Potion((int) (gc.getHitbox().x + gc.getHitbox().width/2), 
                        (int) (gc.getHitbox().y),
                        type));
                    }
                }
            }
        }
    
        public void resetAllObjects() {
    
            loadObject(playing.getLevelManager().getCurrLevels());
    
            for (Potion p: potions) {
                p.resetObject();
            }
            for (GameContainer gc: containers) {
                gc.resetObject();
            }
        }
    
        public void loadObject(Levels newLevel) {
            potions = new ArrayList<>(newLevel.getPotions());
            containers = new ArrayList<>(newLevel.getGameContainers());
            spikes = newLevel.getSpikes();
            projectiles.clear();
        }
    
        private void loadImgs() {
            BufferedImage potionSprite = LoadSave.GetSpriteAtLas(LoadSave.POTIONS_SPRITE);
            potionsImgs = new BufferedImage[2][7];
    
            for (int i = 0; i < potionsImgs.length; i++) {
                for (int j = 0; j < potionsImgs[i].length; j++) {
                    potionsImgs[i][j] = potionSprite.getSubimage(12 * j, 16 * i, 12, 16);
                }
            }
    
            BufferedImage containerSprite = LoadSave.GetSpriteAtLas(LoadSave.OBJECT_SPRITE);
            containerImgs = new BufferedImage[2][8];
    
            for (int i = 0; i < containerImgs.length; i++) {
                for (int j = 0; j < containerImgs[i].length; j++) {
                    containerImgs[i][j] = containerSprite.getSubimage(40 * j, 30 * i, 40, 30);
                }
            }
    
            spikeImg = LoadSave.GetSpriteAtLas(LoadSave.TRAP_SPRITE);
            
            projectileLeftImg = LoadSave.GetSpriteAtLas(LoadSave.ARROW_LEFT);
            projectileRightImg = LoadSave.GetSpriteAtLas(LoadSave.ARROW_RIGHT);
        }
    
        public void update(int[][] lvlData, Player player) {
            for (Potion p : potions) {
                if (p.isActive()) {
                    p.update();
                }
            }
    
            for (GameContainer gc : containers) {
                if (gc.isActive()) {
                    gc.update();
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
    
        public static void shootArcher(ArcherDoggo a, int walkDir) {
            int dir = -1;
            if (walkDir == 2) {
                dir = 1;
            }
            projectiles.add(new Projectile((int) a.getHitbox().x, (int) a.getHitbox().y, dir));
    }

    public void draw(Graphics g, int lvlOffset) {
        drawPotion(g, lvlOffset);
        drawContainer(g, lvlOffset);
        drawTraps(g, lvlOffset);
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

    private void drawContainer(Graphics g, int lvlOffset) {
        for (GameContainer gc : containers) {
            int type = 0;
            if (gc.isActive()) {

                if (gc.objType == BARREL) {
                    type = 1;
                }

                g.drawImage(containerImgs[type][gc.getAniIndex()], 
                (int) (gc.getHitbox().x - gc.getxDrawOffset() - lvlOffset), 
                (int) (gc.getHitbox().y - gc.getyDrawOffset()),
                CONTAINER_WIDTH,
                CONTAINER_HEIGHT,
                 null);
            }
        }
    }

    private void drawPotion(Graphics g, int lvlOffset) {
        for (Potion p : potions) {
            if (p.isActive()) {
                int type = 0;
                if (p.getObjType() == RED_POTION) {
                    type = 1;
                }
                g.drawImage(potionsImgs[type][p.getAniIndex()], 
                (int) (p.getHitbox().x - p.getxDrawOffset() - lvlOffset), 
                (int) (p.getHitbox().y - p.getyDrawOffset()),
                POTION_WIDTH,
                POTION_HEIGHT,
                 null);
            }
        }
    }
    
}
