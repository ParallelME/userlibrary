package br.ufmg.dcc.imageloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import br.ufmg.dcc.parallelme.userlibrary.FlatArray;
import br.ufmg.dcc.parallelme.userlibrary.function.*;
import br.ufmg.dcc.parallelme.userlibrary.image.RGBA;

/**
 * @author Wilson de Carvalho
 */
public class JavaConvolutionFlatArrayParallel {
    private final float kernel[] = {1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};
    private final float kernelDivisor = 15;
    private final int kernelWidth = 3;
    private final int kernelHeight = 3;
    private final int kernelWidthRadius = kernelWidth >>> 1;
    private final int kernelHeightRadius = kernelHeight >>> 1;
    private boolean mortonCode;

    public JavaConvolutionFlatArrayParallel() {
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
        FlatArray<RGBA> data = this.fromBitmap(bitmap);
        this.convolute(data, kernel, kernelDivisor);
        this.toBitmap(bitmap, data);

        return bitmap;
    }

    /**
     * Loads the image data from a bitmap.
     */
    public FlatArray<RGBA> fromBitmap(Bitmap bitmap) {
        // Must be 'final' to be accessed inside UserFunctionWithIndex
        final Bitmap bitmapFoo = bitmap;
        final FlatArray<RGBA> array = new FlatArray<RGBA>(RGBA.class, mortonCode, bitmap.getHeight(), bitmap.getWidth());
        array.fill(new UserFunctionFill<RGBA>() {
            @Override
            public RGBA function() {
                return new RGBA();
            }
        });
        array.par().foreach(new UserFunctionWithIndex<RGBA>() {
            @Override
            public void function(ElementWithIndex<RGBA> e) {
                int[] indexes = new int[2];
                array.getOriginalIndex(e.index, indexes);
                int y = indexes[0];
                int x = indexes[1];
                int color = bitmapFoo.getPixel(x, y);
                e.element.red = Color.red(color) / 255.0f;
                e.element.green = Color.green(color) / 255.0f;
                e.element.blue = Color.blue(color) / 255.0f;
            }
        });
        return array;
    }

    public void toBitmap(final Bitmap bitmap, final FlatArray<RGBA> data) {
        data.par().foreach(new UserFunctionWithIndex<RGBA>() {
            @Override
            public void function(ElementWithIndex<RGBA> e) {
                int[] indexes = new int[2];
                data.getOriginalIndex(e.index, indexes);
                int y = indexes[0];
                int x = indexes[1];
                RGBA rgb = e.element;
                bitmap.setPixel(x, y, Color.rgb(
                        (int) (255.0f * rgb.red),
                        (int) (255.0f * rgb.green),
                        (int) (255.0f * rgb.blue)
                ));
            }
        });
    }

    private void convolute(final FlatArray<RGBA> data, final float[] kernel, final float kernelDivisor)
    {
        final int inputHeight = data.getDimensions()[0];
        final int inputWidth = data.getDimensions()[1];

        data.par().foreach(new UserFunctionWithIndex<RGBA>() {
            @Override
            public void function(ElementWithIndex<RGBA> e) {
                int[] indexes = new int[2];
                RGBA out = new RGBA();
                data.getOriginalIndex(e.index, indexes);
                int y = indexes[0];
                int x = indexes[1];
                for (int kw = kernelWidth - 1; kw >= 0; kw--) {
                    for (int kh = kernelHeight - 1; kh >= 0; kh--) {
                        RGBA pixel = data.get(bound(y + kh - kernelHeightRadius, inputHeight),
                                bound(x + kw - kernelWidthRadius, inputWidth));
                        int position = (kw * kernelHeight) + kh;
                        out.red += (kernel[position] / kernelDivisor * pixel.red);
                        out.green += (kernel[position] / kernelDivisor * pixel.green);
                        out.blue += (kernel[position] / kernelDivisor * pixel.blue);
                    }
                }
                e.element.red = out.red;
                e.element.green = out.green;
                e.element.blue = out.blue;
            }
        });
    }
}
