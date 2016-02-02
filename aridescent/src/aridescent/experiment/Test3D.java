package aridescent.experiment;

import aridescent.constructs.Sphere;
import aridescent.engine.Camera;
import aridescent.engine.Game;
import aridescent.engine.HUD;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Renderable;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.Random;

import static aridescent.engine.util.*;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.lwjgl.opengl.GL11.*;

public class Test3D extends Game {
    Camera camera = new Camera(DISPLAY_WIDTH, DISPLAY_HEIGHT);
    HUD hud = new HUD(this, camera);
    private Jumping jumpState = Jumping.NONE;
    private boolean jumping = false;
    private float jumpFrom = camera.eyey;
    private float jumpTo = jumpFrom+1.2f;
    private float jumpIncrement = 0.05f; // increment per frame for now

    Texture dirt;
    Texture rock;

    private Random random = new Random();
    private Renderable[] renderables;

    FloatBuffer floatBuffer;
    FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(4);

    public static void main(String[] args) {
        try {
            new Test3D(1280, 720).run();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    public Test3D(int width, int height) throws LWJGLException {
        super(width, height);
        System.out.println("Test3D");

        // Create overlay objects, drawn in as 2D layer later
        //hud.newText("TBD", "Arial", 24, 0, 0, 0, Color.cyan);
        String f1string =
                "Press G to ungrab mouse\n" +
                "Press space to jump\n" +
                "Press F1 for this help menu\n" +
                "Hold shift for faster movement";
        hud.newMultilineTextF1(f1string, "Arial", 24, 0, DISPLAY_HEIGHT_INT/2, (DISPLAY_HEIGHT_INT/2)-(24*4), Color.cyan);
        hud.newCrosshair(10f, DISPLAY_WIDTH, DISPLAY_HEIGHT, Color.red);

        // Texture loading
        String fileName = "dirt.png";
        try {
            dirt = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(fileName));
            System.out.printf("%s: width=%d, height=%d\n", fileName, dirt.getImageWidth(), dirt.getImageHeight());
            rock = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("rock.png"));
            System.out.printf("%s: width=%d, height=%d\n", "rock.png", rock.getImageWidth(), rock.getImageHeight());
        }
        catch(IOException e) { e.printStackTrace(); }

        // Randomize sphere locations
        renderables = new Renderable[25];
        Sphere.offsetX = 10f;
        Sphere.offsetY = 0f;
        Sphere.offsetZ = 10f;
        for (int i = 0; i < 25; i++) {
            if (random.nextFloat() >= 0.5f) {
                renderables[i] = new Sphere((random.nextFloat() * 2f),
                        (random.nextFloat() + 2f), (random.nextFloat() * 2f),
                        random.nextFloat(), 10, 10);
            } else {
                renderables[i] = new Sphere(-(random.nextFloat() * 2f),
                        -(random.nextFloat() * 2f), -(random.nextFloat() * 2f),
                        random.nextFloat(), 10, 10);
            }
        }
        floatBuffer = BufferUtils.createFloatBuffer(4);
        colorBuffer.put(new float[] {0f, .9f, .9f, 1f});
        colorBuffer.flip();
        //setFPS(1500); // test fps with higher number to check performance drops
    }

    protected void init() {
        Mouse.setGrabbed(true); // Grab mouse for 1st person view
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        //glViewport(0, 0, DISPLAY_WIDTH_INT, DISPLAY_HEIGHT_INT);
        glEnable(GL_DEPTH_TEST); // enables proper display of objects-infront-of-other-objects
        //glDepthFunc(GL_LESS); // defines the function used to derive ^
        glEnable(GL_BLEND); // Neccesary for e.g. text display

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); // ^ same
        //glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glMaterial(GL_FRONT_AND_BACK, GL_DIFFUSE, colorBuffer);

    }

    protected void render() {
        camera.reinit(); // should this be neccesary?
        camera.render(); // 1st person, camera == player
        glPushMatrix();
        glLight(GL_LIGHT0, GL_POSITION, floatBuffer);

        Sphere.renderSpheres(renderables);

        //drawRotatedTexturedCubes(Rotation.X_CW90, 0.5f, 0.5f, -0.5f, dirt, 10f, 10f, 1f); // wall
        drawCubePlane(0f, 0f, 0f,
                1f, 0f, 0f,
                0f, 1f, 0f,
                dirt, 10, 10);
        drawRotatedTexturedCubes(Rotation.NONE, 0.5f, 0f, 0f, dirt, 10f, 10f, 1f); // flat platform
        drawRotatedTexturedCubes(Rotation.NONE, 0f, 0f, 0.5f, rock, 10f, 10f, 0.5f); // draws the "walkway", some overlapping blocks with plane
        drawRotatedTexturedCubes(Rotation.NONE, 0.5f, 0f, 111f, dirt, 10f, 10f, 1f); // flat platform
        glPopMatrix();
        glDisable(GL_LIGHTING); // FIXME: is this neccesary?
        hud.render();
        glEnable(GL_LIGHTING);
    }

    protected void update() {
        hud.update();
        jumpLogic();

        // light floats
        floatBuffer.clear();
        floatBuffer.put(new float[]{camera.eyex, camera.centery, camera.eyez, 1.0f});
        floatBuffer.flip();
    }

    void jumpLogic() {
        if (jumping) {
            switch (jumpState) {
                case UP:
                    if (camera.eyey >= jumpTo) {
                        jumpState = Jumping.DOWN;
                    } else {
                        camera.eyey += jumpIncrement;
                        camera.centery += jumpIncrement;
                    }
                    break;
                case DOWN:
                    if (camera.eyey <= jumpFrom) {
                        jumpState = Jumping.NONE;
                    } else {
                        camera.eyey -= jumpIncrement;
                        camera.centery -= jumpIncrement;
                    }
                    break;
                case NONE:
                    jumping = false;
                    break;
            }
        }
    }

    enum Rotation {
        NONE, X_CW90, X_CW180, X_CW270
    }

    void setRotation(Rotation rotation) {
        switch(rotation) {
            case NONE:
                break;
            case X_CW90:
                glRotatef(90f, 1f, 0f, 0f);
                break;
            case X_CW180:
                glRotatef(180f, 1f, 0f, 0f);
                break;
            case X_CW270:
                glRotatef(270f, 1f, 0f, 0f);
                break;
        }
    }

    void drawCubePlane(float offsetX, float offsetY, float offsetZ,
                       float rowIncrementX, float rowIncrementY, float rowIncrementZ,
                       float columnIncrementX, float columnIncrementY, float columnIncrementZ,
                       Texture tex, int countRow, int countColumn) {
        glPushMatrix();
        for (int i = 0; i < countRow; i++) {
            glTranslatef(rowIncrementX, rowIncrementY, rowIncrementZ);
            glPushMatrix();
            for (int j = 0; j < countColumn; j++) {
                glTranslatef(columnIncrementX, columnIncrementY, columnIncrementZ);
                glColor3f(1f, 1f, 1f);
                drawTexturedCube(tex, offsetX+0f, offsetX+1f, offsetY+0f, offsetY+1f, offsetZ+0f, offsetZ+1f);
            }
            glPopMatrix();
        }
        glPopMatrix();
    }

    void drawRotatedTexturedCubes(Rotation rotation, float offsetX, float offsetY, float offsetZ,
                                  Texture tex, float xtarget, float ztarget, float zfactor) {
        // TODO: Make into constructs.Cube with Cube.drawCubes(..) (?)
        glPushMatrix();
        setRotation(rotation);
        for (float x = 0; x < xtarget; x += 0.5) {
            glTranslatef(0.5f, 0f, -(ztarget*zfactor));
            for (float z = 0; z < ztarget; z += 0.5) {
                glTranslatef(0f, 0f, 0.5f);
                glColor3f(1f, 1f, 1f);
                drawTexturedCube(tex, offsetX+0f, offsetX+0.5f, offsetY+0f, offsetY+0.5f, offsetZ+0f, offsetZ+0.5f);
            }
        }
        glPopMatrix();
    }

    public void poll() {
        while (Mouse.next()) {
            int event = Mouse.getEventButton();
            switch(event) {
                case (-1): { // Mouse position has changed
                    float DY = Mouse.getDY(); // Vertical change (delta)
                    // FIXME: Find some math for Y-axis movement (tan?)
                    // Calculates new values for camera-looking-at-point (Y-axis)
                    if (camera.centery < 10f && DY > 0) {
                        camera.centery += DY * 0.005f;
                    } else if (camera.centery > -10f && DY < 0) {
                        camera.centery += DY * 0.005f;
                    }

                    float DX = Mouse.getDX(); // Horizontal change (delta)

                    // Calculates new values for camera-looking-at-point (X-axis)
                    if (DX > 0) {
                        camera.angle += 0.005f*DX;
                        camera.lx = (float)sin(camera.angle);
                        camera.lz = (float)-cos(camera.angle);
                    } else {
                        camera.angle -= 0.005f*-DX;
                        camera.lx = (float)sin(camera.angle);
                        camera.lz = (float)-cos(camera.angle);
                    }
                    break;
                }
                default: { // Mouse button was clicked
                    debug(1, "clicked mbutton=%d", event);
                    break;
                }
            }
        }

        while (Keyboard.next()) {
            int event = Keyboard.getEventKey();
            if (Keyboard.getEventKeyState()) {
                switch (event) {
                    case (Keyboard.KEY_ESCAPE): {
                        exit();
                        break;
                    }
                    case (Keyboard.KEY_G): {
                        if (Mouse.isGrabbed()) {
                            Mouse.setGrabbed(false);
                        } else {
                            Mouse.setGrabbed(true);
                        }
                        break;
                    }
                    case (Keyboard.KEY_SPACE): {
                        jumping = true;
                        jumpState = Jumping.UP;
                        break;
                    }

                }
            } else {
                switch (event) {
                    case (Keyboard.KEY_SPACE): {
                        break;
                    }
                }
            }
        }
        float ws_speed;
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) ws_speed = 0.5f;
        else ws_speed = 0.1f;
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            camera.eyex += camera.lx * ws_speed;
            camera.eyez += camera.lz * ws_speed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            camera.eyex -= camera.lx * ws_speed;
            camera.eyez -= camera.lz * ws_speed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            camera.angle += 0.05f;
            camera.lx = (float)sin(camera.angle);
            camera.lz = (float)-cos(camera.angle);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            camera.angle -= 0.05f;
            camera.lx = (float)sin(camera.angle);
            camera.lz = (float)-cos(camera.angle);
        }
    }
}

/** Enum for the different jumping states */
enum Jumping {
    UP, DOWN, NONE
}
