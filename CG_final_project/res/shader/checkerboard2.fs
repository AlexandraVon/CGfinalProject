
varying vec3 normal;
varying vec3 position_eyespace;
varying vec3 position_worldspace;

const float check_size = 0.8;
varying vec4 varyingColour;
varying vec3 varyingNormal;
varying vec4 varyingVertex;
void main()
{
    vec3 vertexPosition = (gl_ModelViewMatrix * varyingVertex).xyz;
    vec3 surfaceNormal = normalize((gl_NormalMatrix * varyingNormal).xyz);
    vec3 lightDirection = normalize(gl_LightSource[0].position.xyz - vertexPosition);

    vec3 ViewDirection=normalize(-vertexPosition);
    vec3 HalfDir=normalize(lightDirection+ViewDirection);
    float NdotH=max(0,dot(surfaceNormal, HalfDir));
    float specular = NdotH;

    float diffuseLightIntensity = max(0, dot(surfaceNormal, lightDirection));
    gl_FragColor.rgb = diffuseLightIntensity * varyingColour.rgb;
    gl_FragColor += gl_LightModel.ambient;
   /* vec3 reflectionDirection = normalize(reflect(-lightDirection, surfaceNormal));
    float specular = max(0.0, dot(surfaceNormal, reflectionDirection));
   */
    if (diffuseLightIntensity != 0) {
        vec3 fspecular = vec3(gl_LightSource[0].specular) 
                  * vec3(gl_FrontMaterial.specular) 
                  * pow(specular,4*gl_FrontMaterial.shininess);
        gl_FragColor += fspecular;
    }

    vec3 color;
    int count=0;

   if(mod(position_worldspace.x,2)>1) count++;
   if(mod(position_worldspace.y,2)>1) count++;
   if(mod(position_worldspace.z,2)>1) count++;

   if(count==1 || count==3){
     color=vec3(0,0,0);
   }else{
      color=vec3(1,1,1);
   }

    gl_FragColor =  color+ gl_FragColor;
}

