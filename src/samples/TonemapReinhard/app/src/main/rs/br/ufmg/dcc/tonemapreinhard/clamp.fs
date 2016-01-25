#pragma version(1)
#pragma rs java_package_name(br.ufmg.dcc.tonemapreinhard)

float3 __attribute__((kernel)) root(float3 in, uint32_t x, uint32_t y) {
    if(in.s0 > 1.0f) in.s0 = 1.0f;
    if(in.s1 > 1.0f) in.s1 = 1.0f;
    if(in.s2 > 1.0f) in.s2 = 1.0f;
    return in;
}
