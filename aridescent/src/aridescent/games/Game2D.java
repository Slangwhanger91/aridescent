package aridescent.games;

import aridescent.engine.CameraPosition;
import aridescent.engine.Game;
import aridescent.engine.Menu;
import aridescent.engine.Player;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

import aridescent.constructs.platforms.*;
import org.newdawn.slick.opengl.TextureImpl;

import static org.lwjgl.opengl.GL11.*;

public class Game2D extends Game {
	
	private Player player;

	public static void main(String[] args) throws Exception {
		try {
			new Game2D(640, 480).run();
		} catch (LWJGLException le) {
			le.printStackTrace();
		}
	}

	protected Game2D(int width, int height) throws LWJGLException {
		super(width, height);

		player = new Player();
		Platform.createMaxPlatforms();
		//Enemy enemy = new Enemy();

		Menu testMenu = new Menu(this);
		setMenu(testMenu);
		showMenu();
		CameraPosition.init(player);
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
			showMenu();
		}
    }

	protected void init() {
		// Modify projection Matrix
		glMatrixMode(GL_PROJECTION);
		//glLoadIdentity();
		glOrtho(0, 640, 0, 480, -1, 1);

		// Modify modelview matrix
		glMatrixMode(GL_MODELVIEW);
		//glLoadIdentity();
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

		glPushMatrix();
		glTranslatef(0f, -(float)CameraPosition.getPosition_y(), 0f);
		glBegin(GL_QUADS);

		glColor3d(0.6, 0.2, 0.1);
		glVertex2d(0, 0);
		glVertex2d(640, 0);

		glVertex2d(640, 32);
		glVertex2d(0, 32);

		// Grass
		glColor3d(0.2, 0.8, 0.2);
		glVertex2d(0, 25);
		glVertex2d(640, 25);

		glVertex2d(640, 32);
		glVertex2d(0, 32);

		glEnd();

		glPopMatrix();

	}

}
