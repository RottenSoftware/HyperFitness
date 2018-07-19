package rottenstudentertainment.hyperfitness.OpenGL;

/**
 * Created by Merty on 02.07.2017.
 */
import android.opengl.GLES20;

import rottenstudentertainment.hyperfitness.MyGLRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.glDrawArrays;


public class Line //other drawing possibility of a square
{
    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec4 vColor;" +

                    "varying vec4 vertex_color;" +

                    "void main() {" +
                    // The matrix must be included as a modifier of gl_Position.
                    // for the matrix multiplication product to be correct.
                    "  vertex_color = vColor;" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "varying vec4 vertex_color;" +

                    "void main() {" +
                    "  gl_FragColor = vertex_color;" +
                    "}";

    private FloatBuffer vertexBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;



    private static final int BYTES_PER_FLOAT = 4;
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COORDINATES_COMPONENT_COUNT = 3;
    private static final int stride = (POSITION_COMPONENT_COUNT + COLOR_COORDINATES_COMPONENT_COUNT)*BYTES_PER_FLOAT;


    static float LineCoords[] =
            {
                    -0.5f,  -0.5f, 0.0f,  0f, 0f, 0f, // first three location, last three color
                    0.5f, -0.5f, 0.0f,    0f, 0f, 0f
            }; // top right




    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public Line() {

        bind_buffer();


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

        GLES20.glLineWidth(10.0f);
    }



    public void set_LineCoords(float x_1, float y_1, float x_2, float y_2, float red_1, float green_1, float blue_1, float red_2, float green_2, float blue_2)
    {
        LineCoords = new float[]
                {
                        x_1,  y_1, 0.0f,  red_1,  green_1,  blue_1,   // 1
                        x_2, y_2, 0.0f, red_2,  green_2,  blue_2  // 2
                }; // top right
        update_vertex_buffer();

    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     * this shape.
     */
    public void draw(float[] mvpMatrix ) {


        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mColorHandle = GLES20.glGetAttribLocation(mProgram, "vColor");





        // Prepare the triangle coordinate data
        vertexBuffer.position(0);
        GLES20.glVertexAttribPointer(
                mPositionHandle, POSITION_COMPONENT_COUNT,
                GLES20.GL_FLOAT, false,
                stride, vertexBuffer);

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        //prepare color data
        vertexBuffer.position(POSITION_COMPONENT_COUNT);
        GLES20.glVertexAttribPointer(mColorHandle, COLOR_COORDINATES_COMPONENT_COUNT, GLES20.GL_FLOAT, false, stride, vertexBuffer);
        vertexBuffer.position(0);
        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mColorHandle);



        //change object size

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");




        // Draw the square
        glDrawArrays(GL_LINES, 0, LineCoords.length / 3);


        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mColorHandle);
    }


    private void bind_buffer()
    {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                LineCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(LineCoords);
        vertexBuffer.position(0);
    }



    private void update_vertex_buffer()
    {
        vertexBuffer.put(LineCoords);
        vertexBuffer.position(0);
    }


}
