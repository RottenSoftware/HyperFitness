package rottenstudentertainment.hyperfitness.test;

/**
 * Created by Merty on 07.11.2017.
 */

public class Page_number
{
    private static int i;

//    public Page_number()
//    {
//        i =0;
//    }
    public static void set_i(int new_i)
    {
        i = new_i;
    }
    public static int get_page()
    {
        return i;
    }

    public static void next_page()
    {
        i++;
    }
}
