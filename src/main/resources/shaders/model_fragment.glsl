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
    vec3 normal = m_normal;

    vec3 magnitude = light.position.xyz - m_position.xyz;

    float distance = length(magnitude);
    float attenuation = 1.0F / ((constant + linear) * distance + quadratic * distance * distance);

    vec3 transformed = normalize(normal);

    float direction = dot(normalize(normal), normalize(magnitude));
    float intensity = max(direction, 0.0F) * (10.0F * light.color.w);

    return vec4((light.color.rgb * intensity) * attenuation, 1.0F);
}

vec4 sphericalAmbient(Light light) {
    vec3 magnitude = light.position.xyz - m_position.xyz;

    float distance = length(magnitude);
    float attenuation = 1.0F / ((constant + linear) * distance + quadratic * distance * distance);

    vec3 direction = normalize(magnitude);
    float intensity = max(direction.r, 0.0F) * (10.0F * light.color.w);

    return vec4((light.color.rgb * intensity) * attenuation, 1.0F);
}

void main() {
    vec4 result = vec4(0.0F);
    vec4 ambient = vec4(0.0F);

    for(int i = 0; i < size; ++i) {
        Light light = lights[i];
        result += spherical(light);
        ambient += sphericalAmbient(light);
    }

    result = vec4(min(result.x, 1.0F), min(result.y, 1.0F), min(result.z, 1.0F), min(result.w, 1.0F));
    ambient = vec4(min(ambient.x, 1.0F), min(ambient.y, 1.0F), min(ambient.z, 1.0F), min(ambient.w, 1.0F));

    vec4 pixel = texture(tSampler, vec3(m_uv)) * vec4(1.0F, 1.0F, 1.0F, 1.0F);
    color = pixel * result;

    if (m_normal.xyz == 0) {
        color = pixel * ambient;
    }
    if (emissiveMap.r > 0) {
        color = pixel;
    }
}