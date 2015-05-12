

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class OBJLoader {
	public static Model loadModel(File f) throws FileNotFoundException, IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(f));

		Model m = new Model();
		String line;
		Texture currentTexture = null;
		while((line=reader.readLine()) != null)
		{
			if(line.startsWith("v "))
			{
				int sp = 0;
				if(line.split(" ")[1].isEmpty()){
					sp = 1;
				}
				float x = Float.valueOf(line.split(" ")[1+sp]);
				float y = Float.valueOf(line.split(" ")[2+sp]);
				float z = Float.valueOf(line.split(" ")[3+sp]);
				m.verticies.add(new Vector3f(x,y,z));
			}else if(line.startsWith("vn "))
			{
				int sp = 0;
				if(line.split(" ")[1].isEmpty()){
					sp = 1;
				}
				float x = Float.valueOf(line.split(" ")[1+sp]);
				float y = Float.valueOf(line.split(" ")[2+sp]);
				float z = Float.valueOf(line.split(" ")[3+sp]);
				m.normals.add(new Vector3f(x,y,z));
			}else if(line.startsWith("vt "))
			{
				int sp = 0;
				if(line.split(" ")[1].isEmpty()){
					sp = 1;
				}
				float x = Float.valueOf(line.split(" ")[1+sp]);
				float y = Float.valueOf(line.split(" ")[2+sp]);
				m.texVerticies.add(new Vector2f(x,1-y));
			}else if(line.startsWith("f "))
			{
				int sp = 0;
				if(line.split(" ")[1].isEmpty()){
					sp = 1;
				}
				float[] last_ind = null;
				if(line.split(" ").length==4){
					last_ind = new float[]{-1f,-1f,-1f};
				}else{
					last_ind = new float[]{Float.valueOf(line.split(" ")[4+sp].split("/")[0])-1,
											Float.valueOf(line.split(" ")[4+sp].split("/")[1])-1,
											Float.valueOf(line.split(" ")[4+sp].split("/")[2])-1};
				}
				Vector4f vertexIndicies = new Vector4f(Float.valueOf(line.split(" ")[1+sp].split("/")[0])-1, 
						Float.valueOf(line.split(" ")[2+sp].split("/")[0])-1, 
						Float.valueOf(line.split(" ")[3+sp].split("/")[0])-1,
						last_ind[0]);
				Vector4f textureIndicies = new Vector4f(Float.valueOf(line.split(" ")[1].split("/")[1])-1, 
						Float.valueOf(line.split(" ")[2+sp].split("/")[1])-1, 
						Float.valueOf(line.split(" ")[3+sp].split("/")[1])-1,
						last_ind[1]);
				Vector4f normalIndicies = new Vector4f(Float.valueOf(line.split(" ")[1].split("/")[2])-1, 
						Float.valueOf(line.split(" ")[2+sp].split("/")[2])-1, 
						Float.valueOf(line.split(" ")[3+sp].split("/")[2])-1,
						last_ind[2]);

				m.faces.add(new Face(vertexIndicies,textureIndicies,normalIndicies,currentTexture.getTextureID()));
			}else if(line.startsWith("g "))
			{
				if(line.length()>2)
				{
					String name = line.split(" ")[1];
					if(new File("res/"+name+".png").exists()){
						currentTexture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/" + name + ".png"));
					}else{
						currentTexture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("res/" + name + ".jpg"));
					}
					//System.out.println(currentTexture.getTextureID());
				}
			}
		}


		reader.close();
		
		/*System.out.println(m.verticies.size() + " verticies");
		System.out.println(m.normals.size() + " normals");
		System.out.println(m.texVerticies.size() + " texture coordinates");
		System.out.println(m.faces.size() + " faces");*/
		return m;
	}
}
