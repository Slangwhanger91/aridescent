package gameGL;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Renderable;

import static org.lwjgl.opengl.GL11.*;

public class util {
    public static void draw2DOverlay(Renderable[] renderables, float right, float bottom) {
        glPushMatrix();
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, right, bottom, 0, -1f, 1f);
        for (Renderable r: renderables) {
            r.render();
        }
        glPopMatrix();
    }

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
