package br.ufmg.dcc.imageloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import br.ufmg.dcc.parallelme.userlibrary.function.*;
import br.ufmg.dcc.parallelme.userlibrary.image.BitmapImage;
import br.ufmg.dcc.parallelme.userlibrary.image.Pixel;
import br.ufmg.dcc.parallelme.userlibrary.image.RGBA;

/**
 * Parallel version of JavaLoader class.
 *
 * @author Wilson de Carvalho
 */
public class JavaLoaderBitmapImage implements Loader {
    public JavaLoaderBitmapImage() {
    }

    public Bitmap load(Resources res, int resource) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        Bitmap bitmap = BitmapFactory.decodeResource(res, resource, options);

        BitmapImage image = new BitmapImage(bitmap);
        this.toFloat(image);
        this.toYxy(image);
        this.toRgb(image);
        bitmap = image.toBitmap();

        return bitmap;
    }

    /**
     * Converts RGB to float.
     */
    public void toFloat(BitmapImage image) {
        image.foreach(new ForeachFunction<Pixel>() {
            @Override
            public void function(Pixel pixel) {
                pixel.rgba.red /= 255.0f;
                pixel.rgba.green /= 255.0f;
                pixel.rgba.blue /= 255.0f;
            }
        });
    }

    /**
     * Converts from float RGB to YXY space.
     */
    private void toYxy(BitmapImage image) {
        final RGBA result = new RGBA();

        image.foreach(new ForeachFunction<Pixel>() {
            @Override
            public void function(Pixel pixel) {
                result.red = result.green = result.blue = 0.0f;
                result.red += 0.5141364f * pixel.rgba.red;
                result.red += 0.3238786f * pixel.rgba.green;
                result.red += 0.16036376f * pixel.rgba.blue;
                result.green += 0.265068f * pixel.rgba.red;
                result.green += 0.67023428f * pixel.rgba.green;
                result.green += 0.06409157f * pixel.rgba.blue;
                result.blue += 0.0241188f * pixel.rgba.red;
                result.blue += 0.1228178f * pixel.rgba.green;
                result.blue += 0.84442666f * pixel.rgba.blue;
                float w = result.red + result.green + result.blue;
                if (w > 0.0) {
                    pixel.rgba.red = result.green;
                    pixel.rgba.green = result.red / w;
                    pixel.rgba.blue = result.green / w;
                } else {
                    pixel.rgba.red = pixel.rgba.green = pixel.rgba.blue = 0.0f;
                }
            }
        });
    }

    /**
     * Converts from YXY to RGB space.
     */
    private void toRgb(BitmapImage image) {
        image.foreach(new ForeachFunction<Pixel>() {
            @Override
            public void function(Pixel pixel) {
                float xVal, zVal;
                float yVal = pixel.rgba.red;       // Y

                if (yVal > 0.0f && pixel.rgba.green > 0.0f && pixel.rgba.blue > 0.0f) {
                    xVal = pixel.rgba.green * yVal / pixel.rgba.blue;
                    zVal = xVal / pixel.rgba.green - xVal - yVal;
                } else {
                    xVal = zVal = 0.0f;
                }
                pixel.rgba.red = pixel.rgba.green = pixel.rgba.blue = 0.0f;
                pixel.rgba.red += 2.5651f * xVal;
                pixel.rgba.red += -1.1665f * yVal;
                pixel.rgba.red += -0.3986f * zVal;
                pixel.rgba.green += -1.0217f * xVal;
                pixel.rgba.green += 1.9777f * yVal;
                pixel.rgba.green += 0.0439f * zVal;
                pixel.rgba.blue += 0.0753f * xVal;
                pixel.rgba.blue += -0.2543f * yVal;
                pixel.rgba.blue += 1.1892f * zVal;
                pixel.rgba.red *= 255;
                pixel.rgba.green *= 255;
                pixel.rgba.blue *= 255;
            }
        });
    }
}
