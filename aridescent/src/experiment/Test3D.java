package experiment;

import gameGL.constructs.Text;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;

import java.util.Random;

import static gameGL.util.*;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluLookAt;
import static org.lwjgl.util.glu.GLU.gluPerspective;

public class Test3D extends Thread {
    Random random = new Random();
    float[][][] rcolor = new float[10][10][3];

    Float gluPerspective_fovy = 45f;
    Float gluPerspective_aspect = 800f/600f;
    Float gluPerspective_zNear = 0.1f;
    Float gluPerspective_zFar = 20f;
    Float gluLookAt_eyex = 5f;
    Float gluLookAt_eyey = 1f;
    Float gluLookAt_eyez = -4f;
    Float gluLookAt_centerx = 0f;
    Float gluLookAt_centery = 0f;
    Float gluLookAt_centerz = 0f;
    Float gluLookAt_upx = 0f;
    Float gluLookAt_upy = 1f;
    Float gluLookAt_upz = 0f;
    float angle = 0f;
    private Float lx = 0f;
    private Float lz = -1f;
    private long fps = 0;

    Text text;

    public static void main(String[] args) {
        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create();
            new Test3D();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    public Test3D() {
        System.out.println("Test3D");
        for (int i = 0; i < rcolor.length; i++) {
            for (int j = 0; j < rcolor[0].length; j++) {
                rcolor[i][j][0] = random.nextFloat();
                rcolor[i][j][1] = random.nextFloat();
                rcolor[i][j][2] = random.nextFloat();
            }
        }

        text = new Text("TBD", "Arial", 24, 0, 0, 0, Color.cyan);
        initGL();
        loop();
    }

    public void exit() {
        Display.destroy();
        System.exit(0);
    }

    void initGL() {
        Mouse.setGrabbed(true);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClearDepth(1.0f);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthFunc(GL_LESS);
        glShadeModel(GL_SMOOTH);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    }

    void loop() {
        long tick = 0;
        long now = System.currentTimeMillis();
        long old_tick = 0;

        while (!Display.isCloseRequested()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            if ((System.currentTimeMillis() - now) > 1000) {
                fps = (tick - old_tick);
                old_tick = tick;
                now = System.currentTimeMillis();
            }

            draw();
            poll();

            Display.update();
            Display.sync(100);
            debug("tick=%d", tick);
            tick++;
        }
    }

    void draw() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(gluPerspective_fovy, gluPerspective_aspect, gluPerspective_zNear, gluPerspective_zFar);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        gluLookAt(gluLookAt_eyex, gluLookAt_eyey, gluLookAt_eyez,
                gluLookAt_eyex+lx, gluLookAt_centery, gluLookAt_eyez+lz,
                gluLookAt_upx, gluLookAt_upy, gluLookAt_upz);
        drawCubes(10f, 10f);
        drawOverlay();
    }

    void drawOverlay() {
        glPushMatrix();
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, 800, 600, 0, -1f, 21f);
        text.setText(String.format("FPS: %d XY: (%f, %f)", fps, gluLookAt_eyex, gluLookAt_eyez));
        text.render();
        glPopMatrix();
    }

    void drawCubes(float xtarget, float ztarget) {
        for (float x = 0; x < xtarget; x += 0.5) {
            glTranslatef(0.5f, 0f, -(ztarget*1f)); // 1f = area of cubes
            for (float z = 0; z < ztarget; z += 0.5) {
                glTranslatef(0f, 0f, 0.5f);
                setRandomColor((int)x, (int)z);
                drawCube();
            }
        }
    }

    void setRandomColor(int x, int z) {
        glColor3f(rcolor[x][z][0], rcolor[x][z][1], rcolor[x][z][2]);
    }

    void drawCube() {
        glBegin(GL_QUADS); // FIXME: Replace with GL_TRIANGLES
        glVertex3f(0f, 0f, 0f);
        glVertex3f(0f, 0.5f, 0f);
        glVertex3f(0.5f, 0.5f, 0f);
        glVertex3f(0.5f, 0f, 0f);

        glVertex3f(0f, 0f, 0.5f);
        glVertex3f(0f, 0.5f, 0.5f);
        glVertex3f(0.5f, 0.5f, 0.5f);
        glVertex3f(0.5f, 0f, 0.5f);

        glVertex3f(0f, 0f, 0f);
        glVertex3f(0f, 0f, 0.5f);
        glVertex3f(0.5f, 0f, 0.5f);
        glVertex3f(0.5f, 0f, 0f);

        glVertex3f(0f, 0.5f, 0f);
        glVertex3f(0f, 0.5f, 0.5f);
        glVertex3f(0.5f, 0.5f, 0.5f);
        glVertex3f(0.5f, 0.5f, 0f);

        glVertex3f(0f, 0f, 0f);
        glVertex3f(0f, 0f, 0.5f);
        glVertex3f(0f, 0.5f, 0.5f);
        glVertex3f(0f, 0.5f, 0f);

        glVertex3f(0.5f, 0f, 0f);
        glVertex3f(0.5f, 0f, 0.5f);
        glVertex3f(0.5f, 0.5f, 0.5f);
        glVertex3f(0.5f, 0.5f, 0f);
        glEnd();
    }

    void poll() {
        int eventCtr = 0;
        while (Mouse.next()) {
            int event = Mouse.getEventButton();
            switch(event) {
                case (-1): {
                    float DY = Mouse.getDY();
                    // FIXME: Change to same math as horizontal look
                    if (gluLookAt_centery < 10f && DY > 0) {
                        gluLookAt_centery += DY * 0.005f;
                    } else if (gluLookAt_centery > -10f && DY < 0) {
                        gluLookAt_centery += DY * 0.005f;
                    }

                    float DX = Mouse.getDX();
                    if (DX > 0) {
                        angle += 0.005f*DX;
                        lx = (float)sin(angle);
                        lz = (float)-cos(angle);
                    } else {
                        angle -= 0.005f*-DX;
                        lx = (float)sin(angle);
                        lz = (float)-cos(angle);
                    }

                    debug3("centery=%f, centerx=%f, centerz=%f",
                            gluLookAt_centery, gluLookAt_centerx, gluLookAt_centerz);
                    break;
                }
                default: {
                    /* Click */
                    debug2("clicked mbutton=%d", event);
                    break;
                }
            }
            eventCtr++;
        }

        while (Keyboard.next()) {
            int event = Keyboard.getEventKey();
            if (Keyboard.getEventKeyState()) {
                switch (event) {
                    case (Keyboard.KEY_ESCAPE): {
                        exit();
                        break;
                    }
                    case (Keyboard.KEY_SPACE): {
                        if (Mouse.isGrabbed()) {
                            Mouse.setGrabbed(false);
                        } else {
                            Mouse.setGrabbed(true);
                        }
                        break;
                    }

                }
            }
            eventCtr++;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            gluLookAt_eyex += lx * 0.1f;
            gluLookAt_eyez += lz * 0.1f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            gluLookAt_eyex -= lx * 0.1f;
            gluLookAt_eyez -= lz * 0.1f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            angle += 0.05f;
            lx = (float)sin(angle);
            lz = (float)-cos(angle);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            angle -= 0.05f;
            lx = (float)sin(angle);
            lz = (float)-cos(angle);
        }
        debug("poll() eventCtr=%d", eventCtr);
    }
}
