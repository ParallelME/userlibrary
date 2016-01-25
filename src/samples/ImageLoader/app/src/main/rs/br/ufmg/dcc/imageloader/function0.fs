#pragma version(1)
#pragma rs java_package_name(br.ufmg.dcc.imageloader)

float3 __attribute__((kernel)) root(uchar4 in, uint32_t x, uint32_t y) {
    float3 out;
    out.s0 = ((float) in.r) / 255.0f;
    out.s1 = ((float) in.g) / 255.0f;
    out.s2 = ((float) in.b) / 255.0f;
    return out;
}
