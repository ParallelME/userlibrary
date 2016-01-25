#pragma version(1)
#pragma rs java_package_name(br.ufmg.dcc.imageloader)

typedef struct __attribute__((packed, aligned(4))) ABCD {
    float3 abcd;
} ABCD_t;
ABCD_t *abcd;

ABCD_t __attribute__((kernel)) root(ABCD_t pixel, uint32_t x, uint32_t y) {
    return pixel;
}
