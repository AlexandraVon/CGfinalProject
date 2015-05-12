import org.lwjgl.opengl.GL11;


public class Bullet extends abstractModel{
	public Bullet(float x_step, float y_step){
		//shooting position
		this.setPos(Helper.PLAYER_X+x_step, Helper.PLAYER_Y+y_step, Helper.PLAYER_DEPTH-0.2f);
		this.setRot(0f, 0f, 0f);
		this.setScale(1f);
		this.displaylist = buildList();
	}
	
	@Override
	public int buildList(){
		//display list for a red bullet
		int curList = GL11.glGenLists(1);
		GL11.glNewList(curList, GL11.GL_COMPILE);
		
		GL11.glColor3f(1f, 0f, 0f);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(-0.01f,-0.01f);
		GL11.glVertex2f(0.01f,-0.01f);
		GL11.glVertex2f(0.01f,0.01f);
		GL11.glVertex2f(-0.01f,0.01f);
		GL11.glEnd();
		
		GL11.glEndList();

		return curList;
	}
	
	@Override
	public void draw(){
		this.update();
		GL11.glDisable(GL11.GL_LIGHTING);
		super.draw();
		GL11.glEnable(GL11.GL_LIGHTING);
	}
	
	public void update(){
		//fly against z-axis
		this.setPos(this.getX(), this.getY(), this.getZ()-Helper.BULLET_INIT_SPEED*(this.getZ()/Helper.PLAYER_DEPTH));
	}
}
