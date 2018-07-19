package rottenstudentertainment.hyperfitness.OpenGL;

import android.opengl.GLES20;

import rottenstudentertainment.hyperfitness.MyGLRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.glDrawArrays;

/**
 * Created by Merty on 07.11.2017.
 */

public class Disc
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
    private int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;




    private static final int BYTES_PER_FLOAT = 4;
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COORDINATES_COMPONENT_COUNT = 4;
    private static final int stride = (POSITION_COMPONENT_COUNT + COLOR_COORDINATES_COMPONENT_COUNT)*BYTES_PER_FLOAT;

    private float inner_r;
    private float outer_r;
    private int res;

    private float [] disc_array; //xy rgb alpha

    public Disc(int res, float inner_r, float outer_r, float red, float green, float blue)
    {
        this.res = res;
        this.inner_r = inner_r;
        this.outer_r = outer_r;
        float[] color ={red, green, blue};
                        // r    g     b
        //float[] color1 ={0.5f, 0.5f, 0.5f};
        //float[] color2 ={0.5f, 0.0f, 0.5f};
       // float[] color3 ={0.2f, 0.8f, 0.2f};
        create_Disc_Array(res, inner_r, outer_r, color );

        set_up_vertex_buffer_and_shader();



    }



    public void set_up_vertex_buffer_and_shader()
    {
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                disc_array.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(disc_array);
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
    }


    public void create_Disc_Array(int res, float inner_r, float outer_r, float[] color1 )
    {
        disc_array= new float[ (res*14 +14)*1 ];

            create_Disc( res, inner_r, outer_r, color1, 0, res+1);
            //create_Disc( res, inner_r, outer_r, color2, res+1, (res+1)*2);
            //create_Disc( res, inner_r*1.1f, outer_r*0.95f, color3,(res+1)*2, (res+1)*3);


    }

    public void create_Disc(int res, float inner_r, float outer_r, float[] color, int start_number, int end_number)
    {
        for(int i = start_number; i < end_number ;i++)
        {
            //outer
            disc_array[i*14] = outer_r*(float) Math.sin( (float) (2* Math.PI) /(float)res*i);
            disc_array[i*14+1] = outer_r*(float) Math.cos( (float) (2* Math.PI) /(float)res*i);
            disc_array[i*14+2] = 0f;
            disc_array[i*14+3] = color[0];
            disc_array[i*14+4] = color[1];
            disc_array[i*14+5] = color[2];
            disc_array[i*14+6] = 1f;

            //inner
            disc_array[i*14+7] = inner_r*(float) Math.sin( (float) (2* Math.PI) /(float)res*i);
            disc_array[i*14+8] = inner_r*(float) Math.cos( (float) (2* Math.PI) /(float)res*i);
            disc_array[i*14+9] = 0f;
            disc_array[i*14+10] = color[0];
            disc_array[i*14+11] = color[1];
            disc_array[i*14+12] = color[2];
            disc_array[i*14+13] = 1f;
        }
    }


    public void draw(float [] mvpMatrix, float x, float y, float time)
    {


        //mvpMatrix = MatrixHelper.move_rotate_scale_matrix(mvpMatrix,1f, x,y, 0f,0f,0f,0f);

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
        glDrawArrays(GL_TRIANGLE_STRIP, 0, disc_array.length / 7 - 2*(int)(time*res));


        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mColorHandle);

    }




}
