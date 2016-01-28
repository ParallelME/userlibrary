/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / _ / /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 *  DCC-UFMG
 */

package br.ufmg.dcc.parallelme.userlibrary.image;

import android.content.res.Resources;

import br.ufmg.dcc.parallelme.userlibrary.function.UserFunction;

/**
 * HDR image processing iterator.
 *
 * @author Pedro Caldeira, Wilson de Carvalho
 * @version 0.1
 */
public class HDRImage implements Image {
    final private Resources res;
    final private int resourceId;
    private float[][][] array;
    private int width = -1;
    private int height = -1;

    public HDRImage(Resources res, int resourceId) {
        this.res = res;
        this.resourceId = resourceId;
    }

    /**
     * Returns the internal array that represents this image.
     *
     * @return A three-dimensional array representing this image.
     */
    public float[][][] toArray() {
        return this.array;
    }

    /**
     * Iterate over this array and apply a user function over all
     * its elements.
     *
     * @param userFunction
     *            User function that must be applied.
     */
    @Override
    public void foreach(UserFunction<Pixel> userFunction) {
        for (int x=0; x<this.width; x++) {
            for (int y=0; y<this.height; y++) {
                userFunction.function(new Pixel(
                        new RGBA(array[x][y][0], array[x][y][1],
                                array[x][y][2], array[x][y][3]), x, y));
            }
        }
    }
}
