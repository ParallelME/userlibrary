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
 * HDR image processing iterator.
 *
 * @author Pedro Caldeira, Wilson de Carvalho
 * @author Renato Utsch
 * @version 0.1
 */
public class HDRImage implements Image, Parallelizable<Pixel> {
    private int width;
    private int height;
    private int size;
    private Pixel[] pixels;

    public HDRImage(byte[] data, int width, int height) {
        this.height = height;
        this.width = width;
        this.size = width * height;
        this.pixels = new Pixel[size];
        float[] rgb = new float[3];
        for (int x = 0; x < width; x++) {
            int base = height * x;
            for (int y = 0; y < height; y++) {
                RGBE.rgbe2float(rgb, data, 4 * (y * width + x));
                pixels[base + y] = new Pixel(new RGBA(rgb[0], rgb[1], rgb[2], 0), x, y);
            }
        }
    }

    /**
     * Iterate over this array and apply a user function over all
     * its elements.
     *
     * @param userFunction
     *            User function that must be applied.
     */
    @Override
    public void foreach(Foreach<Pixel> userFunction) {
        for (int i = 0; i < size; i++)
            userFunction.function(pixels[i]);
    }

    /**
     * Iterates over this HDR image and applies an user function over
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Bitmap toBitmap(){
        Bitmap bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888);
        toBitmap(bitmap);
        return bitmap;
    }

    public void toBitmap(Bitmap bitmap) {
        for(int x = 0; x < width; ++x) {
            int base = x * height;
            for(int y = 0; y < height; ++y) {
                RGBA rgba = pixels[base + y].rgba;
                bitmap.setPixel(x, y, Color.argb(
                        255,
                        (int) (255.0f * rgba.red),
                        (int) (255.0f * rgba.green),
                        (int) (255.0f * rgba.blue)
                ));
            }
        }
    }

    @Override
    public ParallelIterable<Pixel> par() {
        return new ParallelIterator<>();
    }
}
