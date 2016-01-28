package experiment;

import gameGL.Game;
import gameGL.constructs.Crosshair;
import gameGL.constructs.Text;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.Renderable;
import org.newdawn.slick.Color;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import static gameGL.util.*;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluLookAt;
import static org.lwjgl.util.glu.GLU.gluPerspective;

public class Test3D extends Game {
    private Float fovy = 45f;
    private Float aspect = 800f/600f;
    private Float zNear = 0.1f;
    private Float zFar = 50f;
    private Float eyex = 5f;
    private Float eyey = 2f;
    private Float eyez = -4f;
    private Float centerx = 0f;
    private Float centery = 0f;
    private Float centerz = 0f;
    private Float upx = 0f;
    private Float upy = 1f;
    private Float upz = 0f;

    private float angle = 0f;
    private Float lx = 0f;
    private Float lz = -1f;

    private Jumping jumpState = Jumping.NONE;
    private boolean jumping = false;
    private float jumpFrom = eyey;
    private float jumpTo = jumpFrom+1f;
    private float jumpIncrement = 0.04f; // increment per frame for now

    private Text text;
    private Crosshair xhair;
    private Renderable[] overlayObjects;
    // TODO: Make an "Updateable" for logic too, so only "registered" objects are "updated" (?)

    private Random random = new Random();
    private float[][][] rcolor = new float[10][10][3];

    FloatBuffer fbuf;
    private ByteBuffer indicesBuffer;
    int vboID;
    int count;
    private int vaoID;
    private int vboiID;

    void usage() {
        float[] ftest = {
                0f, 0f, 0f,
                1f, 1f, 1f,
                1f, 0f, 0f,
                0f, 1f, 0f,
                0f, 0f, 1f,
                1f, 1f, 0f,
                0f, 1f, 1f,
                1f, 0f, 1f,
        };
        fbuf = BufferUtils.createFloatBuffer(ftest.length);
        fbuf.put(ftest);
        fbuf.flip();

        byte[] indices = {
                0, 3, 5, 2,
                4, 6, 1, 7,
                0, 4, 7, 2,
                3, 6, 1, 5,
                0, 5, 6, 3,
                2, 7, 1, 5,
        };
        count = indices.length;
        indicesBuffer = BufferUtils.createByteBuffer(count);
        indicesBuffer.put(indices);
        indicesBuffer.flip();

        //vaoID = GL30.glGenVertexArrays();

        //GL30.glBindVertexArray(vaoID);

        vboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fbuf, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        //GL30.glBindVertexArray(0);

        vboiID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiID);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    void drawCube() {
        //GL30.glBindVertexArray(vaoID);
        GL20.glEnableVertexAttribArray(0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiID);
        glDrawElements(GL_QUADS, count, GL11.GL_UNSIGNED_BYTE, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL20.glDisableVertexAttribArray(0);
        //GL30.glBindVertexArray(0);
    }

    public static void drawCube(float fromX, float toX,
                                float fromY, float toY,
                                float fromZ, float toZ) {
        glBegin(GL_QUADS); // FIXME: Replace with GL_TRIANGLES
        glVertex3f(fromX, fromY, fromZ);
        glVertex3f(fromX, toY, fromZ);
        glVertex3f(toX, toY, fromZ);
        glVertex3f(toX, fromY, fromZ);

        glVertex3f(fromX, fromY, toZ);
        glVertex3f(fromX, toY, toZ);
        glVertex3f(toX, toY, toZ);
        glVertex3f(toX, fromY, toZ);

        glVertex3f(fromX, fromY, fromZ);
        glVertex3f(fromX, fromY, toZ);
        glVertex3f(toX, fromY, toZ);
        glVertex3f(toX, fromY, fromZ);

        glVertex3f(fromX, toY, fromZ);
        glVertex3f(fromX, toY, toZ);
        glVertex3f(toX, toY, toZ);
        glVertex3f(toX, toY, fromZ);

        glVertex3f(fromX, fromY, fromZ);
        glVertex3f(fromX, fromY, toZ);
        glVertex3f(fromX, toY, toZ);
        glVertex3f(fromX, toY, fromZ);

        glVertex3f(toX, fromY, fromZ);
        glVertex3f(toX, fromY, toZ);
        glVertex3f(toX, toY, toZ);
        glVertex3f(toX, toY, fromZ);
        glEnd();
    }

    public static void main(String[] args) {
        try {
            new Test3D(800, 600).run();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void exit() {
        GL15.glDeleteBuffers(vboID);
        GL15.glDeleteBuffers(vboiID);
        //GL30.glDeleteVertexArrays(vaoID);
        Display.destroy();
        System.exit(0);
    }

    public Test3D(int width, int height) throws LWJGLException {
        super(width, height);

        // Just create a few random colors for the cubes
        System.out.println("Test3D");
        for (int i = 0; i < rcolor.length; i++) {
            for (int j = 0; j < rcolor[0].length; j++) {
                rcolor[i][j][0] = random.nextFloat();
                rcolor[i][j][1] = random.nextFloat();
                rcolor[i][j][2] = random.nextFloat();
            }
        }

        // Create overlay objects, drawn in as 2D layer later
        text = new Text("TBD", "Arial", 24, 0, 0, 0, Color.cyan);
        xhair = new Crosshair(10f, DISPLAY_WIDTH, DISPLAY_HEIGHT, Color.red);
        overlayObjects = new Renderable[]{text, xhair};

        usage();
    }

    protected void init() {
        Mouse.setGrabbed(true); // Grab mouse for 1st person view
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glEnable(GL_DEPTH_TEST); // enables proper display of objects-infront-of-other-objects
        glDepthFunc(GL_LESS); // defines the function used to derive ^
        glEnable(GL_BLEND); // Neccesary for e.g. text display
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); // ^ same
        //glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    }

    protected void render() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(fovy, aspect, zNear, zFar); // define our "viewport" and how far/near we see

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        // Place the camera (player) and define where we're looking
        // eye = "player", center = point-being--looked-at
        gluLookAt(eyex, eyey, eyez,
                eyex+lx, centery, eyez+lz,
                upx, upy, upz);

        //drawCubes(10f, 10f, 1f);
        //drawCubes(10f, 10f, 0.5f); // draws the "walkway", some overlapping blocks with plane
        drawCube();
        draw2DOverlay(overlayObjects, DISPLAY_WIDTH, DISPLAY_HEIGHT);
    }

    protected void update() {
        text.setText(String.format("FPS: %d XY: (%f, %f)", fps, eyex, eyez));
        jumpLogic();
    }

    void jumpLogic() {
        if (jumping) {
            switch (jumpState) {
                case UP:
                    if (eyey >= jumpTo) {
                        jumpState = Jumping.DOWN;
                    } else {
                        eyey += jumpIncrement;
                        centery += jumpIncrement;
                    }
                    break;
                case DOWN:
                    if (eyey <= jumpFrom) {
                        jumpState = Jumping.NONE;
                    } else {
                        eyey -= jumpIncrement;
                        centery -= jumpIncrement;
                    }
                    break;
                case NONE:
                    jumping = false;
                    break;
            }
        }
    }

    void drawCubes(float xtarget, float ztarget, float zfactor) {
        // TODO: Make into constructs.Cube with Cube.drawCubes(..) (?)
        for (float x = 0; x < xtarget; x += 0.5) {
            glTranslatef(0.5f, 0f, -(ztarget*zfactor)); // 1f = area of cubes
            for (float z = 0; z < ztarget; z += 0.5) {
                glTranslatef(0f, 0f, 0.5f);
                setRandomColor((int)x, (int)z);
                drawCube(0f, 0.5f, -0.5f, 0.5f, 0f, 0.5f);
            }
        }
    }

    void setRandomColor(int x, int z) {
        glColor3f(rcolor[x][z][0], rcolor[x][z][1], rcolor[x][z][2]);
    }

    public void poll() {
        int eventCtr = 0;
        while (Mouse.next()) {
            int event = Mouse.getEventButton();
            switch(event) {
                case (-1): { // Mouse position has changed
                    float DY = Mouse.getDY(); // Vertical change (delta)
                    // FIXME: Find some math for Y-axis movement (tan?)
                    // Calculates new values for camera-looking-at-point (Y-axis)
                    if (centery < 10f && DY > 0) {
                        centery += DY * 0.005f;
                    } else if (centery > -10f && DY < 0) {
                        centery += DY * 0.005f;
                    }

                    float DX = Mouse.getDX(); // Horizontal change (delta)

                    // Calculates new values for camera-looking-at-point (X-axis)
                    if (DX > 0) {
                        angle += 0.005f*DX;
                        lx = (float)sin(angle);
                        lz = (float)-cos(angle);
                    } else {
                        angle -= 0.005f*-DX;
                        lx = (float)sin(angle);
                        lz = (float)-cos(angle);
                    }

                    debug3("centery=%f, centerx=%f, centerz=%f",
                            centery, centerx, centerz);
                    break;
                }
                default: { // Mouse button was clicked
                    debug2("clicked mbutton=%d", event);
                    break;
                }
            }
            eventCtr++;
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
            eventCtr++;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            eyex += lx * 0.1f;
            eyez += lz * 0.1f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            eyex -= lx * 0.1f;
            eyez -= lz * 0.1f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            angle += 0.05f;
            lx = (float)sin(angle);
            lz = (float)-cos(angle);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            angle -= 0.05f;
            lx = (float)sin(angle);
            lz = (float)-cos(angle);
        }
        debug("poll() eventCtr=%d", eventCtr);
    }
}

/** Enum for the different jumping states */
enum Jumping {
    UP, DOWN, NONE
}
