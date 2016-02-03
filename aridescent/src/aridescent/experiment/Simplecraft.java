package aridescent.experiment;

import aridescent.constructs.Sphere;
import aridescent.constructs.Text;
import aridescent.engine.Game;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;

import java.nio.FloatBuffer;

import static aridescent.engine.util.debug;
import static java.lang.Math.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

public class Simplecraft extends Game {
    Player player;
    Cubemap map;

    FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(4);
    Text topLeftText = new Text("", "Arial", 24, 0, 0, 0, Color.cyan);

    Sphere testSphere;

    public Simplecraft (int width, int height) throws LWJGLException {
        super(width, height);
        this.player = new Player();
        this.map = new Cubemap();

        testSphere = new Sphere(6f+2.5f, 2.5f, 0f, 2.5f, 21, 21);

        colorBuffer.put(new float[] {0f, .9f, .9f, 1f});
        colorBuffer.flip();
        setFPS(60);
    }

    @Override
    protected void init() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glEnable(GL_LIGHTING);
        glEnable(GL_LIGHT0);
        glMaterial(GL_FRONT_AND_BACK, GL_DIFFUSE, colorBuffer);

        glEnable(GL_DEPTH_TEST);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(60f, 1280f/720f, 0.1f, 60f); // define our "viewport" and how far/near we see

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        Mouse.setGrabbed(true);
    }

    @Override
    protected void render() {
        glLoadIdentity();
        player.render(); // 1st person -> player == camera
        map.drawCube(0f, 5f, 0f, 5f, 0f, 5f);
        map.drawGrid(-100f, 100f, -0.25f, 5f);
        testSphere.render();
        renderOverlay();
    }

    @Override
    protected void update() {
        topLeftText.setText(String.format("[%d] xyz(%.2f, %.2f, %.2f) look(%.2f, %.2f, %.2f)",
                fps,
                player.position.x, player.position.y, player.position.z,
                player.look.x, player.look.y, player.look.z));
    }

    @Override
    protected void poll() {
        player.poll();

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            exit();
        }

        debug(1, player.toString());
    }

    void renderOverlay() {
        glDisable(GL_LIGHTING);
        glPushMatrix();
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glMatrixMode(GL_PROJECTION);
        glPushMatrix();
        glLoadIdentity();
        glOrtho(0, DISPLAY_WIDTH, DISPLAY_HEIGHT, 1, -1f, 1f); // Change view to 2D alike before rendering objects
        topLeftText.render();
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);
        glPopMatrix();
        glEnable(GL_LIGHTING);
    }

    public static void main(String[] args) {
        try {
            Game game = new Simplecraft(1280, 720);
            game.run();
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }
}

class Vec3f {
    float x, y, z;

    Vec3f() {
        x = 0f;
        y = 0f;
        z = 0f;
    }

    Vec3f(float e1, float e2, float e3) {
        this.x = e1;
        this.y = e2;
        this.z = e3;
    }

    Vec3f add(Vec3f other) {
        return new Vec3f(x + other.x, y + other.y, z + other.z);
    }

    Vec3f sub(Vec3f other) {
        return new Vec3f(x - other.x, y - other.y, z - other.z);
    }

    Vec3f scale(float other) {
        return new Vec3f(x * other, y * other, z * other);
    }

    Vec3f cross(Vec3f other) {
        return new Vec3f(
                y * other.z - z * other.y,
                z * other.x - x * other.z,
                x * other.y - y * other.x);
    }

    Vec3f getNormalized() {
        float m = length();
        return new Vec3f(x / m, y / m, z / m);
    }

    float length() {
        return (float)sqrt(x * x + y * y + z * z);
    }
}

class Player {
    float pitch, yaw;
    Vec3f look, direction, position, up;

    Jumping jumpState = Jumping.NONE;
    float jumpFrom;
    float jumpTo;
    float jumpIncrement = 0.1f;
    boolean jumping = false;

    float moveSpeed = 0.1f;
    float sensitivity = 0.05f;

    Player() {
        look = new Vec3f();
        direction = new Vec3f();
        position = new Vec3f();
        up = new Vec3f(0f, 1f, 0f);


        position.z = -4f;
        position.y = 0.25f;
        jumpFrom = position.y;
        jumpTo = jumpFrom+1f;
    }

    void render() {
        gluLookAt(position.x, position.y, position.z,
                look.x, look.y, look.z,
                up.x, up.y, up.z);
    }

    void jumpLogic() {
        if (jumping) {
            switch (jumpState) {
                case UP:
                    if (position.y >= jumpTo) {
                        jumpState = Jumping.DOWN;
                    } else {
                        position.y += jumpIncrement;
                    }
                    break;
                case DOWN:
                    if (position.y <= jumpFrom) {
                        jumpState = Jumping.NONE;
                    } else {
                        position.y -= jumpIncrement;
                    }
                    break;
                case NONE:
                    jumping = false;
                    break;
            }
        }
    }

    public void poll() {
        float speed = moveSpeed;
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            speed *= 4;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            if (!jumping) {
                jumping = true;
                jumpState = Jumping.UP;
            }
        }

        Vec3f np = position;
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            np = np.add(direction.scale(speed));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            np = np.sub(direction.scale(speed));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            np = np.add(direction.cross(up).getNormalized().scale(speed));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            np = np.sub(direction.cross(up).getNormalized().scale(speed));
        }

        position.x = np.x;
        position.z = np.z;
        jumpLogic();

        float dy = Mouse.getDY();
        float dx = Mouse.getDX();

        yaw += dx * sensitivity;
        pitch += dy * sensitivity;

        if (pitch > 89f) {
            pitch = 89f;
        } else if (pitch < -89f) {
            pitch = -89f;
        }

        direction.x = (float)cos(toRadians(pitch)) * (float)cos(toRadians(yaw));
        direction.y = (float)sin(toRadians(pitch));
        direction.z = (float)cos(toRadians(pitch)) * (float)sin(toRadians(yaw));
        direction = direction.getNormalized();
        look = position.add(direction);
    }

    enum Jumping {
        UP, DOWN, NONE
    }

    @Override
    public String toString() {
        return String.format("Player: x=%f, y=%f, z=%f\n" +
                "jumpingState=%s, jumpingIncrement=%f, jumpFrom=%f, jumpTo=%f\n" +
                "moveSpeed=%f\n",
                position.x, position.y, position.z,
                jumpState, jumpIncrement, jumpFrom, jumpTo,
                moveSpeed);
    }
}

class Cubemap {
    /*
    void drawCubePlane(float offsetX, float offsetY, float offsetZ,
                       float rowIncrementX, float rowIncrementY, float rowIncrementZ,
                       float columnIncrementX, float columnIncrementY, float columnIncrementZ,
                       Object tex, int countRow, int countColumn);
    */

    void drawCube(float fromX, float toX,
                  float fromY, float toY,
                  float fromZ, float toZ) {

        glBegin(GL_QUADS);
        glNormal3f(0f, 0f, -1f);
        glVertex3f(fromX, fromY, fromZ);
        glVertex3f(fromX, toY, fromZ);
        glVertex3f(toX, toY, fromZ);
        glVertex3f(toX, fromY, fromZ);

        glNormal3f(0f, 0f, 1f);
        glVertex3f(fromX, fromY, toZ);
        glVertex3f(fromX, toY, toZ);
        glVertex3f(toX, toY, toZ);
        glVertex3f(toX, fromY, toZ);

        glNormal3f(0f, -1f, 0f);
        glVertex3f(fromX, fromY, fromZ);
        glVertex3f(fromX, fromY, toZ);
        glVertex3f(toX, fromY, toZ);
        glVertex3f(toX, fromY, fromZ);

        glNormal3f(0f, 1f, 0f);
        glVertex3f(fromX, toY, fromZ);
        glVertex3f(fromX, toY, toZ);
        glVertex3f(toX, toY, toZ);
        glVertex3f(toX, toY, fromZ);

        glNormal3f(-1f, 0f, 0f);
        glVertex3f(fromX, fromY, fromZ);
        glVertex3f(fromX, fromY, toZ);
        glVertex3f(fromX, toY, toZ);
        glVertex3f(fromX, toY, fromZ);

        glNormal3f(1f, 0f, 0f);
        glVertex3f(toX, fromY, fromZ);
        glVertex3f(toX, fromY, toZ);
        glVertex3f(toX, toY, toZ);
        glVertex3f(toX, toY, fromZ);
        glEnd();
    }

    void drawGrid(float from, float to, float y, float increment) {
        glDisable(GL_LIGHTING);
        glColor3f(0.3f, 0.3f, 0.3f);
        glBegin(GL_LINES);
        for (float i = from; i < to; i += increment) {
            glVertex3f(i, y, to);
            glVertex3f(i, y, from);

            glVertex3f(from, y, i);
            glVertex3f(to, y, i);
        }
        glEnd();
        glEnable(GL_LIGHTING);
    }
}
