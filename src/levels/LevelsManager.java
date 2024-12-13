package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import main.Game;
import utils.StoreImage;

public class LevelsManager {

    private Game game;
    private BufferedImage[] levelSprite;
    private ArrayList<Levels> levels;
    private int lvlIndex = 0;
    private Opening opening;
    private Ending ending;
    private int coinNum[] = new int[6];
    
    public LevelsManager(Game game) {
        this.game = game;
        importOutsideSprite();
        levels = new ArrayList<>();
        buildAllLevels();
        opening = new Opening();
        ending = new Ending();
        coinNum[0] = 0;
    }

    private void buildAllLevels() {
        BufferedImage[] allLevels = StoreImage.getAllLevels();
        for (BufferedImage img : allLevels) {
            levels.add(new Levels(img));
        }
    }

    public void importOutsideSprite() {
        BufferedImage img = StoreImage.GetSpriteAtLas(StoreImage.LEVEL_ATLAS);
        levelSprite = new BufferedImage[72];
        for (int j = 0; j < 6; j++ ) {
            for (int i = 0; i < 12; i++) {
                int index = j*12 + i;
                levelSprite[index] = img.getSubimage(i*32, j*32, 32, 32);
            }
        }
    }

    public void draw(Graphics g, int lvlOffset) {
        if (opening.isActive()) {
            opening.draw(g);
        } else if (ending.isActive()) {
            ending.draw(g);
        } else {
            for (int  j = 0; j < Game.TILES_IN_HEIGHT; j++) {
                for (int i = 0; i < levels.get(lvlIndex).getLevelData()[0].length; i++) {
                    int index = levels.get(lvlIndex).getSpriteIndex(i, j);
                    g.drawImage(levelSprite[index], i*Game.TILES_SIZE - lvlOffset, j*Game.TILES_SIZE , Game.TILES_SIZE, Game.TILES_SIZE, null);
                }
            }
        }
    }

    public void nextLevel() {
        lvlIndex++;
        if (lvlIndex == 0) {
            opening.setActive(true);
        } else {
            opening.setActive(false);
        }
        if (lvlIndex >= levels.size()) {
            lvlIndex = levels.size()-1;
            ending.setActive(true);
        }
        Levels newLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
        game.getPlaying().setLevelOffset(newLevel.getLevelOffset());
        game.getPlaying().getObjectManager().loadObject(newLevel);
    }

    public void update() {
        if (opening.isActive()) {
            opening.update();
        } else if (ending.isActive()) {
            ending.update();
        }
    }

    public void updateCoinNum(int lvlIndex) {
        if (coinNum[lvlIndex] < game.getPlaying().getObjectManager().getCoinNum()) {
            coinNum[lvlIndex] = game.getPlaying().getObjectManager().getCoinNum();
        } 
        if (lvlIndex < 5) {
            game.getMenu().getSelectLevel().setButtonActive(lvlIndex + 1);
        }
    }

    public Levels getCurrLevels() {
        return levels.get(lvlIndex);
    }

    public int getAmountLevel() {
        return levels.size();
    }

    public int getLvlIndex() {
        return lvlIndex;
    }

    public void setLvlIndex(int lvlIndex) {
        this.lvlIndex = lvlIndex;
    }

    public Opening getOpening() {
        return opening;
    }

    public Ending getEnding () {
        return ending;
    }

    public int getCoinNum(int index) {
        return coinNum[index];
    }
}