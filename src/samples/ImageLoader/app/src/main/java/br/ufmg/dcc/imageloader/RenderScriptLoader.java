package br.ufmg.dcc.imageloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v8.renderscript.*;
import android.util.Log;

/**
 * @author Renato Utsch
 */
public class RenderScriptLoader implements Loader {
    RenderScript mRS;
    Allocation mDataAllocation;
    Allocation mBitmapAllocation;

    ScriptField_ABCD array = null;
    int height, width;

    ScriptC_from_bitmap mFromBitmapScript;
    ScriptC_to_yxy mToYxyScript;
    ScriptC_to_rgb mToRgbScript;
    ScriptC_to_bitmap mToBitmapScript;

    /**
     * Loads the image data from a bitmap.
     */
    private void fromBitmap(Bitmap bitmap) {
       /* mBitmapAllocation = Allocation.createFromBitmap(mRS, bitmap,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT | Allocation.USAGE_SHARED);
        mWidth = mBitmapAllocation.getType().getX();
        mHeight = mBitmapAllocation.getType().getY();

        // Creating an allocation using float3's instead of uchar4's.
        Type dataType = new Type.Builder(mRS, Element.F32_3(mRS))
                .setX(mWidth)
                .setY(mHeight)
                .create();
        mDataAllocation = Allocation.createTyped(mRS, dataType);

        mFromBitmapScript.forEach_root(mBitmapAllocation, mDataAllocation);*/
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        //array = new int[width * height * 3];
        array = new ScriptField_ABCD(mRS, width * height);
        mFromBitmapScript.bind_abcd(array);

        int k=0;
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                int color = bitmap.getPixel(x, y);

                ScriptField_ABCD.Item rgb = new ScriptField_ABCD.Item();
                rgb.abcd.x = Color.red(color);
                rgb.abcd.y = Color.green(color);
                rgb.abcd.z = Color.blue(color);
                array.set(rgb, k, false);

                k++;
            }
        }
        array.copyAll();

        Type dataType = new Type.Builder(mRS, ScriptField_ABCD.createElement(mRS)).setX(width*height*3).create();
        mBitmapAllocation = Allocation.createTyped(mRS, dataType);
        mDataAllocation = Allocation.createTyped(mRS, dataType);
        mFromBitmapScript.forEach_root(mBitmapAllocation, mDataAllocation);
    }

    /**
     * Saves the image data to a bitmap. Note that this bitmap must have the
     * same size (width x height) of the image data.
     */
    private void toBitmap(Bitmap bitmap) {
        /*mToBitmapScript.forEach_root(mDataAllocation, mBitmapAllocation);
        //mBitmapAllocation.copyTo(array);
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                int baseIndex = (x * height + y) * 3;
                int red = (int)array.get(baseIndex + 0).abcd;
                int green = (int)array.get(baseIndex + 1).abcd;
                int blue = (int)array.get(baseIndex + 2).abcd;
                bitmap.setPixel(x, y, Color.rgb(red, green, blue));
            }
        }
//        mBitmapAllocation.copyTo(bitmap);*/
    }

    /**
     * Converts from the RGB to the YXY space.
     */
    private void toYxy() {
        mToYxyScript.forEach_root(mDataAllocation, mDataAllocation);
    }

    /**
     * Converts from the YXY to the RGB space.
     */
    private void toRgb() {
        mToRgbScript.forEach_root(mDataAllocation, mDataAllocation);
    }

    public Bitmap load(Resources res, int resource) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        Bitmap bitmap = BitmapFactory.decodeResource(res, resource, options);

        fromBitmap(bitmap);
        //toYxy();
        //toRgb();
        toBitmap(bitmap);

        return bitmap;
    }

    /**
     * Initializes RenderScript.
     */
    RenderScriptLoader(RenderScript rs) {
        mRS = rs;

        mFromBitmapScript = new ScriptC_from_bitmap(mRS);
        mToYxyScript = new ScriptC_to_yxy(mRS);
        mToRgbScript = new ScriptC_to_rgb(mRS);
        mToBitmapScript = new ScriptC_to_bitmap(mRS);
    }
}
