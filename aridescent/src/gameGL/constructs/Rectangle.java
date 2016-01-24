package gameGL.constructs;

import org.lwjgl.util.Renderable;

import static org.lwjgl.opengl.GL11.*;

public class Rectangle implements Renderable {
    private int x, y, w, h, x2, y2;
    private Color color;

    public Rectangle(int xpos, int ypos, int width, int height, Color color) {
        x = xpos;
        y = ypos;
        w = width;
        h = height;
        this.color = color;
        x2 = xpos+width;
        y2 = ypos+height;
    }

    public Rectangle(int xpos, int ypos, int width, int height) {
        this(xpos, ypos, width, height, new Color());
    }

    public void move(int xpos, int ypos) {
        x = xpos;
        y = ypos;
        x2 = xpos+w;
        y2 = ypos+h;
    }

    public void resize(int width, int height) {
        x2 = x+width;
        y2 = x+height;
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public void render() {
        if (color != null) {
            color.render();
        }
        glBegin(GL_POLYGON);
        glVertex2d(x, y);
        glVertex2d(x2, y);
        glVertex2d(x2, y2);
        glVertex2d(x, y2);
        glEnd();
    }
}
