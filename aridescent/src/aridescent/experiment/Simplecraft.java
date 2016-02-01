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
import static java.lang.Math.cos;
import static java.lang.Math.sin;
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

        player.z = -4f;
        player.y = 0.5f;
        // look towards cube
        player.xzangle = -3.29f;
        player.xfactor = 0.15f;
        player.zfactor = 0.98f;
        player.looky = 0.41f;

        testSphere = new Sphere(6f+2.5f, 2.5f, 0f, 2.5f, 21, 21);

        colorBuffer.put(new float[] {0f, .9f, .9f, 1f});
        colorBuffer.flip();
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
        testSphere.render();
        renderOverlay();
    }

    @Override
    protected void update() {
        player.jumpLogic();
        topLeftText.setText(String.format("[%d] xyz(%.2f, %.2f, %.2f) look(%.2f, %.2f, %.2f",
                fps,
                player.x, player.y, player.z,
                player.getLookX(), player.looky, player.getLookZ()));
    }

    @Override
    protected void poll() {
        player.poll();

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            exit();
        }

        debug(player.toString());
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

class Player {
    float x, y, z;
    float looky;

    float xzangle;
    float xfactor, zfactor;
    float yangle;

    Jumping jumpState = Jumping.NONE;
    float jumpFrom = y;
    float jumpTo = y+1f;
    float jumpIncrement = 0.04f;
    boolean jumping = false;

    float moveSpeed = 0.1f;

    void render() {
        gluLookAt(x, y, z,
                getLookX(), looky, getLookZ(),
                0f, 1f, 0f);
    }

    float getLookX() {
        return x+xfactor;
    }

    float getLookZ() {
        return z+zfactor;
    }

    void jumpLogic() {
        if (jumping) {
            switch (jumpState) {
                case UP:
                    if (y >= jumpTo) {
                        jumpState = Jumping.DOWN;
                    } else {
                        y += jumpIncrement;
                        looky += jumpIncrement;
                    }
                    break;
                case DOWN:
                    if (y <= jumpFrom) {
                        jumpState = Jumping.NONE;
                    } else {
                        y -= jumpIncrement;
                        looky -= jumpIncrement;
                    }
                    break;
                case NONE:
                    jumping = false;
                    break;
            }
        }
    }

    public void poll() {
        float DY = Mouse.getDY();
        if (DY != 0) {
            if (DY > 0) {
                looky += DY * 0.005f;
            } else {
                looky += DY * 0.005f;
            }
        }

        float DX = Mouse.getDX();
        if (DX != 0) {
            if (DX > 0) {
                xzangle += 0.005f*DX;
                xfactor = (float)sin(xzangle);
                zfactor = (float)-cos(xzangle);
            } else {
                xzangle -= 0.005f*-DX;
                xfactor = (float)sin(xzangle);
                zfactor = (float)-cos(xzangle);
            }
        }

        /*
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && !jumping) {
            jumping = true;
            jumpState = Jumping.UP;
        }
        */

        float speed = moveSpeed;
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            speed *= 4;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            y += speed;
            looky += speed;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            y -= speed;
            looky -= speed;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            x += xfactor * speed;
            z += zfactor * speed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            x -= xfactor * speed;
            z -= zfactor * speed;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            xzangle += 0.05f;
            xfactor = (float)sin(xzangle);
            zfactor = (float)-cos(xzangle);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            xzangle -= 0.05f;
            xfactor = (float)sin(xzangle);
            zfactor = (float)-cos(xzangle);
        }
    }

    enum Jumping {
        UP, DOWN, NONE
    }

    @Override
    public String toString() {
        return String.format("Player: x=%f, y=%f, z=%f\n" +
                "xzangle=%f, xfactor=%f, zfactor=%f\n" +
                "jumpingState=%s, jumpingIncrement=%f, jumpFrom=%f, jumpTo=%f\n" +
                "moveSpeed=%f, looky=%f, getLookX=%f, getLookZ=%f\n",
                x, y, z,
                xzangle, xfactor, zfactor,
                jumpState, jumpIncrement, jumpFrom, jumpTo,
                moveSpeed, looky, getLookX(), getLookZ());
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
}
