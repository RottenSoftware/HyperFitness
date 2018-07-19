package rottenstudentertainment.hyperfitness.OpenGL;

import android.content.Context;
import android.opengl.GLES20;

import rottenstudentertainment.hyperfitness.MyGLRenderer;
import rottenstudentertainment.hyperfitness.TextureHelper;
import rottenstudentertainment.hyperfitness.util.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Created by Merty on 02.12.2017.
 */

public class background_image
{

    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";

    //attribute constants
    protected static final String A_POSITION = "a_Position";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";


    private final int uTextureUnitLocation;

    //attribute locations
    private final int aPositionLocation;
    private final int aTextureCoordinatesLocation;




    private static final int BYTES_PER_FLOAT = 4;
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int stride = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT)*BYTES_PER_FLOAT;



    private final FloatBuffer vertexBuffer;
    private final int mProgram;

    private int texture;



    static float[] squareCoords;

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public background_image(Context context, String texture_path) {

        texture = TextureHelper.loadAssetTexture(context, texture_path);

        squareCoords = new float[] // square coord + texture coord
                {
                        0.0f, 0.0f, 0.0f,  0.5f, 0.5f,// center
                        -1f,  -1f, 0.0f, 0.0f, 1.0f,  // top left
                        1f, -1f, 0.0f,   1.0f, 1.0f,// bottom left
                        1f, 1f, 0.0f,   1.0f, 0.0f,// bottom right
                        -1f,  1f, 0.0f, 0.0f, 0.0f,
                        -1f,  -1f, 0.0f, 0f, 1.0f   // top left
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
                TextResourceReader.readTextFromAssets(context, "background/shader/background_image_vertex_shader.glsl"));
        int fragmentShader = MyGLRenderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER,
                TextResourceReader.readTextFromAssets(context, "background/shader/background_image_fragment_shader.glsl"));


        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables

        //texture
        //Retrieve uniform locations for shader program
        //uMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX);
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
    public void draw_background() {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        //set the active texture unit to texture unit 0
        glActiveTexture(GL_TEXTURE0);

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

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(aPositionLocation);
        GLES20.glDisableVertexAttribArray(aTextureCoordinatesLocation);
    }



}
