package rottenstudentertainment.hyperfitness.workout;

import android.content.Context;

import rottenstudentertainment.hyperfitness.Logic.DoingWorkout;
import rottenstudentertainment.hyperfitness.OpenGLES20Activity;
import rottenstudentertainment.hyperfitness.R;
import rottenstudentertainment.hyperfitness.Texture;
import rottenstudentertainment.hyperfitness.TextureHelper;
import rottenstudentertainment.hyperfitness.animation.animated_figure;
import rottenstudentertainment.hyperfitness.globals.AppState;
import rottenstudentertainment.hyperfitness.new_animation.Animator;
import rottenstudentertainment.hyperfitness.new_animation.Bones;
import rottenstudentertainment.hyperfitness.new_animation.Extract_mesh_bones_keyframes;
import rottenstudentertainment.hyperfitness.new_animation.Model;
import rottenstudentertainment.hyperfitness.new_animation.Motion_Keyframe;
import rottenstudentertainment.hyperfitness.new_animation.Static_Object;
import rottenstudentertainment.hyperfitness.new_animation.fast_animated_figure;
import rottenstudentertainment.hyperfitness.user_interface.Rotation_angle;
import rottenstudentertainment.hyperfitness.util.Accessor;
import rottenstudentertainment.hyperfitness.util.AndroidButton;
import rottenstudentertainment.hyperfitness.util.Button;
import rottenstudentertainment.hyperfitness.util.MatrixHelper;
import rottenstudentertainment.hyperfitness.util.Timer;
import rottenstudentertainment.hyperfitness.util.Touch_point_parser;
import rottenstudentertainment.hyperfitness.workout.page_objects.AndroidButton_Object;

import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;

/**
 * Created by Merty on 29.10.2017.
 */




public class Page
{
    private Context context;
    private Button button;
    private Touch_point_parser touchpoint;
    private Texture texture;

    private long endTime;
    private long startTime;
    private boolean once;
    private rottenstudentertainment.hyperfitness.Fitness.Page curPage;
    private Model model;

    private ArrayList<Texture> textures;
    private ArrayList<Button> buttons;
    private ArrayList<animated_figure> animations;
    private ArrayList<Timer> timers;
    private ArrayList<AndroidButton> androidButtons;
    public static boolean paused = true;

    private ArrayList<Animator> animator;

    //upload from custom textfiles
    private ArrayList<fast_animated_figure> fast_animations;

    private ArrayList<Static_Object> statics;

    public Page( Context context, Model model, rottenstudentertainment.hyperfitness.Fitness.Page curPage){
        this.context = context;
        this.curPage = curPage;
        this.model = model;
        paused = true;
        model.genTexture(context);

        //ui
        textures = new ArrayList<>();
        buttons = new ArrayList<>();
        timers = new ArrayList<>();
        androidButtons = new ArrayList<>();


        //3d
        animations = new ArrayList<>();
        statics = new ArrayList<>();

        //put model
        fast_animations = new ArrayList<>();
        fast_animations.add( new fast_animated_figure(context, model, curPage.keyframes));

        animator = new ArrayList<>();

        DoingWorkout.setTitle( (OpenGLES20Activity) context, curPage);
        DoingWorkout.setUpExerciseStartQueque();
        //load all textures from text file demand
        /*
        for( int i = 0; i < content.images.size(); i++)
        {
            textures.add( new Texture(context, TextureHelper.loadTexture(context,
                    Accessor.all_resource_ints( content.images.get(i).get_name() ) ), R.drawable.start_display , content.images.get(i).get_width(), content.images.get(i).get_height() ) );
           // Log.e("after", "in");
        }
        */
/*
        for(int i = 0; i < content.androidButtons.size(); i++)
        {
            AndroidButton_Object  andButtonObj = content.androidButtons.get(i);
            androidButtons.add( new AndroidButton( context, andButtonObj));
        }
*/
/*
       for(int i = 0; i < content.buttons.size(); i++)
        {
            buttons.add( new Button(context, TextureHelper.loadTexture(context,
                    R.drawable.sq_start_display),
                    content.buttons.get(i).get_width(), content.buttons.get(i).get_height(), content.buttons.get(i).get_pos_x() , content.buttons.get(i).get_pos_y() ));
        }
*/
/*
        for(int i = 0; i < content.animations.size(); i++)
        {
            animations.add( new animated_figure(context, Accessor.all_resource_ints( content.animations.get(i).get_file_name() ), content.animations.get(i).get_width()) );
        }
*/
/*
        for(int i = 0; i < content.fast_anims.size(); i++)
        {
            fast_animations.add( new fast_animated_figure(context, content.fast_anims.get(i).get_mesh_name(), content.fast_anims.get(i).get_bones_name(), content.fast_anims.get(i).get_keyframes_name(), content.fast_anims.get(i).get_texture_name()));
        }
*/

/*
        for(int i = 0; i < content.statics.size(); i++)
        {
            statics.add( new Static_Object(context, Accessor.all_resource_ints( content.statics.get(i).get_file_name() ), "model_texture") );
        }
*/

        if( curPage.timer != null){
            int startTime = curPage.timer.time;
            timers.add( new Timer(context, startTime));
        }

/*
        for(int i = 0; i < content.animators.size(); i++)
        {
            Bones bones = Extract_mesh_bones_keyframes.read_in_bones_binary(context,  content.animators.get(i).get_bones());
            Motion_Keyframe keyframe = Extract_mesh_bones_keyframes.read_in_keyframes_binary(context,  content.animators.get(i).get_keyframes());

            animator.add( new Animator( keyframe.get_time_stamps(), keyframe.getLocal_keyframes_2d(), bones.get_structure() ) );
        }
*/

       // texture = new Texture(context, TextureHelper.loadTexture(context, R.drawable.sq_start_display), R.drawable.start_display, 0.5f, 0.2f);
        //Log.e("number of drawable", "Workout:" + R.drawable.sq_start_display);
       // button = new Button(context, 0, 0.3f, 0.2f, 0f, -0.5f);
        touchpoint = new Touch_point_parser(0f,0f);
        once = false;
    }

    public void draw_page(float[] m, float[] m3d)
    {

        // 3d stuff
        glEnable(GL_DEPTH_TEST);


        //Log.e("pages", "draw: rotation input: " + Rotation_angle.angle);

       float[] rot_m3d = MatrixHelper.move_rotate_scale_matrix(m3d, 1f, 0f, 0f, 0f, 0f, 0f, Rotation_angle.angle);

        for(int i = 0; i < animations.size(); i++)
        {
            animations.get(i).draw( m3d);
        }

        for(int i = 0; i < statics.size(); i++)
        {
            statics.get(i).draw( m3d);
        }


        float[] keyframebuffer = new float[16];
        for(int i = 0; i < fast_animations.size(); i++)
        {
            if(i == 0 && animator.size() > 0) {keyframebuffer = animator.get(0).update_animation();}
            if(animator.size() > 0) fast_animations.get(i).draw(  rot_m3d, keyframebuffer, true); // for synchronized keyframes
            else fast_animations.get(i).draw(rot_m3d, keyframebuffer, false);
        }

        //ui stuff
        glDisable(GL_DEPTH_TEST);

        for(int i = 0; i < textures.size(); i++)
        {
            //Log.e("class:Page","draw x_pos: " + content.images.get(i).get_pos_x()   );
            //textures.get(i).draw( MatrixHelper.move_rot_objects(m,1.0f, content.images.get(i).get_pos_x(), content.images.get(i).get_pos_y(),0f));
        }

        for(int i = 0; i < buttons.size(); i++)
        {
            //buttons.get(i).draw_button( MatrixHelper.move_rot_objects(m,1.0f, content.buttons.get(i).get_pos_x(), content.buttons.get(i).get_pos_y(),0f));
        }

        for(int i = 0; i < timers.size(); i++)
        {
            Timer timer = timers.get(i);
            timer.draw_timer( m, paused);
            if( timer.getTime() == 0){
                AppState.next_page_forUI( (OpenGLES20Activity) context);
            }
        }
    }

    public void update_input(Touch_point_parser touch_point)
    {
        //Log.e("star_page update:", "x:" + touch_point.get_x()); Log.e("Workout update:", "y:" + touch_point.get_y());
        touchpoint.set_x(touch_point.get_x());
        touchpoint.set_y(touch_point.get_y());

        for(int i = 0; i < buttons.size(); i++)
        {
            buttons.get(i).update_button(touchpoint);
        }

        //button.update_button(touchpoint);

    }

    public boolean status()
    {

        for(int i = 0; i < buttons.size(); i++)
        {
            if (buttons.get(i).activated && !once) {
                startTime = System.currentTimeMillis(); once = true;
            }
            if(buttons.get(i).activated)
            {
                endTime = System.currentTimeMillis();
                if( endTime - startTime > 500) return true;
            }
        }
        return false;
    }

}








