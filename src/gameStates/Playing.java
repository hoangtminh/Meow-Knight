package gameStates;

import java.awt.Color;
import java.awt.Graphics;

import entities.Player;
import levels.EnemyManager;
import levels.LevelsManager;
import main.Game;
import objects.ObjectManager;
import ui.GameOverOverlay;
import ui.LevelCompleteOverlay;
import ui.PauseOverlay;
import utils.LoadSave;
import static utils.Constants.UI.Environment.*;

import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.awt.event.KeyEvent;

public class Playing extends State implements StateMethods {

    private Player player;
    
    private LevelsManager levelsManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;
    
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompleteOverlay levelCompleteOverlay;

    private boolean pause = false;
    private boolean gameOver = false;
    private boolean lvlComplete = false;
    private boolean playerDying = false;

    private int xLvlOffset;
    private int leftBorderOffset = (int) (0.35 * Game.GAME_WIDTH);
    private int rightBorderOffset = (int) (0.65 * Game.GAME_WIDTH);
    private int maxLvlOffset;

    private BufferedImage backImage, bigCloud, smallCloud;
    private int[] smallCloudPos;
    private Random rand = new Random();
    
    public Playing(Game game) {
        super(game);
        initClasses();

        backImage = LoadSave.GetSpriteAtLas(LoadSave.PLAYING_BG_IMG);
        bigCloud = LoadSave.GetSpriteAtLas(LoadSave.BIG_CLOUD);
        smallCloud = LoadSave.GetSpriteAtLas(LoadSave.SMALL_CLOUD);
        smallCloudPos = new int[8];
        for (int i = 0; i < smallCloudPos.length; i++) {
            smallCloudPos[i] = (int) (90*Game.SCALE) + rand.nextInt((int) (100* Game.SCALE));
        }

        calculateLevelOffset();
        loadStartLevel();
    }

    public void loadNextLevel() {
        resetAllPlaying();
        levelsManager.nextLevel();
        player.setSpawn(levelsManager.getCurrLevels().getPlayerSpawn());
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelsManager.getCurrLevels());
        objectManager.loadObject(levelsManager.getCurrLevels());
    }

    private void calculateLevelOffset() {
        maxLvlOffset = levelsManager.getCurrLevels().getLevelOffset();    
    }

    private void initClasses() {
        enemyManager = new EnemyManager(this);
        levelsManager = new LevelsManager(game);
        objectManager = new ObjectManager(this);
        player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), this);
        player.loadLvlData(levelsManager.getCurrLevels().getLevelData());
        player.setSpawn(levelsManager.getCurrLevels().getPlayerSpawn());
        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompleteOverlay = new LevelCompleteOverlay(this);
    }
    
    public void windowFocusLost() {
        player.resetDirection();
    }

    @Override
    public void update() {
        if (pause) {
            pauseOverlay.update();
        } else if (lvlComplete) {
            levelCompleteOverlay.update();
        } else if (gameOver) {
            gameOverOverlay.update();
        } else if (playerDying) {
            player.update();
        } else if (!gameOver) {
            levelsManager.update();
            enemyManager.update(levelsManager.getCurrLevels().getLevelData(), player);
            objectManager.update(levelsManager.getCurrLevels().getLevelData(), player);
            player.update();
            checkCloseToBorder();
        }
    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLvlOffset;
        if (diff > rightBorderOffset) {
            xLvlOffset += diff - rightBorderOffset;
        } else if (diff < leftBorderOffset) {
            xLvlOffset += diff - leftBorderOffset;
        }

        if (xLvlOffset > maxLvlOffset) {
            xLvlOffset = maxLvlOffset;
        } else if (xLvlOffset < 0) {
            xLvlOffset = 0;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backImage, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        drawCloud(g);
        
        levelsManager.draw(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);
        objectManager.draw(g, xLvlOffset);
        
        if (pause) {
            g.setColor(new Color(0,0,0,100));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        } else if (gameOver) {
            gameOverOverlay.draw(g);
        } else if (lvlComplete) {
            levelCompleteOverlay.draw(g);
        }
    }

    private void drawCloud(Graphics g) {
        for (int i = 0; i < 3; i++) {
            g.drawImage(bigCloud,(int) (0 + i*BIG_CLOUD_WIDTH - xLvlOffset*0.3), (int) (204 * Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
        }
        for (int i = 0; i < smallCloudPos.length; i++) {
            g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 4*i - (int) (xLvlOffset*0.7), smallCloudPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
        }
    }

    public void resetAllPlaying() {
        gameOver = false;
        pause = false;
        lvlComplete = false;
        playerDying = false;
        player.resetAll();
        objectManager.resetAllObjects();
        enemyManager.resetAllEnemies();
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

    public void checkPotionTouched(Rectangle2D.Float hitbox) {
        objectManager.checkObjectTouched(hitbox);
    }
    
    public void checkSpikesTouched(Player player) {
        objectManager.checkSpikesTouched(player);
    }

    public void checkObjectHit(Rectangle2D.Float attackBox) {
        objectManager.checkObjectHit(attackBox);
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                player.setAttacking(true);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!gameOver) {
            if (pause) {
                pauseOverlay.mousePressed(e);
            } else if (lvlComplete) {
                levelCompleteOverlay.mousePressed(e);
            }
        } else {
            gameOverOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (pause) {
            pauseOverlay.mouseReleased(e);
        } else if (lvlComplete) {
            levelCompleteOverlay.mouseReleased(e);
        } else {
            gameOverOverlay.mouseReleased(e);
        }
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameOver) {
            if (pause) {
                pauseOverlay.mouseMoved(e);
            } else if (lvlComplete) {
                levelCompleteOverlay.mouseMoved(e);
            }
        } else {
            gameOverOverlay.mouseMoved(e);
        }
    }
    
    public void mouseDragged(MouseEvent e) {
        if (!gameOver) {
            if (pause) {
                pauseOverlay.mouseDragged(e);
            }
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            gameOverOverlay.keyPressed(e);
        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    break;
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                    case KeyEvent.VK_S:
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_BACK_SPACE:
                    pause = true;
                    break;
                default:
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    break;
                case KeyEvent.VK_A:
                    player.setLeft(false);
                    break;
                case KeyEvent.VK_S:
                    break;
                case KeyEvent.VK_D:
                    player.setRight(false);
                    break;
                    case KeyEvent.VK_SPACE:
                    player.setJump(false);
                    break;
                default:
                    break;
            }
        }
    }
    
    public void unPauseGame() {
        pause = false;
        player.resetDirection();
    }
    
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void setLevelOffset(int lvlOffset) {
        this.maxLvlOffset = lvlOffset;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }
    
    public EnemyManager getEnemyManager() {
        return enemyManager;
    }
    
    public Player getPlayer() {
        return player;
    }

    public ObjectManager getObjectManager() {
        return objectManager;
    }
    
    public LevelsManager getLevelManager() {
        return levelsManager;
    }

    public void setLevelComplete(boolean lvlComplete) {
        this.lvlComplete = lvlComplete;
    }

    public void setPlayerDying(boolean playerDying) {
        this.playerDying = playerDying;
    }
}
