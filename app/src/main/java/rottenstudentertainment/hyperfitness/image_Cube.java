package rottenstudentertainment.hyperfitness;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;


public class image_Cube //other drawing possibility of a square
{

    private image_square cube_site;
    private final float[] cube_Matrix = new float[16];
    private float[] rot_Matrix = new float[16];
    private Context context;

    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // The matrix must be included as a modifier of gl_Position.
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;


    static float squareCoords[] =
            {
                    0.0f, 0.0f, 0.0f,  // center
                    -0.5f,  -0.5f, 0.0f,   // top left
                    0.5f, -0.5f, 0.0f,   // bottom left
                    0.5f, 0.5f, 0.0f,   // bottom right
                    -0.5f,  0.5f, 0.0f,
                    -0.5f,  -0.5f, 0.0f   // top left
            }; // top right

//    static float squareCoords[] =
//            {
//            -0.5f,  0.5f, 0.0f,   // top left
//            -0.5f, -0.5f, 0.0f,   // bottom left
//            0.5f, -0.5f, 0.0f,   // bottom right
//            0.5f,  0.5f, 0.0f
//            }; // top right

// altes bild
//    xOrigin,     yOrigin,    0.5f,    0.5f,
//        -size_x + xOrigin,     -size_y + yOrigin,    0f,    1.0f,
//    size_x + xOrigin,     -size_y + yOrigin,    1f,    1.0f,
//    size_x + xOrigin,     size_y + yOrigin,    1f,    0.01f,
//        -size_x + xOrigin,     size_y + yOrigin,    0f,    0.01f,
//        -size_x + xOrigin,     -size_y + yOrigin,    0f,    1.0f,

    private final short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    float color[] = { 1.0f, 0.0f, 0.0f, 1.0f };

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public image_Cube(Context context) {
        this.context = context;
        cube_site = new image_square(context);
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

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
    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     * this shape.
     */
    public void draw(float[] mvpMatrix)
    {
        //1
        setCubeSiteM(mvpMatrix, 0f, 0f, 0.5f, 0f, 0f, 0f);
        cube_site.draw(cube_Matrix, 1);
        //2
        setCubeSiteM(mvpMatrix, 0f, 0f, -0.5f, 0f, 0f, 0f);
        cube_site.draw(cube_Matrix, 2);
        //3
        setCubeSiteM(mvpMatrix, 0.5f, 0f, 0f, 0f, 90.0f, 0f);
        cube_site.draw(cube_Matrix,3);
        //4
        setCubeSiteM(mvpMatrix, -0.5f, 0f, 0f, 0f, -90.0f, 0f);
        cube_site.draw(cube_Matrix, 4);
        //5
        setCubeSiteM(mvpMatrix, 0f, 0.5f, 0f, 90.0f, 0f, 0f);
        cube_site.draw(cube_Matrix, 5);
        //6
        setCubeSiteM(mvpMatrix, 0f, -0.5f, 0f, -90.0f, 0f, 0f);
        cube_site.draw(cube_Matrix, 6);
    }

    public void setCubeSiteM(float[] mvpMatrix, float x, float y, float z, float x_rot, float y_rot, float z_rot)
    {
        float[] model_Matrix = new float[16];
        float[] buffer_Matrix = new float[16];

        //move stuff with the model_matrix
        setIdentityM(model_Matrix, 0);
        translateM(model_Matrix,0 ,x, y, z);
        //set rotation Matrix
        setRotM(x_rot, y_rot, z_rot);
        //apply transformations
        Matrix.multiplyMM(buffer_Matrix, 0, model_Matrix, 0, rot_Matrix, 0);
        Matrix.multiplyMM(cube_Matrix, 0, mvpMatrix, 0, buffer_Matrix, 0);

    }

    public void setRotM( float x_rot, float y_rot, float z_rot)
    {
        float[] rot_x_Matrix = new float[16];
        float[] rot_y_Matrix = new float[16];
        float[] rot_z_Matrix = new float[16];
        float[] rot_buffer_Matrix = new float[16];

        Matrix.setRotateM(rot_x_Matrix, 0, x_rot, 1.0f, 0, 0);
        Matrix.setRotateM(rot_y_Matrix, 0, y_rot, 0, 1.0f, 0);
        Matrix.setRotateM(rot_z_Matrix, 0, z_rot, 0, 0, 1.0f);

        //build rot matrix
        Matrix.multiplyMM(rot_buffer_Matrix, 0, rot_y_Matrix, 0, rot_x_Matrix, 0);
        Matrix.multiplyMM(rot_Matrix, 0, rot_buffer_Matrix, 0, rot_z_Matrix, 0);


    }

}

