package gameStates;

import static main.Game.GAME_WIDTH;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.MenuButton;
import utils.LoadSave;

public class Menu extends State {

    private MenuButton[] buttons = new MenuButton[3];
    private BufferedImage menu_background, background_menu;
    private int menuHeight, menuWidth, menuX, menuY;

    public Menu(Game game) {
        super(game);
        loadButtons();
        loadBackground();
    }

    private void loadBackground() {
        menu_background = LoadSave.GetSpriteAtLas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int) (menu_background.getWidth() * Game.SCALE);
        menuHeight = (int) (menu_background.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (35 * Game.SCALE);

        background_menu = LoadSave.GetSpriteAtLas(LoadSave.BACKGROUND_MENU);
    }

    private void loadButtons() {
       buttons[0] = new MenuButton(GAME_WIDTH / 2, (int) (150 * Game.SCALE), 0, GameState.PLAYING);
       buttons[1] = new MenuButton(GAME_WIDTH / 2, (int) (220 * Game.SCALE), 1, GameState.OPTIONS);
       buttons[2] = new MenuButton(GAME_WIDTH / 2, (int) (290 * Game.SCALE), 2, GameState.QUIT);
    }
  
    public void update() {
        for (MenuButton mb: buttons) {
            mb.update();
        }
    }

    public void draw(Graphics g) {
        g.drawImage(background_menu, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(menu_background, menuX, menuY, menuWidth, menuHeight, null);
        for (MenuButton mb: buttons) {
            mb.draw(g);
        }
    }
 
    public void mouseClicked(MouseEvent e) {
        
    }
     
    public void mousePressed(MouseEvent e) {
        for (MenuButton mb: buttons) {
            if (isIn(e, mb)) {
                mb.setMousePress(true);
                break;
            }
        }
    }
     
    public void mouseReleased(MouseEvent e) {
        for (MenuButton mb: buttons) {
            if (isIn(e, mb)) {
                if (mb.isMousePress()) {
                    mb.applyGameState();
                }
                if (mb.getState() == GameState.PLAYING) {
                    game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLvlIndex());
                }
                break;
            }
            mb.resetBooleans();
        }
    }

     
    public void mouseMoved(MouseEvent e) {
        for (MenuButton mb: buttons) {
            mb.resetBooleans();
        }

        for (MenuButton mb: buttons) {
            if (isIn(e, mb)) {
                mb.setMouseHover(true);
            }
        }
    }

     
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            GameState.state = GameState.PLAYING;
        }
    }

     
    public void keyReleased(KeyEvent e) {
     
    }

    public void keyTyped(KeyEvent e) {
    
    }

    public boolean isIn(MouseEvent e, MenuButton rect) {
        if (rect.getBounds().contains(e.getX(), e.getY())) {
            return true;
        }
        return false;
    }
}