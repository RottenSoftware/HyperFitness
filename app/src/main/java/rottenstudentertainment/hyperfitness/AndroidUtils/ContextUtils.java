package rottenstudentertainment.hyperfitness.AndroidUtils;

import android.content.Context;

import rottenstudentertainment.hyperfitness.OpenGLES20Activity;

public class ContextUtils {

    public static OpenGLES20Activity getMainActivity( Context context){
        return (OpenGLES20Activity) context;
    }
}
