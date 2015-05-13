package CG_pa3_yf2338;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

public class objparser {
	public List<Vector3f> vertices = new ArrayList<Vector3f>();
	public List<Vector3f> normals = new ArrayList<Vector3f>();
	public List<parserface> faces = new ArrayList<parserface>();
	
	public objparser(){
		
	}
}
