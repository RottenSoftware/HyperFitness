    uniform mat4 uMVPMatrix;

    uniform mat4 invbones[50];
    uniform mat4 pose[50];

    //uniform mat4 bindm;
 //test
    uniform mat4 it;
    uniform mat4 test_pose;

    uniform vec3 test;

    attribute vec4 vPosition;

    attribute vec3 weight_vec3;
    attribute vec3 a_indices;



    attribute vec2 a_TextureCoordinates;
    varying vec2 v_TextureCoordinates;

    void main() {


    	vec4 totalLocalPos = vec4(0.0);



    	for(int i=0;i<3;i++)
    	{
    		mat4 jointTransform =  pose[int(a_indices[i])] *   invbones[int (a_indices[i])];
    		//vec4 bindpos = bindm * vPosition;
    		vec4 posePosition = jointTransform * vPosition;
    		totalLocalPos += posePosition * weight_vec3[i];
        }

//        if(int(test[0]) == 1)
//        {
//            totalLocalPos = vec4(0.0);
//
//            for(int i=0;i<3;i++)
//            {
//                mat4 jointTransform = pose[int(a_indices[i])] * invbones[int (a_indices[i])];
//                vec4 posePosition = jointTransform * vPosition;
//                totalLocalPos += posePosition * weight_vec3[i];
//            }
//        }
//        if(int(a_indices[0]) == 100) totalLocalPos = vPosition;
//        if(int(a_indices[0]) == 100)
//        {
//            totalLocalPos = test_pose * it * vPosition;
//        }
        //if(int(test[0]) == 1) totalLocalPos = vPosition;


         v_TextureCoordinates = a_TextureCoordinates;
         gl_Position = uMVPMatrix * totalLocalPos;
    }