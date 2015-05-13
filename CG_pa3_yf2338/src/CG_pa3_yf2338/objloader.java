package CG_pa3_yf2338;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class objloader {
	public static objparser loadmodel(File f) throws FileNotFoundException, IOException{
		BufferedReader reader = new BufferedReader(new FileReader(f));
		objparser model = new objparser();
		String line;
		while((line = reader.readLine())!=null){
			//parse the stuff
			if (line.startsWith("v ")){
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);
				model.vertices.add(new Vector3f(x,y,z));
			}
			else if(line.startsWith("vn ")){
				float x = Float.valueOf(line.split(" ")[1]);
				float y = Float.valueOf(line.split(" ")[2]);
				float z = Float.valueOf(line.split(" ")[3]);
				model.normals.add(new Vector3f(x,y,z));
			}
			else if(line.startsWith("f ")){
				
				Vector3f vertexIndices = null;
				Vector3f normalIndices = null;
				vertexIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[0]),
					Float.valueOf(line.split(" ")[2].split("/")[0]),
					Float.valueOf(line.split(" ")[3].split("/")[0]));
				normalIndices = new Vector3f(Float.valueOf(line.split(" ")[1].split("/")[2]),
					Float.valueOf(line.split(" ")[2].split("/")[2]),
					Float.valueOf(line.split(" ")[3].split("/")[2]));
				model.faces.add(new parserface(vertexIndices,normalIndices));
			}
		}
		reader.close();
		return model;
	}
}
