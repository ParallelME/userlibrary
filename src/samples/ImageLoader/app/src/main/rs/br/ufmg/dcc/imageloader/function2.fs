#pragma version(1)
#pragma rs java_package_name(br.ufmg.dcc.imageloader)

float3 __attribute__((kernel)) root(float3 pixel, uint32_t x, uint32_t y) {
    float xVal, zVal;
    float yVal = pixel.s0;
    if (yVal > 0.0f && pixel.s1 > 0.0f && pixel.s2 > 0.0f) {
        xVal = pixel.s1 * yVal / pixel.s2;
        zVal = xVal / pixel.s1 - xVal - yVal;
    } else {
        xVal = zVal = 0.0f;
    }
    pixel.s0 = pixel.s1 = pixel.s2 = 0.0f;
    pixel.s0 += 2.5651f * xVal;
    pixel.s0 += -1.1665f * yVal;
    pixel.s0 += -0.3986f * zVal;
    pixel.s1 += -1.0217f * xVal;
    pixel.s1 += 1.9777f * yVal;
    pixel.s1 += 0.0439f * zVal;
    pixel.s2 += 0.0753f * xVal;
    pixel.s2 += -0.2543f * yVal;
    pixel.s2 += 1.1892f * zVal;
    return pixel;
}