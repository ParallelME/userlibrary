package br.ufmg.dcc.tonemapreinhard;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import br.ufmg.dcc.tonemapreinhard.formats.RGBE;

/**
 * Created by renatoutsch on 5/26/15.
 */
public class ReinhardJavaOperator implements ReinhardOperator {
    public float[][][] data;
    public int width, height;

    private void loadImage(Resources res, int resource) {
        RGBE.ResourceData ret = RGBE.loadFromResource(res, resource);
        width = ret.width;
        height = ret.height;
        data = new float[height][width][3];

        for(int y = 0; y < height; ++y)
            for(int x = 0; x < width; ++x)
                RGBE.rgbe2float(data[y][x], ret.data, 4 * (y * width + x));
    }

    /** Saves the current data on the given bitmap. Must be the same size. */
    private void toBitmap(Bitmap bitmap, float gamma) {
        float power = 1.0f / gamma;
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                bitmap.setPixel(x, y, Color.rgb(
                        (int) (255.0f * Math.pow(data[y][x][0], power)),
                        (int) (255.0f * Math.pow(data[y][x][1], power)),
                        (int) (255.0f * Math.pow(data[y][x][2], power))
                ));
            }
        }
    }

    private void toRgb() {
        float[] result = new float[3];
        float xVal, yVal, zVal;

        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                yVal = data[y][x][0];       // Y
                result[1] = data[y][x][1];  // x
                result[2] = data[y][x][2];  // y

                if(yVal > 0.0f && result[1] > 0.0f && result[2] > 0.0f) {
                    xVal = result[1] * yVal / result[2];
                    zVal = xVal / result[1] - xVal - yVal;
                }
                else {
                    xVal = zVal = 0.0f;
                }

                data[y][x][0] = data[y][x][1] = data[y][x][2] = 0.0f;
                data[y][x][0] += 2.5651f * xVal;
                data[y][x][0] += -1.1665f * yVal;
                data[y][x][0] += -0.3986f * zVal;
                data[y][x][1] += -1.0217f * xVal;
                data[y][x][1] += 1.9777f * yVal;
                data[y][x][1] += 0.0439f * zVal;
                data[y][x][2] += 0.0753f * xVal;
                data[y][x][2] += -0.2543f * yVal;
                data[y][x][2] += 1.1892f * zVal;
            }
        }
    }

    private void toYxy() {
        float[] result = new float[3];
        float w;

        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                result[0] = result[1] = result[2] = 0.0f;
                result[0] += 0.5141364f * data[y][x][0];
                result[0] += 0.3238786f * data[y][x][1];
                result[0] += 0.16036376f * data[y][x][2];
                result[1] += 0.265068f * data[y][x][0];
                result[1] += 0.67023428f * data[y][x][1];
                result[1] += 0.06409157f * data[y][x][2];
                result[2] += 0.0241188f * data[y][x][0];
                result[2] += 0.1228178f * data[y][x][1];
                result[2] += 0.84442666f * data[y][x][2];
                w = result[0] + result[1] + result[2];

                if(w > 0.0) {
                    data[y][x][0] = result[1];      // Y
                    data[y][x][1] = result[0] / w;  // x
                    data[y][x][2] = result[1] / w;  // y
                }
                else {
                    data[y][x][0] = data[y][x][1] = data[y][x][2] = 0.0f;
                }
            }
        }
    }

    private void clamp() {
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                if(data[y][x][0] > 1.0f) data[y][x][0] = 1.0f;
                if(data[y][x][1] > 1.0f) data[y][x][1] = 1.0f;
                if(data[y][x][2] > 1.0f) data[y][x][2] = 1.0f;
            }
        }
    }
    private double logAverage() {
        double sum = 0.0;
        for(int y = 0; y < height; ++y)
            for(int x = 0; x < width; ++x)
               sum += Math.log(0.00001 + data[y][x][0]);

        return Math.exp(sum / (double)(height * width));
    }

    private void scaleToMidtone(float key) {
        float scaleFactor = 1.0f / (float) logAverage();

        for(int y = 0; y < height; ++y)
            for(int x = 0; x < width; ++x)
                data[y][x][0] *= scaleFactor * key;
    }

    private float getMaxValue() {
        float max = 0.0f;
        for(int y = 0; y < height; ++y)
            for(int x = 0; x < width; ++x)
                if(max < data[y][x][0])
                    max = data[y][x][0];

        return max;
    }

    private void tonemap() {
        float lmax2 = getMaxValue();
        lmax2 *= lmax2;

        for(int y = 0; y < height; ++y)
            for(int x = 0; x < width; ++x)
                data[y][x][0] *= (1.0f + data[y][x][0] / lmax2)
                        / (1.0f + data[y][x][0]);
    }

    public Bitmap runOp(Resources res, int resource, float key, float gamma) {
        loadImage(res, resource);

        toYxy();
        scaleToMidtone(key);
        tonemap();
        toRgb();
        clamp();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        toBitmap(bitmap, gamma);

        data = null;
        return bitmap;
    }
}
