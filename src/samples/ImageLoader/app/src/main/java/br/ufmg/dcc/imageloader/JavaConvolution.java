package br.ufmg.dcc.imageloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import br.ufmg.dcc.parallelme.userlibrary.image.RGBA;


/**
 * @author Wilson de Carvalho
 */
public class JavaConvolution {
    private final float kernel[] = {1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};
    private final float kernelDivisor = 15;
    private final int kernelWidth = 3;
    private final int kernelHeight = 3;
    private final int kernelWidthRadius = kernelWidth >>> 1;
    private final int kernelHeightRadius = kernelHeight >>> 1;
    private int height;
    private int width;

    public JavaConvolution(){
    }

    private static int bound(int value, int endIndex)
    {
        if (value < 0)
            return 0;
        if (value < endIndex)
            return value;
        return endIndex - 1;
    }

    public Bitmap convolute(Resources res, int resource) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap bitmap = BitmapFactory.decodeResource(res, resource, options);

        RGBA[][] data = this.fromBitmap(bitmap);
        this.convolute(data, kernel, kernelDivisor);
        this.toBitmap(bitmap, data);

        return bitmap;
    }

    /**
     * Loads the image data from a bitmap.
     */
    private RGBA[][] fromBitmap(Bitmap bitmap) {
        height = bitmap.getHeight();
        width = bitmap.getWidth();
        RGBA[][] data = new RGBA[height][width];
        for(int y = 0; y < height; ++y)
            for(int x = 0; x < width; ++x)
                data[y][x] = new RGBA();

        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                int color = bitmap.getPixel(x, y);
                data[y][x].red = Color.red(color) / 255.0f;
                data[y][x].green = Color.green(color) / 255.0f;
                data[y][x].blue = Color.blue(color) / 255.0f;
            }
        }
        return data;
    }

    private void convolute(RGBA[][] data, final float[] kernel, final float kernelDivisor)
    {
        RGBA rgb = new RGBA();
        for(int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                rgb.red = rgb.green = rgb.blue = 0;
                for (int kw = kernelWidth - 1; kw >= 0; kw--) {
                    for (int kh = kernelHeight - 1; kh >= 0; kh--) {
                        RGBA pixel = data[bound(y + kh - kernelHeightRadius, height)][bound(x + kw - kernelWidthRadius, width)];
                        int position = (kw * kernelHeight) + kh;
                        rgb.red += (kernel[position] / kernelDivisor * pixel.red);
                        rgb.green += (kernel[position] / kernelDivisor * pixel.green);
                        rgb.blue += (kernel[position] / kernelDivisor * pixel.blue);
                    }
                }
                data[y][x].red = rgb.red;
                data[y][x].green = rgb.green;
                data[y][x].blue = rgb.blue;
            }
        }
    }

    /**
     * Saves the image data to a bitmap. Note that this bitmap must have the
     * same size (width x height) of the image data.
     */
    private void toBitmap(Bitmap bitmap, RGBA[][] data) {
        for(int h = 0; h < height; ++h) {
            for(int w = 0; w < width; ++w) {
                bitmap.setPixel(w, h, Color.rgb(
                        (int) (255.0f * data[h][w].red),
                        (int) (255.0f * data[h][w].green),
                        (int) (255.0f * data[h][w].blue)
                ));
            }
        }
    }
}
