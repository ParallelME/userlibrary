#pragma version(1)
#pragma rs java_package_name(br.ufmg.dcc.imageloader)

float __attribute__((kernel)) root(float pixel, uint32_t x, uint32_t y) {
    return pixel;
}
