import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;


public class Explosion extends abstractModel{
	private ArrayList<Texture> textureSet = new ArrayList<Texture>();
	private Texture curTexture;
	private int counter = 0;
	private int freq = 5;//5 frames between each explosion effect
	private boolean delete = false;
	public Explosion(float x, float y, float z, ArrayList<String> paths){
		this.setPos(x, y, z);
		this.setRot(0f, 0f, 0f);
		this.setScale(1f*z/Helper.EARTH_DEPTH);
		for (String path : paths){
			this.addTexture(path);
		}
	}
	
	@Override
	public int buildList(){
		int curList = GL11.glGenLists(1);
		GL11.glNewList(curList, GL11.GL_COMPILE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, curTexture.getTextureID());
		GL11.glPushMatrix();

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glNormal3f( 0.0f, 0.0f, 1.0f); 
		GL11.glTexCoord2f(0,0);
		GL11.glVertex2f(-1,-1);
		GL11.glNormal3f( 0.0f, 0.0f, 1.0f); 
		GL11.glTexCoord2f(1,0);
		GL11.glVertex2f(1,-1);
		GL11.glNormal3f( 0.0f, 0.0f, 1.0f); 
		GL11.glTexCoord2f(1,1);
		GL11.glVertex2f(1,1);
		GL11.glNormal3f( 0.0f, 0.0f, 1.0f); 
		GL11.glTexCoord2f(0,1);
		GL11.glVertex2f(-1,1);

		GL11.glEnd();

		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEndList();
		return curList;
	}
	
	@Override
	public void draw(){
		this.update();
		super.draw();
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
	
	public void update(){
		counter++;
		if(counter/freq<textureSet.size()){
			curTexture = textureSet.get(counter/freq);
			this.displaylist = buildList();
		}else{
			this.displaylist = -1;
			this.delete = true;
		}
	}
	
	public boolean getDeleteStatus(){
		return this.delete;
	}
}
