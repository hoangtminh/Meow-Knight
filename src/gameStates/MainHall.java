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
    private BufferedImage[] mainGif;
    private BufferedImage mainBoard;
    private SelectLevel selectLevel;
    private int aniIndex, aniTick;

    public MainHall(Game game) {
        super(game);
        loadButtons();
        loadBackground();
        selectLevel = new SelectLevel(this.game.getPlaying());
    }

    private void loadBackground() {
        mainBoard = StoreImage.GetSpriteAtLas(StoreImage.MAIN_BOARD);

        BufferedImage tmp = StoreImage.GetSpriteAtLas(StoreImage.MENU_GIF);
        mainGif = new BufferedImage[6];
        for (int i = 0; i < mainGif.length; i++) {
            mainGif[i] = tmp.getSubimage(i * 1575, 0, 1575, 1250);
        }
    }

    private void loadButtons() {
       buttons[0] = new MenuButton((int) (Game.GAME_WIDTH / 2), (int) (290 * Game.SCALE), 0, GameState.MENU);
       buttons[1] = new MenuButton((int) (Game.GAME_WIDTH / 2), (int) (355 * Game.SCALE), 2, GameState.QUIT);
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
        g.drawImage(mainGif[aniIndex], 0, (int) (-120 * Game.SCALE), 
        Game.GAME_WIDTH, 
        (int) (mainGif[0].getHeight() * Game.GAME_WIDTH / mainGif[0].getWidth()) , 
        null);

        g.drawImage(mainBoard, 
        (int) (Game.GAME_WIDTH/2 - mainBoard.getWidth() * Game.SCALE /2), 
        (int) (275 * Game.SCALE), 
        (int) (mainBoard.getWidth() * Game.SCALE), 
        (int) (mainBoard.getHeight() * Game.SCALE),  
        null);

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
