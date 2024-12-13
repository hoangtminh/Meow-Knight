package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gameStates.GameState;
import gameStates.Playing;
import main.Game;

import static utils.Constants.UI.Loading.*;
import utils.StoreImage;

public class LoadingOverlay {

    private Playing playing;
    private BufferedImage[] loadingImgs, background;
    private int aniTick, aniIndex, bgIndex;
    private GameState nextState;
    
    public LoadingOverlay(Playing playing) {
        this.playing = playing;
        initImgs();
    }
    
    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        g.drawImage(background[bgIndex], 0, -200, 
        (int) (Game.GAME_WIDTH), 
        (int) (background[bgIndex].getHeight() * Game.GAME_WIDTH / background[bgIndex].getWidth()), null);
        g.drawImage(loadingImgs[aniIndex], (int) (Game.GAME_WIDTH/2 - LOADING_WIDTH/2), (int) (50 * Game.SCALE), LOADING_WIDTH, LOADING_HEIGHT, null);
    }

    public void update() {
        updateAnimationTick();
    }

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= 30) {
            aniIndex++;
            bgIndex++;
            if (bgIndex >= 6) {
                bgIndex = 0;
            }
            if (aniIndex >= 9) {
                aniIndex = 0;
                playing.setLoading(false);
                applyGameState(nextState);
            }
            aniTick = 0;
        }
    }

    private void applyGameState(GameState state) {
        switch (state) {
            case PLAYING:
                if (playing.getlvlComplete()) {
                    playing.loadNextLevel();
                }
                if (playing.getLevelManager().getLvlIndex() == 0 && !playing.getLevelManager().getEnding().isActive()) {
                    playing.getLevelManager().getOpening().setActive(true);
                } else if (!playing.getLevelManager().getEnding().isActive()) {
                    playing.getGame().getAudioPlayer().playEnemiesEffect(AudioPlayer.E_HÚ);
                    playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLvlIndex());
                }
                playing.resetAllPlaying();
                break;
            case MENU:
            default:
                playing.resetAllPlaying();
                playing.setGameState(GameState.MENU);
                break;
        }
    }

    private void initImgs() {
        BufferedImage tmp = StoreImage.GetSpriteAtLas(StoreImage.LOADING);

        loadingImgs = new BufferedImage[9];
        for (int i = 0; i < loadingImgs.length; i++) {
            loadingImgs[i] = tmp.getSubimage(i * LOADING_WIDTH_DEFAULT, 0, LOADING_WIDTH_DEFAULT, LOADING_HEIGHT_DEFAULT);
        }

        tmp = StoreImage.GetSpriteAtLas(StoreImage.LOADING_BACKGROUND);
        background = new BufferedImage[6];
        for (int i = 0; i < background.length; i++) {
            background[i] = tmp.getSubimage(i * 1575, 0, 1575, 1250);
        }
    }
    public void setNextState(GameState state) {
        this.nextState = state;
    }
}
