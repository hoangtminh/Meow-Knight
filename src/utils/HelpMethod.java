package utils;

import main.Game;
import objects.Cannon;
import objects.GameContainer;
import objects.Potion;
import objects.Projectile;
import objects.Spike;

import static main.Game.TILES_SIZE;
import static utils.Constants.EnemyConstant.CRABBY;
import static utils.Constants.ObjectsConstants.*;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Crabby;

public class HelpMethod {
    public static boolean canMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        if (!isSolid(x, y, lvlData)) {
            if (!isSolid(x+width, y+height, lvlData)) {
                if (!isSolid(x+width, y, lvlData)) {
                    if (!isSolid(x, y+height, lvlData)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private static boolean isSolid(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * Game.TILES_SIZE;
        if (x < 0 || x >= maxWidth) {
            return true;
        }
        if (y < 0 || y >= Game.GAME_HEIGHT) {
            return true;
        }
        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;
        
        return isTileSolid((int) xIndex, (int) yIndex, lvlData);

    }

    public static float getEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currTile = (int) (hitbox.x / Game.TILES_SIZE);

        if (xSpeed > 0) {
            int tileXPos = currTile * Game.TILES_SIZE;
            int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
            return tileXPos + xOffset - 1;
        } else {
            return currTile * Game.TILES_SIZE;
        }
    }

    public static float getEntityYPosHit(Rectangle2D.Float hitbox, float airSpeed) {
        int currTile = (int) (hitbox.y / Game.TILES_SIZE);
        if (airSpeed > 0) {
            int tileYPos = currTile * Game.TILES_SIZE;
            int yOffset  = (int) (Game.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset-1;
        } else {
            return currTile * Game.TILES_SIZE;
        }
    }   

    public static boolean isOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        if (!isSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData)) {
            if (!isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        return isSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData)
        && isSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }

    public static boolean canCannonSeePlayer(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
        int firstXTile = (int) (firstHitbox.x / TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / TILES_SIZE);

        if (firstXTile < secondXTile) {
            return isAllTileClear(firstXTile, secondXTile, yTile, lvlData);
        } else {
            return isAllTileClear(secondXTile, firstXTile, yTile, lvlData);
        }

    }

    private static boolean isAllTileClear(int xStart, int xEnd, int y, int[][] lvlData) {
        for (int i = 0; i < xEnd - xStart; i++) {
            if (isTileSolid(xStart + i, y, lvlData)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllTileWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
        if (isAllTileClear(xStart, xEnd, y, lvlData)) {
            for (int i = 0; i < xEnd - xStart; i++) {
                if (!isTileSolid(xStart + i, y+1, lvlData)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static boolean isSightClear(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
        int firstXTile = (int) (firstHitbox.x / TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / TILES_SIZE);

        if (firstXTile < secondXTile) {
            return isAllTileWalkable(firstXTile, secondXTile, yTile, lvlData);
        } else {
            return isAllTileWalkable(secondXTile, firstXTile, yTile, lvlData);
        }

    }

    public static boolean IsProjectileHitThings(Projectile p, int[][] lvlData) {
        return isSolid(p.getHitbox().x + p.getHitbox().width/2, p.getHitbox().y + p.getHitbox().height/2, lvlData);
    }

    public static boolean isTileSolid(int xTile, int yTile, int[][] lvlData) {
        int value = lvlData[yTile][xTile];
        if (value >= 48 || value < 0 || value != 11) {
            return true;
        }

        return false;
    }

    public static int[][] GetLevelData(BufferedImage img) {
        int[][] lvlData = new int[img.getHeight()][img.getWidth()];
        
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                int value = color.getRed();
                if (value >= 48) {
                    value = 0;
                }
                lvlData[i][j] = value;
            }
        }
        return lvlData;
    }

    public static ArrayList<Crabby> GetCrabs(BufferedImage img) {
        ArrayList<Crabby> list = new ArrayList<>();
        
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == CRABBY) {
                    list.add(new Crabby(i*Game.TILES_SIZE, j*Game.TILES_SIZE));
                }
            }
        }

        return list;
    }

    public static Point GetPlayerSpawn(BufferedImage img) {
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getGreen();
                if (value == 100) {
                    return new Point(i * TILES_SIZE, j * TILES_SIZE);
                }
            }
        }
        return new Point(1 * TILES_SIZE, 1 * TILES_SIZE);
    }

    public static ArrayList<Potion> GetPotion(BufferedImage img) {
        ArrayList<Potion> list = new ArrayList<>();
        
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == RED_POTION || value == BLUE_POTION) {
                    list.add(new Potion(i*Game.TILES_SIZE, j*Game.TILES_SIZE, value));
                }
            }
        }

        return list;
    }

    public static ArrayList<GameContainer> GetContainer(BufferedImage img) {
        ArrayList<GameContainer> list = new ArrayList<>();
        
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == BOX || value == BARREL) {
                    list.add(new GameContainer(i*Game.TILES_SIZE, j*Game.TILES_SIZE, value));
                }
            }
        }

        return list;
    }

    public static ArrayList<Spike> GetSpikes(BufferedImage img) {
        ArrayList<Spike> list = new ArrayList<>();
        
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == SPIKE) {
                    list.add(new Spike(i*Game.TILES_SIZE, j*Game.TILES_SIZE, SPIKE));
                }
            }
        }

        return list; 
    }

    public static ArrayList<Cannon> GetCannons(BufferedImage img) {
        ArrayList<Cannon> list = new ArrayList<>();
        
        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == CANNON_LEFT || value == CANNON_RIGHT) {
                    list.add(new Cannon(i*Game.TILES_SIZE, j*Game.TILES_SIZE, value));
                }
            }
        }

        return list; 
    }
}   
