import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;



public class Lander extends abstractModel {
	private float rotSpeed;
	private ArrayList<Texture> textureSet = new ArrayList<Texture>();
	private Texture curTexture;
	
	public Lander(String filepath, float speed){
		super(filepath);
		this.rotSpeed = speed;
		this.setRot(0f, 0f, 0f);
	}
	
	// rotation
	public void update(){
		if (this.rotSpeed>360) this.rotSpeed-=360;
		this.setRot(this.getRotX(), this.getRotY()+this.rotSpeed, this.getRotZ());
	}
	
	@Override
	public void draw(){
		this.update();
		super.draw();
		/*
		  GL11.glBegin(GL11.GL_TRIANGLES); // Start Drawing The Cube
	        
	        GL11.glColor3f(0.0f, 1.0f, 0f); // Set The Color To Red
	        
	        GL11.glVertex3f(this.getX(), this.getY(), this.getZ()); // Top Right Of The Quad (Front)
	        GL11.glVertex3f(this.getX()-1, this.getY()-1, this.getZ()); // Top Left Of The Quad (Front)
	        GL11.glVertex3f(this.getX()+1, this.getY()-1, this.getZ()); // Bottom Left Of The Quad (Front)
	

	        GL11.glEnd(); // Done Drawing The Quad
		*/
	}
	
	public void addTexture(String filepath){
		try {
		Texture texture = TextureLoader.getTexture("PNG", new FileInputStream(new File(filepath)));
		this.textureSet.add(texture);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
