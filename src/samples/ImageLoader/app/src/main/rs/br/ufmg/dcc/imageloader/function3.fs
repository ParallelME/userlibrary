#pragma version(1)
#pragma rs java_package_name(br.ufmg.dcc.imageloader)

uchar4 __attribute__((kernel)) root(float3 in, uint32_t x, uint32_t y) {
    uchar4 out;
    out.r = (uchar) (in.s0 * 255.0f);
    out.g = (uchar) (in.s1 * 255.0f);
    out.b = (uchar) (in.s2 * 255.0f);
    out.a = 255;
    return out;
}
