const vec4 lightPosition = vec4(0.0, 0.0, 1600.0, 1.0);

varying float specular_intensity;
varying float diffuse_intensity;

void main(void) {
	vec4 vertexPositionCamera = gl_ModelViewMatrix * gl_Vertex;
	vec3 normal_camera = normalize(gl_NormalMatrix * gl_Normal);

	vec4 light_position_camera = gl_ModelViewMatrix * lightPosition;

	vec3 light_vert = normalize(vec3(light_position_camera - vertexPositionCamera));
	vec3 light_refl = normalize(reflect(light_vert, normal_camera));


	diffuse_intensity = max(dot(light_vert, normal_camera), 0.0);

	specular_intensity = max(dot(light_refl, normalize(vec3(vertexPositionCamera))), 0.0);
	specular_intensity = pow(specular_intensity, 6.0);

	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
	
}