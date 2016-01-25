package br.ufmg.dcc.imageloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

/**
 * @author Renato Utsch
 */
public class JavaLoader implements Loader {
    /**
     * A matrix that contains all pixels of the image. Each image contains
     * 3 floats, representing the three components of the image (initially
     * R, G and B in sequence, but later YXY).
     * Note that when indexing this matrix the height comes before the width, so
     * pixels[2][0] accesses the pixel on the third line and the first column
     * (I always get confused, but height first, width second).
     */
    float[][][] pixels;
    int height, width;

    /**
     * Loads a new bitmap from a resource.
     */
    public Bitmap load(Resources res, int resource) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        Bitmap bitmap = BitmapFactory.decodeResource(res, resource, options);

        fromBitmap(bitmap);
        toYxy();
        toRgb();
        toBitmap(bitmap);

        return bitmap;
    }

    /**
     * Loads the image pixels from a bitmap.
     */
    private void fromBitmap(Bitmap bitmap) {
        height = bitmap.getHeight();
        width = bitmap.getWidth();
        pixels = new float[height][width][3];

        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                int color = bitmap.getPixel(x, y);
                pixels[y][x][0] = Color.red(color) / 255.0f;
                pixels[y][x][1] = Color.green(color) / 255.0f;
                pixels[y][x][2] = Color.blue(color) / 255.0f;
            }
        }
    }

    /**
     * Converts from the RGB to the YXY space.
     */
    private void toYxy() {
        float[] foo = new float[3];
        float w;
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                foo[0] = foo[1] = foo[2] = 0.0f;
                foo[0] += 0.5141364f * pixels[y][x][0];
                foo[0] += 0.3238786f * pixels[y][x][1];
                foo[0] += 0.16036376f * pixels[y][x][2];
                foo[1] += 0.265068f * pixels[y][x][0];
                foo[1] += 0.67023428f * pixels[y][x][1];
                foo[1] += 0.06409157f * pixels[y][x][2];
                foo[2] += 0.0241188f * pixels[y][x][0];
                foo[2] += 0.1228178f * pixels[y][x][1];
                foo[2] += 0.84442666f * pixels[y][x][2];
                w = foo[0] + foo[1] + foo[2];
                if (w > 0.0) {
                    pixels[y][x][0] = foo[1];      // Y
                    pixels[y][x][1] = foo[0] / w;  // x
                    pixels[y][x][2] = foo[1] / w;  // y
                }
                else {
                    pixels[y][x][0] = pixels[y][x][1] = pixels[y][x][2] = 0.0f;
                }
            }
        }
    }

    /**
     * Converts from the YXY to the RGB space.
     */
    private void toRgb() {
        float xVal, yVal, zVal;
        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                yVal = pixels[y][x][0];       // Y
                if(yVal > 0.0f && pixels[y][x][1] > 0.0f && pixels[y][x][2] > 0.0f) {
                    xVal = pixels[y][x][1] * yVal / pixels[y][x][2];
                    zVal = xVal / pixels[y][x][1] - xVal - yVal;
                } else {
                    xVal = zVal = 0.0f;
                }
                pixels[y][x][0] = pixels[y][x][1] = pixels[y][x][2] = 0.0f;
                pixels[y][x][0] += 2.5651f * xVal;
                pixels[y][x][0] += -1.1665f * yVal;
                pixels[y][x][0] += -0.3986f * zVal;
                pixels[y][x][1] += -1.0217f * xVal;
                pixels[y][x][1] += 1.9777f * yVal;
                pixels[y][x][1] += 0.0439f * zVal;
                pixels[y][x][2] += 0.0753f * xVal;
                pixels[y][x][2] += -0.2543f * yVal;
                pixels[y][x][2] += 1.1892f * zVal;
            }
        }
    }

    /**
     * Saves the image pixels to a bitmap. Note that this bitmap must have the
     * same size (width x height) of the image pixels.
     */
    private void toBitmap(Bitmap bitmap) {
        for(int h = 0; h < height; ++h) {
            for(int w = 0; w < width; ++w) {
                bitmap.setPixel(w, h, Color.rgb(
                        (int) (255.0f * pixels[h][w][0]),
                        (int) (255.0f * pixels[h][w][1]),
                        (int) (255.0f * pixels[h][w][2])
                ));
            }
        }
    }
}
