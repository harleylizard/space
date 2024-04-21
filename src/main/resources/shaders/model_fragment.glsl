#version 460 core

const float CONSTANT = 1.0F;
const float LINEAR = 0.09F;
const float QUADRATOC = 0.032F;

const mat4 BAYER_MATRIX = mat4(
0.0F,  8.0F,  2.0F, 10.F,
12.0F,  4.0F, 14.0F,  6.0F,
3.0F, 11.0F,  1.0F,  9.0F,
15.0F,  7.0F, 13.0F,  5.0F
);

out vec4 color;

in vec4 m_position;
in vec3 m_uv;
in vec3 m_normal;
in vec4 m_lightMap;

uniform sampler2DArray tSampler;

vec3 normalMap = texture(tSampler, vec3(m_uv.xy, m_uv.z + 1)).xyz;
vec3 emissiveMap = texture(tSampler, vec3(m_uv.xy, m_uv.z + 2)).xyz;

uniform int size;

struct Light {
    vec4 position;
    vec4 color;
};

layout (binding = 0) buffer lightsBuffer {
    Light lights[];
};

vec4 createPointLight(Light light, mat3 tbn) {
    vec3 magnitude = light.position.xyz - m_position.xyz;

    float distance = length(magnitude);
    float attenuation = 1.0F / ((CONSTANT + LINEAR) * distance + QUADRATOC * distance * distance);

    // Normalise between -1 and 1.
    vec3 transformed = tbn * (normalMap * 2.0F - 1.0F);

    float direction = dot(transformed, normalize(magnitude));

    float radius = 16.0F;
    float intensity = direction * (radius * light.color.w);

    return vec4((light.color.rgb * intensity) * attenuation, 1.0F);
}

vec4 minVec4(vec4 l, float x, float y, float z, float a) {
    return vec4(min(l.x, x), min(l.y, y), min(l.z, z), min(l.a, a));
}

vec4 minVec4(vec4 l, vec4 r) {
    return minVec4(l, r.x, r.y, r.z, r.w);
}

vec4 ditherColor(vec4 color) {
    float grayscale = dot(color.rgb, vec3(0.2126F, 0.7152F, 0.0722F));

    vec2 ditherPos = fract(gl_FragCoord.xy / 4.0F);
    grayscale += (BAYER_MATRIX[int(ditherPos.x)][int(ditherPos.y)] - 7.5F) / 16.0F;

    vec2 fragCoords = mod(gl_FragCoord.xy, 4.0F);
    vec3 offset = vec3(BAYER_MATRIX[int(fragCoords.x)][int(fragCoords.y)] / 16.0F);

    vec3 ditheredColor = color.rgb + (offset * (grayscale - 0.5F));
    return vec4(ditheredColor, 1.0);
}

// Tangent, Bitangent, Normal matrix.
mat3 getTBN() {
    vec3 edge0 = dFdx(m_position.xyz);
    vec3 edge1 = dFdy(m_position.xyz);
    vec2 delta0 = dFdx(m_uv.xy);
    vec2 delta1 = dFdy(m_uv.xy);

    float det = 1.0F / (delta0.x * delta1.y - delta1.x * delta0.y);
    vec3 tangent = vec3(det * (delta1.y * edge0 - delta0.y * edge1));
    vec3 bitangent = vec3(det * (-delta1.x * edge0 + delta0.x * edge1));
    return mat3(tangent, bitangent, m_normal);
}

void main() {
    vec4 pixel = texture(tSampler, vec3(m_uv));
    if (pixel.a == 0) { // Transparent textures.
        discard;
    }
    vec4 result = vec4(0.0F);

    mat3 tbn = getTBN();
    for(int i = 0; i < size; ++i) {
        result += createPointLight(lights[i], tbn);
    }
    // Making sure the light doesn't become completely white when adding low light levels.
    result = minVec4(result, vec4(m_lightMap.rgb * 1.25F, 1.0F));

    float brightness = 1.25F;
    color = ditherColor(pixel * vec4(result.rgb * brightness, 1.0F));

    if (m_normal.xyz == 0) { // Ambient models
        color = pixel * vec4(m_lightMap.rgb, 1.0F);
    }
    if (emissiveMap.r > 0) { // Emissive textures.
        color = ditherColor(pixel);
    }
}