package rottenstudentertainment.hyperfitness.OpenGL;

import android.opengl.GLES20;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class DataBuffer {

    public static Buffer setBuffer( Object data) {

        ByteBuffer bb;
        int numberPerUnit =0;


        if( data instanceof short[] || data instanceof float[]){
            int numberData = 0;
            if( data instanceof short[]){
                numberPerUnit = 2;
                numberData  = ( (short[]) data).length;
            } else if( data instanceof float[]){
                numberPerUnit = 4;
                numberData  = ( (float[]) data).length;
            }

            bb = ByteBuffer.allocateDirect(
                    // (# of coordinate values * 4 bytes per float)
                    //(# of coordinate values * 2 bytes per short)
                    numberData * numberPerUnit);
            bb.order(ByteOrder.nativeOrder());

            if( data instanceof float[]){
                FloatBuffer dataBuffer;
                dataBuffer = bb.asFloatBuffer();
                dataBuffer.put( (float[]) data);
                dataBuffer.position(0);
                return dataBuffer;
            }  else if( data instanceof short[]){
                ShortBuffer dataBuffer;
                dataBuffer = bb.asShortBuffer();
                dataBuffer.put( (short[]) data);
                dataBuffer.position(0);
                return dataBuffer;
            }
        }
        return null;
        }


    public static void enableAndSetVertexAttributes( int id, int stride, int size, Buffer buffer){

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(id);
        // Prepare the triangle coordinate data
        int dataType =0;
        boolean normalized = false;
        if( buffer instanceof ShortBuffer){
            dataType = GLES20.GL_SHORT;
        } else if( buffer instanceof FloatBuffer){
            dataType = GLES20.GL_FLOAT;
        }
        GLES20.glVertexAttribPointer(
                id, size,
                dataType, normalized,
                stride, buffer);

    }
}
