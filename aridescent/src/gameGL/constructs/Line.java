package gameGL.constructs;

import org.lwjgl.util.Renderable;
import org.newdawn.slick.Color;

import static org.lwjgl.opengl.GL11.*;

public class Line implements Renderable {
    private int x, y, x2, y2;
    private Color color;

    public Line(int xpos, int ypos, int xpos2, int ypos2, Color color) {
        x = xpos;
        y = ypos;
        x2 = xpos2;
        y2 = ypos2;
        this.color = color;
    }

    public Line(int xpos, int ypos, int xpos2, int ypos2) {
        this(xpos, ypos, xpos2, ypos2, Color.white);
    }

    public void setEndpoint(int x, int y) {
        x2 = x;
        y2 = y;
    }

    @Override
    public void render() {
        color.bind();
        drawLine(x, y, x2, y2);
    }

    void drawLine(float x, float y, float x2, float y2) {
        glBegin(GL_LINES);
        glVertex2f(x, y);
        glVertex2f(x2, y2);
        glEnd();
    }
}
