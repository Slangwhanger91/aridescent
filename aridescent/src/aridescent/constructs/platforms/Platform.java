package aridescent.constructs.platforms;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glVertex2d;
//import aridescent.engine.CameraPosition;

import aridescent.engine.CameraPosition;

import java.util.ArrayList;

public abstract class Platform {

	/**top left corner*/
	protected double x = 0, y = 0;
	private static final int MAX_AMOUNT_OF_PLATFORMS = 30;
	
	protected double width, height;
	protected static ArrayList<Platform> all_platforms = 
			new ArrayList<>();

	/**Activate platform effects.*/
	public abstract void logic();
	protected abstract void pickColor();
	
	/**<b>x</b> and <b>y</b> will point at the top left corner
	 * of a platform*/
	public void init(double _x, double _y, double _width, double _height){
		x = _x; 
		y = _y;
		width = _width;
		height = _height;
		all_platforms.add(this);
	}
	
	public static void createMaxPlatforms(){
		int platform_type;
		double location_x, location_y;
		for (int i = 0; i < MAX_AMOUNT_OF_PLATFORMS; i++) {
			//platform_type = (int)(Math.random() * 6);
			location_x = Math.random();
			location_y = Math.random();
			// TODO make proper shiet
			new GreenPlatform(location_x*1500, location_y*1000);
			// TODO use this code eventually to randomize aridescent.constructs.platforms
			/*switch(platform_type){
			case 0: // GREEN
				new GreenPlatform(location_x*2000, location_y*1000);
				break;
			case 1: // RED
				new GreenPlatform(location_x*1000, location_y*1000);
				break;
			case 2: // ORANGE
				new GreenPlatform(location_x*1000, location_y*1000);
				break;
			case 3: // YELLOW
				new GreenPlatform(location_x*1000, location_y*1000);
				break;
			case 4: // PURPLE
				new GreenPlatform(location_x*1000, location_y*1000);
				break;
			case 5: // BLUE
				new GreenPlatform(location_x*1000, location_y*1000);
				break;
			}*/
		}
	}
	
	public double getX(){ return x;}
	public double getY(){ return y;}

	/**returns a Platform to stand on or null to keep falling through.*/
	public static Platform findGround(double _x, double _y, double _width){
		for (Platform P : all_platforms) {
			if(_x+_width > P.x && _x < P.x+P.width){
				if(_y <= P.y && _y > P.y-P.height/2){
					return P;
				}
			}
		}

		return null;
	}

	public static void drawAll(){
		for (Platform P : all_platforms) {
			P.draw();
		}
	}
	
	private void draw(){
		// 
		glPushMatrix();

		// Moves to the given x,y position on Display
		glTranslated(x - CameraPosition.getPosition_x(), 
				y - CameraPosition.getPosition_y(), 0);
		//System.out.println(CameraPosition.getPosition_x());
		
		// Color and shape size
		glBegin(GL_QUADS);

		pickColor();
		glVertex2d(0, -height); //bottom left
		glVertex2d(width, -height); //bottom right
		pickColor();
		glVertex2d(width, 0); //top right
		glVertex2d(0, 0); //top left

		glEnd();

		// 
		glPopMatrix();
	}
}
