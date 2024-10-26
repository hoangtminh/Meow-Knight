package levels;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Crabby;
import main.Game;
import objects.*;

import static utils.Constants.EnemyConstant.CRABBY;
import static utils.Constants.ObjectsConstants.*;
import static utils.Constants.PlayerConstants.SPAWN;

public class Levels {
    private int[][] lvlData;
    private BufferedImage img;
    private ArrayList<Crabby> crabs = new ArrayList<Crabby>();
    private ArrayList<Potion> potions = new ArrayList<Potion>();
    private ArrayList<GameContainer> gameContainers = new ArrayList<GameContainer>();
    private ArrayList<Spike> spikes = new ArrayList<Spike>();
    private ArrayList<Cannon> cannons = new ArrayList<Cannon>();

    private int lvlTileWide;
    private int maxTileOffset;
    private int maxLvlOffset;

    private Point playerSpawn;

    public Levels(BufferedImage img) {
        this.img = img;
        lvlData = new int[img.getHeight()][img.getWidth()];
        loadLevel();
        calculateLevelOffset();
    }

    private void loadLevel() {
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i));
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                
                loadLevelData(red, i, j);
                loadEntities(green, j, i);
                loadObject(blue, j, i);
            }
        }
    }

    private void loadLevelData(int value, int x, int y) {
        if (value >= 48) {
            value = 0;
        }
        lvlData[x][y] = value;
    }

    private void loadEntities(int value, int x, int y) {
        switch (value) {
            case CRABBY:
                crabs.add(new Crabby(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
                break;
            case SPAWN:
                playerSpawn = new Point(x * Game.TILES_SIZE, y * Game.TILES_SIZE);
            default:
                break;
        }
    }

    private void loadObject(int value, int x, int y) {
        switch (value) {
            case CANNON_LEFT, CANNON_RIGHT:
                cannons.add(new Cannon(x * Game.TILES_SIZE, y * Game.TILES_SIZE, value));
                break;
            case RED_POTION, BLUE_POTION:
                potions.add(new Potion(x * Game.TILES_SIZE, y * Game.TILES_SIZE, value));
                break;
            case BOX, BARREL:
                gameContainers.add(new GameContainer(x * Game.TILES_SIZE, y * Game.TILES_SIZE, value));
                break;
            case SPIKE:
                spikes.add(new Spike(x * Game.TILES_SIZE, y * Game.TILES_SIZE, SPIKE));
            default:
                break;
        }
    }

    private void calculateLevelOffset() {
        lvlTileWide = img.getWidth();
        maxTileOffset = lvlTileWide - Game.TILES_IN_WIDTH;
        maxLvlOffset = maxTileOffset * Game.TILES_SIZE;    
    }

    public int getSpriteIndex(int x, int y) {
        return lvlData[y][x];
    }

    public int[][] getLevelData() {
        return lvlData;
    }

    public int getLevelOffset() {
        return maxLvlOffset;
    }

    public ArrayList<Potion> getPotions() {
        return potions;
    }

    public ArrayList<GameContainer> getGameContainers() {
        return gameContainers;
    }

    public ArrayList<Crabby> getCrabs() {
        return crabs;
    }

    public ArrayList<Spike> getSpikes() {
        return spikes;
    }

    public ArrayList<Cannon> getCannons() {
        return cannons;
    }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }
}
