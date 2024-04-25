#version 460 core

layout (location = 0) in vec4 position;
layout (location = 1) in vec2 uv;

layout (binding = 0, column_major) uniform matrices {
    mat4 projection;
    mat4 view;
    mat4 model;
};

out gl_PerVertex {
    vec4 gl_Position;
};

out vec2 m_uv;

void main() {
    gl_Position = position;
    m_uv = uv;
}