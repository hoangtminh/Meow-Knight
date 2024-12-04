package gameStates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utils.Constants.ANI_SPEED;

import main.Game;
import ui.MenuButton;
import ui.SelectLevel;
import utils.StoreImage;

public class MainHall extends State{

    private MenuButton[] buttons = new MenuButton[2];
    private BufferedImage[] menuGif;
    private SelectLevel selectLevel;
    private int aniIndex, aniTick;

    public MainHall(Game game) {
        super(game);
        loadButtons();
        loadBackground();
        selectLevel = new SelectLevel(this.game.getPlaying());
    }

    private void loadBackground() {
        BufferedImage tmp = StoreImage.GetSpriteAtLas(StoreImage.MENU_GIF);
        menuGif = new BufferedImage[6];
        for (int i = 0; i < menuGif.length; i++) {
            menuGif[i] = tmp.getSubimage(i * 1575, 0, 1575, 1250);
        }
    }

    private void loadButtons() {
       buttons[0] = new MenuButton((int) (Game.GAME_WIDTH / 2), (int) (270 * Game.SCALE), 0, GameState.MENU);
       buttons[1] = new MenuButton((int) (Game.GAME_WIDTH / 2), (int) (330 * Game.SCALE), 2, GameState.QUIT);
    }

    public void update() {
        for (MenuButton mb: buttons) {
            mb.update();
        }
        updateAnimationTick();
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniIndex++;
            if (aniIndex >= 6) {
                aniIndex = 0;
            }
            aniTick = 0;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(menuGif[aniIndex], 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
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
                if (mb.getState() == GameState.QUIT) {
                    System.exit(1);
                }
                if (mb.isMousePress()) {
                    mb.applyGameState();
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
            GameState.state = GameState.MENU;
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
