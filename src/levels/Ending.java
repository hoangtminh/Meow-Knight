package levels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gameStates.GameState;
import main.Game;
import ui.MenuButton;
import static utils.Constants.UI.Buttons.*;
import utils.StoreImage;

public class Ending {

    private boolean active = false;

    private static float timeDelay = 0.6f;
    private static float textIndex = 0;
    private static int bgIndex = 0;

    // mỗi text 90 từ
    private String text11 = "Sau khi chinh phục tộc chó thành công, Meow Knight trở về ngôi làng cũ của mình.";
    private String text12 = "Những ngôi nhà đổ nát, khói bụi bao trùm, cây cối và hoa cỏ héo úa. Không gian giờ đây chỉ";
    private String text13 = "còn lại những dấu vết của chiến tranh, với bầu không khí lạnh lẽo và đau thương.";
    private String text21 = "Bằng sức mạnh và niềm kiêu hãnh của mèo, Meow đã đập những con t.ró không trượt phát nào.";
    private String text22 = "Những thành viên của tộc chó bại trận bị bắt giữ và buộc lao động không lương để đền tội.";
    private String text23 = "Những con tró bị bóc lột không thương tiếc, lao động ngày và đêm để nhanh chóng xây sửa lại ngôi làng.";
    private String text31 = "Thời gian trôi qua, ngôi làng đã được khôi phục hoàn toàn, thậm chí còn giàu hơn trước. Những ngôi nhà";
    private String text32 = "được xây mới, khu vườn lại rực rỡ màu vàng, thoảng hương mùi tiền, và tiếng cười của tộc mèo vang vọng.";
    private String text33 = "Mặc dù đã bị tàn phá ác liệt, nhưng tộc mèo giờ đây sống trong hạnh phúc với mấy con tró bị bóc lột.";

    private String[][] text = new String[][] { { text11, text12, text13 },
            { text21, text22, text23 },
            { text31, text32, text33 } };
    private int textNum = -1;
    private int textSize = 19;
    private int XTextPos = (int) (Game.GAME_WIDTH / 105 * 10);
    private int YTextPos = (int) (Game.GAME_HEIGHT / 112 * 94);

    private BufferedImage[] background = new BufferedImage[9];

    private String textCredit1 = "Học phần lập trình hướng đối tượng";
    private String textCredit2 = "Thực hiện bởi nhóm 18";
    private String textCredit3 = "Dự án này sẽ không thể hoàn thiện nếu thiếu sự hỗ trợ từ những con người tuyệt vời sau đây:";
    private String textCredit4 = "Hoàng Tuấn Minh - 20235376";
    private String textCredit5 = "Khương Việt Anh - 20235257";
    private String textCredit6 = "Lê Yến Nhi - 20235183";
    private String textCredit7 = "                                       -----------------------------------------";
    private String textCredit8 = "Nhà phát triển chính: Nhóm 18";
    private String textCredit9 = "Ý tưởng / kịch bản: Hoàng Tuấn Minh - Lê Yến Nhi";
    private String textCredit10 = "Thiết kế đồ họa: Hoàng Tuấn Minh - Khương Việt Anh";
    private String textCredit11 = "Lập trình: Hoàng Tuấn Minh - Khương Việt Anh - Lê Yến Nhi";
    private String textCredit12 = "Âm thanh: Hoàng Tuấn Minh";
    private String textCredit13 = "Biên kịch/Thiết kế cốt truyện: Khương Việt Anh";
    private String textCredit14 = "Các nguồn tham khảo:";
    private String textCredit15 = "Đồ họa: itch.io, tự thiết kế với Paint, Asesprite";
    private String textCredit16 = "Âm thanh: Minecraft, Youtube";
    private String textCredit17 = "Lập trình: Chat GPT, Youtube";
    private String textCredit18 = "Map: Tự thiết kế với Paint";
    private String textCredit19 = "                                       ----------------------------------------";
    private String textCredit20 = "Cảm ơn các bạn đã chơi game của chúng tôi";
    private String textCredit21 = "Cảm ơn giảng viên Trần Nhật Hóa đã hỗ trợ về mặt kiến thức";
    private String textCredit22 = "Cảm ơn gia đình, bạn bè, và những người đã hỗ trợ tinh thần.";
    private String textCredit23 = "Chúng tôi hy vọng bạn sẽ tận hưởng trò chơi và ủng hộ các sản phẩm tiếp theo từ chúng tôi!";

    private String[] textCredit = { textCredit1, textCredit2, textCredit3, textCredit4, textCredit5, textCredit6,
            textCredit7, textCredit8, textCredit9, textCredit10, textCredit11, textCredit12, textCredit13, textCredit14,
            textCredit15, textCredit16, textCredit17, textCredit18,textCredit19,textCredit20,textCredit21,textCredit22, textCredit23 };
    private float speed = 0.3f;
    private int textSizeCredit = 30;
    private int opacity = 0;
    private int xPos;
    private float yPos = Game.GAME_HEIGHT + 50;
    private boolean creditActive = false;

    private MenuButton nextBtn;
    private int buttonXPos = Game.GAME_WIDTH - BUTTON_WIDTH/2;
    private int buttonYPos = Game.GAME_HEIGHT - BUTTON_HEIGHT;

    public Ending() {
        initBackground();
        initButton();
    }

    private void initBackground() {
        background[0] = StoreImage.GetSpriteAtLas(StoreImage.ENDING_BACKGROUND_1);
        background[1] = StoreImage.GetSpriteAtLas(StoreImage.ENDING_BACKGROUND_2);
        background[2] = StoreImage.GetSpriteAtLas(StoreImage.ENDING_BACKGROUND_2);
        background[3] = StoreImage.GetSpriteAtLas(StoreImage.ENDING_BACKGROUND_3);
        background[4] = StoreImage.GetSpriteAtLas(StoreImage.ENDING_BACKGROUND_4);
        background[5] = StoreImage.GetSpriteAtLas(StoreImage.ENDING_BACKGROUND_4);
        background[6] = StoreImage.GetSpriteAtLas(StoreImage.ENDING_BACKGROUND_5);
        background[7] = StoreImage.GetSpriteAtLas(StoreImage.ENDING_BACKGROUND_6);
        background[8] = StoreImage.GetSpriteAtLas(StoreImage.ENDING_BACKGROUND_6);
    }

    private void initButton() {
        nextBtn = new MenuButton(buttonXPos, buttonYPos, 4, null);
    }
    
    public void update() {
        nextBtn.update();
        updatePosCredit();
    }

    public void draw(Graphics g) {
        if (!creditActive) {
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
        } else {
            g.drawImage(background[8], 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
            g.setFont(new Font("Arial", Font.HANGING_BASELINE, textSize));
            g.setColor(Color.BLACK);
            drawFullText(g, textNum);
            drawCredit(g, opacity);
            nextBtn.draw(g);
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
        if (bgIndex == 9) {
            creditActive = true;
        }
        if (bgIndex >= background.length + 1) {
            active = false;
            bgIndex = 0;
            textNum = -1;
            textIndex = 0;
            GameState.state = GameState.MAIN_HALL;
        }
    }

    private void drawCredit(Graphics g, int opacity) {
        // background
        g.setColor(new Color(0, 0, 0, opacity / 3));
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        // text
        g.setFont(new Font("Arial", Font.BOLD, textSizeCredit));
        g.setColor(Color.white);
        for (int i = 0; i < textCredit.length; i++) {
            xPos = (int) (Game.GAME_WIDTH / 2 - textCredit[i].length() / 4 * textSizeCredit);
            g.drawString(textCredit[i], xPos, (int) (yPos + i * textSizeCredit * 3 / 2));
        }
    }

    private void updatePosCredit() {
        if (creditActive) {
            yPos -= speed;
            if (opacity < 200 * 4) {
                opacity++;
            }
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
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
}
