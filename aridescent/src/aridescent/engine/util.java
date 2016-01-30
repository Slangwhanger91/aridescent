package aridescent.engine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Renderable;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;

import static org.lwjgl.opengl.GL11.*;

public class util {

    public static void drawCube(float fromX, float toX,
                                float fromY, float toY,
                                float fromZ, float toZ) {
        glBegin(GL_QUADS); // FIXME: Replace with GL_TRIANGLES
        glVertex3f(fromX, fromY, fromZ);
        glVertex3f(fromX, toY, fromZ);
        glVertex3f(toX, toY, fromZ);
        glVertex3f(toX, fromY, fromZ);

        glVertex3f(fromX, fromY, toZ);
        glVertex3f(fromX, toY, toZ);
        glVertex3f(toX, toY, toZ);
        glVertex3f(toX, fromY, toZ);

        glVertex3f(fromX, fromY, fromZ);
        glVertex3f(fromX, fromY, toZ);
        glVertex3f(toX, fromY, toZ);
        glVertex3f(toX, fromY, fromZ);

        glVertex3f(fromX, toY, fromZ);
        glVertex3f(fromX, toY, toZ);
        glVertex3f(toX, toY, toZ);
        glVertex3f(toX, toY, fromZ);

        glVertex3f(fromX, fromY, fromZ);
        glVertex3f(fromX, fromY, toZ);
        glVertex3f(fromX, toY, toZ);
        glVertex3f(fromX, toY, fromZ);

        glVertex3f(toX, fromY, fromZ);
        glVertex3f(toX, fromY, toZ);
        glVertex3f(toX, toY, toZ);
        glVertex3f(toX, toY, fromZ);
        glEnd();
    }

    public static void drawTexturedCube(Texture tex, float fromX, float toX,
                                        float fromY, float toY,
                                        float fromZ, float toZ) {
        tex.bind();
        glBegin(GL_QUADS); // FIXME: Replace with GL_TRIANGLES
        glNormal3f(0f, 0f, 1f);
        glTexCoord2f(0, 0);
        glVertex3f(fromX, fromY, fromZ);
        glTexCoord2f(0, tex.getHeight());
        glVertex3f(fromX, toY, fromZ);
        glTexCoord2f(tex.getWidth(), tex.getHeight());
        glVertex3f(toX, toY, fromZ);
        glTexCoord2f(tex.getWidth(), 0);
        glVertex3f(toX, fromY, fromZ);

        glNormal3f(0f, 0f, -1f);
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

    public static void debug(String format, Object... objs) {
        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD1)) {
            System.out.printf(format + "\n", objs);
        }
    }

    public static void debug2(String format, Object... objs) {
        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2)) {
            System.out.printf(format + "\n", objs);
        }
    }

    public static void debug3(String format, Object... objs) {
        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD3)) {
            System.out.printf(format + "\n", objs);
        }
    }
}
