package rottenstudentertainment.hyperfitness.Fitness;

import java.util.Locale;

public class Text {
    private String en;
    private String de;
    private String es;
    private String zh;

    @Override
    public String toString(){
        String locale = Locale.getDefault().toString();
        if(locale.equals("en") || locale.contains("en_")){
            if( en != null)return en;
        } else if ( locale.equals("de") || locale.contains("de_")){
            if( de != null)return de;
        } else if ( locale.equals("es") || locale.contains("es_")){
            if( es != null)return es;
        } else if ( locale.equals("zh") || locale.contains("zh_")){
            if( zh != null)return zh;
        }
        return en;
    }
}
