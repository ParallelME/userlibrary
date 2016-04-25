package br.ufmg.dcc.imageloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import br.ufmg.dcc.parallelme.userlibrary.function.ForeachFunction;
import br.ufmg.dcc.parallelme.userlibrary.image.BitmapImage;
import br.ufmg.dcc.parallelme.userlibrary.image.Pixel;
import br.ufmg.dcc.parallelme.userlibrary.image.RGB;

/**
 * @author Wilson de Carvalho
 */
public class JavaConvolutionBitmapImage {
    private final float kernel[] = {1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};
    private final float kernelDivisor = 15;
    private final int kernelWidth = 3;
    private final int kernelHeight = 3;
    private final int kernelWidthRadius = kernelWidth >>> 1;
    private final int kernelHeightRadius = kernelHeight >>> 1;
    private boolean mortonCode;
    private int height, width;

    public JavaConvolutionBitmapImage() {
    }

    private static int bound(int value, int endIndex)
    {
        if (value < 0)
            return 0;
        if (value < endIndex)
            return value;
        return endIndex - 1;
    }

    public Bitmap convolute(Resources res, int resource, boolean mortonCode) {
        this.mortonCode = mortonCode;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap bitmap = BitmapFactory.decodeResource(res, resource, options);
        height = bitmap.getHeight();
        width = bitmap.getWidth();
        BitmapImage image = new BitmapImage(bitmap);
        this.convolute(bitmap, image, kernel, kernelDivisor);
        bitmap = image.toBitmap();
        return bitmap;
    }

    private void convolute(final Bitmap bitmap, final BitmapImage image, final float[] kernel, final float kernelDivisor)
    {
        image.foreach(new ForeachFunction<Pixel>() {
            @Override
            public void function(Pixel pixel) {
                RGB rgb = new RGB(0, 0, 0);
                for (int kw = kernelWidth - 1; kw >= 0; kw--) {
                    for (int kh = kernelHeight - 1; kh >= 0; kh--) {
                        int color = bitmap.getPixel(bound(pixel.x + kh - kernelHeightRadius, height),
                                bound(pixel.y + kw - kernelWidthRadius, width));
                        int position = (kw * kernelHeight) + kh;
                        rgb.red += (kernel[position] / kernelDivisor * Color.red(color));
                        rgb.green += (kernel[position] / kernelDivisor * Color.green(color));
                        rgb.blue += (kernel[position] / kernelDivisor * Color.blue(color));
                    }
                }
                pixel.rgba.red = rgb.red;
                pixel.rgba.green = rgb.green;
                pixel.rgba.blue = rgb.blue;
            }
        });
    }
}
