package aridescent.engine;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;

import static org.lwjgl.opengl.GL11.*;

public class util {
    public static void drawTexturedCube(Texture tex, float fromX, float toX,
                                        float fromY, float toY,
                                        float fromZ, float toZ) {
        tex.bind();
        glBegin(GL_QUADS);
        glNormal3f(0f, 0f, -1f);
        glTexCoord2f(0, 0);
        glVertex3f(fromX, fromY, fromZ);
        glTexCoord2f(0, tex.getHeight());
        glVertex3f(fromX, toY, fromZ);
        glTexCoord2f(tex.getWidth(), tex.getHeight());
        glVertex3f(toX, toY, fromZ);
        glTexCoord2f(tex.getWidth(), 0);
        glVertex3f(toX, fromY, fromZ);

        glNormal3f(0f, 0f, 1f);
        glTexCoord2f(0, 0);
        glVertex3f(fromX, fromY, toZ);
        glTexCoord2f(0, tex.getHeight());
        glVertex3f(fromX, toY, toZ);
        glTexCoord2f(tex.getWidth(), tex.getHeight());
        glVertex3f(toX, toY, toZ);
        glTexCoord2f(tex.getWidth(), 0);
        glVertex3f(toX, fromY, toZ);

        glNormal3f(0f, -1f, 0f);
        glTexCoord2f(0, 0);
        glVertex3f(fromX, fromY, fromZ);
        glTexCoord2f(0, tex.getHeight());
        glVertex3f(fromX, fromY, toZ);
        glTexCoord2f(tex.getWidth(), tex.getHeight());
        glVertex3f(toX, fromY, toZ);
        glTexCoord2f(tex.getWidth(), 0);
        glVertex3f(toX, fromY, fromZ);

        glNormal3f(0f, 1f, 0f);
        glTexCoord2f(0, 0);
        glVertex3f(fromX, toY, fromZ);
        glTexCoord2f(0, tex.getHeight());
        glVertex3f(fromX, toY, toZ);
        glTexCoord2f(tex.getWidth(), tex.getHeight());
        glVertex3f(toX, toY, toZ);
        glTexCoord2f(tex.getWidth(), 0);
        glVertex3f(toX, toY, fromZ);

        glNormal3f(-1f, 0f, 0f);
        glTexCoord2f(0, 0);
        glVertex3f(fromX, fromY, fromZ);
        glTexCoord2f(0, tex.getHeight());
        glVertex3f(fromX, fromY, toZ);
        glTexCoord2f(tex.getWidth(), tex.getHeight());
        glVertex3f(fromX, toY, toZ);
        glTexCoord2f(tex.getWidth(), 0);
        glVertex3f(fromX, toY, fromZ);

        glNormal3f(1f, 0f, 0f);
        glTexCoord2f(0, 0);
        glVertex3f(toX, fromY, fromZ);
        glTexCoord2f(0, tex.getHeight());
        glVertex3f(toX, fromY, toZ);
        glTexCoord2f(tex.getWidth(), tex.getHeight());
        glVertex3f(toX, toY, toZ);
        glTexCoord2f(tex.getWidth(), 0);
        glVertex3f(toX, toY, fromZ);
        glEnd();
        TextureImpl.unbind();
    }

    public static void debug(int level, String format, Object... objs) {
        String out = String.format("DEBUG: " + format + "\n", objs);
        switch(level) {
            case 1:
                if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD1)) {
                    System.out.print(out);
                }
                break;
            case 2:
                if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2)) {
                    System.out.print(out);
                }
                break;
            case 3:
                if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD3)) {
                    System.out.print(out);
                }
                break;
            default:
                System.out.print(out);
                break;
        }
    }

    public static void debug(String format, Object... objs) {
        debug(0, format, objs);
    }
}
