package gameGL.constructs;

import org.lwjgl.util.Renderable;
import org.newdawn.slick.Color;

public class Crosshair implements Renderable {
    Line horLine;
    Line verLine;

    public Crosshair(float size, float width, float height, Color color) {
        float half = size/2;
        float xpos1 = (width/2) - half;
        float xpos2 = (width/2) + half;
        float ypos1 = (height/2) - half;
        float ypos2 = (height/2) + half;

        // -- to fix misaligned crosshair
        // TODO: Test different sizes, make sure it's always displayed correctly
        horLine = new Line(--xpos1, height/2f, xpos2, height/2f, color);
        verLine = new Line(width/2f, ypos1, width/2f, ypos2, color);
    }

    @Override
    public void render() {
        horLine.render();
        verLine.render();
    }
}
