#pragma version(1)
#pragma rs java_package_name(br.ufmg.dcc.tonemapreinhard)

float3 __attribute__((kernel)) root(float3 in, uint32_t x, uint32_t y) {
    float3 result, val, out;

    val.y = in.s0;     // Y
    result.s1 = in.s1; // x
    result.s2 = in.s2; // y

    if(val.y > 0.0f && result.s1 > 0.0f && result.s2 > 0.0f) {
        val.x = result.s1 * val.y / result.s2;
        val.z = val.x / result.s1 - val.x - val.y;
    }
    else {
        val.x = val.z = 0.0f;
    }

    // These constants are the conversion coefficients.
    out.s0 = out.s1 = out.s2 = 0.0f;
    out.s0 += 2.5651f * val.x;
    out.s0 += -1.1665f * val.y;
    out.s0 += -0.3986f * val.z;
    out.s1 += -1.0217f * val.x;
    out.s1 += 1.9777f * val.y;
    out.s1 += 0.0439f * val.z;
    out.s2 += 0.0753f * val.x;
    out.s2 += -0.2543f * val.y;
    out.s2 += 1.1892f * val.z;

    return out;
}
