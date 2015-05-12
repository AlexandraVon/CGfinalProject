
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;



public class Main {
	private boolean quit = false;
	private UnicodeFont words;
	
	private int lives = Helper.LIVES;
	private boolean game_over = false;
	private boolean win = false;
	private int level = 1;
	
	private int[] level_enemies_set = new int[]{Helper.LEVEL_ONE, Helper.LEVEL_TWO};
	private int cur_enemies = level_enemies_set[level-1];
	
	private boolean third_view = false;
	
	private boolean player_to_left = false;
	private boolean player_to_right = false;
	private boolean player_to_up = false;
	private boolean player_to_down = false;
	
	//all models in the scene, for collision detection
	ArrayList<Planet> planetSet = new ArrayList<Planet>();
	ArrayList<Bullet> bulletSet = new ArrayList<Bullet>();
	ArrayList<UFO> UFOset = new ArrayList<UFO>();
	ArrayList<Explosion> explosionSet = new ArrayList<Explosion>();
	
	Player player;
	Star star;
	Earth earth;
	
	private float specular_Shine = 60f;
	
	private float light_Ambient[] = { 0.25f, 0.25f, 0.32f, 1.0f }; 
	private float light_Diffuse[] = { 0.25f, 0.25f, 0.25f, 1.0f };
	private float light_Position[] = { 0.0f, 0.0f, 1400.0f, 1.0f };
	
	private float player_x = 0;
	private float player_y = 0;
	
	private int ufo_ship_freq = 300;
	private int ufo_ship_frame = 200;
	
	String fontPath = "res/adventure.ttf";
	
	//start the game: initialization and call main loop
	public void start() {
		//Display initialization
		try {
            Display.setDisplayMode(new DisplayMode(Helper.SCREEN_WIDTH, Helper.SCREEN_HEIGHT));
            Display.setVSyncEnabled(true);
            Display.setTitle(Helper.GAME_NAME);
            Display.create();
        } catch (LWJGLException e) {
            Sys.alert("Error", "Initialization failed!\n" + e.getMessage());
            System.exit(0);
        }
		//GL initialization
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
		GLU.gluPerspective (60.0f,(float)Helper.SCREEN_WIDTH/(float)Helper.SCREEN_HEIGHT, 1f, 1500.0f);//view angle,ratio,nearZ,farZ
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LESS);
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_NORMALIZE); 

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		
		//enable lighting
		GL11.glEnable(GL11.GL_LIGHTING);
		
		ByteBuffer temp = ByteBuffer.allocateDirect(16);
		temp.order(ByteOrder.nativeOrder());
		
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, (FloatBuffer)temp.asFloatBuffer().put(light_Diffuse).flip());
		GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS,(int)specular_Shine);
		
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, (FloatBuffer)temp.asFloatBuffer().put(light_Diffuse).flip());
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION,(FloatBuffer)temp.asFloatBuffer().put(light_Position).flip()); 
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT,(FloatBuffer)temp.asFloatBuffer().put(light_Ambient).flip());

		GL11.glLightf(GL11.GL_LIGHT1, GL11.GL_CONSTANT_ATTENUATION, 0.0f);
		GL11.glLightf(GL11.GL_LIGHT1, GL11.GL_LINEAR_ATTENUATION, 0.00009f);
		GL11.glLightf(GL11.GL_LIGHT1, GL11.GL_QUADRATIC_ATTENUATION, 0.0f);
 
		GL11.glEnable(GL11.GL_LIGHT1);
		
		GL11.glLightModeli(GL12.GL_LIGHT_MODEL_COLOR_CONTROL, GL12.GL_SEPARATE_SPECULAR_COLOR);
		GL11.glColorMaterial(GL11.GL_FRONT_AND_BACK,GL11.GL_AMBIENT_AND_DIFFUSE);
		
		try {
			//font
			words = new UnicodeFont(fontPath, 25, true, false);
			words.addAsciiGlyphs();
			words.addGlyphs(400, 600);
			words.getEffects().add(new ColorEffect(java.awt.Color.RED));
			words.loadGlyphs();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//initialize player
		player = new Player("res/astronaut.obj",0, 180, 0);
		player.setScale(0.35f);
		player.setPos(player_x, player_y, Helper.PLAYER_DEPTH);
		
		//initialize background stars
		star = new Star(1000);
		
		//initialize earth model
		earth = new Earth("res/earth.obj",Helper.EARTH_ROT_SPEED);
		earth.setPos(Helper.EARTH_X, Helper.EARTH_Y, Helper.EARTH_DEPTH);
		earth.setScale(2.1f);
		
		//initialize planet(s) model
		Planet moon = new Planet("res/moon.obj",0.1f,-3,2,-4);
		moon.setScale(1f);
		moon.setRot(0f, 0f, -10f);
		planetSet.add(moon);
		
		Planet alpha = new Planet("res/alpha.obj",1.2f,6,1,-8);
		alpha.setScale(0.6f);
		alpha.setRot(0f, 0f, 30f);
		planetSet.add(alpha);
		
		Planet beta = new Planet("res/beta.obj",0.8f,2,6,-18);
		beta.setScale(1.6f);
		beta.setRot(0f, 0f, -45f);
		planetSet.add(beta);
		
		Planet jupiter = new Planet("res/jupiter.obj",0.3f,-1,5,-20);
		jupiter.setScale(1.2f);
		beta.setRot(0f, 0f, 40f);
		planetSet.add(jupiter);
		
		render();

		Display.destroy();
	}
	
	private void render(){
		if(cur_enemies==0){
			level++;
			cur_enemies = level_enemies_set[level-1];
		}
		if(game_over){
			try {
				//font
				words = new UnicodeFont(fontPath, 35, true, false);
				words.addAsciiGlyphs();
				words.addGlyphs(400, 600);
				words.getEffects().add(new ColorEffect(java.awt.Color.RED));
				words.loadGlyphs();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		while (!Display.isCloseRequested() && !quit) {
			// set up the projection matrix
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GLU.gluPerspective (60.0f,(float)Helper.SCREEN_WIDTH/(float)Helper.SCREEN_HEIGHT, 1f, Helper.SCREEN_DEPTH);
			//position the camera
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			
			//view point
			if(third_view){
				GLU.gluLookAt(Helper.PLAYER_X+player_x, Helper.PLAYER_Y+player_y, Helper.PLAYER_DEPTH+Helper.THIRD_VIEW_OFFSET, 0f, 0.5f, -1f, 0f, 1f, 0f);
			}else{
				GLU.gluLookAt(Helper.PLAYER_X+player_x, Helper.PLAYER_Y+player_y, Helper.PLAYER_DEPTH, 0f, 0.5f, -1f, 0f, 1f, 0f);
			}
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);	
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.5f);
			GL11.glDepthMask(true);
			
			if(player_to_left) player_to_left = !player_to_left;
			else if(player_to_right) player_to_right = !player_to_right;
			else if(player_to_up) player_to_up = !player_to_up;
			else if(player_to_down) player_to_down = !player_to_down;
			else{
				player.setRot(0f, 0f, 0f);
			}
			
			cleanBullet();
			
			if(!game_over) earth.draw();
			
			if(third_view){
				player.setPos(player_x, player_y-0.5f, Helper.EARTH_DEPTH+1.5f);
				player.draw();
			}
			
			for(Planet planet : planetSet){
				planet.draw();
			}
			
			//level 1 enemies
			if(level==1 && ufo_ship_frame==ufo_ship_freq && level_enemies_set[level-1]>0){
				ufo_ship_frame = 0;
				UFO ship_ufo = new UFO("res/spaceship.obj",0.05f, -20, 180, 0);
				ship_ufo.setScale(0.2f);
				UFOset.add(ship_ufo);
				level_enemies_set[level-1] -= 1;
			}
			else if(level==1){
				ufo_ship_frame++;
			}
			
			for(UFO ufo : UFOset){
				ufo.draw();
			}
			
			for(Bullet bullet : bulletSet){
				bullet.draw();
			}
			
			for(Explosion explosion : explosionSet){
				explosion.draw();
			}
			
			displayStatus();
			
			cleanExplosion();
			
			pollInput();
			
			collisionDetection();
			
			Display.update();
		}
	}
	
	public void pollInput() {
		//player position
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && player_x>-5f){
			player_x-=Helper.PLAYER_STEP;
			player.setRot(0f, -Helper.PLAYER_MOVE, 0f);
			player_to_left = true;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && player_x<5f){
			player_x+=Helper.PLAYER_STEP;
			player.setRot(0f, Helper.PLAYER_MOVE, 0f);
			player_to_right = true;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP) && player_y<5f){
			player_y+=Helper.PLAYER_STEP;
			player.setRot(0f, 0f, Helper.PLAYER_MOVE);
			player_to_up = true;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) && player_y>0f){
			player_y-=Helper.PLAYER_STEP;
			player.setRot(0f, 0f, -Helper.PLAYER_MOVE);
			player_to_down = true;
		}
		
		
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
				{
					quit=true;
				}
				if(Keyboard.getEventKey() == Keyboard.KEY_RSHIFT || Keyboard.getEventKey() == Keyboard.KEY_LSHIFT)
				{
					third_view = !third_view;
				}
				if(Keyboard.getEventKey() == Keyboard.KEY_SPACE && !game_over)
				{
					//SPACE for shooting
					for (float x = -0.4f; x<=0.4f; x+=0.1f){
						for (float y = 0f; y <= 0.2f; y+=0.1f){
							Bullet bullet;
							if(third_view){
								bullet = new Bullet(player_x+x,player_y-0.5f+y);
							}else{
								bullet = new Bullet(player_x+x,player_y+y);
							}
							bulletSet.add(bullet);
						}
					}
				}
			}
		}
	}
	
	public void cleanBullet(){
		for (int i=0; i<bulletSet.size(); i++){
			if(bulletSet.get(i).getZ()<=Helper.BULLET_VANISH_POINT) bulletSet.remove(i);
		}
	}
	
	public void cleanExplosion(){
		for(int i=0; i<explosionSet.size(); i++){
			if(explosionSet.get(i).getDeleteStatus()) explosionSet.remove(i);
		}
	}
	
	public void collisionDetection(){
		ArrayList<Bullet> bullet_to_remove = new ArrayList<Bullet>();
		ArrayList<UFO> ufo_to_remove = new ArrayList<UFO>();
		ArrayList<Planet> planet_to_remove = new ArrayList<Planet>();
		
		for (int i = 0; i<bulletSet.size(); i++){
			Bullet bullet = bulletSet.get(i);
			//check for bullet-ufo collision
			for (int j=0; j<UFOset.size(); j++){
				UFO ufo = UFOset.get(j);
				float bullet_dist = Helper.distance(bullet.getX(), bullet.getY(), bullet.getZ(), ufo.getX(), ufo.getY(), ufo.getZ());
				if(bullet_dist<ufo.getScale()){
					ArrayList<String> explosionTextures = new ArrayList<String>();
					explosionTextures.add("res/exp_1_1.png");
					explosionTextures.add("res/exp_1_2.png");
					explosionTextures.add("res/exp_1_3.png");
					explosionTextures.add("res/exp_1_4.png");
					Explosion explosion = new Explosion(bullet.getX(), bullet.getY(), bullet.getZ(),explosionTextures);
					explosionSet.add(explosion);
					bullet_to_remove.add(bullet);
					ufo_to_remove.add(ufo);
				}
			}
			//check for bullet-planet collision
			for(int j=0; j<planetSet.size(); j++){
				Planet planet = planetSet.get(j);
				float bullet_dist = Helper.distance(bullet.getX(), bullet.getY(), bullet.getZ(), planet.getX(), planet.getY(), planet.getZ());
				if(bullet_dist<planet.getScale()*0.5f){
					ArrayList<String> explosionTextures = new ArrayList<String>();
					explosionTextures.add("res/exp_2_1.png");
					explosionTextures.add("res/exp_2_2.png");
					explosionTextures.add("res/exp_2_3.png");
					Explosion explosion = new Explosion(bullet.getX(), bullet.getY(), bullet.getZ(),explosionTextures);
					explosionSet.add(explosion);
					bullet_to_remove.add(bullet);
					planet.setScale(planet.getScale()*0.99f);
					if(planet.getScale()<0.5f){
						explosionTextures = new ArrayList<String>();
						explosionTextures.add("res/exp_1_1.png");
						explosionTextures.add("res/exp_1_2.png");
						explosionTextures.add("res/exp_1_3.png");
						explosionTextures.add("res/exp_1_4.png");
						explosion = new Explosion(planet.getX(), planet.getY(), planet.getZ(), explosionTextures);
						explosionSet.add(explosion);
						planet_to_remove.add(planet);
					}
				}
			}
		}
		//check for ufo-planet collision
		for (int i=0; i<UFOset.size(); i++){
			UFO ufo = UFOset.get(i);
			if(ufo.getZ()>=Helper.EARTH_DEPTH && !game_over){
				ArrayList<String> explosionTextures = new ArrayList<String>();
				explosionTextures.add("res/exp_2_1.png");
				explosionTextures.add("res/exp_2_2.png");
				explosionTextures.add("res/exp_2_3.png");
				Explosion explosion = new Explosion(ufo.getX(), ufo.getY(), ufo.getZ(), explosionTextures);
				explosionSet.add(explosion);
				ufo_to_remove.add(ufo);
				lives --;
				if(lives==0){
					game_over = true;
					explosionTextures = new ArrayList<String>();
					explosionTextures.add("res/exp_2_1.png");
					explosionTextures.add("res/exp_2_2.png");
					explosionTextures.add("res/exp_2_3.png");
					explosion = new Explosion(ufo.getX(), ufo.getY(), ufo.getZ(), explosionTextures);
					explosion.setScale(2f);
					explosionSet.add(explosion);
					earth=null;
				}
			}
			else if(game_over && ufo.getZ()>=Helper.PLAYER_DEPTH){
				UFOset.remove(ufo);
			}
			for (int j=0; j<planetSet.size(); j++){
				Planet planet = planetSet.get(j);
				float planet_dist = Helper.distance(planet.getX(), planet.getY(), planet.getZ(), ufo.getX(), ufo.getY(), ufo.getZ());
				if(planet_dist<planet.getScale()*0.8f){
					ArrayList<String> explosionTextures = new ArrayList<String>();
					explosionTextures.add("res/exp_2_1.png");
					explosionTextures.add("res/exp_2_2.png");
					explosionTextures.add("res/exp_2_3.png");
					Explosion explosion = new Explosion(ufo.getX(), ufo.getY(), ufo.getZ(), explosionTextures);
					explosionSet.add(explosion);
					ufo_to_remove.add(ufo);
					planet.setScale(planet.getScale()*0.9f);
					if(planet.getScale()<0.5f){
						explosionTextures = new ArrayList<String>();
						explosionTextures.add("res/exp_1_1.png");
						explosionTextures.add("res/exp_1_2.png");
						explosionTextures.add("res/exp_1_3.png");
						explosionTextures.add("res/exp_1_4.png");
						explosion = new Explosion(ufo.getX(), ufo.getY(), ufo.getZ(), explosionTextures);
						explosionSet.add(explosion);
						planet_to_remove.add(planet);
					}
				}
			}
		}
		for (Bullet bullet:bullet_to_remove){
			bulletSet.remove(bullet);
		}
		for(UFO ufo:ufo_to_remove){
			boolean hasObj = UFOset.remove(ufo);
			if(hasObj) cur_enemies--;
			if(cur_enemies==0){
//				win=true;
				level++;
				cur_enemies = level_enemies_set[level-1];
			}
		}
		for(Planet planet:planet_to_remove){
			planetSet.remove(planet);
		}
	}
	
	public void displayStatus(){
				star.draw();
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glDisable(GL11.GL_LIGHTING);

				//change the projection matrix to ortographic
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glLoadIdentity();
				GL11.glOrtho(0, Helper.SCREEN_WIDTH, 0, Helper.SCREEN_HEIGHT, 1, -1);

				//back to modelview
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glLoadIdentity();
				
				if(win){
					String string = "YOU WIN!";
					GL11.glPushMatrix();
					GL11.glTranslatef(Helper.SCREEN_WIDTH/2-words.getWidth(string)/2, Helper.SCREEN_HEIGHT/2+30f, 0f);
					GL11.glScalef(1.0f, -1.0f, 1.0f);
					words.drawString(0, 0,  string);
					GL11.glPopMatrix();
				}
				else if(!game_over){
				//lives left string
				String string = "Lives left: " + lives;
				GL11.glPushMatrix();
				GL11.glTranslatef(5f, Helper.SCREEN_HEIGHT-5f, 0f);
				GL11.glScalef(1.0f, -1.0f, 1.0f);
				words.drawString(0, 0,  string);
				GL11.glPopMatrix();
				
				//level status
				string = "LEVEL: " + level;
				GL11.glPushMatrix();
				GL11.glTranslatef(Helper.SCREEN_WIDTH/2-words.getWidth(string)/2, Helper.SCREEN_HEIGHT-5f, 0f);
				GL11.glScalef(1.0f, -1.0f, 1.0f);
				words.drawString(0, 0,  string);
				GL11.glPopMatrix();
				
				//enemies left
				string = "Enemies: " + cur_enemies;
				GL11.glPushMatrix();
				GL11.glTranslatef(Helper.SCREEN_WIDTH-words.getWidth(string)-5f, Helper.SCREEN_HEIGHT-5f, 0f);
				GL11.glScalef(1.0f, -1.0f, 1.0f);
				words.drawString(0, 0,  string);
				GL11.glPopMatrix();
				}
				
				//game over
				else{
				String string = "GAME OVER";
				GL11.glPushMatrix();
				GL11.glTranslatef(Helper.SCREEN_WIDTH/2-words.getWidth(string)/2, Helper.SCREEN_HEIGHT/2+30f, 0f);
				GL11.glScalef(1.0f, -1.0f, 1.0f);
				words.drawString(0, 0,  string);
				GL11.glPopMatrix();
				}
				
				GL11.glEnable(GL11.GL_LIGHTING);
	}
	
	public static void main(String[] argv) {
		Main main = new Main();
		main.start();
	}
}
