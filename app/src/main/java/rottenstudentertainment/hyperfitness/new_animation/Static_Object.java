package rottenstudentertainment.hyperfitness.new_animation;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

import rottenstudentertainment.hyperfitness.MyGLRenderer;
import rottenstudentertainment.hyperfitness.R;
import rottenstudentertainment.hyperfitness.TextureHelper;
import rottenstudentertainment.hyperfitness.util.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;


/**
 * Created by Merty on 03.08.2017.
 */

public class Static_Object
{

    private final FloatBuffer vertexBuffer;
    private final FloatBuffer textureBuffer;
    //private final FloatBuffer normalsBuffer;


    private final int mProgram;
    private int mPositionHandle;
    private int mtextureHandle;
    private int uTextureUnitLocation;
    private int mMVPMatrixHandle;
    private int vertex_count;
    private Mesh mesh;
    private int texture;

    public float check;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;


    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex


    public Static_Object(Context context, int file_id, String texture_name)
    {


        mesh = Extract_mesh_bones_keyframes.read_in_mesh_data(context, file_id);




        vertex_count = mesh.get_vertices().length;

        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                mesh.get_vertices().length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(mesh.get_vertices());
        vertexBuffer.position(0);




        texture = TextureHelper.loadTexture(context, TextureHelper.loadAssetTexture( context, texture_name));

        ByteBuffer cc = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                mesh.get_tex().length * 4);
        cc.order(ByteOrder.nativeOrder());
        textureBuffer = cc.asFloatBuffer();
        textureBuffer.put(mesh.get_tex());
        textureBuffer.position(0);






        // prepare shaders and OpenGL program
        int vertexShader = MyGLRenderer.loadShader(
                GLES20.GL_VERTEX_SHADER,
                TextResourceReader.readTextFileFromResource(context, R.raw.static_object_vertex_shader));
        int fragmentShader = MyGLRenderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER,
                TextResourceReader.readTextFileFromResource(context, R.raw.static_object_fragment_shader));

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables

        //testing matrix
        float test_vecx = mesh.get_vertices()[0];

        //Log.e("weight + bone testing:","erster gemalter vertix:"+ " bone: " + bones.get_name_ids()[mesh.get_skin_incdices_vec3()[0]]  + " influence: " + mesh.get_skin_weights_vec3()[0] + " bone: " + mesh.get_skin_incdices_vec3()[1]  + " influence: " + mesh.get_skin_weights_vec3()[1] + " bone: " + mesh.get_skin_incdices_vec3()[2]  + " influence: " + mesh.get_skin_weights_vec3()[2] );
        //Log.e("bone structure", " bone: " + bones.get_name_ids()[0] + " bone0:"+ bones.get_structure().get(0).get(0) + " bone1:"+ bones.get_structure().get(0).get(1)  );

    }

    public void draw(float[] mvpMatrix)
    {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        uTextureUnitLocation = glGetUniformLocation(mProgram, "u_TextureUnit");

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        //Log.e("attribute id", "mPositionHandle: " + mPositionHandle);

        mtextureHandle = GLES20.glGetAttribLocation(mProgram, "a_TextureCoordinates");
        //Log.e("attribute id", "mtexture_Handle: " + mtextureHandle);


        // texture--------------
        //set the active texture unit to texture unit 0
        glActiveTexture(GL_TEXTURE0);

        // to make sprite transparent
        //glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        //glEnable(GL_BLEND);

        //bind texture to this unit
        glBindTexture(GL_TEXTURE_2D, texture);

        //Tell the texture uniform sampler to use this texture in the shader, read form unit 0
        glUniform1i(uTextureUnitLocation,0);
        //texture---------------

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);



        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mtextureHandle);

        // Prepare the texture data
        GLES20.glVertexAttribPointer(
                mtextureHandle, 2,
                GLES20.GL_FLOAT, false,
                2*4, textureBuffer);

        float[] it= {1, 0, 0, 0,
                    0, 1, 0, 0,
                    0, 0, 1, 0,
                0.044f, 0.441f, -5.783f, 1f};
        float[] p0= {1, 0, 0, 0,
                    0, 1, 0, 0,
                    0, 0, 1, 0,
                -0.044f, -0.441f, 5.783f, 1f};
        float[] p1= {0, 0, -1, 0,
                    0, 1, 0, 0,
                    1, 0, 0, 0,
                -0.044f, -0.441f, 5.783f, 1f};
        float[] p2= {0, 0, -1, 0,
                    0, 1, 0, 0,
                    1, 0, 0, 0,
                -0.044f, -0.441f, 5.783f, 1f};

        float[] roty_calc = new float[16];

        Matrix.setRotateM(roty_calc, 0, -90.0f, 0, 1.0f, 0);

        float[] trans_m= {1, 0, 0, 0,
                0, 1, 0, 0,
                0, 0, 1, 0,
                -0.044f, -0.441f, 5.783f, 1f};
        Matrix.multiplyMM(p2 , 0, trans_m, 0, roty_calc, 0);
        //MatrixHelper.matrix_printer(roty_calc);
        //MatrixHelper.matrix_printer(p2);



        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");




        glDrawArrays(GL_TRIANGLES, 0, mesh.get_vertices().length/3);


        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mtextureHandle);
    }

}
