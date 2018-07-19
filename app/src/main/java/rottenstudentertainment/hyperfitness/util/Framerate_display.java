package rottenstudentertainment.hyperfitness.util;

import android.util.Log;

/**
 * Created by Merty on 08.11.2017.
 */


public class Framerate_display
{
    private static long start = System.currentTimeMillis();;
    private static long end = System.currentTimeMillis();;
    private static int i = 0;


    public static void check_framerate()
    {
        end = System.currentTimeMillis();

        if(end -start > 1000)
        {
            Log.e("Framerate_display:", "current framerate: " + i);
            start = end;
            i = 0;
        }
        else i++;
    }

}
