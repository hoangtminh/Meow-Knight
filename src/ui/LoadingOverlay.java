package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import gameStates.GameState;
import gameStates.Playing;
import main.Game;

import static utils.Constants.ANI_SPEED;
import static utils.Constants.UI.Loading.*;
import utils.StoreImage;

public class LoadingOverlay {

    private Playing playing;
    private BufferedImage[] loadingImgs;
    private int aniTick, aniIndex;
    private GameState nextState;
    
    public LoadingOverlay(Playing playing) {
        this.playing = playing;
        initImgs();
    }
    
    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        g.drawImage(loadingImgs[aniIndex], (int) (Game.GAME_WIDTH/2 - LOADING_WIDTH/2), LOADING_HEIGHT + 150, LOADING_WIDTH, LOADING_HEIGHT, null);
    }

    public void update() {
        updateAnimationTick();
    }

    protected void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniIndex++;
            if (aniIndex >= 14) {
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
                playing.resetAllPlaying();
                playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLvlIndex());
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

        loadingImgs = new BufferedImage[14];
        for (int i = 0; i < loadingImgs.length; i++) {
            loadingImgs[i] = tmp.getSubimage(i * LOADING_WIDTH_DEFAULT, 0, LOADING_WIDTH_DEFAULT, LOADING_HEIGHT_DEFAULT);
        }
    }
    public void setNextState(GameState state) {
        this.nextState = state;
    }
    
}
