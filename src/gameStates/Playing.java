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
import ui.LoadingOverlay;
import ui.PauseOverlay;
import utils.StoreImage;
import static utils.Constants.UI.Environment.*;

import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import audio.AudioPlayer;

import java.awt.event.KeyEvent;

public class Playing extends State {

    private Player player;
    
    private LevelsManager levelsManager;
    private EnemyManager enemyManager;
    private ObjectManager objectManager;
    
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompleteOverlay levelCompleteOverlay;
    private LoadingOverlay loadingOverlay;

    private boolean pause = false;
    private boolean gameOver = false;
    private boolean lvlComplete = false;
    private boolean playerDying = false;
    private boolean loading = false;

    private int xLvlOffset;
    private int leftBorderOffset = (int) (0.35 * Game.GAME_WIDTH);
    private int rightBorderOffset = (int) (0.65 * Game.GAME_WIDTH);
    private int maxLvlOffset;

    private BufferedImage backImage, smallCloud;
    private int[] smallCloudPos;
    private Random rand = new Random();
    
    public Playing(Game game) {
        super(game);
        initClasses();
        
        backImage = StoreImage.GetSpriteAtLas(StoreImage.PLAYING_BG_IMG);
        smallCloud = StoreImage.GetSpriteAtLas(StoreImage.SMALL_CLOUD);
        smallCloudPos = new int[10];
        for (int i = 0; i < smallCloudPos.length; i++) {
            smallCloudPos[i] = (int) (90*Game.SCALE) + rand.nextInt((int) (100* Game.SCALE));
        } 
        
        loadStartLevel();
        calculateLevelOffset();
    }

    public void loadNextLevel() {
        levelsManager.nextLevel();
        player.setSpawn(levelsManager.getCurrLevels().getPlayerSpawn());
        resetAllPlaying();
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
        loadingOverlay = new LoadingOverlay(this);
    }
    
    public void windowFocusLost() {
        player.resetDirection();
    }

    public void update() {
        if (pause  && !loading) {
            pauseOverlay.update();
        } else if (lvlComplete && !loading) {
            levelCompleteOverlay.update();
        } else if (gameOver && !loading) {
            gameOverOverlay.update();
        } else if (playerDying) {
            player.update();
        } else if (!gameOver && !loading) {
            levelsManager.update();
            if (!levelsManager.getOpening().isActive() || !levelsManager.getEnding().isActive()) {
                enemyManager.update(levelsManager.getCurrLevels().getLevelData(), player);
                objectManager.update(levelsManager.getCurrLevels().getLevelData(), player);
                player.update();
                checkCloseToBorder();
            }
        } else if (loading) {
            loadingOverlay.update();
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

    public void draw(Graphics g) {
        g.drawImage(backImage, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        drawCloud(g);
        
        if (levelsManager.getOpening().isActive() || levelsManager.getEnding().isActive()) {
            levelsManager.draw(g, xLvlOffset);
        } else if (player.getCurrentHealth() > 0) {
            enemyManager.draw(g, xLvlOffset);
            levelsManager.draw(g, xLvlOffset);
            player.render(g, xLvlOffset);
            objectManager.draw(g, xLvlOffset);
        } else {
            enemyManager.draw(g, xLvlOffset);
            player.render(g, xLvlOffset);
            levelsManager.draw(g, xLvlOffset);
            objectManager.draw(g, xLvlOffset);
        }
        if (pause && !loading) {
            g.setColor(new Color(0,0,0,100));
            g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        } else if (gameOver && !loading) {
            gameOverOverlay.draw(g);
        } else if (lvlComplete && !loading) {
            levelCompleteOverlay.draw(g);
        } else if (loading) {
            loadingOverlay.draw(g);
        }
    }

    private void drawCloud(Graphics g) {
        for (int i = 0; i < smallCloudPos.length; i++) {
            g.drawImage(smallCloud, SMALL_CLOUD_WIDTH * 4*i - (int) (xLvlOffset*0.7), smallCloudPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
        }
    }

    public void resetAllPlaying() {
        gameOver = false;
        pause = false;
        lvlComplete = false;
        playerDying = false;
        loading = false;
        player.resetAll();
        objectManager.resetAllObjects();
        enemyManager.resetAllEnemies();
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) {
        enemyManager.checkEnemyHit(attackBox);
    }

    public void checkPotionTouched(Player player) {
        objectManager.checkObjectTouched(player);
    }

    public void checkCoinTouched(Player player) {
        objectManager.checkCoinTouched(player);
    }
    
    public void checkSpikesTouched(Player player) {
        objectManager.checkSpikesTouched(player);
    }

    public void checkObjectHit(Rectangle2D.Float attackBox) {
        objectManager.checkObjectHit(attackBox);
    }
    
    public void mouseClicked(MouseEvent e) {
        if (!gameOver) {
            
        }
    }

    public void mousePressed(MouseEvent e) {
        if (!gameOver) {
            if (pause) {
                pauseOverlay.mousePressed(e);
            } else if (lvlComplete) {
                levelCompleteOverlay.mousePressed(e);
            } else if (levelsManager.getOpening().isActive()) {
                levelsManager.getOpening().mousePressed(e);
            } else if (levelsManager.getEnding().isActive()) {
                levelsManager.getEnding().mousePressed(e);
            }
        } else {
            gameOverOverlay.mousePressed(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (pause) {
            pauseOverlay.mouseReleased(e);
        } else if (lvlComplete) {
            levelCompleteOverlay.mouseReleased(e);
        } else if (gameOver) {
            gameOverOverlay.mouseReleased(e);
        } else if (levelsManager.getOpening().isActive()) {
            levelsManager.getOpening().mouseReleased(e);
        } else if (levelsManager.getEnding().isActive()) {
            levelsManager.getEnding().mouseReleased(e);
        }
    }
    
    public void mouseMoved(MouseEvent e) {
        if (!gameOver) {
            if (pause) {
                pauseOverlay.mouseMoved(e);
            } else if (lvlComplete) {
                levelCompleteOverlay.mouseMoved(e);
            } else if (levelsManager.getOpening().isActive()) {
                levelsManager.getOpening().mouseMoved(e);
            } else if (levelsManager.getEnding().isActive()) {
                levelsManager.getEnding().mouseMoved(e);
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
    
    public void keyPressed(KeyEvent e) {
        if (gameOver) {
            gameOverOverlay.keyPressed(e);
        } else if (!levelsManager.getOpening().isActive() || !levelsManager.getEnding().isActive()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    break;
                case KeyEvent.VK_A:
                    player.setLeft(true);
                    break;
                case KeyEvent.VK_S:
                    player.dodging();
                    break;
                case KeyEvent.VK_D:
                    player.setRight(true);
                    break;
                case KeyEvent.VK_SPACE:
                    break;
                case KeyEvent.VK_J:
                    player.setAttacking(true);
                    break;
                case KeyEvent.VK_K:
                    player.setJump(true);
                    break;
                case KeyEvent.VK_U:
                    player.powerAttack();
                    break;
                case KeyEvent.VK_BACK_SPACE:
                    pause = true;
                    break;
                default:
                break;
            }
        }
    }

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
                    // player.setJump(false);
                    break;
                case KeyEvent.VK_K:
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

    public LoadingOverlay getLoadingOverlay() {
        return loadingOverlay;
    }

    public boolean getlvlComplete() {
        return lvlComplete;
    }

    public void setLevelComplete(boolean lvlComplete) {
        this.lvlComplete = lvlComplete;
        if (lvlComplete) {
            game.getAudioPlayer().stopSong();
            game.getAudioPlayer().playEffect(AudioPlayer.LVL_COMPLETED);
            levelsManager.updateCoinNum(levelsManager.getLvlIndex());
        }
    }

    public void setPlayerDying(boolean playerDying) {
        this.playerDying = playerDying;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }
}
