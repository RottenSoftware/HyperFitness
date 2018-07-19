package rottenstudentertainment.hyperfitness;

/**
 * Created by Merty on 02.07.2017.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;


public class Texture //other drawing possibility of a square
{

    protected static final String U_COLOR = "u_Color";
    //uniform constants
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";

    //attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";


    //Uniform locations
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;

    //attribute locations
    private final int aPositionLocation;
    private final int aTextureCoordinatesLocation;


    private static final int BYTES_PER_FLOAT = 4;
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int stride = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT)*BYTES_PER_FLOAT;

    public float width;
    public float height;


    private final String vertexShaderCode =
    "uniform mat4 u_Matrix;" +

    "attribute vec4 a_Position;" +
    "attribute vec2 a_TextureCoordinates;" +

    "varying vec2 v_TextureCoordinates;" +

    "void main()"+
    "{" +
        "v_TextureCoordinates = a_TextureCoordinates;" +
        "gl_Position = u_Matrix * a_Position;"+
    "}";

    private final String fragmentShaderCode =
    "precision mediump float;" +

    "uniform sampler2D u_TextureUnit;" +
    "varying vec2 v_TextureCoordinates;" +

    "void main()" +
    "{" +
        "gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);" +
    "}";

    private final FloatBuffer vertexBuffer;
    //private final ShortBuffer drawListBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    private int texture;


    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;


    static float squareCoords[] =  // square coord + texture coord
            {
                    0.0f, 0.0f, 0.0f,  0.5f, 0.5f,// center
                    -0.5f,  -0.5f, 0.0f, 0.0f, 1.0f,  // top left
                    0.5f, -0.5f, 0.0f,   1.0f, 1.0f,// bottom left
                    0.5f, 0.5f, 0.0f,   1.0f, 0.0f,// bottom right
                    -0.5f,  0.5f, 0.0f, 0.0f, 0.0f,
                    -0.5f,  -0.5f, 0.0f, 0f, 1.0f   // top left
            }; // top right


    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    float color[] = { 1.0f, 0.0f, 0.0f, 1.0f };

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public Texture(Context context, int texture, int textureid, float width, float height) {

        this.texture = texture;
        //get image width and height
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inScaled = false;
//        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), textureid, options);
//        width = bitmap.getWidth();
//        height = bitmap.getHeight();
//        bitmap.recycle();
//        Log.e("bild ratio test", "height: " + height + " width: " + width );

        width = width/2;
        height = height/2;

        squareCoords = new float[] // square coord + texture coord
        {
            0.0f, 0.0f, 0.0f,  0.5f, 0.5f,// center
                    -width,  -height, 0.0f, 0.0f, 1.0f,  // top left
                    width, -height, 0.0f,   1.0f, 1.0f,// bottom left
                    width, height, 0.0f,   1.0f, 0.0f,// bottom right
                    -width,  height, 0.0f, 0.0f, 0.0f,
                    -width,  -height, 0.0f, 0f, 1.0f   // top left
        }; // top right


        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);





        // prepare shaders and OpenGL program
        int vertexShader = MyGLRenderer.loadShader(
                GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);


        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables

        //texture
        //Retrieve uniform locations for shader program
        uMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX);
        uTextureUnitLocation = glGetUniformLocation(mProgram, U_TEXTURE_UNIT);

        //Retrieve attribute locations for the shader program
        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION);
        aTextureCoordinatesLocation = glGetAttribLocation(mProgram, A_TEXTURE_COORDINATES);

    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     * this shape.
     */
    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        //texture-------

        //pass the matrix into shader program
        glUniformMatrix4fv(uMatrixLocation, 1, false, mvpMatrix, 0);

        //set the active texture unit to texture unit 0
        glActiveTexture(GL_TEXTURE0);

        // to make sprite transparent
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);


        //bind texture to this unit
        glBindTexture(GL_TEXTURE_2D, texture);

        //Tell the texture uniform sampler to use this texture in the shader, read form unit 0
        glUniform1i(uTextureUnitLocation,0);

        //texture---------
//texture
        vertexBuffer.position(0); //1
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, stride, vertexBuffer);
        glEnableVertexAttribArray(aPositionLocation);
        vertexBuffer.position(0);

        vertexBuffer.position(POSITION_COMPONENT_COUNT);  //2
        glVertexAttribPointer(aTextureCoordinatesLocation, TEXTURE_COORDINATES_COMPONENT_COUNT, GL_FLOAT, false, stride, vertexBuffer);
        glEnableVertexAttribArray(aTextureCoordinatesLocation);
        vertexBuffer.position(0);

//texture

        // Draw the square
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

//        GLES20.glDrawElements(
//                GLES20.GL_TRIANGLES, drawOrder.length,
//                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(aPositionLocation);
        GLES20.glDisableVertexAttribArray(aTextureCoordinatesLocation);
    }




}

