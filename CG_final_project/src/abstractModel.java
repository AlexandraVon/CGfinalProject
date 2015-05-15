import java.io.File;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class abstractModel {
	//model
	Model model = null;
	
	//position of the model
	private float pos_x;
	private float pos_y;
	private float pos_z;
	//scale of the model
	private float scale;
	//rotation
	private float rot_x;
	private float rot_y;
	private float rot_z;
	
	//display list
	int displaylist = 0;
	/*
	 * Constructors
	 */
	public abstractModel(String filepath){
		try {
			model = OBJLoader.loadModel(new File(filepath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		displaylist = this.buildList();
	}
	//constructor for copy the same model
	public abstractModel(){}
	
	//prepare the display list of the model
	public int buildList(){
		int curList = GL11.glGenLists(1);
		GL11.glNewList(curList, GL11.GL_COMPILE);
		int curTexture = -1;
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		Face face = null;
		for(int i=0;i<model.faces.size();i++)
		{
			face=model.faces.get(i);
			if(face.texture!=curTexture)
			{
				curTexture = face.texture;
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, curTexture);
			}

			GL11.glColor3f(1f, 1f, 1f);
			if(face.vertex.w>0f){
				GL11.glBegin(GL11.GL_QUADS);
			}else{
				GL11.glBegin(GL11.GL_TRIANGLES);
			}
			Vector3f n1 = model.normals.get((int) face.normal.x );
			GL11.glNormal3f(n1.x, n1.y, n1.z);
			Vector2f t1 = model.texVerticies.get((int) face.textures.x);
			GL11.glTexCoord2f(t1.x, t1.y);
			Vector3f v1 = model.verticies.get((int) face.vertex.x);
			GL11.glVertex3f(v1.x, v1.y, v1.z);
			
			Vector3f n2 = model.normals.get((int) face.normal.y);
			GL11.glNormal3f(n2.x, n2.y, n2.z);
			Vector2f t2 = model.texVerticies.get((int) face.textures.y);
			GL11.glTexCoord2f(t2.x, t2.y);
			Vector3f v2 = model.verticies.get((int) face.vertex.y);
			GL11.glVertex3f(v2.x, v2.y, v2.z);

			Vector3f n3 = model.normals.get((int) face.normal.z);
			GL11.glNormal3f(n3.x, n3.y, n3.z);
			Vector2f t3 = model.texVerticies.get((int) face.textures.z);
			GL11.glTexCoord2f(t3.x, t3.y);
			Vector3f v3 = model.verticies.get((int) face.vertex.z);
			GL11.glVertex3f(v3.x, v3.y, v3.z);
			
			if(face.vertex.w>0f){
				Vector3f n4 = model.normals.get((int) face.normal.w);
				GL11.glNormal3f(n4.x, n4.y, n4.z);
				Vector2f t4 = model.texVerticies.get((int) face.textures.w);
				GL11.glTexCoord2f(t4.x, t4.y);
				Vector3f v4 = model.verticies.get((int) face.vertex.w);
				GL11.glVertex3f(v4.x, v4.y, v4.z);
			}
			
			GL11.glEnd();
		}
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEndList();
		
		return curList;
	}
	
	//draw the model
	public void draw(){
		GL11.glPushMatrix();
		
		//translate the model to its assigned position
		GL11.glTranslatef(this.pos_x, this.pos_y, this.pos_z);
		//rotate the model in order: x,y,z
		GL11.glRotatef(rot_z, 0f, 0f, 1f);
		GL11.glRotatef(rot_y, 0f, 1f, 0f);
		GL11.glRotatef(rot_x, 1f, 0f, 0f);
		//scale the model to its assigned scale factor
		GL11.glScalef(this.scale,this.scale,this.scale);
		
		//call the prepared display list
		GL11.glCallList(this.displaylist);
		
		GL11.glPopMatrix();
	}
	
	//set position
	public void setPos(float x, float y, float z){
		this.pos_x = x;
		this.pos_y = y;
		this.pos_z = z;
	}
	//get position
	public float getX(){
		return this.pos_x;
	}
	public float getY(){
		return this.pos_y;
	}
	public float getZ(){
		return this.pos_z;
	}
	
	
	//set rotation
	public void setRot(float x, float y, float z){
		this.rot_x = x;
		this.rot_y = y;
		this.rot_z = z;
	}
	//get rotation
	public float getRotX(){
		return this.rot_x;
	}
	public float getRotY(){
		return this.rot_y;
	}
	public float getRotZ(){
		return this.rot_z;
	}
	
	//set scale
	public void setScale(float s){
		this.scale = s;
	}
	//get scale
	public float getScale(){
		return this.scale;
	}
}
