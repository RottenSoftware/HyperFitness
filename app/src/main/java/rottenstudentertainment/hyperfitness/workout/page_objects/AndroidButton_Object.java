package rottenstudentertainment.hyperfitness.workout.page_objects;

public class AndroidButton_Object {
    private String buttonId;
    private String buttonText;
    private String buttonFunction;

    public AndroidButton_Object(String buttonId, String buttonText,  String buttonFunction)
    {
        this.buttonId = buttonId;
        this.buttonText = buttonText;
        this.buttonFunction = buttonFunction;
    }

    public String getButtonId(){
        return buttonId;
    }

    public String getButtonText(){
        return buttonText;
    }

    public String getButtonFunction(){
        return buttonFunction;
    }
}
