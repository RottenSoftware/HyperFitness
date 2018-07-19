package rottenstudentertainment.hyperfitness.new_animation;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by Merty on 27.12.2016.
 */


public class Extract_mesh_bones_keyframes
{

    public static Mesh read_in_mesh_data(Context context, int resourceId)
    {
        float[] mesh_data = {0.0f, 0.0f, 0.0f};  // to extract
        float[] normals_data = {0.0f, 0.0f, 0.0f};
        float[] tex_data = {0.0f, 0.0f, 0.0f};

        float[] weights_data = {0.0f, 0.0f, 0.0f}; // array of weights
        short[] indices_data = {0, 0};  //array of bone indices

        ArrayList <Float> l_mesh_data = new ArrayList<>();  // to extract
        ArrayList <Float> l_normals_data = new ArrayList<>();  // to extract
        ArrayList <Float> l_tex_data = new ArrayList<>();  // to extract
        ArrayList <Float> l_weights_data = new ArrayList<>();  // to extract
        ArrayList <Short> l_indices_data = new ArrayList<>();  // to extract




        try
        {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


            String nextLine;


            int counter = 0; // skip every even line
            int type_counter= 0;

            while ( (nextLine = bufferedReader.readLine()) != null )
            {
                if(counter == 0)
                {
                    counter++;
                    continue;
                }
                else
                {
                    String[] posData = nextLine.split(" ");

                    if(type_counter == 0)
                    {

                        for(int i= 0; i < posData.length; i++)
                        {
                            l_mesh_data.add(  Float.parseFloat(posData[i]) );
                        }
                        type_counter++;
                    }
                    else if(type_counter == 1)
                    {
                        for(int i= 0; i < posData.length; i++)
                        {
                            l_normals_data.add(  Float.parseFloat(posData[i]) );
                        }
                        type_counter++;
                    }
                    else if(type_counter == 2)
                    {
                        for(int i= 0; i < posData.length; i++)
                        {
                            l_tex_data.add(  Float.parseFloat(posData[i]) );
                        }
                        type_counter++;
                    }
                    else if(type_counter == 3)
                    {
                        for(int i= 0; i < posData.length; i++)
                        {
                            l_weights_data.add(  Float.parseFloat(posData[i]) );
                        }
                        type_counter++;
                    }
                    else if(type_counter == 4)
                    {
                        for(int i= 0; i < posData.length; i++)
                        {
                            l_indices_data.add(  Short.parseShort(posData[i]) );
                        }
                        type_counter++;
                    }



                    counter =0;
                }






             }


            //vertices arralist to float array
            mesh_data = new float[l_mesh_data.size()];
            for(int i = 0; i < l_mesh_data.size(); i++) mesh_data[i] = l_mesh_data.get(i);

            //normals arralist to float array
            normals_data = new float[l_normals_data.size()];
            for(int i = 0; i < l_normals_data.size(); i++) normals_data[i] = l_normals_data.get(i);

            //texture coords arralist to float array
            tex_data = new float[l_tex_data.size()];
            for(int i = 0; i < l_tex_data.size(); i++) tex_data[i] = l_tex_data.get(i);

            //weights arralist to float array
            weights_data = new float[l_weights_data.size()];
            for(int i = 0; i < l_weights_data.size(); i++) weights_data[i] = l_weights_data.get(i);

            //normals arralist to float array
            indices_data = new short[l_indices_data.size()];
            for(int i = 0; i < l_indices_data.size(); i++) indices_data[i] = l_indices_data.get(i);



            return new Mesh(mesh_data, normals_data, tex_data, weights_data, indices_data);








        } catch (IOException e)
        {
            throw new RuntimeException("Could not open Resource: " + resourceId, e);
        } catch (Resources.NotFoundException nfe)
        {
            throw new RuntimeException("Resource not found: " + resourceId, nfe);
        }

    }





        public static Bones read_in_bones_data(Context context, int resourceId)
        {
            float[] inv_bind_matrices = {0.0f, 0.0f, 0.0f};  // to extract


            ArrayList <Float> l_inv_bind_matrices = new ArrayList<>();  // to extract
            Vector<Vector<Integer>> bone_structure = new Vector<>();




            try
            {
                InputStream inputStream = context.getResources().openRawResource(resourceId);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


                String nextLine;


                int counter = 0; // skip every even line

                while ( (nextLine = bufferedReader.readLine()) != null )
                {
                    if(counter >= 3)
                    {

                        String[] posData = nextLine.split(" ");

                        if(counter == 3)
                        {

                            for(int i= 0; i < posData.length; i++)
                            {
                                l_inv_bind_matrices.add(  Float.parseFloat(posData[i]) );
                            }
                        }
                        else if(counter > 4 && posData.length !=0)
                        {
                            Vector<Integer> structure = new Vector<>();
                            for(int i= 0; i < posData.length; i++)
                            {
                                structure.add(  Integer.parseInt(posData[i]) );
                            }
                            bone_structure.add(structure);
                        }

                    }

                    counter++;


                }




                //inv bind matrices arralist to float array
                inv_bind_matrices = new float[l_inv_bind_matrices.size()];
                for(int i = 0; i < l_inv_bind_matrices.size(); i++) inv_bind_matrices[i] = l_inv_bind_matrices.get(i);



                return new Bones( inv_bind_matrices, bone_structure );








            } catch (IOException e)
            {
                throw new RuntimeException("Could not open Resource: " + resourceId, e);
            } catch (Resources.NotFoundException nfe)
            {
                throw new RuntimeException("Resource not found: " + resourceId, nfe);
            }

        }



    public static Motion_Keyframe read_in_keyframe_data(Context context, int resourceId)
    {
        float[] timestamps = {0.0f, 0.0f, 0.0f};  // to extract
        float[][] local_keyframes_2d = new float[1][1];

        ArrayList <Float> l_timestamps = new ArrayList<>();  // to extract
        Vector<Vector<Float>> keyframes_for_bones = new Vector<>();



        try
        {
            DataInputStream inputStream = new  DataInputStream(context.getResources().openRawResource(resourceId));
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


            String nextLine;


            int counter = 0; // skip every even line

            while ( (nextLine = bufferedReader.readLine()) != null )
            {
                if(counter >= 1)
                {

                    String[] posData = nextLine.split(" ");

                    if(counter == 1)
                    {

                        for(int i= 0; i < posData.length; i++)
                        {
                            l_timestamps.add(  Float.parseFloat(posData[i]) );
                        }
                    }
                    else if(counter > 2 && posData.length !=0)
                    {
                        Vector<Float> keyframes = new Vector<>();
                        for(int i= 0; i < posData.length; i++)
                        {
                            keyframes.add(  Float.parseFloat(posData[i]) );
                        }
                        keyframes_for_bones.add(keyframes);
                    }

                }

                counter++;


            }




            //inv bind matrices arralist to float array
            timestamps = new float[l_timestamps.size()];
            for(int i = 0; i < l_timestamps.size(); i++) timestamps[i] = l_timestamps.get(i);

            local_keyframes_2d = new float[keyframes_for_bones.size()][keyframes_for_bones.get(0).size()];


            for(int i = 0; i < local_keyframes_2d.length; i++)
            {
                for(int j = 0; j < local_keyframes_2d[0].length; j++)
                {
                    local_keyframes_2d[i][j] = keyframes_for_bones.get(i).get(j);
                }
            }



            return new Motion_Keyframe( timestamps, local_keyframes_2d );




        } catch (IOException e)
        {
            throw new RuntimeException("Could not open Resource: " + resourceId, e);
        } catch (Resources.NotFoundException nfe)
        {
            throw new RuntimeException("Resource not found: " + resourceId, nfe);
        }

    }


        public static Mesh read_in_mesh_binary(Context context, String file_name)
        {

            file_name = "3d/animation_mesh/" + file_name + ".mesh_binary";


            try
            {
                InputStream inputStream = context.getResources().getAssets().open(file_name);
            	ObjectInputStream input_stream = new ObjectInputStream(inputStream);

                float [] mesh_data = get_float_array_from_binary(input_stream);



                float[] normals_data =  get_float_array_from_binary(input_stream);

                float[] tex_data =  get_float_array_from_binary(input_stream);

                float[] weights_data = get_float_array_from_binary(input_stream);

                short[] indices_data = get_short_array_from_binary(input_stream);

                input_stream.close();
                inputStream.close();

                return new Mesh(mesh_data, normals_data, tex_data, weights_data, indices_data);


            } catch (IOException e)
            {
                throw new RuntimeException("Could not open Resource: " +file_name, e);
            } catch (Resources.NotFoundException nfe)
            {
                throw new RuntimeException("Resource not found: " + file_name, nfe);
            }

        }

    public static Bones read_in_bones_binary(Context context, String file_name)
    {


        Log.e("extract_bones"," entered function");

        file_name = "3d/bones/" + file_name + ".bones_binary";

        Log.e("extract_bones"," file name: " + file_name);

        try
        {
            InputStream inputStream = context.getResources().getAssets().open(file_name);
            ObjectInputStream input_stream = new ObjectInputStream(inputStream);

            //DataInputStream inputStream = new DataInputStream(context.getResources().openRawResource(resourceId));
            //ObjectInputStream input_stream = new ObjectInputStream(inputStream);


            Log.e("extract_bones"," opened file");

            String[] name_ids =  get_String_array_from_binary(input_stream);


            float[] invBindMats = get_float_array_from_binary(input_stream);

            Vector<Vector<Integer>> bone_structure = new Vector<>();
            Vector<Integer> bones;

            int n_bones = input_stream.readInt();

            Log.e("extract_bones"," numbr of bones:" + n_bones);

            int n_family;

            for(int i = 0; i < n_bones; i++)
            {
                bones = new Vector<>();
                n_family = input_stream.readInt();
                Log.e("extract_bones"," number of bones:" + n_family  );
                for(int j  = 0; j < n_family; j++) bones.add(input_stream.readInt());
                bone_structure.add(bones);
            }

            Log.e("extract_bones"," last bone:" + bone_structure.get(bone_structure.size()-1).get(0)  );

            input_stream.close();
            inputStream.close();

            return new Bones(invBindMats,  bone_structure );


        } catch (IOException e)
        {
            throw new RuntimeException("Could not open Resource: " + file_name, e);
        } catch (Resources.NotFoundException nfe)
        {
            throw new RuntimeException("Resource not found: " + file_name, nfe);
        }

    }




    public static Motion_Keyframe read_in_keyframes_binary(Context context,String file_name )
    {

        file_name = "3d/keyframes/" + file_name + ".keyframe_binary";


        try
        {
            InputStream inputStream = context.getResources().getAssets().open(file_name);
            ObjectInputStream input_stream = new ObjectInputStream(inputStream);

            //InputStream inputStream = context.getResources().openRawResource(resourceId);
            //ObjectInputStream input_stream = new ObjectInputStream(inputStream);

            float[] timestamp = get_float_array_from_binary(input_stream);



            int  n_keyframes = input_stream.readInt();
            int  n_bones_x_16 = input_stream.readInt();

            float[][] local_keyframes_2d = new float[n_keyframes][n_bones_x_16];


            for (int i = 0; i < n_keyframes; i++)
            {
                for (int j = 0; j < n_bones_x_16; j++) local_keyframes_2d[i][j] = input_stream.readFloat();
            }


            input_stream.close();
            inputStream.close();

            return new Motion_Keyframe( timestamp, local_keyframes_2d);


        } catch (IOException e) {
            throw new RuntimeException("Could not open Resource: " + file_name, e);
        } catch (Resources.NotFoundException nfe) {
            throw new RuntimeException("Resource not found: " + file_name, nfe);
        }

    }


        public static float[] get_float_array_from_binary(ObjectInputStream input_stream) throws IOException
        {
            int array_size = input_stream.readInt();
            float [] array_data = new float[array_size];
            for(int i = 0; i < array_size; i++) array_data[i] = input_stream.readFloat();

            return array_data;
        }



    public static short[] get_short_array_from_binary(ObjectInputStream input_stream) throws IOException
    {
        int array_size = input_stream.readInt();
        short [] array_data = new short[array_size];
        for(int i = 0; i < array_size; i++) array_data[i] = input_stream.readShort();

        return array_data;
    }


        public static String[] get_String_array_from_binary(ObjectInputStream input_stream) throws IOException
        {
            int array_size = input_stream.readInt();
            String [] array_data = new String[array_size];
            for(int i = 0; i < array_size; i++) array_data[i] = input_stream.readUTF();

            return array_data;
        }



}



