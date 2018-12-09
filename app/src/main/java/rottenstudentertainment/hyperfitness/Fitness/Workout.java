package rottenstudentertainment.hyperfitness.Fitness;

import java.util.List;
import org.simpleframework.xml.*;

public class Workout {
    public Title title;
    public int level; //eins bis 10 oder so
    public String description;
    public List<Model_Object> models;
    public List <Page> pages;
    public List<String> tags;
}
