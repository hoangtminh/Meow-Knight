package levels;

import static utils.Constants.UI.Buttons.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import ui.MenuButton;
import utils.StoreImage;

public class Opening {

    private boolean active = false;

    private static float timeDelay = 0.1f;
    private static float textIndex = 0;
    private static int bgIndex = 0;

    // mỗi text 90 từ
    private String text11 = "Một gia tộc mèo sống giữa khung cảnh thanh bình, được bao quanh bởi thiên nhiên tràn đầy sức sống";
    private String text12 = "Những chú mèo nhỏ chạy nhảy tung tăng, trong khi mèo trưởng thành ngồi thư thái bên ánh nắng vàng";
    private String text13 = "Tất cả ánh mắt đều toát lên niềm vui, hòa hợp như một gia đình đoàn kết và tràn đầy yêu thương";
    private String text21 = "Bầu trời đột ngột chuyển sang màu xám, mây đen cuộn trào báo hiệu tai họa. Mấy con chó hung dữ, với";
    private String text22 = "ánh mắt đỏ rực và răng nanh sắc nhọn, lao vào tấn công. Tiếng gầm rú và hỗn loạn vang lên khắp nơi.";
    private String text23 = "Những chiến binh mèo chống trả. Ngọn lửa bùng cháy, xóa nhòa những giấc mơ hạnh phúc của gia tộc mèo.";
    private String text31 = "Giữa đống tàn tích, một chú mèo đứng lên với ánh mắt sắc lạnh và đôi tai dựng thẳng đầy kiên định. ";
    private String text32 = "Nó căm hận nhìn về phía đường chân trời, nơi kẻ thù đã khuất bóng. Chú mèo dường như muốn tuyên thệ:";
    private String text33 = "''*** **! Ta hận các ngươi, ta sẽ tìm đến các ngươi. Món nợ này ta phải đòi cả gốc lẫn lãi!!!!!''";

    private String[][] text = new String[][] { { text11, text12, text13 },
            { text21, text22, text23 },
            { text31, text32, text33 } };
    private int textNum = -1;
    private int textSize = 19;
    private int XTextPos = (int) (Game.GAME_WIDTH / 105 * 10);
    private int YTextPos = (int) (Game.GAME_HEIGHT / 112 * 94);

    private BufferedImage[] background = new BufferedImage[9];

    private MenuButton nextBtn;
    private int buttonXPos = Game.GAME_WIDTH - BUTTON_WIDTH/2;
    private int buttonYPos = Game.GAME_HEIGHT - BUTTON_HEIGHT;

    public Opening() {
        initBackground();
        initButton();
    }

    private void initBackground() {
        background[0] = StoreImage.GetSpriteAtLas(StoreImage.OPENING_BACKGROUND_1);
        background[1] = StoreImage.GetSpriteAtLas(StoreImage.OPENING_BACKGROUND_2);
        background[2] = StoreImage.GetSpriteAtLas(StoreImage.OPENING_BACKGROUND_2);
        background[3] = StoreImage.GetSpriteAtLas(StoreImage.OPENING_BACKGROUND_3);
        background[4] = StoreImage.GetSpriteAtLas(StoreImage.OPENING_BACKGROUND_4);
        background[5] = StoreImage.GetSpriteAtLas(StoreImage.OPENING_BACKGROUND_4);
        background[6] = StoreImage.GetSpriteAtLas(StoreImage.OPENING_BACKGROUND_5);
        background[7] = StoreImage.GetSpriteAtLas(StoreImage.OPENING_BACKGROUND_6);
        background[8] = StoreImage.GetSpriteAtLas(StoreImage.OPENING_BACKGROUND_6);
    }

    private void initButton() {
        nextBtn = new MenuButton(buttonXPos, buttonYPos, 4, null);
    }
    
    public void update() {
        nextBtn.update();
    }

    public void draw(Graphics g) {
        g.drawImage(background[bgIndex], 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        nextBtn.draw(g);
        g.setFont(new Font("Arial", Font.HANGING_BASELINE, textSize));
        g.setColor(Color.BLACK);

        if ((bgIndex + 1) % 3 == 2) {
            if (textIndex <= getLengthText(textNum)) {
                drawSubText(g, textNum, textIndex);
                textIndex += timeDelay;
            } else {
                drawFullText(g, textNum);
                bgIndex++;
            }
        } else if ((bgIndex + 1) % 3 == 0) {
            drawFullText(g, textNum);
        }
    }

    private void drawFullText(Graphics g, int textNum) {
        g.drawString(text[textNum][0], XTextPos, YTextPos);
        g.drawString(text[textNum][1], XTextPos, YTextPos + 2 * textSize);
        g.drawString(text[textNum][2], XTextPos, YTextPos + 4 * textSize);
    }

    private void drawSubText(Graphics g, int textNum, float textIndex) {
        if (textIndex < text[textNum][0].length()) {
            g.drawString(text[textNum][0].substring(0, (int) textIndex), XTextPos, YTextPos);
        } else if (textIndex < (text[textNum][1].length() + text[textNum][0].length())
                && textIndex > text[textNum][0].length()) {
            g.drawString(text[textNum][0], XTextPos, YTextPos);
            g.drawString(text[textNum][1].substring(0, (int) textIndex - text[textNum][0].length()), XTextPos,
                    YTextPos + 2 * textSize);
        } else {
            g.drawString(text[textNum][0], XTextPos, YTextPos);
            g.drawString(text[textNum][1], XTextPos, YTextPos + 2 * textSize);
            g.drawString(
                    text[textNum][2].substring(0,
                            (int) textIndex - text[textNum][0].length() - text[textNum][1].length()),
                    XTextPos,
                    YTextPos + 4 * textSize);
        }

    }

    private int getLengthText(int textNum) {
        return text[textNum][0].length() + text[textNum][1].length() + text[textNum][2].length();
    }

    private void updatebgIndex() {
        bgIndex = bgIndex + 1;
        if ((bgIndex + 1) % 3 == 2) {
            textNum = (textNum + 1) % text.length;
            textIndex = 0;
        }
        if (bgIndex >= background.length) {
            active = false;
            bgIndex = 0;
        }
    }

    public void mouseMoved(MouseEvent e) {
        nextBtn.resetBooleans();
        if (isIn(e, nextBtn)) {
            nextBtn.setMouseHover(true);
        }
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, nextBtn)) {
            nextBtn.setMousePress(true);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, nextBtn)) {
            nextBtn.resetBooleans();
            updatebgIndex();
        }
    }

    public boolean isIn(MouseEvent e, MenuButton b) {
        if (b.getBounds().contains(e.getX(), e.getY())) {
            return true;
        }
        return false;
    }

    public void setActive(boolean active) {
        // this.active = active;
    }

    public boolean isActive() {
        return active;
    }
}
