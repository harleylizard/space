#version 460 core

const float constant = 1.0F;
const float linear = 0.09F;
const float quadratic = 0.032F;

out vec4 color;

in vec4 m_position;
in vec3 m_uv;
in vec3 m_normal;
in vec4 m_lightMap;

uniform sampler2DArray tSampler;

vec3 normalMap = texture(tSampler, vec3(m_uv.x, m_uv.y, m_uv.z + 1)).xyz;
vec3 emissiveMap = texture(tSampler, vec3(m_uv.x, m_uv.y, m_uv.z + 2)).xyz;

uniform int size;

struct Light {
    vec4 position;
    vec4 color;
};

layout (binding = 0) buffer lightsBuffer {
    Light lights[];
};

vec4 spherical(Light light) {
    vec3 magnitude = light.position.xyz - m_position.xyz;

    float distance = length(magnitude);
    float attenuation = 1.0F / ((constant + linear) * distance + quadratic * distance * distance);

    vec3 transformed = normalize(m_normal);

    float direction = dot(transformed, normalize(magnitude));
    float intensity = max(direction, 0.0F) * (10.0F * light.color.w);

    return vec4((light.color.rgb * intensity) * attenuation, 1.0F);
}

void main() {
    vec4 result = vec4(0.0F);

    for(int i = 0; i < size; ++i) {
        Light light = lights[i];
        result += spherical(light);
    }
    result = vec4(min(result.x, 1.0F), min(result.y, 1.0F), min(result.z, 1.0F), min(result.w, 1.0F));

    vec4 pixel = texture(tSampler, vec3(m_uv)) * vec4(1.0F, 1.0F, 1.0F, 1.0F);

    vec4 added = vec4(result.rgb * 3.0F + m_lightMap.rgb, 1.0F);
    vec4 clamped = vec4(min(added.r, 1.0F), min(added.g, 1.0F), min(added.b, 1.0F), min(added.a, 1.0F));
    color = pixel * clamped * result;

    if (m_normal.xyz == 0) {
        color = pixel * m_lightMap;
    }
    if (emissiveMap.r > 0) {
        color = pixel;
    }
}