package gameGL.constructs;

import org.lwjgl.util.Renderable;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureImpl;

public class Text implements Renderable {
    int xpos, ypos;
    Font font;
    String text;
    Color color;

    public Text(String text, String fontName, int fontSize, int fontFace,
                int xpos, int ypos, Color color) {
        java.awt.Font AWTFont = new java.awt.Font(fontName, fontFace, fontSize);
        font = new TrueTypeFont(AWTFont, true);

        this.xpos = xpos;
        this.ypos = ypos;
        this.color = color;
        this.text = text;
    }

    public Text(String text, String fontName, int fontSize, int fontFace,
                int xpos, int ypos) {
        this(text, fontName, fontSize, fontFace, xpos, ypos, Color.black);
    }

    public Text(String text, int fontSize, int fontFace,
                int xpos, int ypos) {
        this(text, "Arial", fontSize, fontFace, xpos, ypos, Color.black);
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void render() {
        font.drawString(xpos, ypos, text, color);
        TextureImpl.bindNone();
    }
}
