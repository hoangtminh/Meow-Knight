package main;

import java.awt.Graphics;

import audio.AudioPlayer;
import gameStates.GameState;
import ui.AudioOptions;
import gameStates.*;

public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_set = 120;
    private final int UPS_set = 200;

    private MainHall mainHall;
    private Playing playing;
    private Menu menu;
    private AudioOptions audioOptions;
    private GameOption gameOption;
    private AudioPlayer audioPlayer;

    public static final int TILES_DEFAULT_SIZE = 32;
    public static final float SCALE = 1.5f;
    public static final int TILES_IN_WIDTH = 26;
    public static final int TILES_IN_HEIGHT = 14;
    public static final int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public static final int GAME_WIDTH = (int) (TILES_IN_WIDTH * TILES_SIZE);
    public static final int GAME_HEIGHT = (int) (TILES_IN_HEIGHT * TILES_SIZE);

    public Game() {
        initClasses();

        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
         
        startGameLoop();
    }

    private void initClasses() {
        mainHall = new MainHall(this);
        audioOptions = new AudioOptions(this);
        audioPlayer = new AudioPlayer();
        gameOption = new GameOption(this);
        playing = new Playing(this);
        menu = new Menu(this);
    }

    public void update() {
        switch (GameState.state) {
            case MAIN_HALL:
                mainHall.update();
                break;
            case MENU:
                menu.update();
                break;
            case PLAYING:
                playing.update();
                break;
            case OPTIONS:
                gameOption.update();
                break;
            default:
                break;
        }
    }

    public void render(Graphics g) {
        switch (GameState.state) {
            case MAIN_HALL:
                mainHall.draw(g);
                break;
            case MENU:
                menu.draw(g);
                break;
            case PLAYING:
                playing.draw(g);
                break;
            case OPTIONS:
                gameOption.draw(g);
                break;
            default:
                break;
        }
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double timePerFrame = 1000000000.0 / FPS_set;
        double timePerUpdate = 1000000000.0 / UPS_set;
        long lastFrame = System.nanoTime();
        long now = System.nanoTime();
        long previusTime = System.nanoTime();
        double deltaU = 0;

        while (true) {
            now = System.nanoTime();
            long currentTime = System.nanoTime();
            deltaU += (currentTime - previusTime) / timePerUpdate;
            previusTime = currentTime;

            if (deltaU >= 1) {
                update();
                deltaU--;
            }
            if (now - lastFrame >= timePerFrame) {
                gamePanel.repaint();
                lastFrame = now;
            }
        }
    }

    public void windowFocusLost() {
        if (GameState.state == GameState.PLAYING) {
            playing.getPlayer().resetDirection();
        }
    }

    public Playing getPlaying() {
        return playing;
    }

    public Menu getMenu() {
        return menu;
    }

    public MainHall getMainHall() {
        return mainHall;
    }

    public AudioOptions getAudioOptions() {
        return audioOptions;
    }

    public GameOption getGameOption() {
        return gameOption;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }
}
