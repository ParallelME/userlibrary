package br.ufmg.dcc.imageloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import br.ufmg.dcc.parallelme.userlibrary.*;
import br.ufmg.dcc.parallelme.userlibrary.function.ElementWithIndex2d;
import br.ufmg.dcc.parallelme.userlibrary.function.UserFunctionFill;
import br.ufmg.dcc.parallelme.userlibrary.function.UserFunctionWithIndex2d;
import br.ufmg.dcc.parallelme.userlibrary.image.RGB;

/**
 * @author Wilson de Carvalho
 */
public class JavaConvolutionFlatArray {
    private final float kernel[] = {1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};
    private final float kernelDivisor = 15;
    private final int kernelWidth = 3;
    private final int kernelHeight = 3;
    private final int kernelWidthRadius = kernelWidth >>> 1;
    private final int kernelHeightRadius = kernelHeight >>> 1;
    private boolean mortonCode;
    private int height, width;

    public JavaConvolutionFlatArray() {
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
        FlatArray2d<RGB> data = this.fromBitmap(bitmap);
        this.convolute(data, kernel, kernelDivisor);
        this.toBitmap(bitmap, data);

        return bitmap;
    }

    /**
     * Loads the image data from a bitmap.
     */
    public FlatArray2d<RGB> fromBitmap(Bitmap bitmap) {
        // Must be 'final' to be accessed inside UserFunctionWithIndex
        final Bitmap bitmapFoo = bitmap;
        final FlatArray2d<RGB> array = new FlatArray2d<RGB>(RGB.class, mortonCode, bitmap.getWidth(), bitmap.getHeight());
        array.fill(new UserFunctionFill<RGB>() {
            @Override
            public RGB function() {
                return new RGB(0, 0, 0);
            }
        });
        array.foreach(new UserFunctionWithIndex2d<RGB>() {
            @Override
            public void function(ElementWithIndex2d<RGB> e) {
                int color = bitmapFoo.getPixel(e.x, e.y);
                e.element.red = Color.red(color) / 255.0f;
                e.element.green = Color.green(color) / 255.0f;
                e.element.blue = Color.blue(color) / 255.0f;
            }
        });
        return array;
    }

    public void toBitmap(final Bitmap bitmap, final FlatArray2d<RGB> data) {
        data.foreach(new UserFunctionWithIndex2d<RGB>() {
            @Override
            public void function(ElementWithIndex2d<RGB> e) {
                RGB rgb = e.element;
                bitmap.setPixel(e.x, e.y, Color.rgb(
                        (int) (255.0f * rgb.red),
                        (int) (255.0f * rgb.green),
                        (int) (255.0f * rgb.blue)
                ));
            }
        });
    }

    private void convolute(final FlatArray2d<RGB> data, final float[] kernel, final float kernelDivisor)
    {
        data.foreach(new UserFunctionWithIndex2d<RGB>() {
            @Override
            public void function(ElementWithIndex2d<RGB> eix) {
                RGB rgb = new RGB(0, 0, 0);
                for (int kw = kernelWidth - 1; kw >= 0; kw--) {
                    for (int kh = kernelHeight - 1; kh >= 0; kh--) {
                        RGB pixel = data.get(bound(eix.x + kh - kernelHeightRadius, height),
                                bound(eix.y + kw - kernelWidthRadius, width));
                        int position = (kw * kernelHeight) + kh;
                        rgb.red += (kernel[position] / kernelDivisor * pixel.red);
                        rgb.green += (kernel[position] / kernelDivisor * pixel.green);
                        rgb.blue += (kernel[position] / kernelDivisor * pixel.blue);
                    }
                }
                eix.element.red = rgb.red;
                eix.element.green = rgb.green;
                eix.element.blue = rgb.blue;
            }
        });
    }
}
