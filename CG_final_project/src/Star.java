
import org.lwjgl.opengl.GL11;

public class Star {
	int displaylist = 0;
	public Star(int amount){
		int curList = GL11.glGenLists(1);
		GL11.glNewList(curList, GL11.GL_COMPILE);
		for (int i=0; i<amount; i++){
			float x = ((float)Math.random()*2*(float)Helper.SCREEN_WIDTH-(float)Helper.SCREEN_WIDTH)*1.5f;
			float y = ((float)Math.random()*2*(float)Helper.SCREEN_HEIGHT-(float)Helper.SCREEN_HEIGHT)*1.5f;
			float size = (float)Math.random()*2f;
			float intensity = 0.5f+(float)Math.random()*0.5f;
			GL11.glPointSize(size);
			GL11.glColor3f(intensity, intensity, intensity);
	        GL11.glBegin(GL11.GL_POINTS);
	        GL11.glVertex2f(x,y);
	        GL11.glEnd();
	        GL11.glFlush();
		}
		GL11.glEndList();
		this.displaylist = curList;
	}
	
	public void draw(){
		GL11.glPushMatrix();
		GL11.glCallList(this.displaylist);
		GL11.glPopMatrix();
	}
}
