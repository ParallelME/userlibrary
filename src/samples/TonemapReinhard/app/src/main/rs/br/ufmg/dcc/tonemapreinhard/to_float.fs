#pragma version(1)
#pragma rs java_package_name(br.ufmg.dcc.tonemapreinhard)

float3 __attribute__((kernel)) root(uchar4 in, uint32_t x, uint32_t y) {
    float3 out;
    float f;

    if(in.s3 != 0) {
        f = ldexp(1.0f, (in.s3 & 0xFF) - (128 + 8));
        out.s0 = (in.s0 & 0xFF) * f;
        out.s1 = (in.s1 & 0xFF) * f;
        out.s2 = (in.s2 & 0xFF) * f;
    }
    else {
        out.s0 = 0.0f;
        out.s1 = 0.0f;
        out.s2 = 0.0f;
    }

    return out;
}
