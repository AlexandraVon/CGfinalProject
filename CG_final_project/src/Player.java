import org.lwjgl.opengl.GL11;


public class Player extends abstractModel{
	float pos_rot_x;
	float pos_rot_y;
	float pos_rot_z;
	public Player(String filepath, float rot_x, float rot_y, float rot_z){
		super(filepath);
		this.pos_rot_x = rot_x;
		this.pos_rot_y = rot_y;
		this.pos_rot_z = rot_z;
	}
	
	@Override
	public void draw(){
		GL11.glPushMatrix();
		
		//translate the model to its assigned position
		GL11.glTranslatef(this.getX(), this.getY(), this.getZ());
		//rotate the model in order: x,y,z
		GL11.glRotatef(this.getRotX()+this.pos_rot_z, 0f, 0f, 1f);
		GL11.glRotatef(this.getRotY()+this.pos_rot_y, 0f, 1f, 0f);
		GL11.glRotatef(this.getRotZ()+this.pos_rot_x, 1f, 0f, 0f);
		//scale the model to its assigned scale factor
		GL11.glScalef(this.getScale(),this.getScale(),this.getScale());
		
		//call the prepared display list
		GL11.glCallList(this.displaylist);
		
		GL11.glPopMatrix();
	}
}
