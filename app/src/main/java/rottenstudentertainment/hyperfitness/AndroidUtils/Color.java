package rottenstudentertainment.hyperfitness.AndroidUtils;

public class Color {
    private int r;
    private int g;
    private int b;
    private int a;

    public Color( String hexColor){
        hex2Rgb( hexColor);
        this.a = 1;
    }

    public Color( int r, int g, int b){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1;
    }
    public float getR(){
        return intToFloatValue( r);
    }

    public float getG(){
        return intToFloatValue( g);
    }

    public float getB(){
        return intToFloatValue( b);
    }

    public float getA(){
        return intToFloatValue( a);
    }

    private void hex2Rgb(String hexColor) {
        this.a = Integer.valueOf(hexColor.substring(1, 3), 16);
        this.r = Integer.valueOf(hexColor.substring(3, 5), 16);
        this.g = Integer.valueOf(hexColor.substring(5, 7), 16);
        this.b = Integer.valueOf(hexColor.substring(7, 9), 16);
    }

    private float intToFloatValue( int baseColor){
        float newBaseColor = baseColor/255f;
        return newBaseColor;
    }

    @Override
    public String toString(){
        return "rgba( "+
                this.r + ", " +
                this.g + ", " +
                this.b + ", " +
                this.a + ")";
    }
}
