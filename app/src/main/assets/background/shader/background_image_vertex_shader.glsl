attribute vec4 a_Position;
attribute vec2 a_TextureCoordinates;

varying vec2 v_TextureCoordinates;
varying vec4 Position;

void main()
{
    v_TextureCoordinates = a_TextureCoordinates;
    Position = a_Position;
    //Position[3]=-0.5f;
    gl_Position = Position;
}
