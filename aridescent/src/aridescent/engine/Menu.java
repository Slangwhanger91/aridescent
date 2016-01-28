package aridescent.engine;

import aridescent.constructs.Image;
import aridescent.constructs.Line;
import aridescent.constructs.Rectangle;
import aridescent.constructs.Text;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.Renderable;

import static org.lwjgl.opengl.GL11.*;

public class Menu {
    final static int WIDTH = 640;
    final static int HEIGHT = 480;
    Game game;
    boolean endMenuFlag = false;
    boolean drag = false;

    //ArrayList<Renderable> renderables = new ArrayList<>();
    Renderable[] renderables;


    Rectangle testRectangle = new Rectangle(10, 10, 50, 50);
    Rectangle boxFollower = new Rectangle(0, 0, 6, 6);
    Line line = new Line(0,0,0,0);
    Image imageTest;
    Text coords1;
    Text coords2;


    public Menu(Game game) {
        this.game = game;
        imageTest = new Image("test_image.PNG", 296, 296);

        /*
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
                */
        coords1 = new Text("", 24, java.awt.Font.PLAIN, 450, 350);
        coords2 = new Text("", 24, java.awt.Font.PLAIN, 450, 375);
        renderables = new Renderable[]{
                imageTest,
                testRectangle,
                boxFollower,
                line,
                new Text("S » Start", "Arial", 24, java.awt.Font.PLAIN, 450, 300),
                new Text("P » Pause", "Arial", 24, java.awt.Font.PLAIN, 450, 324),
                coords1,
                coords2,
        new Text("Hold numpad 1-3 for debug", "Arial", 24,
                java.awt.Font.PLAIN, 25, 450),
        };
    }

    public void show() {
        init();
        loop();
        endMenuFlag = false;
    }

    public void end() {
        endMenuFlag = true;
    }

    void init() {
        //glShadeModel(GL_SMOOTH);
        //glDisable(GL_DEPTH_TEST);
        //glDisable(GL_LIGHTING);
        //glClearDepth(1);

        //glViewport(0,0,WIDTH,HEIGHT);
        //glMatrixMode(GL_PROJECTION);
        //glLoadIdentity();
        //glOrtho(0, WIDTH, HEIGHT, 1, 0, 1);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    void loop() {
        long tick = 0;
        glClearColor(0.6f, 0.6f, 0.6f, 1.0f);

        while (!endMenuFlag) {
            if (Display.isCloseRequested()) {
                game.exit();
            }
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            logic();
            //render();
            Game.draw2DOverlay(renderables, game.getWidth(), game.getHeight());
            poll();

            Display.update();
            Display.sync(30);
            util.debug("tick=%d", tick);
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
                /* Pressed */
                switch (event) {
                    case (Keyboard.KEY_ESCAPE): {
                        game.exit();
                        break;
                    }
                    case (Keyboard.KEY_S): {
                        end();
                        break;
                    }
                }
            } else {
                /* Released */
                util.debug("key_released=%d", event);
            }
            eventCtr++;
        }
        util.debug("poll() eventCtr=%d", eventCtr);
    }
}
