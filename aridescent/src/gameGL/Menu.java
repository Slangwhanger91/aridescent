package gameGL;

import gameGL.constructs.Rectangle;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.Renderable;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class Menu {
    final static int WIDTH = 640;
    final static int HEIGHT = 480;
    boolean unpauseFlag = false;
    boolean endMenuFlag = false;

    Rectangle testRectangle = new Rectangle(10, 10, 50, 50);
    Rectangle boxFollower = new Rectangle(0, 0, 6, 6);
    private Texture testTexture;
    Font font;
    boolean drag = false;

    ArrayList<Renderable> renderables = new ArrayList<>();

    public Menu() {
        try {
            testTexture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("test_image.png"));
            System.out.printf("testTexture: width=%d, height=%d\n", testTexture.getImageWidth(), testTexture.getImageHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
        java.awt.Font AWTFont = new java.awt.Font("Times New Roman", java.awt.Font.BOLD, 24);
        font = new TrueTypeFont(AWTFont, true);

        renderables.add(testRectangle);
        renderables.add(boxFollower);
        //registerEventHandler(morTest);
    }

    public void show() {
        init();
        loop();
    }

    public void end() {
        endMenuFlag = true;
    }

    public void exit() {
        Display.destroy();
        System.exit(0);
    }

    void init() {
        glEnable(GL_TEXTURE_2D);
        //glShadeModel(GL_SMOOTH);
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_LIGHTING);
        glClearDepth(1);
        Keyboard.enableRepeatEvents(true);

        //glViewport(0,0,WIDTH,HEIGHT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    void loop() {
        long now;
        long tick = 0;
        long diff;
        long diff_target = 7;

        glClearColor(0.6f, 0.6f, 0.6f, 1.0f);

        while (!endMenuFlag) {
            if (Display.isCloseRequested()) {
                exit();
            }
            now = System.currentTimeMillis();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            logic();
            render();
            poll();

            Display.update();
            diff = System.currentTimeMillis() - now;
            if (diff < diff_target) {
                try {
                    Thread.sleep(1+(diff_target-diff));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            util.debug("tick=%d, diff=%d, diff_target=%d", tick, diff, diff_target);
            tick++;
        }
    }

    void pause() {
        unpauseFlag = false;
        /* Goes into the pause-loop */
        long tick = 0;

        while (true) {
            if (unpauseFlag) {
                break;
            } else if (Display.isCloseRequested()) {
                /* Make sure we exit if [X] was pressed */
                exit();
            }
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            render();
            pauseDraw();
            pausePoll();

            Display.update();
            Display.sync(30);
            util.debug("pause_tick=%d", tick);
            tick++;
        }
    }

    void logic() {
        boxFollower.move(Mouse.getEventX(), HEIGHT-Mouse.getEventY());
        if (drag) {
            testRectangle.move(Mouse.getEventX(),HEIGHT-Mouse.getEventY());
        }
    }

    void render() {
        for (Renderable r: renderables) {
            r.render();
        }
        glColor3d(1.0, 0, 0);
        drawLine(Mouse.getX(), HEIGHT-Mouse.getY());
        glColor3d(0, 1, 0);
        //glRectf(25, 75, 25+225, 25+225);
        glColor3d(0.5, 0.5, 0.5);
        //drawRectangle(morTest.area);

        //Color.white.bind();
        font.drawString(450f, 300f, "S » Start", Color.black);
        font.drawString(450f, 324f, "P » Pause", Color.black);
        font.drawString(450f, 350f, String.format("(%d, %d)", Mouse.getX(), HEIGHT-Mouse.getY()), Color.black);
        font.drawString(450f, 375f, String.format("(%d, %d)", Mouse.getX(), Mouse.getY()), Color.black);
        font.drawString(25f, 450f, "Hold numpad 1-3 for debug", Color.black);
        //glDisable(GL_BLEND);

        final float posmod = 296;
        drawTexture(testTexture, posmod, posmod, posmod+testTexture.getImageWidth(), posmod+testTexture.getImageHeight());

        glColor4d(0, 0, 1, 0.5f);
        drawRectangle(testRectangle);
    }

    void drawLine(float x, float y) {
        glBegin(GL_LINES);
        glVertex2f(0f, 0f);
        glVertex2f(x, y);
        glEnd();
    }

    void pauseDraw() {
    }


    /* For when (0,0) is bottom-left
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

    /* For when (0,0) is top-left */
    void drawTexture(Texture tex, float x1, float y1, float x2, float y2) {
        Color.white.bind();
        tex.bind();
        //glBindTexture(GL_TEXTURE_2D, tex.getTextureID());

        glBegin(GL_POLYGON);
        glTexCoord2f(0, 0);
        glVertex2d(x1, y1);
        glTexCoord2f(0, tex.getHeight());
        glVertex2d(x1, y2);
        glTexCoord2f(tex.getWidth(), tex.getHeight());
        glVertex2d(x2, y2);
        glTexCoord2f(tex.getWidth(), 0);
        glVertex2d(x2, y1);
        glEnd();
        //glBindTexture(GL_TEXTURE_2D, 0);
        TextureImpl.bindNone();
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

    void pausePoll() {
        while (Keyboard.next()) {
            int event = Keyboard.getEventKey();
            if (Keyboard.getEventKeyState()) {
                switch (event) {
                    case (Keyboard.KEY_P): {
                        unpauseFlag = true;
                        break;
                    }
                    case (Keyboard.KEY_ESCAPE): {
                        exit();
                        break;
                    }
                }
            }
        }
    }

    void poll() {
        while (Mouse.next()) {
            int mouseEvent = Mouse.getEventButton();
            switch (mouseEvent) {
                case (-1): {
                    /* Handles position events */
                    break;
                }
                default: {
                    /* Handles click events */
                    if (Mouse.getEventButtonState()) {
                        /* Down state */
                        switch (mouseEvent) {
                            case (0): {
                                drag = true;
                                /* FIXME: Replace drag boolean with register()/unregister() system? */
                                break;
                            }
                        }
                        util.debug2("mouseEvent=%d state=down\n", mouseEvent);
                    } else {
                        /* Up state */
                        switch (mouseEvent) {
                            case (0): {
                                drag = false;
                                break;
                            }
                        }
                        util.debug2("mouseEvent=%d state=up\n", mouseEvent);
                    }
                    break;
                }
            }
        }

        int eventCtr = 0;
        while (Keyboard.next()) {
            int event = Keyboard.getEventKey();
            if (Keyboard.getEventKeyState()) {
                switch (event) {
                    case (Keyboard.KEY_P): {
                        pause();
                        break;
                    }
                    case (Keyboard.KEY_ESCAPE): {
                        exit();
                        break;
                    }
                    case (Keyboard.KEY_S): {
                        end();
                        break;
                    }
                }
            }
            eventCtr++;
        }
        util.debug("poll() eventCtr=%d", eventCtr);
    }
}

