#pragma version(1)
#pragma rs java_package_name(br.ufmg.dcc.tonemapreinhard)

rs_allocation gData;
rs_allocation gMax;
int gDataXSize;
int gDataYSize;

void get_max_value() {
    float3 f;
    float max = 0.0f;

    for(int i = 0; i < gDataXSize; ++i) {
        for(int j = 0; j < gDataYSize; ++j) {
            f = rsGetElementAt_float3(gData, i, j);
            if(f.s0 > max)
                max = f.s0;
        }
    }
    rsSetElementAt_float(gMax, max, 0);
}
