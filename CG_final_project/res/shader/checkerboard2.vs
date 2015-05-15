varying vec3 normal;
varying vec3 position_eyespace;
varying vec3 position_worldspace;

varying vec4 varyingColour;
varying vec3 varyingNormal;
varying vec4 varyingVertex;

void main()
{
       varyingColour = gl_FrontMaterial.diffuse;

    varyingNormal = gl_Normal;
varyingVertex = gl_Vertex;

   position_eyespace=vec3(gl_ModelViewProjectionMatrix * gl_Vertex);
   position_worldspace=gl_Vertex.xyz;
   normal=normalize(gl_NormalMatrix*gl_Normal);


    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;

}
