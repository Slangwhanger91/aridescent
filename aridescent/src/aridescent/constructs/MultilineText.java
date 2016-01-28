package aridescent.constructs;

import org.lwjgl.util.Renderable;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.TrueTypeFont;

public class MultilineText implements Renderable {
    private int xpos, ypos;
    private Font font;
    private String[] text;
    private Color color;
    /*
    public Text(String text, String fontName, int fontSize, int fontFace,
                int xpos, int ypos, Color color) {
     */
    public MultilineText(String text, String fontName,
                         int fontSize, int fontFace, int xpos, int ypos, Color color) {
        java.awt.Font AWTFont = new java.awt.Font(fontName, fontFace, fontSize);
        font = new TrueTypeFont(AWTFont, true);

        this.xpos = xpos;
        this.ypos = ypos;
        this.text = text.split("\n");
        this.color = color;
    }

    @Override
    public void render() {
        int line = 0;
        int offset;
        for (String s: text) {
            offset = line++*font.getLineHeight();
            font.drawString(xpos, ypos+offset, s, color);
        }
    }
}
