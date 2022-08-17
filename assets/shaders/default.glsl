#type vertex
#version 450 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;
layout (location = 2) in vec2 aTexCoord;

uniform mat4 uProjectionMatrix;
uniform mat4 uViewMatrix;

out vec4 fColor;
out vec2 fTexCoord;

void main()
{
    fColor = aColor;
    fTexCoord = aTexCoord;
    gl_Position = uProjectionMatrix * uViewMatrix * vec4(aPos, 2.0);
}

#type fragment
#version 450 core

uniform float uTime;
uniform sampler2D uSampler;

in vec4 fColor;
in vec2 fTexCoord;

out vec4 color;

void main()
{
    color = texture(uSampler, fTexCoord);
}
