

import org.lwjgl.util.vector.Vector4f;

public class Face {
	public Vector4f vertex = new Vector4f();
	public Vector4f normal = new Vector4f();
	public Vector4f textures = new Vector4f();
	public int texture;
	
	public Face(Vector4f vertex,Vector4f textures,Vector4f normal,int texture)
	{
		this.vertex = vertex;
		this.normal = normal;
		this.textures = textures;
		this.texture = texture;
	}
}
