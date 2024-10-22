package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gameStates.GameState;
import main.Game;
import utils.LoadSave;

public class LevelsManager {

    private Game game;
    private BufferedImage[] levelSprite;
    private ArrayList<Levels> levels;
    private int lvlIndex = 0;
    
    public LevelsManager(Game game) {
        this.game = game;
        importOutsideSprite();
        levels = new ArrayList<>();
        buildAllLevels();
    }

    private void buildAllLevels() {
        BufferedImage[] allLevels = LoadSave.getAllLevels();
        for (BufferedImage img : allLevels) {
            levels.add(new Levels(img));
        }
    }

    public void importOutsideSprite() {
        BufferedImage img = LoadSave.GetSpriteAtLas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[48];
        for (int j = 0; j < 4; j++ ) {
            for (int i = 0; i < 12; i++) {
                int index = j*12 + i;
                levelSprite[index] = img.getSubimage(i*32, j*32, 32, 32);
            }
        }
    }

    public void draw(Graphics g, int lvlOffset) {
        for (int  j = 0; j < Game.TILES_IN_HEIGHT; j++) {
            for (int i = 0; i < levels.get(lvlIndex).getLevelData()[0].length; i++) {
                int index = levels.get(lvlIndex).getSpriteIndex(i, j);
                g.drawImage(levelSprite[index], i*Game.TILES_SIZE - lvlOffset, j*Game.TILES_SIZE , Game.TILES_SIZE, Game.TILES_SIZE, null);
            }
        }
    }

    public void nextLevel() {
        lvlIndex++;
        if (lvlIndex >= levels.size()) {
            lvlIndex = 0;
            System.out.println("Completed Game");
            GameState.state = GameState.MENU;
        }

        Levels newLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
        game.getPlaying().setLevelOffset(newLevel.getLevelOffset());
        game.getPlaying().getObjectManager().loadObject(newLevel);
    }

    public void update() {

    }

    public Levels getCurrLevels() {
        return levels.get(lvlIndex);
    }

    public int getAmountLevel() {
        return levels.size();
    }

}
