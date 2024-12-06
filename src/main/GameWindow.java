package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import utils.StoreImage;

public class GameWindow {
    private JFrame jframe;
    public GameWindow(GamePanel gamePanel) {
        jframe = new JFrame();

        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(gamePanel);
        jframe.setLocationRelativeTo(null);
        jframe.setLocation(0,0);
        jframe.setResizable(false);
        jframe.pack();
        jframe.setVisible(true);
        jframe.setTitle("Meow Knight");
        BufferedImage logo = StoreImage.GetSpriteAtLas(StoreImage.LOGO);
        jframe.setIconImage(logo);

        jframe.addWindowFocusListener(new WindowFocusListener() {

            @Override
            public void windowGainedFocus(WindowEvent e) {

            }
            @Override
            public void windowLostFocus(WindowEvent e) {
                gamePanel.getGame().windowFocusLost();
            }
            
        });
    }
}
