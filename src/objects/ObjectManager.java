package objects;

import static utils.Constants.ObjectsConstants.*;
import static utils.Constants.Projectiles.*;
import static utils.HelpMethod.canCannonSeePlayer;
import static utils.HelpMethod.IsProjectileHitThings;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Player;
import gameStates.Playing;
import levels.Levels;
import main.Game;
import utils.LoadSave;

public class ObjectManager {

    private Playing playing;
    private BufferedImage[][] potionsImgs, containerImgs;
    private BufferedImage spikeImg, cannonBallImg;
    private BufferedImage[] cannonImgs;

    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;
    private ArrayList<Cannon> cannons;
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    
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
        for (Cannon c: cannons) {
            c.resetObject();
        }
    }

    public void loadObject(Levels newLevel) {
        potions = new ArrayList<>(newLevel.getPotions());
        containers = new ArrayList<>(newLevel.getGameContainers());
        spikes = newLevel.getSpikes();
        cannons = newLevel.getCannons();
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

        cannonImgs = new BufferedImage[7];
        BufferedImage tmp = LoadSave.GetSpriteAtLas(LoadSave.CANNON_SPRITE);
        for (int i = 0; i < cannonImgs.length; i++) {
            cannonImgs[i] = tmp.getSubimage(i * 40, 0, 40, 26);
        }
        
        cannonBallImg = LoadSave.GetSpriteAtLas(LoadSave.CANNON_BALL);
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

        updateCannons(lvlData, player);
        updateProjectiles(lvlData, player);
    }

    private void updateProjectiles(int[][] lvlData, Player player) {
        for (Projectile p: projectiles) {
            if (p.isActive()) {
                p.updatePos();
                if (p.getHitbox().intersects(player.getHitbox())) {
                    player.changeHealth(-20);
                    p.setActive(false);
                } else if (IsProjectileHitThings(p, lvlData)) {
                    p.setActive(false);
                }
            }
        }
    }

    private boolean isPlayerInRange(Cannon c, Player player) {
        int absValue = (int) Math.abs(player.getHitbox().x - c.getHitbox().x);
        return absValue <= Game.TILES_SIZE * 5;
    }

    private void updateCannons(int[][] lvlData, Player player) {
        for (Cannon c: cannons) {
            if (!c.doAnimation) {
                if (c.getTileY() == player.getTileY()) {
                    if (isPlayerInRange(c, player)) {
                        if (isPlayerInFrontOfCannon(c, player)) {
                            if (canCannonSeePlayer(lvlData, c.getHitbox(), player.getHitbox(), c.getTileY())) {
                                c.setDoAnimation(true);
                            }
                        }
                    }
                }
            }
            if (c.getAniIndex() == 4 && c.getAniTick() == 0) {
                shootCannon(c);
            }
            c.update();
        }
    }

    private void shootCannon(Cannon c) {
        int dir = 1;
        if (c.getObjType() == CANNON_LEFT) {
            dir = -1;
        }
        projectiles.add(new Projectile((int) c.getHitbox().x, (int) c.getHitbox().y, dir));
    }

    private boolean isPlayerInFrontOfCannon(Cannon c, Player player) {
        if (c.getObjType() == CANNON_LEFT) {
            if (c.getHitbox().x > player.getHitbox().x) {
                return true;
            }
        } else if (c.getHitbox().x < player.getHitbox().x) {
            return true;
        }
        return false;
    }

    public void draw(Graphics g, int lvlOffset) {
        drawPotion(g, lvlOffset);
        drawContainer(g, lvlOffset);
        drawTraps(g, lvlOffset);
        drawCannons(g, lvlOffset);
        drawProjectile(g, lvlOffset);
    }

    private void drawProjectile(Graphics g, int lvlOffset) {
        for (Projectile p: projectiles) {
            if (p.isActive()) {
                g.drawImage(cannonBallImg, 
                (int) (p.getHitbox().x - lvlOffset), 
                (int) (p.getHitbox().y), 
                CANNON_BALL_WIDTH,
                CANNON_BALL_HEIGHT,
                null);
            }
        }    
    }

    private void drawCannons(Graphics g, int lvlOffset) {
        for (Cannon c: cannons) {
            int x = (int) (c.getHitbox().x - lvlOffset);
            int width = CANNON_WIDTH;

            if (c.getObjType() == CANNON_RIGHT){
                x += width;
                width *= -1;
            }
            g.drawImage(cannonImgs[c.getAniIndex()], x, (int) (c.getHitbox().y), width, CANNON_HEIGHT, null);
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
