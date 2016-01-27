package gameGL;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

import platforms.*;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;
  

public class GameWindow extends Game {
	
	private Player player;

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
		renderText();
		glDisable(GL_BLEND);
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
		glLoadIdentity();
		glOrtho(0, 640, 480, 1, -1, 1); //FIXME 0, 640, 0, 480, -1, 1 for normal view

		// Modify modelview matrix
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		initText();
	}

	//=================================================================================
	TrueTypeFont font;
	TrueTypeFont font2;
	 
	public void initText() {
	    // load a default java font
	    Font awtFont = new Font("Times New Roman", Font.BOLD, 24);
	    font = new TrueTypeFont(awtFont, false);
	         
	    try {
	        //InputStream inputStream = ResourceLoader.getResourceAsStream("/res/myfont.ttf");
	         
	        Font awtFont2 = Font.decode("Arial");
	        awtFont2 = awtFont2.deriveFont(24f); // set font size
	        font2 = new TrueTypeFont(awtFont2, false);
	             
	    } catch (Exception e) {
	        e.printStackTrace();
	    }   
	}
	
	public void renderText() {
		glEnable(GL_BLEND);
	    font.drawString(100, 50, "THE LIGHTWEIGHT JAVA GAMES LIBRARY", Color.yellow);
	    font2.drawString(100, 100, "NICE LOOKING FONTS!", Color.green);
	}
	//=================================================================================
	
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
