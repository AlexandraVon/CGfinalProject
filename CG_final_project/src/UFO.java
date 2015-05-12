import org.lwjgl.opengl.GL11;


public class UFO extends abstractModel{
	//each UFO is targeting at the earth and fly from some random place in the space
	private float speed_x;
	private float speed_y;
	private float speed_z;
	private float pos_rot_x;
	private float pos_rot_y;
	private float pos_rot_z;
	private float time_to_win;
	private float margin_x = 0f;
	private float margin_y = 1f;
	private float angle;
	public UFO(String filepath, float spd, float rot_x, float rot_y, float rot_z){
		super(filepath);
		this.speed_z = spd;
		this.pos_rot_x = rot_x;
		this.pos_rot_y = rot_y;
		this.pos_rot_z = rot_z;
		//randomly generate the appearing point of the UFO
		float x = ((float)Math.random()*8f-4f)*10;
		if (x>0) margin_x = 1*(float)Math.random();
		else margin_x = -1*(float)Math.random();
		float y = 20f+(float)Math.random()*3f;
		float z = Helper.BULLET_VANISH_POINT;
		this.setPos(x, y, z);
		this.time_to_win = (Helper.EARTH_DEPTH-z)/this.speed_z;
		this.speed_x = (Helper.EARTH_X+margin_x-x)/time_to_win;
		this.speed_y = (Helper.EARTH_Y+margin_y-y)/time_to_win;
		
		angle = (float)Math.toDegrees(Math.atan((double)(Helper.EARTH_X-x)/(double)(Helper.PLAYER_DEPTH-z)));
		this.setRot(this.pos_rot_x, this.pos_rot_y+angle, this.pos_rot_z);
	}
	public UFO(){}
	
	@Override
	public void draw(){
		this.update();
		super.draw();
	}
	
	public void update(){
		//toward to the earth
		this.setPos(this.getX()+this.speed_x, this.getY()+this.speed_y, this.getZ()+this.speed_z);
	}
}
