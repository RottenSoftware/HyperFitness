package rottenstudentertainment.hyperfitness.Fitness;

public class Description extends Text {

    @Override
    public String toString(){
        String description = super.toString();
        return  description.replace("\\n","\n"); //line breaks
    }

}
