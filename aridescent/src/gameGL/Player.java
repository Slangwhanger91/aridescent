package gameGL;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex2d;

import org.lwjgl.input.Keyboard;

public class Player {

	private double x, y, xspeed, yspeed;
	private boolean jumpPressed, jumpWasPressed;
	private int jumpsRemaining;

	public Player(){

		x = 100;
		y = 100;
		xspeed = 0;
		yspeed = 0;
	}

	public void logic(){
		x += xspeed;
		y += yspeed;

		yspeed -= 0.4;
		// Break falling velocity and correct to actual ground level
		if(y <= 32){ // if on ground
			yspeed = 0;
			y = 32;
			jumpsRemaining = 2;

			// Friction
			if(!Keyboard.isKeyDown(Keyboard.KEY_LEFT) && xspeed < 0) xspeed = xspeed*0.9;
			if(!Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && xspeed > 0) xspeed = xspeed*0.9;
		}

		// Jump
		if(jumpPressed && !jumpWasPressed && jumpsRemaining-- > 0) yspeed = 7;

		// Gain momentum with held movement
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) xspeed = Math.max(-5, xspeed-1);
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) xspeed = Math.min(5, xspeed+1);

		jumpWasPressed = jumpPressed;
		jumpPressed = Keyboard.isKeyDown(Keyboard.KEY_UP);
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

		glColor3d(0, 1, 0);
		glVertex2d(8, 0); //bottom right

		glColor3d(0, 0, 1);
		glVertex2d(8, 16); //top right

		glColor3d(1, 1, 0);
		glVertex2d(-8, 16); //top left

		glEnd();

		// 
		glPopMatrix();
	}
}
