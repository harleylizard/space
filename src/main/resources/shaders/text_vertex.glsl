#version 460 core

layout (location = 0) in vec4 position;

layout (binding = 0, column_major) uniform matrices {
    mat4 projection;
    mat4 view;
    mat4 model;
};

struct Type {
    mat4 position;
    vec4 uv;
};

layout (binding = 1, column_major) buffer bufferStorage {
    Type types[];
};

out gl_PerVertex {
    vec4 gl_Position;
};

out vec2 m_uv;

void main() {
    Type type = types[gl_InstanceID];
    gl_Position = projection * view * model * type.position * position;

    vec4 uv = type.uv;
    if (gl_VertexID == 0) {
        m_uv = vec2(uv.x, uv.w);
    }
    if (gl_VertexID == 1) {
        m_uv = vec2(uv.z, uv.w);
    }
    if (gl_VertexID == 2) {
        m_uv = vec2(uv.z, uv.y);
    }
    if (gl_VertexID == 3) {
        m_uv = vec2(uv.x, uv.y);
    }
}