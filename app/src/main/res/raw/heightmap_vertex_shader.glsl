

            uniform mat4 uMVPMatrix;
                    uniform vec3 u_VectorToLight;
                    attribute vec3 vPosition;
                    attribute vec3 a_Normal;

                    varying vec3 v_Color;
                    void main() {

                    v_Color = mix(vec3(0.18,0.467,0.153), vec3(0.66, 0.67,0.68), vPosition.z);

                     vec3 VectorToLight = normalize(u_VectorToLight);
                     vec3 scaledNormal = normalize(a_Normal);
                     float diffuse = max(dot(scaledNormal, VectorToLight), 0.0);
                     v_Color = (v_Color*diffuse +0.2)/1.2f;
                     gl_Position = uMVPMatrix * vec4(vPosition, 1.0)*2.0f;
                    }
