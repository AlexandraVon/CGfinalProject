
public class Planet extends Earth{

	public Planet(String filepath, float speed, float x_dist, float y_dist, float z_dist) {
		super(filepath, speed);
		this.setPos(Helper.EARTH_X+x_dist, Helper.EARTH_Y+y_dist, Helper.EARTH_DEPTH+z_dist);
	}
	
}
