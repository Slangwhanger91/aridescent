package experiment;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static gameGL.util.debug;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluLookAt;
import static org.lwjgl.util.glu.GLU.gluPerspective;

public class Clean3D extends Thread {
    final float WIDTH = 800;
    final float HEIGHT = 600;

    float gluPerspective_fovy = 45f;
    float gluPerspective_aspect = WIDTH/HEIGHT;
    float gluPerspective_zNear = 1f;
    float gluPerspective_zFar = 21f;
    float glRotatef_angle = 0f;
    float glRotatef_x = 0f;
    float glRotatef_y = 1f;
    float glRotatef_z = 0f;
    float glTranslatef_x = 0f;
    float glTranslatef_y = 0f;
    float glTranslatef_z = 0f;
    float gluLookAt_eyex = 0f;
    float gluLookAt_eyey = 5f;
    float gluLookAt_eyez = 0f;
    float gluLookAt_centerx = 0f;
    float gluLookAt_centery = 0f;
    float gluLookAt_centerz = 0f;
    float gluLookAt_upx = 1f;
    float gluLookAt_upy = 0f;
    float gluLookAt_upz = 0f;

    public static void main(String[] args) {
        new Clean3D().start();
    }

    @Override
    public synchronized void run() {
        try {
            Display.setDisplayMode(
                    new DisplayMode((int) WIDTH,
                            (int) HEIGHT));
            Display.create();
            init();
            loop();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    void loop() {
        long tick = 0;
        while (!Display.isCloseRequested()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            render();
            poll();

            Display.update();
            Display.sync(30);
            debug("tick=%d", tick++);
        }
    }

    void init() {
        glClearColor(0f, 0f, 0f, 1f);
        glClearDepth(1f);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
    }

    void render() {
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        //glTranslatef(2f, 0f, 5f);

        glBegin(GL_TRIANGLES);
        /* BOTTOM */
        glColor3f(1f, 1f, 1f);
        glVertex3f(1f, 0f, 1f);
        glVertex3f(1f, 0f, -1f);
        glVertex3f(-1f, 0f, -1f);

        glVertex3f(1f, 0f, 1f);
        glVertex3f(-1f, 0f, -1f);
        glVertex3f(-1f, 0f, 1f);

        /* TOP */
        glColor3f(0.9f, 0.9f, 0.9f);
        glVertex3f(1f, 1f, 1f);
        glVertex3f(1f, 1f, -1f);
        glVertex3f(-1f, 1f, -1f);

        glVertex3f(1f, 1f, 1f);
        glVertex3f(-1f, 1f, -1f);
        glVertex3f(-1f, 1f, 1f);
        glEnd();

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(45f, gluPerspective_aspect, 0.1f, 100f);
        gluLookAt(0f, 3f, -5f, 0f, 0f, 0f, 0f, 1f, 0f);
    }

    void drawQuads() {
        // BOTTOM PLANE
        glBegin(GL_QUADS);
        glColor3f(1f, 1f, 1f);
        glVertex3f(0.5f, 0f, 0.5f);
        glVertex3f(-0.5f, 0f, 0.5f);
        glVertex3f(-0.5f, 0f, -0.5f);
        glVertex3f(0.5f, 0f, -0.5f);

        // TOP PLANE
        glColor3f(1f, 1f, 1f);
        glVertex3f(0.5f, 1f, 0.5f);
        glVertex3f(-0.5f, 1f, 0.5f);
        glVertex3f(-0.5f, 1f, -0.5f);
        glVertex3f(0.5f, 1f, -0.5f);
        glEnd();
    }

    void poll() {
        while (Mouse.next()) {
            int event = Mouse.getEventButton();
            switch(event) {
                case (-1): {
                    break;
                }
                default: {
                    break;
                }
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Display.destroy();
            System.exit(0);
        }
    }
}

