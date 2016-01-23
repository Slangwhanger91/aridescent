package gameGL;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.Point;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class Test3D {
    private boolean exitFlag = false;

    float left = -1f;
    float right = 1f;
    float bottom = -1f;
    float top = 1f;
    float zNear = 1f;
    float zFar = 10f;
    float repeat = 0f;

    float x = 1f;
    float y = 0f;
    float z = -4f;

    //private Texture testTexture;
    public static void main(String[] args) {
        try {
            Test3D m3d = new Test3D();
            m3d.show();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    public Test3D() throws LWJGLException {
        Display.setDisplayMode(new DisplayMode(800, 600));
        Display.create();
        exitFlag = false;
    }

    public boolean show() {
        System.out.println("Test3D show()");
        init();
        loop();
        Display.destroy();
        return exitFlag;
    }

    void init() {
        glViewport(0, 0, 800, 600);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glFrustum(-1f, 1f, -1f, 1f, 1f, 10f);
        //glOrtho(0, 640, 0, 480, 1, -1);

        //glMatrixMode(GL_MODELVIEW);
        //glLoadIdentity();
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClearDepth(1.0f);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
        glShadeModel(GL_SMOOTH);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

        //Keyboard.enableRepeatEvents(true);
    }

    void loop() {
        long tick = 0;

        while (true) {
            if (exitFlag) {
                break;
            } else if (Display.isCloseRequested()) {
                /* Make sure we exit if [X] was pressed */
                exitFlag = true;
                break;
            }
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            draw();
            poll();

            Display.update();
            Display.sync(60);
            debug("tick=%d", tick);
            tick++;
        }
    }

    void draw() {
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glTranslatef(x, y, z);

        glBegin(GL_TRIANGLES);
        glColor3f(1f, 0, 0);
        glVertex3d(0f, 1f, 0f);
        glVertex3d(-1f, -1f, 1f);
        glVertex3d(1f, -1f, 1f);

        glColor3f(0f, 1, 0);
        glVertex3d(0f, 1f, 0f);
        glVertex3d(1f, -1f, 1f);
        glVertex3d(1f, -1f, -1f);

        glColor3f(0f, 0, 1);
        glVertex3d(0f, 1f, 0f);
        glVertex3d(1f, -1f, -1f);
        glVertex3d(-1f, -1f, -1f);

        glColor3f(1f, 1, 1);
        glVertex3d(0f, 1f, 0f);
        glVertex3d(-1f, -1f, -1f);
        glVertex3d(-1f, -1f, 1f);
        glEnd();
    }

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
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    void drawRectangle(int x1, int y1, int x2, int y2) {
        /* Equivalent with glRectf(x1,y1,x2,y2) */
        glBegin(GL_POLYGON);
        glVertex2d(x1, y1);
        glVertex2d(x2, y1);
        glVertex2d(x2, y2);
        glVertex2d(x1, y2);
        glEnd();
    }

    void drawRectangle(Rectangle rect) {
        glRectf(rect.getX(), rect.getY(), rect.getX()+rect.getWidth(), rect.getY()+rect.getHeight());
    }

    void poll() {

        int eventCtr = 0;
        while (Keyboard.next()) {
            int event = Keyboard.getEventKey();
            if (Keyboard.getEventKeyState()) {
                switch (event) {
                    case (Keyboard.KEY_ESCAPE): {
                        exitFlag = true;
                        break;
                    }
                    case (Keyboard.KEY_A): {
                        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                            left += 0.1f;
                        } else {
                            left -= 0.1f;
                        }
                        break;
                    }
                    case (Keyboard.KEY_W): {
                        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                            top += 0.1f;
                        } else {
                            top -= 0.1f;
                        }
                        break;
                    }
                    case (Keyboard.KEY_S): {
                        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                            bottom += 0.1f;
                        } else {
                            bottom -= 0.1f;
                        }
                        break;
                    }
                    case (Keyboard.KEY_D): {
                        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                            right += 0.1f;
                        } else {
                            right -= 0.1f;
                        }
                        break;
                    }
                    case (Keyboard.KEY_Z): {
                        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                            zNear += 0.1f;
                        } else {
                            zNear -= 0.1f;
                        }
                        break;
                    }
                    case (Keyboard.KEY_F): {
                        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                            zFar += 0.1f;
                        } else {
                            zFar -= 0.1f;
                        }
                        break;
                    }
                    case (Keyboard.KEY_R): {
                        left = -1f;
                        right = 1f;
                        bottom = -1f;
                        top = 1f;
                        zNear = 1f;
                        zFar = 10f;
                        break;
                    }
                    case (Keyboard.KEY_U): {
                        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                            x += 0.1f;
                        } else {
                            x -= 0.1f;
                        }
                        break;
                    }
                    case (Keyboard.KEY_I): {
                        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                            y += 0.1f;
                        } else {
                            y -= 0.1f;
                        }
                        break;
                    }
                    case (Keyboard.KEY_O): {
                        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                            z += 0.1f;
                        } else {
                            z -= 0.1f;
                        }
                        break;
                    }
                    case (Keyboard.KEY_T): {
                        System.out.printf("left=%f, right=%f, bottom=%f, top=%f, zNear=%f, zFar=%f\n",
                                left, right, bottom, top, zNear, zFar);
                        System.out.printf("repeat=%f\n", repeat);
                        System.out.printf("x=%f, y=%f, z=%f\n", x, y, z);
                        break;
                    }
                    case (Keyboard.KEY_L): {
                        glMatrixMode(GL_MODELVIEW);
                        //glLoadIdentity();
                        //glTranslatef(0f, 0f, 0f);
                        glRotatef(0f+repeat, 0f, 0f, 1f);
                        //glTranslatef(-1.0f, 0.0f, -4.0f);
                        repeat += 1f;
                        debug2("repeat=%f", repeat);
                        break;
                    }
                }
            }
            eventCtr++;
        }
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glFrustum(left, right, bottom, top, zNear, zFar);
        debug("poll() eventCtr=%d", eventCtr);
    }

    static void debug(String format, Object... objs) {
        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD1)) {
            System.out.printf(format + "\n", objs);
        }
    }

    static void debug2(String format, Object... objs) {
        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2)) {
            System.out.printf(format + "\n", objs);
        }
    }

    static void debug3(String format, Object... objs) {
        if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD3)) {
            System.out.printf(format + "\n", objs);
        }
    }
}
