package aridescent.constructs;

import org.lwjgl.util.Renderable;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.TextureImpl;

import static org.lwjgl.opengl.GL11.*;

public class Sphere implements Renderable {
    public static float offsetX = 0f, offsetY = 0f, offsetZ = 0;
    float x, y, z;
    float radius;
    int hlines, vlines;
    org.lwjgl.util.glu.Sphere lwjglSphere;

    public Sphere(float x, float y, float z, float radius, int hlines, int vlines) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.hlines = hlines;
        this.vlines = vlines;

        lwjglSphere = new org.lwjgl.util.glu.Sphere();
    }

    @Override
    public void render() {
        glTranslatef(x, y, z);
        lwjglSphere.draw(radius, hlines, vlines);
    }

    public static void renderSpheres(Renderable[] spheres) {
        Color.darkGray.bind();
        glPushMatrix();
        glTranslatef(offsetX, offsetY, offsetZ);
        for (Renderable r: spheres) {
            r.render();
        }
        glPopMatrix();
        TextureImpl.unbind();
    }

}
