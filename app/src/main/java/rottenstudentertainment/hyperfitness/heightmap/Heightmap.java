package rottenstudentertainment.hyperfitness.heightmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.opengl.GLES20;

import rottenstudentertainment.hyperfitness.MyGLRenderer;
import rottenstudentertainment.hyperfitness.R;
import rottenstudentertainment.hyperfitness.util.Geometry;
import rottenstudentertainment.hyperfitness.util.Geometry.*;
import rottenstudentertainment.hyperfitness.util.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * Created by Merty on 02.08.2017.
 */

public class Heightmap
{
    private static final int COORDS_PER_VERTEX = 3; // 4 bytes per vertex
    private static final int NORMAL_COMPONENT_COUNT=3;
    private static final int TOTAL_COMPONENT_COUNT = NORMAL_COMPONENT_COUNT + COORDS_PER_VERTEX;
    private static final int BYTES_PER_FLOAT = 4;

    private static final int STRIDE = (NORMAL_COMPONENT_COUNT + COORDS_PER_VERTEX)*BYTES_PER_FLOAT;

    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "uniform vec3 u_VectorToLight;" +
                    "attribute vec3 vPosition;" +
                    "attribute vec3 a_Normal;" +

                    "varying vec3 v_Color;" +
                    "void main() {" +

                    "v_Color = mix(vec3(0.18,0.467,0.153), vec3(0.66, 0.67,0.68), vPosition.z);" +

                    " vec3 VectorToLight = normalize(u_VectorToLight);" +
                    " vec3 scaledNormal = normalize(a_Normal);" +
                    " float diffuse = max(dot(scaledNormal, VectorToLight), 0.0);" +
                    " v_Color = (v_Color*diffuse +0.2)/1.2f;" +
                    " gl_Position = uMVPMatrix * vec4(vPosition, 1.0)*2.0f;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "varying vec3 v_Color;" +
                    "void main() {" +
                    "  gl_FragColor = vec4(v_Color,1.0);" +
                    "}";

    private static int POSITION_COMPONENT_COUNT = 3;
    private final int width;
    private final int height;
    private final int numElements;
    public float[] heightmap_vertices;
    private short[] draworder;

    public float[] Light_Vec = {1.0f,1.0f,2.0f};

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mMVPMatrixHandle;
    private int mNormalsHandle;
    private int mLightHandle;


    public Heightmap(Context context, int tex_id)
    {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.heightmap_256, options);
        width = bitmap.getWidth();
        height = bitmap.getHeight();

        if(width * height > 65536)
        {
            throw new RuntimeException("Heightmap is too large for the index buffer");
        }

        numElements = calculateNumElements();
        heightmap_vertices = new float[width * height * TOTAL_COMPONENT_COUNT];

       // Log.e("heightmap matrix:", "heightmap_vertices size: " + heightmap_vertices.length );
       // Log.e("heightmap matrix:", "image height: " + height );
        //Log.e("heightmap matrix:", "image width: " + width );

        loadBitmapData(bitmap);

        setIndexData();

        //setup opengl
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                heightmap_vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(heightmap_vertices);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                draworder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(draworder);
        drawListBuffer.position(0);



        // prepare shaders and OpenGL program
        int vertexShader = MyGLRenderer.loadShader(
                GLES20.GL_VERTEX_SHADER,
                TextResourceReader.readTextFileFromResource(context, R.raw.heightmap_vertex_shader));
        int fragmentShader = MyGLRenderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER,
                TextResourceReader.readTextFileFromResource(context, R.raw.heightmap_fragment_shader));

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables


;

    }


    private int calculateNumElements()
    {
        return (width - 1)* (height-1)*2*3;
    }

    private void loadBitmapData(Bitmap bitmap)
    {
        final int[] pixels = new int[width*height];
        bitmap.getPixels(pixels, 0, width, 0,0, width, height);
        bitmap.recycle();

        int offset = 0;

        for (int row = 0;row < height; row++)
        {
            for(int col = 0; col < width; col++)
            {
                final float xPostion = ((float) col / (width-1)) - 0.5f;
                final float yPostion = ((float) row / (height-1)) - 0.5f;
                final float zPostion = (float) Color.red(pixels[(row * height) + col]) / (float) 255;

                heightmap_vertices[offset++] = xPostion;
                heightmap_vertices[offset++] = yPostion;
                heightmap_vertices[offset++] = zPostion;

                final Point top = getPoint(pixels, row -1, col);
                final Point left = getPoint(pixels, row, col-1);
                final Point right = getPoint(pixels, row , col+1);
                final Point bottom = getPoint(pixels, row +1, col);

                final Vector rightToLeft = Geometry.vectorBetween(right, left);
                final Vector topToBottom = Geometry.vectorBetween(top, bottom);
                final Vector normal = rightToLeft.crossProduct(topToBottom).normalize();

                heightmap_vertices[offset++] = normal.x;
                heightmap_vertices[offset++] = normal.y;
                heightmap_vertices[offset++] = normal.z;

            }


        }

    }

    private void setIndexData()
    {
        draworder = new short[numElements];
        int offset = 0;

        for (int row = 0;row < height-1; row++)
        {
            for(int col = 0; col < width-1; col++)
            {

                short topLeftIndexNum = (short) (row * width + col);
                short topRightIndexNum = (short) (row * width + col +1);
                short bottomLeftIndexNum = (short) ((row+1) * width + col);
                short bottomRightIndexNum = (short) ((row+1) * width + col +1);

                draworder[offset++] = topLeftIndexNum;
                draworder[offset++] = bottomLeftIndexNum;
                draworder[offset++] = topRightIndexNum;

                draworder[offset++] = topRightIndexNum;
                draworder[offset++] = bottomLeftIndexNum;
                draworder[offset++] = bottomRightIndexNum;


            }


        }

    }

    public void draw(float[] mvpMatrix)
    {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // get handle to vertex shader's _Normal member
        mNormalsHandle = GLES20.glGetAttribLocation(mProgram, "a_Normal");



        vertexBuffer.position(0); //1
        glVertexAttribPointer(mPositionHandle, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexBuffer);
        glEnableVertexAttribArray(mPositionHandle);
        vertexBuffer.position(0);

        vertexBuffer.position(POSITION_COMPONENT_COUNT);  //2
        glVertexAttribPointer(mNormalsHandle, NORMAL_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexBuffer);
        glEnableVertexAttribArray(mNormalsHandle);
        vertexBuffer.position(0);


        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");

        // get handle to shape's transformation vector
        mLightHandle = GLES20.glGetUniformLocation(mProgram, "u_VectorToLight");
        MyGLRenderer.checkGlError("glGetUniformLocation");

        // Add light vector
        GLES20.glUniform3f(mLightHandle, Light_Vec[0], Light_Vec[1], Light_Vec[2]);
        //MyGLRenderer.checkGlError("glUniform3f");

        // Draw object
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, draworder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mNormalsHandle);
    }

    private Point getPoint(int[] pixels, int row, int col)
    {
        float x = ((float) col / (float)(width-1))-0.5f;
        float y = ((float) row / (float)(width-1))-0.5f;

        row = clamp(row, 0, width-1);
        col = clamp(col, 0, height-1);

        float z = (float) Color.red(pixels[(row*height) + col])/ (float) 255;

        return new Point(x,y,z);
    }
    private int clamp(int val, int min, int max)
    {
        return Math.max(min,Math.min(max,val));
    }
}
