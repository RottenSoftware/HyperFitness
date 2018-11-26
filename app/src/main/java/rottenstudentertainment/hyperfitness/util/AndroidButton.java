package rottenstudentertainment.hyperfitness.util;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import rottenstudentertainment.hyperfitness.AndroidUtils.ViewUtils;
import rottenstudentertainment.hyperfitness.OpenGLES20Activity;
import rottenstudentertainment.hyperfitness.R;
import rottenstudentertainment.hyperfitness.workout.page_objects.AndroidButton_Object;

public class AndroidButton {

    private String buttonFunction;
    private Button button;
    private Context context;

    private boolean activated;

    public AndroidButton(Context context, AndroidButton_Object andButtonObj){
      activated = false;
      this.context = context;
      this.buttonFunction = andButtonObj.getButtonFunction();
      getButtonById(context, andButtonObj.getButtonId());
      setButtonListener();
      setButtonText( andButtonObj.getButtonText());
    }

    private Button getButtonById( Context context, String id){
        //OpenGLES20Activity activity = (OpenGLES20Activity)context;
        //int resID = activity.getResources().getIdentifier(id, "id", activity.getPackageName());
        //Button button = (Button) activity.findViewById( resID);
        button = (Button) ViewUtils.findViewByString(context, id);
        return button;
    }

    public void setButtonListener() {
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ButtonFunctions.doFunction( context, button, buttonFunction);
                }
            });
    }

    private void setButtonText( String buttonText){
        button.setText( buttonText);
    }

}
