package gameGL;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;

public class Menu {
    private boolean endMenuFlag = false;
    private boolean exitFlag = false;

    public boolean show() {
        init();
        loop();
        return exitFlag;
    }

    void init() {
        setCamera();
    }

    void loop() {
        while (true) {
            if (exitFlag || endMenuFlag) {
                break;
            } else if (Display.isCloseRequested()) {
                /* Make sure we exit if [X] was pressed */
                exitFlag = true;
                break;
            }
            glClear(GL_COLOR_BUFFER_BIT);

            draw();
            poll();

            Display.update();
            Display.sync(60);
        }
    }

    void draw() {
        glColor3d(1.0, 0, 0);
        drawRectangle(5, 5, 50, 50);
        glColor3d(0, 1, 0);
        glRectf(50, 50, 250, 250);
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

    void poll() {
        if (Mouse.isButtonDown(0)) {
            endMenuFlag = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            exitFlag = true;
        }
    }

    void setCamera(){
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, 640, 480, 0, 0, 1);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }
}
