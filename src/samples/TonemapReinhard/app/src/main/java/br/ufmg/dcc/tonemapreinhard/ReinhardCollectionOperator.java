package br.ufmg.dcc.tonemapreinhard;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import br.ufmg.dcc.parallelme.userlibrary.function.UserFunction;
import br.ufmg.dcc.parallelme.userlibrary.image.ImageType;
import br.ufmg.dcc.parallelme.userlibrary.image.Pixel;
import br.ufmg.dcc.tonemapreinhard.formats.RGB;
import br.ufmg.dcc.parallelme.userlibrary.image.Image;

/**
 * @author Pedro Caldeira
 */
public class ReinhardCollectionOperator implements ReinhardOperator {
    private int height, width;
    private Image image;
    private double sum = 0.0;
    private float max = 0.0f;

    @Override
    public Bitmap runOp(Resources res, int resource, float key, float gamma) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap bitmap = BitmapFactory.decodeResource(res, resource, options);
        this.height = bitmap.getHeight();
        this.width = bitmap.getWidth();

        this.image = new Image(res, resource, ImageType.HDR);

        this.toYxy();
        this.scaleToMidtone(key);
        this.tonemap();
        this.toRgb();
        this.clamp();

        return bitmap;
    }


    private void toYxy(){
        image.foreach(new UserFunction<Pixel>() {
            @Override
            public void function(Pixel pixel) {
                float[] result = new float[3];
                float w;

                result[0] = result[1] = result[2] = 0.0f;
                result[0] += 0.5141364f * pixel.rgba.red;
                result[0] += 0.3238786f * pixel.rgba.green;
                result[0] += 0.16036376f * pixel.rgba.blue;
                result[1] += 0.265068f * pixel.rgba.red;
                result[1] += 0.67023428f * pixel.rgba.green;
                result[1] += 0.06409157f * pixel.rgba.blue;
                result[2] += 0.0241188f * pixel.rgba.red;
                result[2] += 0.1228178f * pixel.rgba.green;
                result[2] += 0.84442666f * pixel.rgba.blue;
                w = result[0] + result[1] + result[2];
                if (w > 0.0) {
                    pixel.rgba.red = result[1];
                    pixel.rgba.green = result[0] / w;
                    pixel.rgba.blue = result[1] / w;
                } else {
                    pixel.rgba.red = pixel.rgba.green = pixel.rgba.blue = 0.0f;
                }
            }
        });
    }


    //This is a good example. We lack a way to return a single value from all kernel instances.
    private double logAverage() {
        sum = 0;

        this.image.foreach(new UserFunction<Pixel>() {
            @Override
            public void function(Pixel pixel) {
                sum += Math.log(0.00001f + pixel.rgba.red);
            }
        });

        return Math.exp(sum/(double) (height*width));
    }

    private void scaleToMidtone(final float key) {
        final double scaleFactor = 1.0f / this.logAverage();

        this.image.foreach(new UserFunction<Pixel>() {
            @Override
            public void function(Pixel pixel) {
                pixel.rgba.red *= scaleFactor * key;
            }
        });
    }

    private float getMaxValue() {
        max = 0;
        this.image.foreach(new UserFunction<Pixel>() {
            @Override
            public void function(Pixel pixel) {
                if(pixel.rgba.red > max) max = pixel.rgba.red;
            }
        });

        return max;
    }

    private void tonemap() {
        final double max2 = Math.pow(getMaxValue(), 2);
        this.image.foreach(new UserFunction<Pixel>() {
            @Override
            public void function(Pixel pixel) {
                pixel.rgba.red *= (1.0f + pixel.rgba.red / max2) / (1.0f + pixel.rgba.red);
            }
        });
    }

    private void toRgb(){
        this.image.foreach(new UserFunction<Pixel>() {
            @Override
            public void function(Pixel pixel) {
                RGB val = new RGB();
                RGB result = new RGB();
                RGB out = new RGB();

                val.green = pixel.rgba.red;     // Y
                result.green = pixel.rgba.green; // x
                result.blue = pixel.rgba.blue; // y

                if(val.green > 0.0f && result.green > 0.0f && result.blue > 0.0f) {
                    val.red = result.green * val.green / result.blue;
                    val.blue = val.red / result.green - val.red - val.green;
                }
                else {
                    val.red = val.blue = 0.0f;
                }

                // These constants are the conversion coefficients.
                out.red = out.green = out.blue = 0.0f;
                out.red += 2.5651f * val.red;
                out.red += -1.1665f * val.green;
                out.red += -0.3986f * val.blue;
                out.green += -1.0217f * val.red;
                out.green += 1.9777f * val.green;
                out.green += 0.0439f * val.blue;
                out.blue += 0.0753f * val.red;
                out.blue += -0.2543f * val.green;
                out.blue += 1.1892f * val.blue;

                pixel.rgba.red = out.red;
                pixel.rgba.green = out.green;
                pixel.rgba.blue = out.blue;
            }
        });
    }
    
    private void clamp() {
        this.image.foreach(new UserFunction<Pixel>() {
            @Override
            public void function(Pixel pixel) {
                if(pixel.rgba.red > 1.0f) pixel.rgba.red = 1.0f;
                if(pixel.rgba.green > 1.0f) pixel.rgba.green = 1.0f;
                if(pixel.rgba.blue > 1.0f) pixel.rgba.blue = 1.0f;
            }
        });
    }
}
