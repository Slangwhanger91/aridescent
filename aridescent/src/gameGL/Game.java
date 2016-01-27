package gameGL;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static gameGL.util.debug;
import static org.lwjgl.opengl.GL11.*;

public abstract class Game {
    protected final float DISPLAY_WIDTH;
    protected final float DISPLAY_HEIGHT;
    protected long fps = 0;

    private int fpsTarget = 100;
    private int glClearBits = GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT;
    private boolean menuFlag = false;
    private Menu menu;

    protected Game(int width, int height) throws LWJGLException {
        Display.setDisplayMode(new DisplayMode(width, height));
        Display.create();
        DISPLAY_WIDTH = width;
        DISPLAY_HEIGHT = height;
    }

    protected void run() {
        init();
        loop();
    }

   protected void exit() {
        Display.destroy();
        System.exit(0);
    }

    private void loop() {
        long tick = 0;
        long now = System.currentTimeMillis();
        long old_tick = 0;

        while (!Display.isCloseRequested()) {
            glClear(glClearBits);
            if ((System.currentTimeMillis() - now) > 1000) {
                fps = (tick - old_tick);
                old_tick = tick;
                now = System.currentTimeMillis();
            }

            update();
            render();
            poll();

            Display.update();
            Display.sync(fpsTarget);
            debug("tick=%d", tick);
            tick++;
        }
    }

    protected void setFPS(int target) {
        fpsTarget = target;
    }

    protected void setGlClear(int bits) {
        glClearBits = bits;
    }

    protected void setMenu(Menu menu) {
        this.menu = menu;
        menuFlag = true;
    }

    protected void showMenu() {
        menu.show();
    }

    protected abstract void init();
    protected abstract void update();
    protected abstract void render();
    protected abstract void poll();
}
