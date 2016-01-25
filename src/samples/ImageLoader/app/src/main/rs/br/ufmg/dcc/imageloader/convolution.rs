#pragma version(1)
#pragma rs java_package_name(br.ufmg.dcc.imageloader)

float kernel[9] = {1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f };
uint32_t kernelWidth = 3;
uint32_t kernelHeight = 3;
uint32_t kernelDivisor = 15;
uint32_t inputWidth = 0;
uint32_t inputHeight = 0;

rs_allocation in;
rs_allocation a_out;

rs_script script;

static float4 getPixel(int32_t x, int32_t y){
    if(x < 0 || x >= rsAllocationGetDimX(in) || y < 0 || y >= rsAllocationGetDimY(in)){
        uchar4 black;
        black.r = 0.0f;
        black.g = 0.0f;
        black.b = 0.0f;
        black.a = 255;
        return rsUnpackColor8888(black);
    }
    else {
        return rsUnpackColor8888(rsGetElementAt_uchar4(in, x, y));
    }
}

static int32_t bound(int32_t value, int32_t endIndex) {
    if (value < 0)
      return 0;
    if (value < endIndex)
      return value;
    return endIndex - 1;
}

void root(const uchar4* v_in, uint32_t x, uint32_t y) {
    uint32_t kernelWidthRadius = kernelWidth >> 1;
    uint32_t kernelHeightRadius = kernelHeight >> 1;

    float4 out;
    out.r = 0.0f;
    out.g = 0.0f;
    out.b = 0.0f;
    out.a = 255;

    for (int32_t kw = kernelWidth - 1; kw >= 0; kw--) {
        for (int32_t kh = kernelHeight - 1; kh >= 0; kh--) {
            float4 pixel = getPixel(bound(x + kw - kernelWidthRadius, inputWidth),
                                    bound(y + kh - kernelHeightRadius, inputHeight));
            int position = (kw * kernelHeight) + kh;
            out.r += (kernel[position] / kernelDivisor * pixel.r);
            out.g += (kernel[position] / kernelDivisor * pixel.g);
            out.b += (kernel[position] / kernelDivisor * pixel.b);
        }
    }
    rsSetElementAt_uchar4(a_out, rsPackColorTo8888(out), x, y);
}

void convolute(int width, int height){
    inputWidth = width;
    inputHeight = height;
    rsForEach(script, in, a_out);
}