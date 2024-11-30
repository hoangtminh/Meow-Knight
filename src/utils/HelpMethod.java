package utils;

import main.Game;

import objects.Projectile;

import java.awt.geom.Rectangle2D;

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
        int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);

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

    public static boolean isSightClear(int[][] lvlData, Rectangle2D.Float enemyHitbox, Rectangle2D.Float playerHitbox, int yTile) {
        int enemyXTile = (int) (enemyHitbox.x / Game.TILES_SIZE);
        int playerXTile;
        if (isSolid(playerHitbox.x, playerHitbox.y + playerHitbox.height + 1, lvlData)) {
            playerXTile = (int) (playerHitbox.x / Game.TILES_SIZE);
        } else {
            playerXTile = (int) ((playerHitbox.x + playerHitbox.width) / Game.TILES_SIZE);
        } 

        if (enemyXTile < playerXTile) {
            return isAllTileWalkable(enemyXTile, playerXTile, yTile, lvlData);
        } else {
            return isAllTileWalkable(playerXTile, enemyXTile, yTile, lvlData);
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

    public static boolean IsWater(Rectangle2D.Float hitbox, int[][] lvlData) {    
        int xIndexLeft = (int) (hitbox.x / Game.TILES_SIZE);
        int xIndexRight = (int) ((hitbox.x + hitbox.width) / Game.TILES_SIZE);
        int yIndex = (int) ((hitbox.y + hitbox.height + 1) / Game.TILES_SIZE);

        int valueLeft = lvlData[yIndex][xIndexLeft];
        int valueRight = lvlData[yIndex][xIndexRight];
        if ((valueLeft >= 48 && valueLeft <= 50) || (valueLeft >= 60 && valueLeft <= 62)) {
            if ((valueRight >= 48 && valueRight <= 50) || (valueRight >= 60 && valueRight <= 62)) {
                return true;
            }
        }
        return false;
    }
}   
