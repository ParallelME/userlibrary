/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.userlibrary.image;

import android.graphics.Bitmap;
import android.graphics.Color;

import org.parallelme.userlibrary.function.ForeachFunction;

/**
 * Bitmap image processing iterator.
 *
 * @author Wilson de Carvalho
 * @version 0.1
 */
public class BitmapImage implements Image {
    final private Bitmap bitmap;

    public BitmapImage(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    /**
     * Returns the internal bitmap that represents this image.
     *
     * @return A Bitmap object representing this image.
     */
    public Bitmap toBitmap() {
        return this.bitmap;
    }

    /**
     * Iterate over this array and apply a user function over all
     * its elements.
     *
     * @param userFunction
     *            User function that must be applied.
     */
    @Override
    public void foreach(ForeachFunction<Pixel> userFunction) {
        int width = this.bitmap.getWidth();
        int height = this.bitmap.getHeight();
        Pixel pixel;
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                int color = bitmap.getPixel(x, y);
                pixel = new Pixel(
                        new RGBA(Color.red(color), Color.green(color),
                                Color.blue(color), Color.alpha(color)), x, y);
                userFunction.function(pixel);
                this.bitmap.setPixel(x,y,Color.argb( (int) pixel.rgba.alpha, (int) pixel.rgba.red, (int) pixel.rgba.green, (int) pixel.rgba.blue));
            }
        }
    }
}
