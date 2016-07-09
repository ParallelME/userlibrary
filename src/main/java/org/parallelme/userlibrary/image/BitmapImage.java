/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.userlibrary.image;

import android.graphics.Bitmap;

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
                this.pixels[base + y] = new Pixel(new RGBA(bitmap.getPixel(x, y)), x, y);
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
                bitmap.setPixel(x, y, pixels[base + y].rgba.toColor());
            }
        }
    }
}
