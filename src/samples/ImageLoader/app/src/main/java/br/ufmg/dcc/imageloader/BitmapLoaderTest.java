package br.ufmg.dcc.imageloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import br.ufmg.dcc.parallelme.userlibrary.function.ForeachFunction;
import br.ufmg.dcc.parallelme.userlibrary.image.BitmapImage;
import br.ufmg.dcc.parallelme.userlibrary.image.Pixel;
import br.ufmg.dcc.parallelme.userlibrary.image.RGB;

/**
 * @author Wilson de Carvalho.
 */
public class BitmapLoaderTest {
    public Bitmap load(Resources res, int resource) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        Bitmap bitmap = BitmapFactory.decodeResource(res, resource, options);

        BitmapImage image = new BitmapImage(bitmap);
        // to Yxy
        image.foreach(new ForeachFunction<Pixel>() {
            @Override
            public void function(Pixel pixel) {
                RGB foo = new RGB(0, 0, 0);
                foo.red = foo.green = foo.blue = 0.0f;
                foo.red += 0.5141364f * pixel.rgba.red;
                foo.red += 0.3238786f * pixel.rgba.green;
                foo.red += 0.16036376f * pixel.rgba.blue;
                foo.green += 0.265068f * pixel.rgba.red;
                foo.green += 0.67023428f * pixel.rgba.green;
                foo.green += 0.06409157f * pixel.rgba.blue;
                foo.blue += 0.0241188f * pixel.rgba.red;
                foo.blue += 0.1228178f * pixel.rgba.green;
                foo.blue += 0.84442666f * pixel.rgba.blue;
                float w = foo.red + foo.green + foo.blue;
                if (w > 0.0f) {
                    pixel.rgba.red = foo.green;
                    pixel.rgba.green = foo.red / w;
                    pixel.rgba.blue = foo.green / w;
                } else {
                    pixel.rgba.red = pixel.rgba.green = pixel.rgba.blue = 0.0f;
                }
            }
        });
        // to RGB
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
            }
        });
        bitmap = image.toBitmap();

        return bitmap;
    }
}
