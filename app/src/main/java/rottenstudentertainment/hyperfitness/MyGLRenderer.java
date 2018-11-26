/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rottenstudentertainment.hyperfitness;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import rottenstudentertainment.hyperfitness.animation.animated_figure;
import rottenstudentertainment.hyperfitness.heightmap.Heightmap;
import rottenstudentertainment.hyperfitness.util.Framerate_display;
import rottenstudentertainment.hyperfitness.util.MatrixHelper;
import rottenstudentertainment.hyperfitness.util.Touch_point_parser;
import rottenstudentertainment.hyperfitness.workout.Workout;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glEnable;
import static android.opengl.Matrix.orthoM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Provides drawing instructions for a GLSurfaceView object. This class
 * must override the OpenGL ES drawing lifecycle methods:
 * <ul>
 *   <li>{@link GLSurfaceView.Renderer#onSurfaceCreated}</li>
 *   <li>{@link GLSurfaceView.Renderer#onDrawFrame}</li>
 *   <li>{@link GLSurfaceView.Renderer#onSurfaceChanged}</li>
 * </ul>
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";
    //private Sprite_Square sprite_square;
    private animated_figure anim;

    private float[] ortho_Matrix = new float[16];
    private Touch_point_parser touch_point_parser;

    private Heightmap heightmap;


    private Workout workout;

    private Context context;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private  float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    private final float[] mRotationMatrix_x = new float[16];
    private final float[] mRotationMatrix_y = new float[16];
    private final float[] mRotationMatrix_total = new float[16];
    private final float[] model_Matrix = new float[16];

    private float mAngle;
    private float ThetaAngle;
    private float PhiAngle;

    private Framerate_display framerate_display;


    public MyGLRenderer(Context context)
    {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        glEnable(GL_DEPTH_TEST);


        workout = new Workout(context);
        touch_point_parser = new Touch_point_parser(0f,0f);

    }

    @Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];
        float[] m_buffer = new float[16];
        float[] model_buffer = new float[16];

        framerate_display.check_framerate();

        long startTime = System.currentTimeMillis();

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);



        //move stuff with the model_matrix
        setIdentityM(model_Matrix, 0);
        translateM(model_Matrix,0 ,0f, 0f, 2.0f);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0f);


        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, 1.0f);

        Matrix.setRotateM(mRotationMatrix_x, 0, PhiAngle, 1.0f, 0, 0);

        Matrix.setRotateM(mRotationMatrix_y, 0, ThetaAngle, 0, 1.0f, 0);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

        Matrix.multiplyMM(mRotationMatrix_total, 0, mRotationMatrix_x, 0, mRotationMatrix_y, 0);
        Matrix.multiplyMM(m_buffer, 0, mMVPMatrix, 0, mRotationMatrix_total, 0);



        float size=1.0f;
        float[] shit = new float[16];
        float[] scalem = new float[16];
        setIdentityM(scalem,0);
        scalem[0]= -size;
        scalem[5]= size;
        scalem[10]= size;

        Matrix.multiplyMM(shit, 0, m_buffer, 0,scalem, 0);


        workout.update_input(touch_point_parser);

        workout.draw_page(ortho_Matrix, mMVPMatrix);

        long endTime = System.currentTimeMillis(); // end of frame time
        long dt =  (endTime - startTime);  // length of frame time
        if(dt < 33)
        {
            //SystemClock.sleep( 33 - dt);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        orthoM(ortho_Matrix, 0, -ratio, ratio, -1f, 1f, -1f, 1f);

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

        //move stuff with the model_matrix
        setIdentityM(model_Matrix, 0);
        translateM(model_Matrix,0 ,0f, 0f, 2.0f);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);


        mMVPMatrix = MatrixHelper.move_rotate_scale_matrix(mMVPMatrix, 0.15f, 0f,-0.7f,2f,90f,0f,180f);

    }

    /**
     * Utility method for compiling a OpenGL shader.
     *
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
    * Utility method for debugging OpenGL calls. Provide the name of the call
    * just after making it:
    *
    * <pre>
    * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
    * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
    *
    * If the operation is not successful, the check throws an error.
    *
    * @param glOperation - Name of the OpenGL call to check.
    */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    /**
     * Returns the rotation angle of the triangle shape (mTriangle).
     *
     * @return - A float representing the rotation angle.
     */
    public float getAngle() {
        return mAngle;
    }

    public float getThetaAngle() {
        return ThetaAngle;
    }
    public float getPhiAngle() {
        return PhiAngle;
    }


    public void set_xy(float x, float y)
    {
        touch_point_parser = new Touch_point_parser(x,y);
    }

    public Touch_point_parser get_touch_point(float x, float y)
    {
        return new Touch_point_parser(x,y);
    }

    /**
     * Sets the rotation angle of the triangle shape (mTriangle).
     */
    public void setAngle(float angle) {
        mAngle = angle;
    }

    public void setPhiAngle(float phiangle) {
        PhiAngle = phiangle;
    }
    public void setThetaAngle(float thetaangle) {
        ThetaAngle = thetaangle;
    }


}