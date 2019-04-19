    uniform mat4 uMVPMatrix;
    uniform mat4 invbones[75];
    uniform mat4 pose[75];


    attribute vec3 vPosition;
    attribute vec4 normals;
    attribute vec3 weight_vec3;
    attribute vec3 a_indices;
    attribute vec2 a_TextureCoordinates;

    varying vec2 v_TextureCoordinates;
    varying vec3 adjusted_normal;

    void main() {
    	vec4 totalLocalPos = vec4(0.0);
    	vec4 totalLocalNormal = vec4(0.0);
        vec4 normal_sin_w = vec4( normals.xyz, 0.0);

    	for(int i=0;i<3;i++)
    	{
    		mat4 jointTransform =  pose[int(a_indices[i])] *   invbones[int (a_indices[i])];
    		vec4 posePosition = jointTransform * vec4( vPosition, 1.0);
    		totalLocalPos += posePosition * weight_vec3[i];

    		vec4 worldNormal = jointTransform * normal_sin_w;
            totalLocalNormal += worldNormal * weight_vec3[i];
        }
         adjusted_normal = ( uMVPMatrix * totalLocalNormal).xyz;
         v_TextureCoordinates = a_TextureCoordinates;
         gl_Position = uMVPMatrix * totalLocalPos;
    }