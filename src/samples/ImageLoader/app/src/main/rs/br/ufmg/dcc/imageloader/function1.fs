#pragma version(1)
#pragma rs java_package_name(br.ufmg.dcc.imageloader)

float3 __attribute__((kernel)) root(float3 pixel, uint32_t x, uint32_t y) {
    float3 foo;
    foo.s0 = foo.s1 = foo.s2 = 0.0f;
    foo.s0 += 0.5141364f * pixel.s0;
    foo.s0 += 0.3238786f * pixel.s1;
    foo.s0 += 0.16036376f * pixel.s2;
    foo.s1 += 0.265068f * pixel.s0;
    foo.s1 += 0.67023428f * pixel.s1;
    foo.s1 += 0.06409157f * pixel.s2;
    foo.s2 += 0.0241188f * pixel.s0;
    foo.s2 += 0.1228178f * pixel.s1;
    foo.s2 += 0.84442666f * pixel.s2;
    float w = foo.s0 + foo.s1 + foo.s2;
    if (w > 0.0f) {
        pixel.s0 = foo.s1;
        pixel.s1 = foo.s0 / w;
        pixel.s2 = foo.s1 / w;
    } else {
        pixel.s0 = pixel.s1 = pixel.s2 = 0.0f;
    }
    return pixel;
}
