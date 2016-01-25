package br.ufmg.dcc.imageloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import br.ufmg.dcc.parallelme.userlibrary.FlatArray;
import br.ufmg.dcc.parallelme.userlibrary.function.*;
import br.ufmg.dcc.parallelme.userlibrary.image.RGBA;

/**
 * Parallel version of JavaLoader class.
 *
 * @author Wilson de Carvalho
 */
public class JavaLoaderFlatArrayParallel implements Loader {
    private int height, width;
    private boolean mortonCode;

    public JavaLoaderFlatArrayParallel() {
    }

    public Bitmap load(Resources res, int resource, boolean mortonCode) {
        this.mortonCode = mortonCode;
        return this.load(res, resource);
    }

    public Bitmap load(Resources res, int resource) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        Bitmap bitmap = BitmapFactory.decodeResource(res, resource, options);

        FlatArray<RGBA> data = this.fromBitmap(bitmap);
        this.toYxy(data);
        this.toRgb(data);
        toBitmap(bitmap, data);

        return bitmap;
    }

    /**
     * Loads the image data from a bitmap.
     */
    public FlatArray<RGBA> fromBitmap(Bitmap bitmap) {
        // Must be 'final' to be accessed inside UserFunctionPlacement
        final Bitmap bitmapFoo = bitmap;
        height = bitmap.getHeight();
        width = bitmap.getWidth();

        final FlatArray<RGBA> array = new FlatArray<RGBA>(RGBA.class, mortonCode, height, width);
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

    /**
     * Converts from the RGB to the YXY space.
     */
    private void toYxy(final FlatArray<RGBA> data) {
        data.par().foreach(new UserFunctionWithIndex<RGBA>() {
            @Override
            public void function(ElementWithIndex<RGBA> e) {
                RGBA result = new RGBA();
                int[] indexes = new int[2];
                data.getOriginalIndex(e.index, indexes);
                int y = indexes[0];
                int x = indexes[1];
                RGBA rgb = e.element;
                result.red = result.green = result.blue = 0.0f;
                result.red += 0.5141364f * rgb.red;
                result.red += 0.3238786f * rgb.green;
                result.red += 0.16036376f * rgb.blue;
                result.green += 0.265068f * rgb.red;
                result.green += 0.67023428f * rgb.green;
                result.green += 0.06409157f * rgb.blue;
                result.blue += 0.0241188f * rgb.red;
                result.blue += 0.1228178f * rgb.green;
                result.blue += 0.84442666f * rgb.blue;
                float w = result.red + result.green + result.blue;
                if (w > 0.0) {
                    rgb.red = result.green;
                    rgb.green = result.red / w;
                    rgb.blue = result.green / w;
                } else {
                    rgb.red = rgb.green = rgb.blue = 0.0f;
                }
            }
        });
    }

    /**
     * Converts from the YXY to the RGB space.
     */
    private void toRgb(FlatArray<RGBA> array) {
        array.par().foreach(new UserFunction<RGBA>() {
            @Override
            public void function(RGBA element) {
                float xVal, zVal;
                float yVal = element.red;       // Y

                if (yVal > 0.0f && element.green > 0.0f && element.blue > 0.0f) {
                    xVal = element.green * yVal / element.blue;
                    zVal = xVal / element.green - xVal - yVal;
                } else {
                    xVal = zVal = 0.0f;
                }
                element.red = element.green = element.blue = 0.0f;
                element.red += 2.5651f * xVal;
                element.red += -1.1665f * yVal;
                element.red += -0.3986f * zVal;
                element.green += -1.0217f * xVal;
                element.green += 1.9777f * yVal;
                element.green += 0.0439f * zVal;
                element.blue += 0.0753f * xVal;
                element.blue += -0.2543f * yVal;
                element.blue += 1.1892f * zVal;
            }
        });
    }

    public void toBitmap(final Bitmap bitmap, final FlatArray<RGBA> data) {
        data.par().foreach(new UserFunctionWithIndex<RGBA>() {
            @Override
            public void function(ElementWithIndex<RGBA> e) {
                int[] indexes = new int[2];
                data.getOriginalIndex(e.index, indexes);
                int y = indexes[0];
                int x = indexes[1];
                bitmap.setPixel(x, y, Color.rgb(
                        (int) (255.0f * e.element.red),
                        (int) (255.0f * e.element.green),
                        (int) (255.0f * e.element.blue)));
            }
        });
   }
}