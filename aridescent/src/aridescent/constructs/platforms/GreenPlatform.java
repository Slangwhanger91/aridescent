package aridescent.constructs.platforms;

import static org.lwjgl.opengl.GL11.glColor3d;

/**Character may jump through and land on this platform*/
public class GreenPlatform extends Platform {
	
	static int color_index = 0;
	
	public GreenPlatform(double x, double y) {
		init(x, y, 80, 16);
		
	}

	
	@Override
	protected void pickColor() {
		switch(color_index){
		case 0:
			glColor3d(1, 0.1, 0.1);
			break;
		case 1:
			glColor3d(0, 1, 0);
			break;
		}
		color_index = (color_index+1) % 2;
	}


	@Override
	public void logic() {
		// TODO Auto-generated method stub	
	}
	
}
