package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import gameStates.GameState;
import main.GamePanel;

public class MouseInputs implements MouseListener, MouseMotionListener {

    private GamePanel gamePanel;
    public MouseInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        switch (GameState.state) {
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseDragged(e);
                break;
            case OPTIONS:
                gamePanel.getGame().getGameOption().mouseDragged(e);
                break; 
            default:
                break;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch (GameState.state) {
            case MAIN_HALL:
                gamePanel.getGame().getMainHall().mouseMoved(e);
                break;
            case MENU:
                gamePanel.getGame().getMenu().mouseMoved(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseMoved(e);
                break;
            case OPTIONS:
                gamePanel.getGame().getGameOption().mouseMoved(e);
                break; 
            default:
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (GameState.state) {
            case MAIN_HALL:
                break;
            case MENU:
                break;
            case PLAYING:
                break;
            case OPTIONS:
                break;
            default:
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (GameState.state) {
            case MAIN_HALL:
                gamePanel.getGame().getMainHall().mousePressed(e);
                break;
            case MENU:
                gamePanel.getGame().getMenu().mousePressed(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mousePressed(e);
                break;
            case OPTIONS:
                gamePanel.getGame().getGameOption().mousePressed(e);
                break; 
            default:
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (GameState.state) {
            case MAIN_HALL:
                gamePanel.getGame().getMainHall().mouseReleased(e);
                break;
            case MENU:
                gamePanel.getGame().getMenu().mouseReleased(e);
                break;
            case PLAYING:
                gamePanel.getGame().getPlaying().mouseReleased(e);
                break;
            case OPTIONS:
                gamePanel.getGame().getGameOption().mouseReleased(e);
                break; 
            default:
                break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    
}
