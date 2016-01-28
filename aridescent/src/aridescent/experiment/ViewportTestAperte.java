package aridescent.experiment;

import aridescent.constructs.Text;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;

import static aridescent.engine.util.debug;
import static org.lwjgl.opengl.GL11.*;

public class ViewportTestAperte {
    final static float WIDTH = 640;
    final static float HEIGHT = 480;
    private final Text text;
    int viewx = 0;
    int viewy = 0;
    private int maxx = 12800;

    public static void main(String[] args) {
        try {
            Display.setDisplayMode(
                    new DisplayMode((int) WIDTH,
                            (int) HEIGHT));
            Display.create();
            new ViewportTestAperte();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

    }

    ViewportTestAperte() {
        text = new Text("TBD", 24, 0, 0, 0);
        text.setColor(Color.blue);
        init();
        loop();
    }

    void init() {
        glClearColor(0f, 0f, 0f, 1f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0f, 640f, 480f, 0f, -1f, 1f);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        //glViewport(0, 0, 1280, 960);
    }

    void render() {
        drawBox(maxx, 960);
        glViewport(0, 0, 640, 480);
        text.setText(String.format("(%d, %d)", viewx, viewy));
        text.render();
        glViewport(viewx, viewy, maxx, 960);
    }

    void drawBox(float x, float y) {
        glBegin(GL_QUADS);
        glColor3f(1f, 1f, 1f);
        glVertex2f(x, y);
        glVertex2f(x, 0f);
        glVertex2f(0f, 0f);
        glVertex2f(0f, y);
        glEnd();
    }

    void poll() {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Display.destroy();
            System.exit(0);
        }

        if (Mouse.isButtonDown(0)) {
            int x = Mouse.getX();
            int y = Mouse.getY();
            if (x > 500) {
                viewx -= 50;
            }
            if (y > 340) {
                viewy -= 50;
            }
            if (x < 140) {
                viewx += 50;
            }
            if (y < 140) {
                viewy += 50;
            }
            glViewport(viewx, viewy, maxx, 960);
        }
        if (Mouse.isButtonDown(1)) {
            viewx = 0;
            viewy = 0;
            glViewport(viewx, viewy, maxx, 960);
        }

        while(Mouse.next()) {
            switch(Mouse.getEventButton()) {
                case(-1): {
                    break;
                }
                default: {
                    break;
                }

            }
        }
    }

    void loop() {
        long tick = 0;
        while(!Display.isCloseRequested()) {
            glClear(GL_COLOR_BUFFER_BIT);
            render();
            poll();

            Display.update();
            Display.sync(30);
            debug("tick=%d", tick++);
        }
    }
}
