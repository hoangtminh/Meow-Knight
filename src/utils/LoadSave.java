package utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

public class LoadSave {

    // public static final String PLAYER_ATLAS = "player_sprites.png";
    public static final String PLAYER_ATLAS = "Meow_Knight.png";
    public static final String LEVEL_ATLAS = "outside_sprites.png";
    public static final String LEVEL_ONE_DATA = "level_one_data_long.png";
    public static final String MENU_BUTTONS = "button_atlas.png";
    public static final String MENU_BACKGROUND = "menu_background.png";
    public static final String BACKGROUND_MENu = "background_menu.png";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
    public static final String SOUND_BUTTON = "sound_button.png";
    public static final String URM_BUTTONS = "urm_buttons.png";
    public static final String VOLUME_BUTTONS = "volume_buttons.png";
    public static final String PLAYING_BG_IMG = "playing_bg_img.png";
    public static final String BIG_CLOUD = "big_clouds.png";
    public static final String SMALL_CLOUD = "small_clouds.png";
    public static final String CRABBY_SPRITE = "crabby_sprite.png";
    public static final String STATUS_BAR = "health_power_bar.png";
    public static final String LEVEL_COMPLETE = "completed_sprite.png";
    public static final String POTIONS_SPRITE = "potions_sprites.png";
    public static final String OBJECT_SPRITE = "objects_sprites.png";
    public static final String TRAP_SPRITE = "trap_atlas.png";
    public static final String CANNON_SPRITE = "cannon_atlas.png";
    public static final String CANNON_BALL = "ball.png";

    public static BufferedImage GetSpriteAtLas(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
        
        try {

            img = ImageIO.read(is);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }    

        return img;
    }    

    public static BufferedImage[] getAllLevels() {
        BufferedImage[] imgs;

        URL url = LoadSave.class.getResource("/lvls");
        File file = null;

        try {
            file = new File(url.toURI());
        } catch (Exception e) {
            e.printStackTrace();;
        }

        File[] files = file.listFiles();
        File[] filesSorted = new File[files.length];

        for (int i = 0; i < filesSorted.length; i++) {
            for (int j = 0; j < files.length; j++) {
                if (files[j].getName().equals((i+1) + ".png"));
                    filesSorted[i] = files[j];
            }
        }

        imgs = new BufferedImage[filesSorted.length];
        for (int i = 0; i < filesSorted.length; i++) {
            try {
                imgs[i] = ImageIO.read(files[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return imgs;
    }
}
