package gameGL.constructs;

import org.lwjgl.util.Renderable;

import static org.lwjgl.opengl.GL11.glColor4f;

public class Color implements Renderable {
    float r, g, b, o;

    Color(float r, float g, float b, float o) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.o = o;
    }

    Color(float r, float g, float b) {
        this(r, g, b, 1f);
    }

    Color() {
        this(0f, 0f, 0f, 1f);
    }

    @Override
    public void render() {
        glColor4f(r, g, b, o);
    }
}
