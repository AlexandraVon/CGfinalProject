
uniform sampler2D tex,l3d;

varying vec4 varyingColour;

varying vec3 varyingNormal;

varying vec4 varyingVertex;

void main() {
    vec3 vertexPosition = (gl_ModelViewMatrix * varyingVertex).xyz;
    vec3 surfaceNormal = normalize((gl_NormalMatrix * varyingNormal).xyz);
    vec3 lightDirection = normalize(gl_LightSource[0].position.xyz - vertexPosition);
    
    
    vec3 ct,cf;
    vec4 texel;
    float at,af;
    
    texel = texture2D(tex,gl_TexCoord[0].st)+texture2D(l3d,gl_TexCoord[0].st);
    ct= texel.rgb;
    at= texel.a;
    
    vec3 ViewDirection=normalize(-vertexPosition);
    vec3 HalfDir=normalize(lightDirection+ViewDirection);
    float NdotH=max(0.0, dot(surfaceNormal, HalfDir));
    float specular = NdotH;
    
    float diffuseLightIntensity = max(0.0, dot(surfaceNormal, lightDirection));
    gl_FragColor.rgb = diffuseLightIntensity * varyingColour.rgb;
    gl_FragColor += gl_LightModel.ambient;
    
    af= gl_FrontMaterial.diffuse.a;
    
    /* vec3 reflectionDirection = normalize(reflect(-lightDirection, surfaceNormal));
     float specular = max(0.0, dot(surfaceNormal, reflectionDirection));
     */
    if (diffuseLightIntensity != 0.0) {
        vec3 fspecular = vec3(gl_LightSource[0].specular)
        * vec3(gl_FrontMaterial.specular)
        * pow(specular,4.0 *gl_FrontMaterial.shininess);
        gl_FragColor.rgb += fspecular;
        cf=gl_FragColor.rgb;
        
        gl_FragColor =vec4(ct*cf, at*af);
    }
}

