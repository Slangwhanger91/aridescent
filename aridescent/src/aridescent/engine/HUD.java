package aridescent.engine;

import aridescent.constructs.Crosshair;
import aridescent.constructs.MultilineText;
import aridescent.constructs.Text;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Renderable;
import org.newdawn.slick.Color;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class HUD implements Renderable, Updateable {
    Game game;
    Camera cam;

    //private Renderable[] overlayObjects;
    private ArrayList<Renderable> overlayObjects = new ArrayList<>();
    private ArrayList<Renderable> overlayObjectsF1 = new ArrayList<>();
    //private Renderable[] overlayObjectsF1;

    Text text = new Text("", "Arial", 24, 0, 0, 0, Color.cyan);

    public HUD(Game game, Camera cam) {
        this.game = game;
        this.cam = cam;
        overlayObjects.add(text);
    }


    @Override
    public void render() {
        draw2DOverlay(overlayObjects, game.DISPLAY_WIDTH, game.DISPLAY_HEIGHT);

        if (Keyboard.isKeyDown(Keyboard.KEY_F1)) {
            // FIXME: Better handling of when-held-overlay
            draw2DOverlay(overlayObjectsF1, game.DISPLAY_WIDTH, game.DISPLAY_HEIGHT);
        }
    }

    public void add(Renderable renderable) {
        overlayObjects.add(renderable);
    }

    public void addToWhenHeld(Renderable renderable) {
        overlayObjectsF1.add(renderable);
    }

    public void newMultilineText(String text, String fontName, int fontSize, int fontFace, int xpos, int ypos, Color color) {
        overlayObjects.add(new MultilineText(text, fontName, fontSize, fontFace, xpos, ypos, color));
    }

    public void newMultilineTextF1(String text, String fontName, int fontSize, int fontFace, int xpos, int ypos, Color color) {
        overlayObjectsF1.add(new MultilineText(text, fontName, fontSize, fontFace, xpos, ypos, color));
    }

    public void newText(String text, String fontName, int fontSize, int fontFace, int xpos, int ypos, Color color) {
        overlayObjects.add(new Text(text, fontName, fontSize, fontFace, xpos, ypos, color));
    }

    public void newCrosshair(float size, float width, float height, Color color) {
        overlayObjects.add(new Crosshair(size, width, height, color));
    }


    @Override
    public void update() {
        text.setText(
                String.format("FPS: %d pos: (%.2f, %.2f, %.2f) " +
                                "look: (%.2f, %.2f, %.2f)",
                        game.fps, cam.eyex, cam.eyey, cam.eyez, cam.eyex+cam.lx, cam.centery, cam.eyez+cam.lz));
    }

    /** Method for drawing an array of Renderable objects as a 2D overlay.
     *
     * @param renderables array of Renderable objects
     * @param right variable representing width of display
     * @param bottom variable representing height of display
     */
    public static void draw2DOverlay(Renderable[] renderables, float right, float bottom) {
        set2D(right, bottom);
        for (Renderable r: renderables) {
            r.render();
        }
        unset2D();
    }

    public static void draw2DOverlay(ArrayList<Renderable> renderables, float right, float bottom) {
        set2D(right, bottom);
        for (Renderable r: renderables) {
            r.render();
        }
        unset2D();
    }

    private static void set2D(float right, float bottom) {
        glPushMatrix();
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(0, right, bottom, 1, -1f, 1f); // Change view to 2D alike before rendering objects
    }

    private static void unset2D() {
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);
        glPopMatrix();
    }
}
