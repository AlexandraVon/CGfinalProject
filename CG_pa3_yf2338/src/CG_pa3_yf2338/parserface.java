package CG_pa3_yf2338;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class parserface {
	public Vector3f vertex = new Vector3f();//three indices, not vertex or normal
	public Vector3f normal = new Vector3f();
	
	public parserface(Vector3f vertex, Vector3f normal){
		this.vertex = vertex;
		this.normal = normal;
	}
}
