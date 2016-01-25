package br.ufmg.dcc.imageloader;

import android.graphics.Bitmap;
import android.content.res.Resources;

public interface Loader {
    /**
     * Loads an image from the resources and returns the corresponding bitmap.
     */
    public Bitmap load(Resources res, int resource);
}
