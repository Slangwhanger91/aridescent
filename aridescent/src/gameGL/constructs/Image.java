package gameGL.constructs;

import org.lwjgl.util.Renderable;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class Image implements Renderable {
    private Texture texture;
    float x1, x2, y1, y2;

    public Image(String fileName, int xpos, int ypos) {
        x1 = xpos;
        y1 = ypos;
        try {
            texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(fileName));
            System.out.printf("%s: width=%d, height=%d\n", fileName, texture.getImageWidth(), texture.getImageHeight());
        }
        catch(IOException e) { e.printStackTrace(); }

        if (texture != null) {
            x2 = xpos+texture.getImageWidth();
            y2 = ypos+texture.getImageHeight();
        }
    }

    @Override
    public void render() {
        Color.white.bind();
        texture.bind();
        //glBindTexture(GL_TEXTURE_2D, tex.getTextureID());
        drawTexture(texture, x1, y1, x2, y2);
        //glBindTexture(GL_TEXTURE_2D, 0);
        TextureImpl.bindNone();
    }

    void drawTexture(Texture texture, float x1, float y1, float x2, float y2) {
        glBegin(GL_POLYGON);
        glTexCoord2f(0, 0);
        glVertex2f(x1, y1);
        glTexCoord2f(0, texture.getHeight());
        glVertex2f(x1, y2);
        glTexCoord2f(texture.getWidth(), texture.getHeight());
        glVertex2f(x2, y2);
        glTexCoord2f(texture.getWidth(), 0);
        glVertex2f(x2, y1);
        glEnd();
    }

    /* For when position (0,0) is bottom-left
    void drawTexture(Texture tex, float x1, float y1, float x2, float y2) {
        Color.white.bind();
        glBindTexture(GL_TEXTURE_2D, tex.getTextureID());

        glBegin(GL_POLYGON);
        glTexCoord2f(0, tex.getHeight());
        glVertex2d(x1, y1);
        glTexCoord2f(0, 0);
        glVertex2d(x1, y2);
        glTexCoord2f(tex.getWidth(), 0);
        glVertex2d(x2, y2);
        glTexCoord2f(tex.getWidth(), tex.getHeight());
        glVertex2d(x2, y1);
        glEnd();
        glTexCoord2f(0, 0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }
    */
}
