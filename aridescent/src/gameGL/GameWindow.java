package gameGL;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

public class GameWindow {
	
	private Player player;

	public static void main(String[] args) throws Exception {
		try {
			new GameWindow().start();
		} catch (LWJGLException le) {
			le.printStackTrace();
		}
	}

	void start() throws LWJGLException {
		Display.setDisplayMode(new DisplayMode(640, 480));
		Display.create();

		player = new Player();
		Platform platform = new Platform();
		//Enemy enemy = new Enemy();

		Menu testMenu = new Menu();
        /* Checks return value from menu to decide to start game or just exit program. */
		testMenu.show();

		setCamera();

		while(!Display.isCloseRequested()){
			// Clear screen to black every frame
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			drawBackground();
			
			// Player logic and animation
			player.logic();
			player.draw();

			// Platforms logic and animation
			platform.logic();
			platform.draw();
			
			// Enemy logic and animation
			//enemy.logic();
			//enemy.draw();
			
			poll();
			Display.update();
			Display.sync(60);
		}
		Display.destroy();
	}

	void poll() {
		/* Doesn't seem neccesary.
		if (!Keyboard.isCreated()) {
			try {
				Keyboard.create();
			} catch (LWJGLException e) {
				e.printStackTrace();
			}
		}
		*/
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			exit();
        }
    }

	void exit() {
		Display.destroy();
		System.exit(0);
	}

	void setCamera(){
		// Modify projection Matrix
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 640, 0, 480, -1, 1);

		// Modify modelview matrix
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}

	void drawBackground(){
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
