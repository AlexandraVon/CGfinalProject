
public class Earth extends abstractModel {
	private float rotSpeed;
	
	public Earth(String filepath, float speed){
		super(filepath);
		this.rotSpeed = speed;
		this.setRot(0f, 0f, 0f);
	}
	
	//self rotation
	public void update(){
		if (this.rotSpeed>360) this.rotSpeed-=360;
		this.setRot(this.getRotX(), this.getRotY()+this.rotSpeed, this.getRotZ());
	}
	
	@Override
	public void draw(){
		this.update();
		super.draw();
	}
}
