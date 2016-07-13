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

/**
 * Bitmap image processing class.
 *
 * @author Wilson de Carvalho
 * @author Renato Utsch
 * @version 0.1
 */
public class BitmapImage extends Image {
    public BitmapImage(Bitmap bitmap) {
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
        this.size = width * height;
        this.pixels = new Pixel[size];
        for(int x = 0; x < width; x++) {
            int base = x * height;
            for(int y = 0; y < height; y++) {
                int pixelColor = bitmap.getPixel(x, y);
                this.pixels[base + y] = new Pixel(new RGBA((float)Color.red(pixelColor),
                        (float)Color.green(pixelColor), (float)Color.blue(pixelColor), 0f), x, y);
            }
        }
    }

    /**
     * Returns the internal bitmap that represents this image.
     *
     * @return A Bitmap object representing this image.
     */
    public Bitmap toBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        toBitmap(bitmap);
        return bitmap;
    }

    /**
     * Copy the internal Bitmap data to a user defined bitmap object.
     *
     * @param bitmap Bitmap that must be filled with current bitmap data.
     */
    public void toBitmap(final Bitmap bitmap) {
        for (int x = 0; x < width; x++) {
            int base = x * height;
            for (int y = 0; y < height; y++) {
                RGBA rgba = pixels[base + y].rgba;
                bitmap.setPixel(x, y, Color.argb((int) rgba.alpha, (int) rgba.red, (int) rgba.green,
                        (int) rgba.blue));
            }
        }
    }
}
