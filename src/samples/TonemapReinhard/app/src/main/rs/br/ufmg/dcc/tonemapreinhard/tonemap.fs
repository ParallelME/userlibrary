#pragma version(1)
#pragma rs java_package_name(br.ufmg.dcc.tonemapreinhard)

float gLmax2;

float3 __attribute__((kernel)) root(float3 in, uint32_t x, uint32_t y) {
    in.s0 *= (1.0f + in.s0 / gLmax2) / (1.0f + in.s0);
    return in;
}
