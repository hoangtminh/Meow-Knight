package gameStates;

import static main.Game.GAME_WIDTH;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.MenuButton;
import ui.SelectLevel;
import utils.StoreImage;

public class Menu extends State {

    private MenuButton[] buttons = new MenuButton[4];
    private BufferedImage menu_background, background_menu;
    private int menuHeight, menuWidth, menuX, menuY;
    private SelectLevel selectLevel;

    public Menu(Game game) {
        super(game);
        loadButtons();
        loadBackground();
        selectLevel = new SelectLevel(this.game.getPlaying());
    }

    private void loadBackground() {
        menu_background = StoreImage.GetSpriteAtLas(StoreImage.MENU_BACKGROUND);
        menuWidth = (int) (menu_background.getWidth() * Game.SCALE);
        menuHeight = (int) (menu_background.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (35 * Game.SCALE);

        background_menu = StoreImage.GetSpriteAtLas(StoreImage.BACKGROUND_MENU);
    }

    private void loadButtons() {
       buttons[0] = new MenuButton((int) (GAME_WIDTH / 2 - 90 * Game.SCALE), (int) (170 * Game.SCALE), 0, GameState.PLAYING);
       buttons[1] = new MenuButton((int) (GAME_WIDTH / 2 + 90 * Game.SCALE), (int) (170 * Game.SCALE), 1, GameState.OPTIONS);
       buttons[2] = new MenuButton((int) (GAME_WIDTH / 2 + 90 * Game.SCALE), (int) (270 * Game.SCALE), 2, GameState.QUIT);
       buttons[3] = new MenuButton((int) (GAME_WIDTH / 2 - 90 * Game.SCALE), (int) (270 * Game.SCALE), 3, GameState.MENU);
    }

    public void update() {
        if (selectLevel.isActive()) {
            selectLevel.update();
        } else {
            for (MenuButton mb: buttons) {
                mb.update();
            }
        }

    }

    public void draw(Graphics g) {
        g.drawImage(background_menu, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        if (selectLevel.isActive()) {
            selectLevel.draw(g);
        } else {
            g.drawImage(menu_background, menuX, menuY, menuWidth, menuHeight, null);
            for (MenuButton mb: buttons) {
                mb.draw(g);
            }
        }
        
    }
 
    public void mouseClicked(MouseEvent e) {
        
    }
     
    public void mousePressed(MouseEvent e) {
        if (selectLevel.isActive()) {
            selectLevel.mousePressed(e);
        } else {
            for (MenuButton mb: buttons) {
                if (isIn(e, mb)) {
                    mb.setMousePress(true);
                    break;
                }
            }
        }
    }
     
    public void mouseReleased(MouseEvent e) {
        if (selectLevel.isActive()) {
            selectLevel.mouseReleased(e);
        } else {
            for (MenuButton mb: buttons) {
                if (isIn(e, mb)) {
                    if (mb.getState() == GameState.QUIT) {
                        System.exit(1);
                    }
                    if (mb.isMousePress()) {
                        mb.applyGameState();
                    }
                    if (mb.getState() == GameState.PLAYING) {
                        game.getPlaying().setLoading(true);
                        game.getPlaying().getLoadingOverlay().setNextState(GameState.PLAYING);
                    }
                    if (mb.getState() == GameState.MENU) {
                        selectLevel.setActive(true);
                    }
                    break;
                }
                mb.resetBooleans();
            }
        }
    }

     
    public void mouseMoved(MouseEvent e) {
        if (selectLevel.isActive()) {
            selectLevel.mouseMoved(e);
        } else {
            for (MenuButton mb: buttons) {
                mb.resetBooleans();
            }

            for (MenuButton mb: buttons) {
                if (isIn(e, mb)) {
                    mb.setMouseHover(true);
                }
            }
        }
    }

     
    public void keyPressed(KeyEvent e) {
        if (selectLevel.isActive()) {

        } else {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                GameState.state = GameState.PLAYING;
            }
        }
    }

     
    public void keyReleased(KeyEvent e) {
     
    }

    public void keyTyped(KeyEvent e) {
    
    }

    public boolean isIn(MouseEvent e, MenuButton rect) {
        if (selectLevel.isActive()) {
            return false;
        } else {
            if (rect.getBounds().contains(e.getX(), e.getY())) {
                return true;
            }
            return false;
        }
    }
}