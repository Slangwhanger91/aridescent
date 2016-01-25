package gameGL;

import gameGL.constructs.Image;
import gameGL.constructs.Line;
import gameGL.constructs.Rectangle;
import gameGL.constructs.Text;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.Renderable;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class Menu {
    final static int WIDTH = 640;
    final static int HEIGHT = 480;
    boolean unpauseFlag = false;
    boolean endMenuFlag = false;
    boolean drag = false;

    ArrayList<Renderable> renderables = new ArrayList<>();

    Rectangle testRectangle = new Rectangle(10, 10, 50, 50);
    Rectangle boxFollower = new Rectangle(0, 0, 6, 6);
    Line line = new Line(0,0,0,0);
    Image imageTest;
    Text coords1;
    Text coords2;


    public Menu() {

        imageTest = new Image("test_image.PNG", 296, 296);

        renderables.add(imageTest);
        renderables.add(testRectangle);
        renderables.add(boxFollower);
        renderables.add(line);

        renderables.add(new Text("S » Start", "Arial", 24, java.awt.Font.PLAIN, 450, 300));
        renderables.add(new Text("P » Pause", "Arial", 24, java.awt.Font.PLAIN, 450, 324));
        coords1 = new Text("", 24, java.awt.Font.PLAIN, 450, 350);
        renderables.add(coords1);
        coords2 = new Text("", 24, java.awt.Font.PLAIN, 450, 375);
        renderables.add(coords2);
        renderables.add(new Text("Hold numpad 1-3 for debug", "Arial", 24,
                java.awt.Font.PLAIN, 25, 450));
    }

    public void show() {
        init();
        loop();
    }

    public void end() {
        endMenuFlag = true;
        glDisable(GL_BLEND);
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
        coords1.setText(String.format("(%d, %d)", Mouse.getX(), Mouse.getY()));
        coords2.setText(String.format("(%d, %d)", Mouse.getX(), HEIGHT-Mouse.getY()));
        boxFollower.move(Mouse.getEventX(), HEIGHT-Mouse.getEventY());
        line.setEndpoint(Mouse.getX(), HEIGHT-Mouse.getY());
        if (drag) {
            testRectangle.move(Mouse.getEventX(),HEIGHT-Mouse.getEventY());
        }
    }

    void render() {
        for (Renderable r: renderables) {
            r.render();
        }

        //Color.white.bind();
    }


    void pauseDraw() {
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

