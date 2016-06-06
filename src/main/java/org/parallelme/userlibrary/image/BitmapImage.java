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

import org.parallelme.userlibrary.function.Foreach;
import org.parallelme.userlibrary.function.Reduce;
import org.parallelme.userlibrary.parallel.ParallelIterable;
import org.parallelme.userlibrary.parallel.ParallelIterator;
import org.parallelme.userlibrary.parallel.Parallelizable;

/**
 * Bitmap image processing iterator.
 *
 * @author Wilson de Carvalho
 * @author Renato Utsch
 * @version 0.1
 */
public class BitmapImage implements Image, Parallelizable<Pixel> {
    private int width;
    private int height;
    private int size;
    private Pixel[] pixels;

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

    /**
     * Iterate over this array and apply a user function over all
     * its elements.
     *
     * @param userFunction User function that must be applied.
     */
    @Override
    public void foreach(Foreach<Pixel> userFunction) {
        for (int i = 0; i < size; i++)
            userFunction.function(pixels[i]);
    }

    /**
     * Iterates over this bitmap and applies an user function over
     * all its elements.
     *
     * @param userFunction User function that is to be applied.
     * @return The result of applying the user function.
     */
    @Override
    public Pixel reduce(Reduce<Pixel> userFunction) {
        Pixel pixel1 = pixels[0];
        for (int i = 1; i < size; i++)
                pixel1 = userFunction.function(pixel1, pixels[i]);

        return pixel1;
    }

    @Override
    public ParallelIterable<Pixel> par() {
        return new ParallelIterator<>();
    }
}
