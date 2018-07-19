package rottenstudentertainment.hyperfitness.workout.page_objects;

/**
 * Created by Merty on 21.11.2017.
 */

public class animator_object
{

    private String keyframes;
    private String bones;

    public animator_object(String keyframes, String bones)
    {
        this.keyframes = keyframes;
        this.bones = bones;
    }

    public String get_keyframes(){return keyframes;}
    public String get_bones(){return bones;}


}
