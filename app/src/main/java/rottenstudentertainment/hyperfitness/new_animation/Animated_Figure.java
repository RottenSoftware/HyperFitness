package rottenstudentertainment.hyperfitness.new_animation;

import android.content.Context;
import android.opengl.GLES20;

import rottenstudentertainment.hyperfitness.MyGLRenderer;
import rottenstudentertainment.hyperfitness.OpenGL.DataBuffer;
import rottenstudentertainment.hyperfitness.OpenGL.VertexBuffer;
import rottenstudentertainment.hyperfitness.TextureHelper;

import rottenstudentertainment.hyperfitness.globals.Globals;
import rottenstudentertainment.hyperfitness.util.TextResourceReader;

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
import static rottenstudentertainment.hyperfitness.OpenGL.Program.createAndLinkProgram;
import static rottenstudentertainment.hyperfitness.globals.Globals.useLightning;
import static rottenstudentertainment.hyperfitness.workout.PageDisplayer.paused;


/**
 * Created by Merty on 03.08.2017.
 */

public class Animated_Figure
{


    private final FloatBuffer vertexBuffer;
    private final FloatBuffer normalBuffer;
    private final FloatBuffer weightBuffer;
    private final ShortBuffer boneIndicesBuffer;
    private final FloatBuffer textureBuffer;

    private final static int INDICES_STRIDE = 3*2;
    private final static int TEXTURE_STRIDE = 2*4;
    private final int mProgram;
    private int mPositionHandle;
    private int mNormalHandle;
    private int mtextureHandle;
    private int uTextureUnitLocation;
    private int ulightDirectionLocation;
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

    private float[] lightVector = { 0.0f, 0.0f, -1.0f};

    private VertexBuffer meshBuffer;


    private final float ambientLight = 0.3f;
    private int uAmbientHandle;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;


    private final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    /*
    public Animated_Figure( Context context, String mesh_file, String bones_file, String keyframes_file, String texture_file){
        mesh = Extract_mesh_bones_keyframes.read_in_mesh_binary( context, mesh_file);
        //Log.e("fast_animated", "successfully loaded: mesh");
        bones = Extract_mesh_bones_keyframes.read_in_bones_binary( context, bones_file); //bones - skeleton
        //Log.e("fast_animated", "successfully loaded: bones");
        texture_file = "3d/textures/" + texture_file + ".png";
        texture = TextureHelper.loadAssetTexture(context, texture_file);
    }
    */

    public Animated_Figure( Context context, Model model, String keyframes_file) {
        mesh = model.mesh;
        bones = model.bones;
        texture = model.texture;
        motion_keyframe = Extract_mesh_bones_keyframes.read_in_keyframes_binary( context, keyframes_file); //import poses
        //Log.e("fast_animated", "successfully loaded: keyframe");

        animator = new Animator( motion_keyframe.get_time_stamps(), motion_keyframe.getLocal_keyframes_2d(), bones.get_structure());

        vertex_count = mesh.get_vertices().length;

        vertexBuffer = (FloatBuffer) DataBuffer.setBuffer( mesh.get_vertices());
        normalBuffer = (FloatBuffer) DataBuffer.setBuffer( mesh.get_normals());
        weightBuffer = (FloatBuffer) DataBuffer.setBuffer( mesh.get_skin_weights_vec3());
        textureBuffer = (FloatBuffer) DataBuffer.setBuffer( mesh.get_tex());



        // initialize short buffer for bone indices
        boneIndicesBuffer = (ShortBuffer) DataBuffer.setBuffer( mesh.get_skin_incdices_vec3());

        // prepare shaders and OpenGL program
        int vertexShader = getShader( context, GLES20.GL_VERTEX_SHADER);
        int fragmentShader = getShader( context, GLES20.GL_FRAGMENT_SHADER);

        mProgram = createAndLinkProgram( vertexShader, fragmentShader);

        if(Globals.useGPUStorage){
            meshBuffer = new VertexBuffer(  mesh.get_vertices());
        }

    }

    public void draw(float[] mvpMatrix, float[] keyframebuffer, boolean synchronizer)
    {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        if( useLightning) {
            ulightDirectionLocation = glGetUniformLocation(mProgram, "lightDirection");
            uAmbientHandle = glGetUniformLocation(mProgram, "ambientLight");
            mNormalHandle = GLES20.glGetAttribLocation(mProgram, "normals");
        }
        uTextureUnitLocation = glGetUniformLocation( mProgram, "u_TextureUnit");

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation( mProgram, "vPosition");
        //Log.e("attribute id", "mPositionHandle: " + mPositionHandle);

        mtextureHandle = GLES20.glGetAttribLocation( mProgram, "a_TextureCoordinates");
        //Log.e("attribute id", "mtexture_Handle: " + mtextureHandle);

        mSkin_indicices = GLES20.glGetAttribLocation( mProgram, "a_indices");
        // Log.e("attribute id", "mSkind_indices: " + mSkin_indicices);
        mSkin_weights = GLES20.glGetAttribLocation( mProgram, "weight_vec3");
        //Log.e("attribute id", "mSkind_weight: " + mSkin_weights);

        // texture--------------
        //set the active texture unit to texture unit 0
        glActiveTexture( GL_TEXTURE0);

        // to make sprite transparent
        glBlendFunc( GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable( GL_BLEND);

        //bind texture to this unit
        glBindTexture(GL_TEXTURE_2D, texture);

        //Tell the texture uniform sampler to use this texture in the shader, read form unit 0
        glUniform1i(uTextureUnitLocation,0);
        //texture---------------

        if( useLightning) {
            //set directional light
            GLES20.glUniform3f(ulightDirectionLocation, lightVector[0], lightVector[1], lightVector[2]);
            GLES20.glUniform1f(uAmbientHandle, ambientLight);
            // Prepare normal data
            DataBuffer.enableAndSetVertexAttributes( mNormalHandle, VERTEX_STRIDE, COORDS_PER_VERTEX, normalBuffer);
        }


        if(Globals.useGPUStorage)
        {
            // Enable a handle to the triangle vertices
            //GLES20.glEnableVertexAttribArray(mPositionHandle);
            meshBuffer.setVertexAttribPointer(0, mPositionHandle, COORDS_PER_VERTEX, VERTEX_STRIDE);
        }
        else
        {
            DataBuffer.enableAndSetVertexAttributes( mPositionHandle, VERTEX_STRIDE, COORDS_PER_VERTEX, vertexBuffer);
        }

        DataBuffer.enableAndSetVertexAttributes( mSkin_weights, VERTEX_STRIDE, 3, weightBuffer);
        DataBuffer.enableAndSetVertexAttributes( mSkin_indicices, INDICES_STRIDE, 3, boneIndicesBuffer);
        DataBuffer.enableAndSetVertexAttributes( mtextureHandle, TEXTURE_STRIDE, 2, textureBuffer);


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

        animator.update_animation( paused);

        // set pose
        mPose_handle = GLES20.glGetUniformLocation(mProgram, "pose");
        MyGLRenderer.checkGlError("glGetUniformLocation");
        // Apply the projection and view transformation
        if(!synchronizer)  GLES20.glUniformMatrix4fv( mPose_handle, animator.get_global_keyframes().length/16, true, animator.get_global_keyframes(), 0);
        else  GLES20.glUniformMatrix4fv( mPose_handle, animator.get_global_keyframes().length/16, true, keyframebuffer, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");


        // GLES20.glDisable(GL_CULL_FACE);
        // Draw
        glDrawArrays(GL_TRIANGLES, 0, mesh.get_vertices().length/3);


        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mtextureHandle);
        GLES20.glDisableVertexAttribArray(mSkin_weights);
        GLES20.glDisableVertexAttribArray(mSkin_indicices);
    }


    public static int getShader( Context context, int type ){
        String shaderString =null;
        if( type == GLES20.GL_VERTEX_SHADER){
            if(useLightning){
                shaderString = TextResourceReader.readTextFromAssets( context, "3d/shader/animation_lightning_vertex_shader.glsl" );
            }
            else{
                shaderString = TextResourceReader.readTextFromAssets( context, "3d/shader/animation_vertex_shader.glsl" );
            }
        } else if( type == GLES20.GL_FRAGMENT_SHADER){
            if(useLightning){
                shaderString = TextResourceReader.readTextFromAssets( context, "3d/shader/animation_lightning_fragment_shader.glsl" );
            }
            else{
                shaderString = TextResourceReader.readTextFromAssets( context, "3d/shader/animation_fragment_shader.glsl" );
            }
        }
        return MyGLRenderer.loadShader( type, shaderString);
    }

}