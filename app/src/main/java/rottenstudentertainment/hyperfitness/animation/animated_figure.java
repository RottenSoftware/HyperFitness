package rottenstudentertainment.hyperfitness.animation;

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
import java.nio.ShortBuffer;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;


/**
 * Created by Merty on 03.08.2017.
 */

public class animated_figure
{
    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "attribute vec2 a_TextureCoordinates;" +
                    "varying vec2 v_TextureCoordinates;" +

                    "void main() {" +
                    "v_TextureCoordinates = a_TextureCoordinates;" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform sampler2D u_TextureUnit;" +
                    "varying vec2 v_TextureCoordinates;" +
                    "void main() {" +
                    "gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);" +
                    "}";
    private final FloatBuffer vertexBuffer;
    private final FloatBuffer weightBuffer;
    private final ShortBuffer boneIndicesBuffer;

//    private final ShortBuffer drawListBuffer;
    private final FloatBuffer textureBuffer;
    private final int mProgram;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    public float check;
    private int mPositionHandle;
    private int mtextureHandle;
    private int uTextureUnitLocation;
    private int mMVPMatrixHandle;
    private int vertex_count;
    private Mesh mesh;
    private Bones bones;
    private Motion_Keyframe motion_keyframe;
    private int texture;
    private int mSkin_indicices;
    private int mSkin_weights;
    private int mPose_handle;
    private int mInv_bone_handle;
    private Animator animator;


    public animated_figure(Context context, int file_id, float check)
    {

        this.check = check;

        mesh = TextResourceReader.readObjectFileFromColladaResource(context, file_id);
        bones = TextResourceReader.readbonesFromColladaSource(context, file_id); //bones - skeleton
        motion_keyframe = TextResourceReader.readKeyframesFromColada(context, file_id, bones.get_name_ids(), bones.get_structure()); //import poses

        animator = new Animator(motion_keyframe.get_time_stamps(), motion_keyframe.getLocal_keyframes_2d(), bones.get_structure() );
        //for(int i = 0; i < bones.get_invBindMats().length; i++) Log.e("matrix element test", " element: " + bones.get_invBindMats()[i]);

        vertex_count = mesh.get_vertices().length;

        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                mesh.get_vertices().length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(mesh.get_vertices());
        vertexBuffer.position(0);

        ByteBuffer dd = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                mesh.get_skin_weights_vec3().length * 4);
        dd.order(ByteOrder.nativeOrder());
        weightBuffer = dd.asFloatBuffer();
        weightBuffer.put(mesh.get_skin_weights_vec3());
        weightBuffer.position(0);


        texture = TextureHelper.loadTexture(context, R.drawable.model_texture);

        ByteBuffer cc = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                mesh.get_tex().length * 4);
        cc.order(ByteOrder.nativeOrder());
        textureBuffer = cc.asFloatBuffer();
        textureBuffer.put(mesh.get_tex());
        textureBuffer.position(0);

        short[] element_extract = new short[mesh.get_elements().length/3];
        for(int i =0; i < element_extract.length; i++)
        {
            element_extract[i]= mesh.get_elements()[i*3];
        }

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
         // (# of coordinate values * 2 bytes per short)
        mesh.get_skin_incdices_vec3().length * 2);
        dlb.order(ByteOrder.nativeOrder());
        boneIndicesBuffer = dlb.asShortBuffer();
        boneIndicesBuffer.put(mesh.get_skin_incdices_vec3());
        boneIndicesBuffer.position(0);


//        // initialize byte buffer for the draw list
//        ByteBuffer dlb = ByteBuffer.allocateDirect(
//                // (# of coordinate values * 2 bytes per short)
//                mesh.get_elements().length/3 * 2);
//        dlb.order(ByteOrder.nativeOrder());
//        drawListBuffer = dlb.asShortBuffer();
//        drawListBuffer.put(element_extract);
//        drawListBuffer.position(0);




        // prepare shaders and OpenGL program
        int vertexShader = MyGLRenderer.loadShader(
                GLES20.GL_VERTEX_SHADER,
                TextResourceReader.readTextFileFromResource(context, R.raw.animation_vertex_shader));
        int fragmentShader = MyGLRenderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER,
                TextResourceReader.readTextFileFromResource(context, R.raw.animation_fragment_shader));

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

        mSkin_indicices = GLES20.glGetAttribLocation(mProgram, "a_indices");
       // Log.e("attribute id", "mSkind_indices: " + mSkin_indicices);
        mSkin_weights = GLES20.glGetAttribLocation(mProgram, "weight_vec3");
        //Log.e("attribute id", "mSkind_weight: " + mSkin_weights);

        // texture--------------
        //set the active texture unit to texture unit 0
        glActiveTexture(GL_TEXTURE0);

        // to make sprite transparent
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);

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
        GLES20.glEnableVertexAttribArray(mSkin_weights);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
                mSkin_weights, 3,
                GLES20.GL_FLOAT, false,
                vertexStride, weightBuffer);

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mSkin_indicices);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
               mSkin_indicices, 3,
                GLES20.GL_SHORT, false,
                3*2, boneIndicesBuffer);




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


        float[] together = new float[16];
        float[] eins = new float[16];

       // float[] bind_matrix = {0.08629155f, -9.90506e-4f, -0.9959562f, 0, -0.07424811f, 0.07670029f, -0.01935671f, 0, 0.9907239f, 0.005834579f, 0.08535805f, 0, 0, 0, 0, 1};
        Matrix.setIdentityM(eins, 0);
        Matrix.multiplyMM(together , 0, p2, 0, it, 0);


        //testest---------------------------------------
        // get handle to shape's transformation matrix
        int handle1 = GLES20.glGetUniformLocation(mProgram, "it");
        GLES20.glUniformMatrix4fv(handle1, 1, false, eins, 0);
        int handle2 = GLES20.glGetUniformLocation(mProgram, "test_pose");
        GLES20.glUniformMatrix4fv(handle2, 1, false, together, 0);
        // testest---------------------------------------

        //uniform test value to pass int
        int mtesthandle = GLES20.glGetUniformLocation(mProgram, "test");
        GLES20.glUniform3f(mtesthandle, 1.0f, 0.0f, 0.0f);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");


        //Log.e("inv_matrix[0]", "size: " + bones.get_invBindMats().length/16);
        //set bones
        mInv_bone_handle = GLES20.glGetUniformLocation(mProgram, "invbones");
        MyGLRenderer.checkGlError("glGetUniformLocation");
        GLES20.glUniformMatrix4fv(mInv_bone_handle, bones.get_invBindMats().length /16, true, bones.get_invBindMats(), 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");



        animator.update_animation();

        //Log.e("pose[0]", "size: " + motion_keyframe.get_poses().get(0).length/16);
        // set pose
        mPose_handle = GLES20.glGetUniformLocation(mProgram, "pose");
        MyGLRenderer.checkGlError("glGetUniformLocation");
        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mPose_handle, animator.get_global_keyframes().length/16, true, animator.get_global_keyframes(), 0);
        //GLES20.glUniformMatrix4fv(mPose_handle, motion_keyframe.get_poses().get(0).length/16, true, motion_keyframe.get_poses().get(1), 0);
        //GLES20.glUniformMatrix4fv(mPose_handle, 2, false, bones.get_BindMats(), 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");


/*        float[] bindshape = {1, 0, 0, (float)9.31323e-10, 0, 1, 0, 0, 0, 0, 1, (float)2.38419e-7, 0, 0, 0, 1};
        int bindhandle;
        bindhandle = GLES20.glGetUniformLocation(mProgram, "bindm");
        MyGLRenderer.checkGlError("glGetUniformLocation");
        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(bindhandle, 1, false,bindshape, 0);
        //GLES20.glUniformMatrix4fv(mPose_handle, 2, false, bones.get_BindMats(), 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");*/


       // GLES20.glDisable(GL_CULL_FACE);
        // Draw
        glDrawArrays(GL_TRIANGLES, 0, mesh.get_elements().length/3);


        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mtextureHandle);
        GLES20.glDisableVertexAttribArray(mSkin_weights);
        GLES20.glDisableVertexAttribArray(mSkin_indicices);
    }

}
