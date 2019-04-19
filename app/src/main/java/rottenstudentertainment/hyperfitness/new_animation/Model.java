package rottenstudentertainment.hyperfitness.new_animation;

import android.content.Context;

import rottenstudentertainment.hyperfitness.TextureHelper;

/**
 * stores Mesh, Bones and Texture
 */
public class Model {
    public Mesh mesh;
    public Bones bones;
    public int texture;
    public String texturePath;
    public float scale = 1.0f;

    public Model( Mesh mesh, Bones bones, int texture){
        this.mesh = mesh;
        this.bones = bones;
        this.texture = texture;
    }

    public Model( Mesh mesh, String scaleString, Bones bones, String texturePath){
        this.mesh = mesh;
        this.bones = bones;
        this.texturePath = texturePath;
        if( scaleString != null) this.scale = Float.parseFloat(scaleString);
    }

    public void genTexture( Context context){
        if( texturePath != null){
            texture = TextureHelper.loadAssetTexture( context, texturePath);
        }

    }
}
