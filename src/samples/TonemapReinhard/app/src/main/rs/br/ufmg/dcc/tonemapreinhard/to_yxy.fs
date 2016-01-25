#pragma version(1)
#pragma rs java_package_name(br.ufmg.dcc.tonemapreinhard)

float3 __attribute__((kernel)) root(float3 in, uint32_t x, uint32_t y) {
    float3 result, out;

    // These constants are the conversion coefficients.
    result.s0 = result.s1 = result.s2 = 0.0f;
    result.s0 += 0.5141364f * in.s0;
    result.s0 += 0.3238786f * in.s1;
    result.s0 += 0.16036376f * in.s2;
    result.s1 += 0.265068f * in.s0;
    result.s1 += 0.67023428f * in.s1;
    result.s1 += 0.06409157f * in.s2;
    result.s2 += 0.0241188f * in.s0;
    result.s2 += 0.1228178f * in.s1;
    result.s2 += 0.84442666f * in.s2;
    float w = result.s0 + result.s1 + result.s2;

    if(w > 0.0f) {
        out.s0 = result.s1;     // Y
        out.s1 = result.s0 / w; // x
        out.s2 = result.s1 / w; // y
    }
    else {
        out.s0 = out.s1 = out.s2 = 0.0f;
    }

    return out;
}
