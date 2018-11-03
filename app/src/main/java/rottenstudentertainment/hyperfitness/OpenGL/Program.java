package rottenstudentertainment.hyperfitness.OpenGL;

import android.opengl.GLES20;

public class Program {

    public static int createAndLinkProgram(int vertexShader, int fragmentShader){
        int mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);
        return mProgram;

    }
}
