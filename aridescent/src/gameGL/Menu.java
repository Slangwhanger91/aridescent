package gameGL;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.Point;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;


import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class Menu {
    private boolean endMenuFlag = false;
    private boolean exitFlag = false;

    private Rectangle testRectangle = new Rectangle(10, 10, 50, 50);
    MouseOverRectangle morTest = new MouseOverRectangle(50, 50, 100, 100);
    private PollableEvents[] checkList = new PollableEvents[4];
    private Texture testTexture;

    public Menu() {
        try {
            testTexture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("test_image.png"));
            System.out.printf("testTexture: width=%d, height=%d\n", testTexture.getImageWidth(), testTexture.getImageHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean show() {
        init();
        loop();
        return exitFlag;
    }

    void init() {
        glEnable(GL_TEXTURE_2D);
        setCamera();
        checkList[0] = morTest;
    }

    void loop() {
        long now;
        long tick = 0;
        long diff;
        long diff_target = 17;

        glClearColor(0.6f, 0.6f, 0.6f, 1.0f);

        while (true) {
            if (exitFlag || endMenuFlag) {
                break;
            } else if (Display.isCloseRequested()) {
                /* Make sure we exit if [X] was pressed */
                exitFlag = true;
                break;
            }
            now = System.currentTimeMillis();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            draw();
            poll();

            Display.update();
            //Display.sync(60);
            diff = System.currentTimeMillis() - now;
            if (diff < diff_target) {
                try {
                    Thread.sleep(1+(diff_target-diff));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.printf("tick=%d, diff=%d, diff_target=%d\n", tick, diff, diff_target);
            tick++;
        }
    }

    void draw() {
        glColor3d(1.0, 0, 0);
        drawRectangle(5, 5, 50, 50);
        glColor3d(0, 1, 0);
        glRectf(50, 50, 250, 250);
        glColor3d(0, 0, 1);
        drawRectangle(testRectangle);
        glColor3d(0.5, 0.5, 0.5);
        drawRectangle(morTest.area);

        final float posmod = 296;
        drawTexture(testTexture, posmod, posmod, posmod+testTexture.getImageWidth(), posmod+testTexture.getImageHeight());
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
        int movementIncrement = 10;

        if (Mouse.isButtonDown(0)) {
            endMenuFlag = true;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            exitFlag = true;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            if (testRectangle.getY() > 0) {
                testRectangle.translate(0, -movementIncrement);
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            if (testRectangle.getY() < 480-testRectangle.getHeight()) {
                testRectangle.translate(0, movementIncrement);
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            if (testRectangle.getX() < 640 - testRectangle.getWidth()) {
                testRectangle.translate(movementIncrement, 0);
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            if (testRectangle.getX() > 0) {
                testRectangle.translate(-movementIncrement, 0);
            }
        }

        for (PollableEvents pe: checkList) {
            if (pe != null) {
                pe.check();
            }
        }
    }

    void setCamera(){
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, 640, 0, 480, 1, -1);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }
}

interface PollableEvents {
    /* Messy start on an events interface */
    void check();
    void action();
}

class MouseOverRectangle implements PollableEvents {
    /* Messy example of mouseover area class */
    Rectangle area;
    boolean reset = false;

    public MouseOverRectangle(Rectangle rect) {
        this.area = rect;
    }

    public MouseOverRectangle(int xp, int yp, int w, int h) {
        this.area = new Rectangle(xp, yp, w, h);
    }

    public void check() {
        Point position = new Point(Mouse.getX(), Mouse.getY());
        System.out.println(position);
        if (area.contains(position)) {
            action();
        }
    }

    public void action() {
        System.out.println("MouseOverRectangle hit");
    }
}
