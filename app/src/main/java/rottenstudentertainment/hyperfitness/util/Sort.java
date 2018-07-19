package rottenstudentertainment.hyperfitness.util;

/**
 * Created by Merty on 11.08.2017.
 */

public class Sort
{
    public static short[] sort_float_array(float[] array)
    {

        short[] order = new short[array.length];
        for(short i = 0; i < array.length-1; i++) order[i]=i;  // original order

        short s_buffer;
        float f_buffer;

        for(int i = 0; i < array.length-1; i++)
        {
            for(int p = 0; p < array.length-1 - i; p++)
            if(array[p] < array[p+1])
            {
                f_buffer = array[p];
                array[p] = array[p+1];
                array[p+1] = f_buffer;

                s_buffer = order[p];
                order[p] = order[p+1];
                order[p+1] = s_buffer;
            }
        }

        return order;
    }
}
