package rottenstudentertainment.hyperfitness.Fitness;

import java.util.List;
import org.simpleframework.xml.*;

public class Model_Object {
    public List<String> texture_names;
    public String model_name;
    public String bones_name;
    public String scale; //to sclae models; not set -> 1.0 -> 0 else parseFloat
}
