#version 460 core

const float constant = 1.0F;
const float linear = 0.09F;
const float quadratic = 0.032F;

const mat3 bayerMatrix = mat3(
    vec3(0.0F, 32.0F, 8.0F),
    vec3(48.0F, 16.0F, 40.0F),
    vec3(12.0F, 44.0F, 4.0F)
);

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

    float radius = 16.0F;
    float intensity = max(direction, 0.0F) * (radius * light.color.w);

    return vec4((light.color.rgb * intensity) * attenuation, 1.0F);
}

vec4 minVec4(vec4 l, float x, float y, float z, float a) {
    return vec4(min(l.x, x), min(l.y, y), min(l.z, z), min(l.a, a));
}

vec4 minVec4(vec4 l, vec4 r) {
    return minVec4(l, r.x, r.y, r.z, r.w);
}

vec4 ditherColor(vec4 color) {
    float grayscale = dot(color.rgb, vec3(0.299F, 0.587F, 0.114F));

    vec2 size = vec2(gl_FragCoord.x, gl_FragCoord.y) * 1.0F;
    vec3 offset = vec3(
        bayerMatrix[int(mod(size.y, 3.0F))][int(mod(size.x, 3.0F))],
        bayerMatrix[int(mod(size.y + 1.0F, 3.0F))][int(mod(size.x + 1.0F, 3.0F))],
        bayerMatrix[int(mod(size.y + 2.0F, 3.0F))][int(mod(size.x + 2.0F, 3.0F))]
    )* (1.0F / 64.0F);

    vec3 ditheredColor = color.rgb + (offset * (grayscale - 0.5F));
    return vec4(ditheredColor, 1.0F);
}

void main() {
    vec4 pixel = texture(tSampler, vec3(m_uv)) * vec4(1.0F, 1.0F, 1.0F, 1.0F);
    if (pixel.a == 0) {
        discard;
    }
    vec4 result = vec4(0.0F);

    for(int i = 0; i < size; ++i) {
        result += spherical(lights[i]);
    }
    result = minVec4(result, vec4(m_lightMap.rgb * 1.25F, 1.0F));

    float brightness = 0.75F;
    color = ditherColor(pixel * vec4(result.rgb * brightness, 1.0F));

    if (m_normal.xyz == 0) {
        color = pixel * m_lightMap;
    }
    if (emissiveMap.r > 0) {
        color = pixel;
    }
}