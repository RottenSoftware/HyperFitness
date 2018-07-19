package rottenstudentertainment.hyperfitness.animation;

import static java.lang.Math.sqrt;

/**
 * Created by Merty on 28.08.2017.
 * to interploate matrix rotations
 */

public class Quaternion {
    public static float[] matrix_quaternion_extract(float[] matrix) {
        float w, x, y, z;
        float diagonal = matrix[0] + matrix[5] + matrix[10];
        if (diagonal > 0) {
            float w4 = (float) (sqrt(diagonal + 1f) * 2f);
            w = w4 / 4f;
            x = (matrix[9] - matrix[6]) / w4;
            y = (matrix[2] - matrix[8]) / w4;
            z = (matrix[4] - matrix[1]) / w4;
        } else if ((matrix[0] > matrix[5]) && (matrix[0] > matrix[10])) {
            float x4 = (float) (sqrt(1f + matrix[0] - matrix[5] - matrix[10]) * 2f);
            w = (matrix[9] - matrix[6]) / x4;
            x = x4 / 4f;
            y = (matrix[1] + matrix[4]) / x4;
            z = (matrix[2] + matrix[8]) / x4;
        } else if (matrix[5] > matrix[10]) {
            float y4 = (float) (sqrt(1f + matrix[5] - matrix[0] - matrix[10]) * 2f);
            w = (matrix[2] - matrix[8]) / y4;
            x = (matrix[1] + matrix[4]) / y4;
            y = y4 / 4f;
            z = (matrix[6] + matrix[9]) / y4;
        } else {
            float z4 = (float) (sqrt(1f + matrix[10] - matrix[0] - matrix[5]) * 2f);
            w = (matrix[4] - matrix[1]) / z4;
            x = (matrix[2] + matrix[8]) / z4;
            y = (matrix[6] + matrix[9]) / z4;
            z = z4 / 4f;
        }
        float[] quaternion = {w, x, y, z};
        quaternion = normalize_quat(quaternion);
        return quaternion;
    }

    public static float[] normalize_quat(float[] vec) {
        float sum = 0f;
        for (int i = 0; i < vec.length; i++) sum += vec[i] * vec[i];
        for (int i = 0; i < vec.length; i++) vec[i] = vec[i] / (float) sqrt(sum);
        return vec;
    }


    public static float[] interpolate_quadts(float[] a, float[] b, float progress) {
        float[] result = new float[4];
        a = normalize_quat(a);
        b = normalize_quat(b);
        float dot = a[0] * b[0] + a[1] * b[1] + a[2] * b[2] + a[3] * b[3];
        float blendI = 1f - progress;
        if (dot < 0) {
            result[0] = blendI * a[0] + progress * -b[0];
            result[1] = blendI * a[1] + progress * -b[1];
            result[2] = blendI * a[2] + progress * -b[2];
            result[3] = blendI * a[3] + progress * -b[3];
        } else {
            result[0] = blendI * a[0] + progress * b[0];
            result[1] = blendI * a[1] + progress * b[1];
            result[2] = blendI * a[2] + progress * b[2];
            result[3] = blendI * a[3] + progress * b[3];
        }
        result = normalize_quat(result);
        return result;
    }

    public static float[] dump_matrix(float[] quaternion) {
        return quaternion;
    }

    public static float[] create_rot_matrix(float[] quaternion)
    {
        float[] matrix = new float[16];
        float w = quaternion[0];
        float x = quaternion[1];
        float y = quaternion[2];
        float z = quaternion[3];
        final float xy = x * y;
        final float xz = x * z;
        final float xw = x * w;
        final float yz = y * z;
        final float yw = y * w;
        final float zw = z * w;
        final float xSquared = x * x;
        final float ySquared = y * y;
        final float zSquared = z * z;
        matrix[0] = 1 - 2 * (ySquared + zSquared);
        matrix[1] = 2 * (xy - zw);
        matrix[2] = 2 * (xz + yw);
        matrix[3] = 0;
        matrix[4] = 2 * (xy + zw);
        matrix[5] = 1 - 2 * (xSquared + zSquared);
        matrix[6] = 2 * (yz - xw);
        matrix[7] = 0;
        matrix[8] = 2 * (xz - yw);
        matrix[9] = 2 * (yz + xw);
        matrix[10] = 1 - 2 * (xSquared + ySquared);
        matrix[11] = 0;
        matrix[12] = 0;
        matrix[13] = 0;
        matrix[14] = 0;
        matrix[15] = 1;

        return matrix;
    }


    public static float[] interpolate_matrices(float[] matrix_one, float[] matrix_two, float progress)
    {
        return create_rot_matrix(  interpolate_quadts( matrix_quaternion_extract(matrix_one) , matrix_quaternion_extract(matrix_two), progress) );
    }

}