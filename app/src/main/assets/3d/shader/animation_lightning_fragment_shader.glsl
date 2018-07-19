precision mediump float;
uniform sampler2D u_TextureUnit;
varying vec2 v_TextureCoordinates;
varying vec3 adjusted_normal;
uniform vec3 lightDirection;
uniform float ambientLight;

void main()
{
    vec3 textureColor = texture2D( u_TextureUnit, v_TextureCoordinates).rgb;
    vec3 unitNormal = normalize(adjusted_normal);
    vec3 unitLight =  normalize(lightDirection);
    float lightAmplitude = max(dot( unitLight, unitNormal), 0.0) * ( 1.0 - ambientLight) + ambientLight;
    //textureColor[0] = textureColor[0] * lightAmplitude;
    //textureColor[1] = textureColor[1] * lightAmplitude;
    //textureColor[2] = textureColor[2] * lightAmplitude;
    textureColor = textureColor * lightAmplitude;
    gl_FragColor = vec4(textureColor, 1.0);
}