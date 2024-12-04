package levels;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.*;
import main.Game;
import objects.*;

import static utils.Constants.EnemyConstant.ARCHER;
import static utils.Constants.EnemyConstant.SWORD;
import static utils.Constants.EnemyConstant.BOXING;
import static utils.Constants.EnemyConstant.AXE;
import static utils.Constants.ObjectsConstants.*;
import static utils.Constants.PlayerConstants.SPAWN;

public class Levels {
    private int[][] lvlData;
    private BufferedImage img;
    private ArrayList<ArcherDoggo> archers = new ArrayList<ArcherDoggo>();
    private ArrayList<SwordDoggo> swords = new ArrayList<SwordDoggo>();
    private ArrayList<BoxingDoggo> boxings = new ArrayList<BoxingDoggo>();
    private ArrayList<AxeDoggo> axes = new ArrayList<AxeDoggo>();

    private ArrayList<Heal> heals = new ArrayList<Heal>();
    private ArrayList<Chest> chests = new ArrayList<Chest>();
    private ArrayList<Spike> spikes = new ArrayList<Spike>();
    private ArrayList<Coin> coins = new ArrayList<Coin>();

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
        if (value >= 71) {
            value = 0;
        }
        lvlData[x][y] = value;
    }

    private void loadEntities(int value, int x, int y) {
        switch (value) {
            case ARCHER:
                archers.add(new ArcherDoggo(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
                break;
            case SWORD:
                swords.add(new SwordDoggo(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
                break;
            case BOXING:
                boxings.add(new BoxingDoggo(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
                break;
            case AXE:
                axes.add(new AxeDoggo(x * Game.TILES_SIZE, y * Game.TILES_SIZE));
                break;
            case SPAWN:
                playerSpawn = new Point(x * Game.TILES_SIZE, y * Game.TILES_SIZE);
            default:
                break;
        }
    }

    private void loadObject(int value, int x, int y) {
        switch (value) {
            case HEAL:
                heals.add(new Heal(x * Game.TILES_SIZE, y * Game.TILES_SIZE, HEAL));
                break;
            case CHEST:
                chests.add(new Chest(x * Game.TILES_SIZE, y * Game.TILES_SIZE, CHEST));
                break;
            case SPIKE:
                spikes.add(new Spike(x * Game.TILES_SIZE, y * Game.TILES_SIZE, SPIKE));
                break;
            case COIN:
                coins.add(new Coin(x * Game.TILES_SIZE, y * Game.TILES_SIZE, COIN));
                break;
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

    public ArrayList<Heal> getHeals() {
        return heals;
    }

    public ArrayList<Chest> getChests() {
        return chests;
    }

    public ArrayList<ArcherDoggo> getArchers() {
        return archers;
    }

    public ArrayList<Spike> getSpikes() {
        return spikes;
    }
    
    public ArrayList<SwordDoggo> getSwords() {
        return swords;
    }
    
    public ArrayList<BoxingDoggo> getBoxings() {
        return boxings;
    }
    
    public ArrayList<AxeDoggo> getAxes() {
        return axes;
    }
    
    public ArrayList<Coin> getCoins() {
        return coins;
    }
    
    public Point getPlayerSpawn() {
        return playerSpawn;
    }
}
