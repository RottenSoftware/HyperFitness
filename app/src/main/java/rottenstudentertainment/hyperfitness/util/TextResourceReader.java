package rottenstudentertainment.hyperfitness.util;

import android.content.Context;
import android.content.res.Resources;

import rottenstudentertainment.hyperfitness.animation.Bones;
import rottenstudentertainment.hyperfitness.animation.Mesh;
import rottenstudentertainment.hyperfitness.animation.Motion_Keyframe;

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

    public static Mesh readObjectFileFromColladaResource(Context context, int resourceId)
    {
        float[] mesh_data = {0.0f, 0.0f, 0.0f};  // to extract
        float[] normals_data = {0.0f, 0.0f, 0.0f};
        float[] tex_data = {0.0f, 0.0f, 0.0f};
        short[] elements = {0,0};
        int poly_num=0;
        float[] weights_data = {0.0f, 0.0f, 0.0f}; // array of weights
        short[] v_count_data = {0, 0};  //stores by how many joints the vertex is influenced
        short[] v_data = {0, 0}; //stores by which bones and weight location


        String geomtag = "<library_geometries>";
        String farraytag = "<float_array";
        String controltag = "<library_controllers>";
int count=0;
        try
        {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            boolean[] node = {false, false, false};  // to check for right lines

            String nextLine;


            int check_list = 0; // keep track of loaded data arrays

            while ( (nextLine = bufferedReader.readLine()) != null )
            {


                String[] posData = nextLine.split(" ");


                for(int i = 0; i <  posData.length; i++)
                {
                    if(posData[i].length() == 0) continue;
                    else if (posData[i].equals(geomtag)) node[0] = true;
                    else if (posData[i].equals(farraytag) && node[0])
                    {
                        if(check_list == 0) { mesh_data = extract_mesh_data(posData, i+2); check_list++;  break;}
                        else if (check_list == 1) { normals_data = extract_mesh_data(posData, i+2); check_list++;  break;}
                        else if (check_list == 2) { tex_data = extract_mesh_data(posData, i+2); check_list++;  break;}
                        break;
                    }
                    else if(check_list == 3)
                    {
                       char[] poly_tester = posData[i].toCharArray();
                        if(poly_tester[0] == 60 && poly_tester[1] == 112 && poly_tester[2] == 62  )
                        {
                            //Log.e("poly tester", "poly tester 0 1 2; :" + poly_tester[0] + poly_tester[1]+ poly_tester[2]);
                            elements = collada_poly_extractor(poly_num, poly_tester, posData, i);
                            check_list++;
                            node[0]= false;
                            break;
                        }
                    }
                    // skinning information
                    if( posData[i].equals(controltag) ) {node[1] = true; break;}
                    else if (posData[i].equals(farraytag) && node[1]  && check_list == 4 ) {check_list++; break; }
                    else if (posData[i].equals(farraytag) && node[1]  && check_list == 5 ) { weights_data = extract_mesh_data(posData, i+2); check_list++;  break;}
                    else if(check_list == 6)
                    {
                        char[] poly_tester = posData[i].toCharArray();
                        if(poly_tester[0] == 60 && poly_tester[1] == 118 && poly_tester[2] == 99 && poly_tester[3] == 111 && poly_tester[4] == 117 && poly_tester[5] == 110  && poly_tester[6] == 116 )
                        {
                           // Log.e("poly tester", "poly tester 0 1 2; :" + poly_tester[0] + poly_tester[1]+ poly_tester[2]);
                            v_count_data = collada_poly_extractor(poly_num, poly_tester, posData, i);
                            check_list++;
                            break;
                        }
                    }
                    else if(check_list == 7)
                    {
                        char[] poly_tester = posData[i].toCharArray();
                        if(poly_tester[0] == 60 && poly_tester[1] == 118 && poly_tester[2] == 62 )
                        {
                            //Log.e("poly tester", "poly tester 0 1 2; :" + poly_tester[0] + poly_tester[1]+ poly_tester[2]);
                            v_data = collada_poly_extractor(poly_num, poly_tester, posData, i);
                            check_list++;
                            break;
                        }
                    }
                }

                if(check_list == 8) break;
                //Log.e("checklist", "checklist:" + check_list + "node 1" + node[1]);
             }
            //for(int i = 0; i < v_data.length; i++)Log.e("weights", ": " + v_data[i]);
            return new Mesh(mesh_data, normals_data, tex_data, elements, weights_data, v_count_data, v_data);








        } catch (IOException e)
        {
            throw new RuntimeException("Could not open Resource: " + resourceId, e);
        } catch (Resources.NotFoundException nfe)
        {
            throw new RuntimeException("Resource not found: " + resourceId, nfe);
        }
        //return null;
    }





    public static Bones readbonesFromColladaSource(Context context, int resourceId)
    {
        String[] names_id = {"Herbert"};
        float[] inv_bind_mats = {0.0f, 0.0f};




        String nametag = "<Name_array";
        String farraytag = "<float_array";
        String controltag = "<library_controllers>";

        String start_node = "<node";
        String end_node = "</node>";
        String matrix_tag = "<matrix";
        String visualtag = "<library_visual_scenes>";

        int bone_cnt;
        int current_bone_id = -1;


        String[] name_ids = {"Hallo"};

        Vector <Vector<Integer>> bones_structure;


        try
        {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            boolean[] node = {false, false, false};  // to check for right lines


            bones_structure= new Vector<>();
            bone_cnt = 0;

            int prev_vecpos = 0;

            String nextLine;

            float[] bind_matrices = {0.0f, 0.0f};

            int check_list = 0; // keep track of loaded data arrays

            boolean start_node_tagging = false;
            int node_tagging = 0;

            while ( (nextLine = bufferedReader.readLine()) != null )
            {


                String[] posData = nextLine.split(" ");

//Log.e("check_list", ": " + check_list);
                for(int i = 0; i <  posData.length; i++)
                {
                    if (posData[i].length() == 0) continue;
                    else if (posData[i].equals(controltag) ) check_list++;
                    else if (posData[i].equals(nametag) && check_list == 1) {check_list++; name_ids = string_extractor(posData, i+2); break;}
                    else if (posData[i].equals(farraytag) && check_list == 2) {check_list++; inv_bind_mats = extract_mesh_data( posData, i+2 ); break;}
                    else if (posData[i].equals(visualtag) && check_list == 3)
                    {
                        bones_structure = new Vector<>();
                        for(int p = 0; p < name_ids.length;p++) bones_structure.add( new Vector<Integer>());
                        bind_matrices = new float[name_ids.length*16];
                        check_list++; break;
                    }
                    else if (posData[i].equals(start_node) && check_list == 4)
                    {
                        int new_bone_id = check_and_get_bone_id(posData[i+1], name_ids);
                        //Log.e("bone id", ": " + new_bone_id);
                        if(new_bone_id >= 0)
                        {
                            for(int p = 0; p < bone_cnt - node_tagging; p++) bones_structure.get(new_bone_id).add( bones_structure.get(prev_vecpos).get(p) );
                            bones_structure.get(new_bone_id).add(new_bone_id);
                            bone_cnt++;
                            start_node_tagging = true;
                            prev_vecpos = new_bone_id;
                            current_bone_id = new_bone_id;
                        }
                        break;
                        //if(start_node_tagging) node_tagging++;
                    }
                    else if(posData[i].equals(end_node) && check_list == 4 && start_node_tagging) node_tagging++;
                    else if(posData[i].equals(matrix_tag) && check_list == 4 && start_node_tagging && current_bone_id > -1)
                    {
                        float[] dummy = matrix_extractor(posData, i+1);
                        for(int p = 0; p < 16; p++) bind_matrices[current_bone_id*16 + p] = dummy[p];
                    }
                }
            }

           //Log.e("checklist", ": " + check_list);
            //for(int p = 0; p < bind_matrices.length; p++) Log.e("bind matrices", "element: " + bind_matrices[p]);

            //for(int i = 0; i < name_ids.length; i++ )Log.e("out", ": " + name_ids[i] );

           // Log.e("bone structure", "size: " + bones_structure.size() + "bone struture 1 size: " + bones_structure.get(1).size());

            return new Bones(name_ids, inv_bind_mats, bones_structure, bind_matrices);








        } catch (IOException e)
        {
            throw new RuntimeException("Could not open Resource: " + resourceId, e);
        } catch (Resources.NotFoundException nfe)
        {
            throw new RuntimeException("Resource not found: " + resourceId, nfe);
        }
        //return null;
    }



    public static Motion_Keyframe readKeyframesFromColada(Context context, int resourceId, String [] nameids, Vector <Vector<Integer>> bones_structure)
    {

        float[] time_stamps = {0.0f, 0.0f};
        float[][] pos_array_2d = new float[1][1];
        ArrayList<float[]> poses = new ArrayList<>();

        String nametag = "<Name_array";
        String farraytag = "<float_array";
        String animtag = "<library_animations>";


        int count=0;
        try
        {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            boolean[] node = {false, false, false};  // to check for right lines

            String nextLine;


            int check_list = 0; // keep track of loaded data arrays
            int idpos = 0;
            while ( (nextLine = bufferedReader.readLine()) != null )
            {


                String[] posData = nextLine.split(" ");

//Log.e("check_list", ": " + check_list);
                for(int i = 0; i <  posData.length; i++)
                {
                    if (posData[i].length() == 0) continue;
                    else if (posData[i].equals(animtag) ) check_list++;
                    else if (posData[i].equals(farraytag) && check_list == 1) {check_list++; time_stamps = extract_mesh_data( posData, i+2 ); pos_array_2d = new float[nameids.length][time_stamps.length*16]; break;}
                    else if (check_list == 2)
                    {
                        if( idpos < nameids.length && posData[i].equals(farraytag) )
                        {
                            int bone_pos = test_for_bone(posData[i+1], nameids);
                            if(bone_pos > -1)
                            {
                                pos_array_2d[bone_pos] = extract_mesh_data(posData, i + 2);
                                idpos++;
                                break;
                            }
                        }
                    }

                }
                if(idpos >= nameids.length)
                {
                    for(int i = 0; i < nameids.length; i++)poses.add(pos_array_2d[i]);
                    break;
                }
            }
            //Log.e("pose matrixes", ": " + poses.size());
            //for(int i= 0; i < nameids.length; i++) Log.e("nameids", ": " + nameids[i]);
            //for(int i = 0; i< poses.get(0).length; i++) Log.e("pose matrixes", ": " + poses.get(0)[i]);
            return new Motion_Keyframe(time_stamps, poses, bones_structure);








        } catch (IOException e)
        {
            throw new RuntimeException("Could not open Resource: " + resourceId, e);
        } catch (Resources.NotFoundException nfe)
        {
            throw new RuntimeException("Resource not found: " + resourceId, nfe);
        }
        //return null;
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






}



