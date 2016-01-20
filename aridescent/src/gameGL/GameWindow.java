package gameGL;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

public class GameWindow {
	public static void main(String[] args) throws Exception{
		Display.setDisplayMode(new DisplayMode(640, 480));
		Display.create();
		while(!Display.isCloseRequested()){
			setCamera();
			drawBackground();
			
			// Game logic should fit here
			
			
			Display.update();
			Display.sync(60);
		}
		Display.destroy();
	}

	public static void setCamera(){
		// Clear screen to black every frame
		glClear(GL_COLOR_BUFFER_BIT);
		// Modify projection Matrix
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 640, 0, 480, -1, 1);

		// Modify modelview matrix
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}

	public static void drawBackground(){
		// Sky

		glBegin(GL_QUADS);

		glColor3d(0.7, 0.8, 0.9);
		glVertex2d(0, 0);
		glVertex2d(640, 0);

		glColor3d(0.5, 0.6, 0.8);
		glVertex2d(640, 480);
		glVertex2d(0, 480);

		glEnd();

		// Ground

		glBegin(GL_QUADS);

		glColor3d(0.6, 0.2, 0.1);
		glVertex2d(0, 0);
		glVertex2d(640, 0);

		glVertex2d(640, 32);
		glVertex2d(0, 32);

		glEnd();

		// Grass

		glBegin(GL_QUADS);

		glColor3d(0.2, 0.8, 0.2);
		glVertex2d(0, 25);
		glVertex2d(640, 25);

		glVertex2d(640, 32);
		glVertex2d(0, 32);

		glEnd();

	}

}
