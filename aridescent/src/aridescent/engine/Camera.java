package aridescent.engine;

import org.lwjgl.util.Renderable;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

public class Camera implements Updateable, Renderable {
     public Float fovy = 90f;
     public Float aspect;
     public Float zNear = 0.1f;
     public Float zFar = 50f;

     public Float eyex = 4.95f;
     public Float eyey = 2.5f;
     public Float eyez = 39.45f;
     public Float centerx = 0f;
     public Float centery = 2f;
     public Float centerz = 0f;

     public float angle = 0f;
     public Float lx = 0f;
     public Float lz = -1f;

    public Camera(float width, float height) {
        aspect = width/height;
    }

    @Override
    public void update() {
    }

    public void reinit() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(fovy, aspect, zNear, zFar); // define our "viewport" and how far/near we see

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }

    @Override
    public void render() {

        // "Place" the camera (player) and define where we're "looking"
        // eye = "player", center = point-being-looked-at
        gluLookAt(eyex, eyey, eyez,
                eyex+lx, centery, eyez+lz,
                0f, 1f, 0f);
    }
}
