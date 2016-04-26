/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 *  DCC-UFMG
 */

package br.ufmg.dcc.parallelme.userlibrary.image;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;

import br.ufmg.dcc.parallelme.userlibrary.function.ForeachFunction;

/**
 * HDR image processing iterator.
 *
 * @author Pedro Caldeira, Wilson de Carvalho
 * @version 0.1
 */
public class HDRImage implements Image {

    private int width;
    private int height;
    private Pixel[][] pixels;

    public HDRImage(Resources res, int resourceId) {
        this(RGBE.loadFromResource(res, resourceId));
    }

    public HDRImage(RGBE.ResourceData ret) {
        this.height = ret.height;
        this.width = ret.width;
        this.pixels = new Pixel[ret.width][ret.height];
        float[] rgb = new float[3];
        for (int i=0; i < ret.width; i++)
            for (int j=0; j< ret.height; j++){
                RGBE.rgbe2float(rgb, ret.data, 4 * (j * width + i));
                pixels[i][j] = new Pixel(new RGBA(rgb[0], rgb[1], rgb[2], 255), i, j);
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
    public void foreach(ForeachFunction<Pixel> userFunction) {
        for (int x=0; x<this.width; x++) {
            for (int y=0; y<this.height; y++) {
                userFunction.function(pixels[x][y]);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Bitmap toBitmap(){
        Bitmap bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888);
        for(int y = 0; y < this.height; ++y) {
            for(int x = 0; x < this.width; ++x) {
                bitmap.setPixel(x, y, Color.argb(
                        255,
                        (int) (255.0f * pixels[x][y].rgba.red),
                        (int) (255.0f * pixels[x][y].rgba.green),
                        (int) (255.0f * pixels[x][y].rgba.blue)
                ));
            }
        }
        return bitmap;
    }

}
