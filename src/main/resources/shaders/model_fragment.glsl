#version 460 core

out vec4 color;

in vec2 m_uv;

uniform sampler2DArray textureSampler;

void main() {
    color = texture(textureSampler, vec3(m_uv, 0)) * vec4(1.0F, 1.0F, 1.0F, 1.0F);
}