package gameGL;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex2d;

public class Platform {
	
	double x, y;
	
	Platform(){
		x = 100;
		y = 60;
	}

	public void logic(){
		
	}
	
	public void draw(){
		// 
		glPushMatrix();

		// Moves to the given x,y position on Display
		glTranslated(x, y, 0);

		// Color and shape size
		glBegin(GL_QUADS);

		glColor3d(1, 0.1, 0.1);
		glVertex2d(-16, 0); //bottom left
		glVertex2d(16, 0); //bottom right
		glColor3d(0, 1, 0);
		glVertex2d(18, 16); //top right
		glVertex2d(-18, 16); //top left

		glEnd();

		// 
		glPopMatrix();
	}
}
