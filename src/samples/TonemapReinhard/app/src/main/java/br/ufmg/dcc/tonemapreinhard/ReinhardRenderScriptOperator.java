package br.ufmg.dcc.tonemapreinhard;

import android.support.v8.renderscript.*;
import android.content.res.Resources;
import android.graphics.Bitmap;
import br.ufmg.dcc.tonemapreinhard.formats.RGBE;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by renatoutsch on 5/27/15.
 */
public class ReinhardRenderScriptOperator implements ReinhardOperator {
    RenderScript mRS;
    Allocation mDataAllocation;
    Allocation mImageAllocation;
    int mWidth, mHeight;

    ScriptC_to_float mToFloatScript;
    ScriptC_to_yxy mToYxyScript;
    ScriptC_to_rgb mToRgbScript;
    ScriptC_clamp mClampScript;
    ScriptC_to_bitmap mToBitmapScript;
    ScriptC_log_average mLogAverageScript;
    ScriptC_scale_to_midtone mScaleToMidtoneScript;
    ScriptC_get_max_value mGetMaxValueScript;
    ScriptC_tonemap mTonemapScript;

    private void createAllocations(Resources res, int resource) {
        RGBE.ResourceData ret = RGBE.loadFromResource(res, resource);
        mWidth = ret.width;
        mHeight = ret.height;

        Type imageType = new Type.Builder(mRS, Element.RGBA_8888(mRS))
                .setX(mWidth)
                .setY(mHeight)
                .create();
        Type dataType = new Type.Builder(mRS, Element.F32_3(mRS))
                .setX(mWidth)
                .setY(mHeight)
                .create();

        mImageAllocation = Allocation.createTyped(mRS, imageType,
                Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        mDataAllocation = Allocation.createTyped(mRS, dataType);
        mImageAllocation.copyFrom(ret.data);

        mToFloatScript.forEach_root(mImageAllocation, mDataAllocation);
    }

    public void toBitmap(Bitmap bitmap, float gamma) {
        // The power used for gamma correction is 1.0f / gamma:
        mToBitmapScript.set_gPower(1.0f / gamma);
        mToBitmapScript.forEach_root(mDataAllocation, mImageAllocation);

        mImageAllocation.copyTo(bitmap);
    }

    public void toYxy() {
        mToYxyScript.forEach_root(mDataAllocation, mDataAllocation);
    }

    public void toRgb() {
        mToRgbScript.forEach_root(mDataAllocation, mDataAllocation);
    }

    public void clamp() {
        mClampScript.forEach_root(mDataAllocation, mDataAllocation);
    }

    public double logAverage() {
        float[] average = new float[1];
        Allocation averageAllocation = Allocation.createSized(mRS, Element.F32(mRS), 1);

        mLogAverageScript.set_gData(mDataAllocation);
        mLogAverageScript.set_gDataXSize(mDataAllocation.getType().getX());
        mLogAverageScript.set_gDataYSize(mDataAllocation.getType().getY());
        mLogAverageScript.set_gAverage(averageAllocation);
        mLogAverageScript.invoke_log_average();
        averageAllocation.copyTo(average);

        return average[0];
    }

    public void scaleToMidtone(float key) {
        float scaleFactor = 1.0f / (float) logAverage();

        mScaleToMidtoneScript.set_gScaleFactor(scaleFactor * key);
        mScaleToMidtoneScript.forEach_root(mDataAllocation, mDataAllocation);
    }

    public float getMaxValue() {
        float[] max = new float[1];
        Allocation maxAllocation = Allocation.createSized(mRS, Element.F32(mRS), 1);

        mGetMaxValueScript.set_gData(mDataAllocation);
        mGetMaxValueScript.set_gDataXSize(mDataAllocation.getType().getX());
        mGetMaxValueScript.set_gDataYSize(mDataAllocation.getType().getY());
        mGetMaxValueScript.set_gMax(maxAllocation);
        mGetMaxValueScript.invoke_get_max_value();
        maxAllocation.copyTo(max);

        return max[0];
    }

    public void tonemap() {
        float lmax2 = getMaxValue();
        lmax2 *= lmax2;

        mTonemapScript.set_gLmax2(lmax2);
        mTonemapScript.forEach_root(mDataAllocation, mDataAllocation);
    }

    public Bitmap runOp(Resources res, int resource, float key, float gamma) {
        createAllocations(res, resource);

        toYxy();
        scaleToMidtone(key);
        tonemap();
        toRgb();
        clamp();

        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        toBitmap(bitmap, gamma);

        mImageAllocation = null;
        mDataAllocation = null;
        return bitmap;
    }

    ReinhardRenderScriptOperator(RenderScript rs) {
        mRS = rs;

        mToFloatScript = new ScriptC_to_float(mRS);
        mToBitmapScript = new ScriptC_to_bitmap(mRS);
        mToYxyScript = new ScriptC_to_yxy(mRS);
        mToRgbScript = new ScriptC_to_rgb(mRS);
        mClampScript = new ScriptC_clamp(mRS);
        mLogAverageScript = new ScriptC_log_average(mRS);
        mScaleToMidtoneScript = new ScriptC_scale_to_midtone(mRS);
        mGetMaxValueScript = new ScriptC_get_max_value(mRS);
        mTonemapScript = new ScriptC_tonemap(mRS);
    }
}
