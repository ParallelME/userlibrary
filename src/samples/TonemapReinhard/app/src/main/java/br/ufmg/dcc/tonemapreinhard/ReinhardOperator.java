package br.ufmg.dcc.tonemapreinhard;

import android.graphics.Bitmap;
import android.content.res.Resources;

/**
 * Created by renatoutsch on 6/4/15.
 */
public interface ReinhardOperator {
    public Bitmap runOp(Resources res, int resource, float key, float gamma);
}
