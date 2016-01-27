package gameGL;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glVertex2d;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import platforms.*;

import static org.lwjgl.opengl.GL11.*;

public class GameWindow extends Game {
	
	private Player player;
	private boolean menuFlag = false;

	public static void main(String[] args) throws Exception {
		try {
			new GameWindow(640, 480).run();
		} catch (LWJGLException le) {
			le.printStackTrace();
		}
	}

	protected GameWindow(int width, int height) throws LWJGLException {
		super(width, height);

		player = new Player();
		Platform.createMaxPlatforms();
		/*new GreenPlatform(100, 60);
		new GreenPlatform(200, 100);
		new GreenPlatform(300, 150);
		new GreenPlatform(400, 200);
		new GreenPlatform(500, 250);*/
		//Enemy enemy = new Enemy();

		Menu testMenu = new Menu();
        /* Checks return value from menu to decide to start game or just exit program. */
		testMenu.show();
		CameraPosition.init(player);

		/* Temporarily disabled
		if (menuFlag) {
			// Pauses and shows menu again (which is already in memory and paused)
			testMenu.show();
			menuFlag = false;
			setCamera();
		}
        */
	}

	protected void update() {
		player.logic();
		//enemy.logic();
	}

	protected void render() {
		drawBackground();

		// Player logic and animation
		player.draw();
		//enemy.draw();

		// Platforms animation
		Platform.drawAll();
	}

	protected void poll() {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			exit();
        }

		if (Keyboard.isKeyDown(Keyboard.KEY_F10)) {
			menuFlag = true;
		}
    }

	protected void init() {
		// Modify projection Matrix
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 640, 0, 480, -1, 1);

		// Modify modelview matrix
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}

	private void drawBackground(){
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
		glVertex2d(0, 0 - CameraPosition.getPosition_y());
		glVertex2d(640, 0 - CameraPosition.getPosition_y());

		glVertex2d(640, 32 - CameraPosition.getPosition_y());
		glVertex2d(0, 32 - CameraPosition.getPosition_y());

		glEnd();

		// Grass

		glBegin(GL_QUADS);

		glColor3d(0.2, 0.8, 0.2);
		glVertex2d(0, 25 - CameraPosition.getPosition_y());
		glVertex2d(640, 25 - CameraPosition.getPosition_y());

		glVertex2d(640, 32 - CameraPosition.getPosition_y());
		glVertex2d(0, 32 - CameraPosition.getPosition_y());

		glEnd();

	}

}
