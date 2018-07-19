package rottenstudentertainment.hyperfitness.test;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Merty on 06.11.2017.
 */

public class test_arraylists
{

    public ArrayList<test_array> arrays;

    public test_arraylists()
    {
       arrays = new ArrayList<>();
      //füllen
       arrays.add(new test_array(1,2));
       arrays.add(new test_array(3,4));


        Log.e("test_arraylists", "arrays.get(0).get(0): " + arrays.get(0).numbers.get(0));
    }
}
