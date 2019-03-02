package rottenstudentertainment.hyperfitness.util;

import android.content.Context;
import android.content.res.Resources;

import rottenstudentertainment.hyperfitness.new_animation.Bones;
import rottenstudentertainment.hyperfitness.new_animation.Mesh;
import rottenstudentertainment.hyperfitness.new_animation.Motion_Keyframe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Merty on 27.12.2016.
 */


public class TextResourceReader
{
    public static String readTextFileFromResource(Context context, int resourceId)
    {
        StringBuilder body = new StringBuilder();


        try
        {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String nextLine;

            while ( (nextLine = bufferedReader.readLine()) != null )
                {
                    body.append(nextLine);
                    body.append('\n');
                }
        } catch (IOException e)
        {
            throw new RuntimeException("Could not open Resource: " + resourceId, e);
        } catch (Resources.NotFoundException nfe)
        {
            throw new RuntimeException("Resource not found: " + resourceId, nfe);
        }
        return body.toString();
    }



    private static float[] extract_mesh_data(String[] input_string, int offset)
    {
        float[] output_data;
        StringBuilder extraction_count = new StringBuilder();
        StringBuilder extraction_first_x = new StringBuilder();


        char[] extractor = input_string[offset].toCharArray();

        boolean b_vertex_cnt = false;
        for (int p = 0; p < extractor.length; p++)
        {
            if (!b_vertex_cnt) {
                if ((extractor[p] > 47 && extractor[p] < 58) || extractor[p] == 46 || extractor[p] == 45)
                    extraction_count.append(extractor[p]);
            } else if ((extractor[p] > 47 && extractor[p] < 58) || extractor[p] == 46 || extractor[p] == 45 || extractor[p] == 101 )
                extraction_first_x.append(extractor[p]);
            if (extractor[p] == 62) b_vertex_cnt = true;
        }
        output_data = new float[(int) Float.parseFloat(extraction_count.toString())];
        output_data[0] = Float.parseFloat(extraction_first_x.toString());
        int i_cnt=1;
        for (int p = offset+1; p < input_string.length-1; p++)
        {
            if(input_string[p].length() > 0) { output_data[i_cnt] = Float.parseFloat(input_string[p]); i_cnt++; }
        }
        //Log.e("from function", "last string: " + input_string[input_string.length-1]);
        extractor = input_string[input_string.length-1].toCharArray();
        StringBuilder extraction_last_z = new StringBuilder();
        for (int p = 0; p < extractor.length; p++) {
            if ((extractor[p] > 47 && extractor[p] < 58) || extractor[p] == 46 || extractor[p] == 45)
                extraction_last_z.append(extractor[p]);
            else break;
        }

        output_data[output_data.length -1] = Float.parseFloat(extraction_last_z.toString());

        //for (int p = 0; p < output_data.length; p++) Log.e("from function", "value: " + output_data[p]);

        return output_data;
    }


    private static short[] collada_poly_extractor(int poly_num, char[] poly_tester, String[] input_string, int offset)
    {
        short[] output_data;
        Vector<Short> element_list = new Vector<>();

        element_list.add( Short.parseShort(number_extractor(input_string[offset])));

        for (int p = offset+1; p < input_string.length-1; p++)
        {
            if(input_string[p].length() > 0) { element_list.add( Short.parseShort(input_string[p]));  }
        }
        //Log.e("from function", "last string: " + input_string[input_string.length-1]);
        poly_tester = input_string[input_string.length-1].toCharArray();
        StringBuilder extraction_last_z = new StringBuilder();
        for (int p = 0; p < poly_tester.length; p++) {
            if ((poly_tester[p] > 47 && poly_tester[p] < 58) || poly_tester[p] == 46 || poly_tester[p] == 45)
                extraction_last_z.append(poly_tester[p]);
            else break;
        }

        if(extraction_last_z.length() != 0)element_list.add( Short.parseShort(extraction_last_z.toString())  );

        //Log.e("poly extractor", "laste element from element list: " + element_list.get(element_list.size() -1));
        output_data = new short[element_list.size()];
        for(int i = 0; i < output_data.length; i++)
        {
           output_data[i]=  element_list.get(i);
        }

        return output_data;
    }

    private static String number_extractor(String mixed_number)
    {
        char[] text_and_number = mixed_number.toCharArray();
        StringBuilder extraction = new StringBuilder();
        for (int p = 0; p < text_and_number.length; p++) {
            if ((text_and_number[p] > 47 && text_and_number[p] < 58) || text_and_number[p] == 46 || text_and_number[p] == 45)
                extraction.append(text_and_number[p]);
        }
        return extraction.toString();
    }

    private static float[] matrix_extractor(String[] input_string, int offset)
    {
        float[] output_data;
        StringBuilder extraction_count = new StringBuilder();
        StringBuilder extraction_first_x = new StringBuilder();
        Vector<Float> output_vec = new Vector<>();

        char[] extractor = input_string[offset].toCharArray();

        for (int p = 0; p < extractor.length; p++)
        {
                if ((extractor[p] > 47 && extractor[p] < 58) || extractor[p] == 46 || extractor[p] == 45 || extractor[p] == 101)  extraction_first_x.append(extractor[p]);
        }
        output_vec.add( Float.parseFloat( extraction_first_x.toString() ) );
        for (int p = offset+1; p < input_string.length-1; p++)
        {
            if(input_string[p].length() > 0) { output_vec.add( Float.parseFloat(input_string[p]) );  }
        }
        extractor = input_string[input_string.length-1].toCharArray();
        StringBuilder extraction_last_z = new StringBuilder();
        for (int p = 0; p < extractor.length; p++) {
            if ((extractor[p] > 47 && extractor[p] < 58) || extractor[p] == 46 || extractor[p] == 45)
                extraction_last_z.append(extractor[p]);
            else break;
        }

        output_vec.add( Float.parseFloat(extraction_last_z.toString()) );

        output_data = new float[output_vec.size()];
        for(int p = 0; p < output_data.length; p++) output_data[p] = output_vec.get(p);

        return output_data;

    }

    private static String[] string_extractor(String[] input_string, int offset)
    {
        StringBuilder extraction_first_x = new StringBuilder();

        Vector<String> names_list = new Vector<>();

        char[] extractor = input_string[offset].toCharArray();

        boolean start = false;
        for (int p = 0; p < extractor.length; p++)
        {
                if(start) extraction_first_x.append(extractor[p]);
                if (extractor[p] == 62 ) start = true;
        }
        names_list.add(extraction_first_x.toString());

        //Log.e("names_list", ": " + names_list.get(0) + " offset: " + offset );
        int i_cnt=1;
        for (int p = offset+1; p < input_string.length-1; p++)
        {
            //Log.e("input_string", ": " + input_string[p] + " p: " + p + "i cnt: " + i_cnt);
            if(input_string[p].length() > 0) { names_list.add(input_string[p]); i_cnt++; }
        }
        extractor = input_string[input_string.length-1].toCharArray();
        StringBuilder extraction_last_z = new StringBuilder();

        boolean stop = false;
        for (int p = 0; p < extractor.length; p++) {
            if (extractor[p] == 60 ) stop = true;
               if(!stop) extraction_last_z.append(extractor[p]);
        }

        names_list.add(extraction_last_z.toString());
        String[] output_string = new String[names_list.size()];
        for (int i = 0; i < names_list.size(); i++)
        {
            output_string[i]=names_list.get(i);
            //Log.e("string array", output_string[i]);
        }
        return output_string;

    }
private static int test_for_bone(String line, String[] nameid)
{
    String armature = "id=\"Armature_";
    String moutarray = "_pose_matrix-output-array\"";

    for(int i = 0; i< nameid.length;i++)
    {
        String tester = armature + nameid[i] + moutarray;

        //Log.e("active", "line: " + line + " tester: " + tester);

        if(line.equals(tester)) return i;
    }

     return -1;
}


    private static int check_and_get_bone_id(String candidate, String[] bone_id_names)
    {
        //Log.e("candidate", ": " + candidate);
        char[] name_tester = candidate.toCharArray();
        StringBuilder builder = new StringBuilder();
        boolean add = false;
        for(int i = 0; i < name_tester.length; i++)
        {
            if(name_tester[i] == 34 && !add) {add = true; continue;}
            else if(name_tester[i] == 34 && add) {add = false; continue; }
            if(add)  builder.append(name_tester[i]);
        }
        String test = builder.toString();
        for(int i = 0; i < bone_id_names.length; i++)
        {
            if( bone_id_names[i].equals(test)) return i;
        }

        return -1;
    }


    public static String readTextFromAssets(Context context, String file_name)
    {
        StringBuilder body = new StringBuilder();


        try
        {
            InputStream inputStream = context.getResources().getAssets().open(file_name);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String nextLine;

            while ( (nextLine = bufferedReader.readLine()) != null )
            {
                body.append(nextLine);
                body.append('\n');
            }
        } catch (IOException e)
        {
            throw new RuntimeException("Could not open Resource: " + file_name, e);
        } catch (Resources.NotFoundException nfe)
        {
            throw new RuntimeException("Resource not found: " + file_name, nfe);
        }
        return body.toString();
    }


    public static String[] readFilesFromFolder(Context context, String folderName)
    {
        try {
            String[] workoutList = context.getResources().getAssets().list( folderName);
            return workoutList;

        } catch (IOException e) {
        }
        return null;
    }



}



