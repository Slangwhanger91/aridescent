package gameGL;

public final class CameraPosition {

	private static double position_x, position_y;
	private static Player player;

	public static void init(Player _player){
		player = _player;
		position_x = _player.getX() -300; 
		position_y = _player.getY() -200;
	}

	public static double getPosition_x() { return position_x; }
	public static double getPosition_y() { return position_y; }

	public static void addX(double x){
		if(LimitCameraX()){
			position_x += x;
		}	
	}

	public static void addY(double y){
		if(LimitCameraY()){
			position_y += y;
		}
	}

	private static boolean LimitCameraX(){
		//System.out.println((position_x - player.getX()) +", "+(position_x - player.getX()));
		return position_x - player.getX() > -200 
				|| position_x - player.getX() < -400; 
	}

	private static boolean LimitCameraY(){
		//System.out.println((position_y - player.getY()));
		return position_y - player.getY() > -100 
				|| position_y - player.getY() < -300;
	}

}
