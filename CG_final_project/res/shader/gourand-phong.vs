

varying vec3 vColor;
varying vec3 vNormal;
varying vec4 vVertex;

void main() {
vNormal = gl_Normal;
    vVertex = gl_Vertex;
   float diffuseLightIntensity = 0.0;
    
	vec3 vertexPosition = (gl_ModelViewMatrix * gl_Vertex).xyz;
	
	vec3 lightDirection = normalize(gl_LightSource[0].position.xyz - vertexPosition);
		vec3 surfaceNormal  = (gl_NormalMatrix * gl_Normal).xyz;
	
    
		
    if(dot(surfaceNormal, lightDirection) > 0.0){
        diffuseLightIntensity=dot(surfaceNormal, lightDirection);
    }
    
   vColor.rgb=  vec3(gl_LightSource[0].diffuse)*diffuseLightIntensity * gl_FrontMaterial.diffuse.rgb;

vColor+= gl_LightModel.ambient.rgb* vec3(gl_FrontMaterial.ambient);

	vec3 reflectionDirection = normalize(reflect(-lightDirection, surfaceNormal));
		float specular = max(0.0, dot(surfaceNormal, reflectionDirection));
	if (diffuseLightIntensity != 0.0 ) {
				vec3 fspecular = vec3(gl_FrontMaterial.specular) 
                  * pow(specular,gl_FrontMaterial.shininess)
                  *vec3(gl_LightSource[0].specular) ; 
		vColor.rgb += fspecular;

	}
    
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;



}
