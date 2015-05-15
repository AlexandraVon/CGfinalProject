import java.util.ArrayList;

import org.lwjgl.opengl.GL11;


public class Alien extends abstractModel{
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
	private float time=0;
	
	ArrayList<Bullet> bulletSet = new ArrayList<Bullet>();
	
	public Alien(String filepath, float spd, float rot_x, float rot_y, float rot_z){
		super(filepath);
		this.speed_z = spd;
		this.pos_rot_x = rot_x;
		this.pos_rot_y = rot_y;
		this.pos_rot_z = rot_z;
		//randomly generate the appearing point of the alien
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
	public Alien(){}
	
	@Override
	public void draw(){
		this.update();
		
	
		for (float x = -0.2f; x<=0.2f; x+=0.1f){
			for (float y = 0f; y <= 0.2f; y+=0.1f){
				Bullet bullet;
		
					bullet = new Bullet(this.getX()+x,this.getY()-0.5f+y);
					bullet.setPos(this.getX()+x, this.getY()-0.5f+y, this.getZ());
			       // bulletSet.add(bullet);	
			
			}
		}
		for(Bullet bullet : bulletSet){
			
				//bullet.draw();
		}
		
		super.draw();
		cleanBullet();
	}
	
	public void cleanBullet(){
		for (int i=0; i<bulletSet.size(); i++){
			if(bulletSet.get(i).getZ()<=1150f) bulletSet.remove(i);
		}
	}
	public void update(){
		time=time+1;
		//toward to the earth
		this.setPos(this.getX()+time*this.speed_x, this.getY()+time*this.speed_y, this.getZ()+time*this.speed_z);
	}
}
