#pragma version(1)
#pragma rs java_package_name(br.ufmg.dcc.tonemapreinhard)

float gPower;

uchar4 __attribute__((kernel)) root(float3 in, uint32_t x, uint32_t y) {
    uchar4 out;
    out.r = (uchar) (255.0f * pow(in.s0, gPower));
    out.g = (uchar) (255.0f * pow(in.s1, gPower));
    out.b = (uchar) (255.0f * pow(in.s2, gPower));
    out.a = 255;
    return out;
}
