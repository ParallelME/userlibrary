#pragma version(1)
#pragma rs java_package_name(br.ufmg.dcc.tonemapreinhard)

rs_allocation gData;
rs_allocation gAverage;
int gDataXSize;
int gDataYSize;

void log_average() {
    float3 f;
    float sum = 0.0f;

    for(int i = 0; i < gDataXSize; ++i) {
        for(int j = 0; j < gDataYSize; ++j) {
            f = rsGetElementAt_float3(gData, i, j);
            sum += log(0.00001f + f.s0);
        }
    }

    rsSetElementAt_float(gAverage, exp(sum / (float)(gDataXSize * gDataYSize)), 0);
}
