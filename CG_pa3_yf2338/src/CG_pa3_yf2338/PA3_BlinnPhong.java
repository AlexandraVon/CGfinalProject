package CG_pa3_yf2338;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

public class PA3_BlinnPhong {

    String windowTitle = "Blinn Phong";
    public boolean closeRequested = false;

    long lastFrameTime; // used to calculate delta
    
    float triangleAngle; // Angle of rotation for the triangles
    float quadAngle; // Angle of rotation for the quads

    float rot_x, rot_y, rot_z;
    float trans_x, trans_y, trans_z;
    
    int mode = 1;
    
    boolean flashed = false;
    float flash_x = 0.1f,flash_y = 0.1f,flash_z=0.1f;
    
    ShaderProgram shader;
    
    public void run() {

        createWindow();
        initGL();
        initShaders();
        
        while (!closeRequested) {
            pollInput();
            renderGL("mySphere.obj");

            Display.update();
        }
        
        cleanup();
    }
    
    public objparser loadModel(String fname){
    	objparser obj = null;
    	try {
			obj = objloader.loadmodel(new File(fname));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return obj;
    }
    
    private void initGL() {

        /* OpenGL */
        int width = Display.getDisplayMode().getWidth();
        int height = Display.getDisplayMode().getHeight();
        
        GL11.glViewport(0, 0, width, height); // Reset The Current Viewport
        GL11.glMatrixMode(GL11.GL_PROJECTION); // Select The Projection Matrix
        GL11.glLoadIdentity(); // Reset The Projection Matrix
        GLU.gluPerspective(45.0f, ((float) width / (float) height), 0.1f, 100.0f); // Calculate The Aspect Ratio Of The Window
        GL11.glMatrixMode(GL11.GL_MODELVIEW); // Select The Modelview Matrix
        GL11.glLoadIdentity(); // Reset The Modelview Matrix

        GL11.glShadeModel(GL11.GL_SMOOTH); // Enables Smooth Shading
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
        GL11.glClearDepth(1.0f); // Depth Buffer Setup
        GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
        GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Test To Do
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST); // Really Nice Perspective Calculations
               
    }
    
    private void initShaders() {
        String vertex_shader =
        		"uniform vec3 r;" +
        		"uniform int mode;"+
        				
				"const vec4 lightDir1 = vec4(8.0, 10.0, 5.0, 1.0);" +//world coords of the light
				"const vec4 lightDir2 = vec4(0.0, -10.0, 0.0, 1.0);" +
        		
        		"varying float lambertian1;" +
        		"varying float specular1;" +
        		
				"varying float lambertian2;" +
				"varying float specular2;" +
        		
        		"void main () {" +
        		"vec4 vertex_position = gl_ModelViewMatrix * gl_Vertex;" +//vertex
        		"vec3 normal = normalize(gl_NormalMatrix * gl_Normal);" +//unit normal vector
        		"vec4 light_position1 = lightDir1;" +//light source
        		"vec4 light_position2 = lightDir2;" +
        		
        		"vec3 light_vert1 = normalize(vec3(light_position1 - vertex_position));" +//incident direction unit vector
        		"vec3 light_refl1 = normalize(reflect(-light_vert1, normal));" +//reflection direction unit vector
        		"vec3 light_vert2 = normalize(vec3(light_position2 - vertex_position));" +
        		"vec3 light_refl2 = normalize(reflect(-light_vert2, normal));" +
        		"vec3 view_dir = normalize(vec3(-vertex_position));" +
        		
        		"lambertian1 = max(dot(light_vert1,normal), 0.0);" +
        		"lambertian2 = max(dot(light_vert2,normal), 0.0);" +
        		"specular1 = 0.0;" +
        		"specular2 = 0.0;" +
        		
        		"if(lambertian1 > 0.0) {" +
        		"vec3 halfDir1 = normalize(light_vert1 + view_dir);"+
				"float specAngle1 = max(dot(halfDir1, normal), 0.0);" +
        		"specular1 = pow(specAngle1, 6.0);" +
        		
        		"if(mode == 2) specular1 *= lambertian1;" +
        		"if(mode == 3) specular1 *= 0.0;" +
        		"}" +
        			
				"if(lambertian2 > 0.0) {" +
				"vec3 halfDir2 = normalize(light_vert2 + view_dir);"+
				"float specAngle2 = max(dot(halfDir2, normal), 0.0);" +
				"specular2 = pow(specAngle2, 6.0);" +
        		
				"if(mode == 2) specular2 *= lambertian2;" +
				"if(mode == 3) specular2 *= 0.0;" +
				"}" +
        		
        		"gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;" +
        		"}";
        
        String fragment_shader =
        		"uniform vec3 r;" +
        		"uniform int mode;" +
        		
				"varying float lambertian1;" +
				"varying float specular1;" +

				"varying float lambertian2;" +
				"varying float specular2;" +
				
				"const vec3 color = vec3(1.0, 1.0, 1.0);" +
				"const vec3 specColor1 = vec3(1.0, 1.0, 1.0);" +
				"const vec3 specColor2 = vec3(0.5, 0.5, 0.5);" +
				
        		
				"float rand(float n){return fract(sin(n) * 43758.5453123);}" +
        		
        		"void main () {" +
        		"vec3 diffuseColor = vec3(color.x*rand(r.x), color.y*rand(r.y), color.z*rand(r.z));" +
        		"vec4 fragcolor = vec4(lambertian1*diffuseColor + specular1*specColor1 + lambertian2*diffuseColor + specular2*specColor2, 1.0);"+
        		"gl_FragColor = fragcolor;" +
        		"}";

        try {
            shader = new ShaderProgram(vertex_shader, fragment_shader);
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void renderGL(String modelName) {
    	objparser obj = loadModel(modelName);
    	List<parserface> faces = obj.faces;

        // start to use shaders
        shader.begin();
        
        shader.setUniform3f("r", flash_x, flash_y, flash_z);
        shader.setUniformi("mode", mode);
        
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer
        GL11.glLoadIdentity(); // Reset The View
        GL11.glTranslatef(0.0f, 0.0f, -7.0f); // Move Right And Into The Screen

        if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) rot_y -= 4;
        if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) rot_y += 4;
        if(Keyboard.isKeyDown(Keyboard.KEY_UP)) rot_z += 4;
        if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) rot_z -= 4;
        
        if (Mouse.isInsideWindow() && Mouse.isButtonDown(0)) {
            float mouseDX = Mouse.getDX();
            float mouseDY = -Mouse.getDY();
            trans_x += mouseDX*0.01;
            trans_y -= mouseDY*0.01;
        }
        
        GL11.glRotatef(rot_x,1f,0f,0f);
        GL11.glRotatef(rot_y,0f,1f,0f);
        GL11.glRotatef(rot_z,0f,0f,1f);
        
        GL11.glTranslatef(trans_x,trans_y,trans_z);
        
        for(int i=0; i<faces.size(); i++){
        	parserface face = faces.get(i);
        	
        	GL11.glBegin(GL11.GL_TRIANGLES);
        	
//        	Vector3f n1 = obj.normals.get((int) face.normal.x-1);
//        	GL11.glNormal3f(n1.x, n1.y, n1.z);
        	
        	Vector3f v1 = obj.vertices.get((int) face.vertex.x-1);
        	GL11.glNormal3f(v1.x, v1.y, v1.z);
        	GL11.glVertex3f(v1.x,v1.y,v1.z);
        	
        	Vector3f v2 = obj.vertices.get((int) face.vertex.y-1);
        	GL11.glNormal3f(v2.x, v2.y, v2.z);
        	GL11.glVertex3f(v2.x,v2.y,v2.z);
        	
        	Vector3f v3 = obj.vertices.get((int) face.vertex.z-1);
        	GL11.glNormal3f(v3.x, v3.y, v3.z);
        	GL11.glVertex3f(v3.x,v3.y,v3.z);
        	
        	GL11.glEnd();
        }

        shader.end();
//        snapshot();
    }

    /**
     * Poll Input
     */
    public void pollInput() {
        // scroll through key events
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
                    closeRequested = true;
                else if (Keyboard.getEventKey() == Keyboard.KEY_P)
                    snapshot();
                else if (Keyboard.getEventKey() == Keyboard.KEY_RETURN){
                		flash_x = (float)Math.random();
                		flash_y = (float)Math.random();
                		flash_z = (float)Math.random();
                }
                else if (Keyboard.getEventKey() == Keyboard.KEY_1)
                	mode = 1;
                else if (Keyboard.getEventKey() == Keyboard.KEY_2)
                	mode = 2;
                else if (Keyboard.getEventKey() == Keyboard.KEY_3)
                	mode = 3;
            }
        }

        if (Display.isCloseRequested()) {
            closeRequested = true;
        }
    }
    
    int c = 77;
    public void snapshot() {
    	c++;
        System.out.println("Taking a snapshot ... snapshot"+c+".png");

        GL11.glReadBuffer(GL11.GL_FRONT);

        int width = Display.getDisplayMode().getWidth();
        int height= Display.getDisplayMode().getHeight();
        int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer );

        File file = new File("snapshots/snapshot"+c+".png"); // The file to save to.
        String format = "PNG"; // Example: "PNG" or "JPG"
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
   
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                int i = (x + (width * y)) * bpp;
                int r = buffer.get(i) & 0xFF;
                int g = buffer.get(i + 1) & 0xFF;
                int b = buffer.get(i + 2) & 0xFF;
                image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
            }
        }
           
        try {
            ImageIO.write(image, format, file);
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void createWindow() {
        try {
            Display.setDisplayMode(new DisplayMode(640, 480));
            Display.setVSyncEnabled(true);
            Display.setTitle(windowTitle);
            Display.create();
        } catch (LWJGLException e) {
            Sys.alert("Error", "Initialization failed!\n\n" + e.getMessage());
            System.exit(0);
        }
    }
    
    /**
     * Destroy and clean up resources
     */
    private void cleanup() {
        Display.destroy();
    }
    
    public static void main(String[] args) {
        new PA3_BlinnPhong().run();
    }
}

