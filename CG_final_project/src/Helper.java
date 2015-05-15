
public class Helper {
	public static String GAME_NAME = "Earth Defence";
	public static int LIVES = 5;
	public static int LEVELS = 2;
	public static int LEVEL_ONE = 5;
	public static int LEVEL_TWO = 10;
	public static int SCREEN_WIDTH = 800;
	public static int SCREEN_HEIGHT = 600;
	public static int SCREEN_DEPTH = 1300;
	public static float EARTH_X = 0f;
	public static float EARTH_Y = 0f;
	public static float EARTH_DEPTH = 1198f;
	public static float EARTH_ROT_SPEED = 0.3f;
	public static float THIRD_VIEW_OFFSET = 1.2f;
	public static float PLAYER_X = 0f;
	public static float PLAYER_Y = 1f;
	public static float PLAYER_DEPTH = 1200f;
	public static float PLAYER_STEP = 0.01f;
	public static float PLAYER_MOVE = 10f;
	public static float BULLET_INIT_SPEED = 0.1f;
	public static float BULLET_VANISH_POINT = 1150f;
	
	public static float distance(float x1, float y1, float z1, float x2, float y2, float z2){
		float dist_sqr = (float)Math.pow((x1-x2), 2) + (float)Math.pow((y1-y2), 2) + (float)Math.pow((z1-z2), 2);
		float dist = (float)Math.sqrt(dist_sqr);
		return dist;
	}
}
