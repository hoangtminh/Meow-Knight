package levels;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Crabby;
import main.Game;
import objects.*;
import utils.HelpMethod;

import static utils.HelpMethod.GetLevelData;
import static utils.HelpMethod.GetCrabs;
import static utils.HelpMethod.GetPlayerSpawn;

public class Levels {
    private int[][] lvlData;
    private BufferedImage img;
    private ArrayList<Crabby> crabs;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> gameContainers;
    private ArrayList<Spike> spikes;
    private ArrayList<Cannon> cannons;

    private int lvlTileWide;
    private int maxTileOffset;
    private int maxLvlOffset;

    private Point playerSpawn;

    public Levels(BufferedImage img) {
        this.img = img;
        createLevelData();
        createEnemy();
        createPotions();
        createGameContainers();
        createSpikes();
        createCannons();
        calculateLevelOffset();
        calculatePlayerSpawn();
    }

    private void createCannons() {
        cannons = HelpMethod.GetCannons(img);     
    }

    private void createSpikes() {
        spikes = HelpMethod.GetSpikes(img);    
    }

    private void createGameContainers() {
        gameContainers = HelpMethod.GetContainer(img);
    }

    private void createPotions() {
        potions = HelpMethod.GetPotion(img);
    }
    
    private void createEnemy() {
        crabs = GetCrabs(img);
    }

    private void createLevelData() {
        lvlData = GetLevelData(img);
    }

    private void calculatePlayerSpawn() {
        playerSpawn = GetPlayerSpawn(img);    
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
