package gameStates;

import main.Game;
import ui.MenuButton;

import java.awt.event.MouseEvent;

import audio.AudioPlayer;

public class State {
    protected Game game;
    
    public State(Game game) {
        this.game = game;
    }

    public boolean isIn(MouseEvent e, MenuButton rect) {
        if (rect.getBounds().contains(e.getX(), e.getY())) {
            return true;
        }
        return false;
    }

    public Game getGame() {
        return game;
    }

    public void setGameState(GameState state) {
        switch (state) {
            case MENU:
                game.getAudioPlayer().playSong(AudioPlayer.MENU);
                break;
            case PLAYING:
                game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLvlIndex());
            default:
                break;
        }
        GameState.state = state;
    }
}
