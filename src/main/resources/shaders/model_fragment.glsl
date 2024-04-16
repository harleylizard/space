#version 460 core

const float constant = 1.0F;
const float linear = 0.09F;
const float quadratic = 0.032F;

out vec4 color;

in vec4 m_position;
in vec3 m_uv;
in vec3 m_normal;

uniform sampler2DArray tSampler;

vec3 normalMap = texture(tSampler, vec3(m_uv.x, m_uv.y, m_uv.z + 1)).xyz;

uniform int size;

struct Light {
    vec4 position;
    vec4 color;
};

layout (binding = 0) buffer lightsBuffer {
    Light lights[];
};

vec4 spherical(Light light) {
    vec3 normal = m_normal;

    vec3 magnitude = light.position.xyz - vec3(-m_position.x, m_position.y, -m_position.z);
    vec3 direction = normalize(magnitude);

    vec3 transformed = normalize(normal + (normalMap.rgb * 2.0 - 1.0));
    float intensity = max(dot(transformed, direction), 0.0F) * (10.0F * light.color.w * 1.0F);

    float distance = length(magnitude);
    float attenuation = 1.0F / ((constant + linear) * distance + quadratic * distance * distance);

    float rangeAttenuation = smoothstep(0.0, 1.0F, distance);

    return vec4((light.color.rgb * intensity) * attenuation * rangeAttenuation, 1.0F);
}

void main() {
    vec4 result = vec4(0.25F);

    for(int i = 0; i < size; ++i) {
        result += spherical(lights[i]);
    }
    result = vec4(min(result.x, 1.0F), min(result.y, 1.0F), min(result.z, 1.0F), min(result.w, 1.0F));

    color = texture(tSampler, vec3(m_uv)) * vec4(1.0F, 1.0F, 1.0F, 1.0F) * result;
}