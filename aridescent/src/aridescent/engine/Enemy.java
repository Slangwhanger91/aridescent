package aridescent.engine;

import static org.lwjgl.opengl.GL11.*;

public class Enemy {

	private double x, y;

	public Enemy(){

		x = 200;
		y = 32;
	}

	public void logic(){
		x++;

	}

	public void draw(){
		// 
		glPushMatrix();

		// Moves to the given x,y position on Display
		glTranslated(x, y, 0);

		// Color and shape size
		glBegin(GL_QUADS);

		glColor3d(1, 0, 0);
		glVertex2d(-8, 0); //bottom left
		glVertex2d(8, 0); //bottom right
		glVertex2d(8, 16); //top right
		glVertex2d(-8, 16); //top left

		glEnd();

		// 
		glPopMatrix();
	}
}
