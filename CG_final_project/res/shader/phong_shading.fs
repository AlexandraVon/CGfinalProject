

varying vec4 varyingColour;

varying vec3 varyingNormal;

varying vec4 varyingVertex;

void main() {
    vec3 vertexPosition = (gl_ModelViewMatrix * varyingVertex).xyz;
    vec3 surfaceNormal = normalize((gl_NormalMatrix * varyingNormal).xyz);
    vec3 lightDirection = normalize(gl_LightSource[0].position.xyz - vertexPosition);
    vec3 ViewDirection=normalize(-vertexPosition);

    gl_FragColor += gl_LightModel.ambient* vec3(gl_FrontMaterial.ambient) ;  //Ia*Ka

    float diffuseLightIntensity = max(0, dot(surfaceNormal, lightDirection));
    gl_FragColor.rgb += vec3(gl_LightSource[0].diffuse)*diffuseLightIntensity * varyingColour.rgb ;  //id*(NL)*kd
   
    vec3 reflectionDirection = normalize(reflect(-lightDirection, surfaceNormal));
    float specular = max(0.0, dot(reflectionDirection,ViewDirection));
    if (diffuseLightIntensity != 0) {
         vec3 fspecular =vec3(gl_FrontMaterial.specular) 
                  * pow(specular,gl_FrontMaterial.shininess)
                  *vec3(gl_LightSource[0].specular) ;   //ks*(RV)^a*is
        gl_FragColor += fspecular;
    }
}