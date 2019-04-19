package rottenstudentertainment.hyperfitness.Fitness;

import android.content.Context;


import rottenstudentertainment.hyperfitness.TextureHelper;
import rottenstudentertainment.hyperfitness.globals.Logger;
import rottenstudentertainment.hyperfitness.new_animation.Bones;
import rottenstudentertainment.hyperfitness.new_animation.Extract_mesh_bones_keyframes;
import rottenstudentertainment.hyperfitness.new_animation.Mesh;
import rottenstudentertainment.hyperfitness.new_animation.Model;
import rottenstudentertainment.hyperfitness.serialize.XmlSerializer;

public class WorkoutWrapper {

    public Workout workout;
    public Model model;


    public WorkoutWrapper(){

    }

    public void getWorkoutFromFile(Context context, String filename){
        //bsp path: filename= "workouts/Workout number 2__merty__181213__erstes,workout.xml"
        this.workout = XmlSerializer.getWorkout( context, "workouts/" + filename);
    }

    public void loadModel( Context context){
        Model_Object model_obj = getModelFilenames();
        Mesh mesh = Extract_mesh_bones_keyframes.read_in_mesh_binary( context, model_obj.model_name);
        Bones bones = Extract_mesh_bones_keyframes.read_in_bones_binary( context, model_obj.bones_name);
        String texturePath = "3d/textures/" + model_obj.texture_names.get(0) + ".png";
        //int texture = TextureHelper.loadAssetTexture( context, texturePath); only possible in opengl thread ( renderer)
        String scaleString = model_obj.scale;
        model = new Model( mesh, scaleString, bones, texturePath);
    }

    private Model_Object getModelFilenames(){
        if( workout.models.size() > 0){
            return workout.models.get(0); // implemetieren für mehere Models in Zukunft
        }
        Logger.LogError("getModel", "no model found!");
        return null;
    }
}
