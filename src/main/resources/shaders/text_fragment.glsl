#version 460 core

out vec4 color;

in vec2 m_uv;

uniform sampler2D sampler;

void main() {
    color = texture(sampler, m_uv) * vec4(1.0F, 1.0F, 1.0F, 1.0F);
}