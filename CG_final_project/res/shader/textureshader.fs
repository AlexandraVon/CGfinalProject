
uniform sampler2D tex,l3d;

varying vec3 lightDir, normal;

void main() {
    
    
    vec3 ct,cf;
    vec4 texel;
    float at,af,intensity;
    
    intensity = max(0.0, dot(lightDir,normalize(normal)));
    cf=intensity*(gl_FrontMaterial.diffuse).rgb+gl_FrontMaterial.ambient.rgb;
    
    af= gl_FrontMaterial.diffuse.a;
    
    texel = texture2D(tex,gl_TexCoord[0].st)+texture2D(l3d,gl_TexCoord[0].st);
    ct= texel.rgb;
    at= texel.a;

    gl_FragColor =vec4(ct*cf, at*af);
    
}