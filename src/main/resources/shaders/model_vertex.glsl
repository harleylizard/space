#version 460 core

layout (location = 0) in vec4 position;
layout (location = 1) in vec3 uv;
layout (location = 2) in vec3 normal;
layout (location = 3) in vec4 lightMap;

layout (binding = 0, column_major) uniform matrices {
    mat4 projection;
    mat4 view;
    mat4 model;
};

out gl_PerVertex {
    vec4 gl_Position;
};

out vec4 m_position;
out vec3 m_uv;
out vec3 m_normal;
out vec4 m_lightMap;

void main() {
    gl_Position = projection * view * model * position;

    m_position = model * position;
    m_uv = uv;
    m_normal = mat3(transpose(inverse(model))) * normal;
    m_lightMap = lightMap;
}