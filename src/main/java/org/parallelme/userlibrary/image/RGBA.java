/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.userlibrary.image;

import android.graphics.Color;

/**
 * Support for RGBA color space.
 * The attributes are public to increase user code readability and increase Java version (single
 * thread) performance.
 *
 * @author Wilson de Carvalho
 * @author Renato Utsch
 */
public final class RGBA extends RGB {
    public float alpha = 0;

    public RGBA() {}

    public RGBA(float red, float green, float blue, float alpha) {
        super(red, green, blue);
        this.alpha = alpha;
    }

    public RGBA(int c) {
        super(Color.red(c), Color.green(c), Color.blue(c));
        this.alpha = Color.alpha(c);
    }

    public int toColor() {
        return Color.argb((int) this.alpha, (int) this.red, (int) this.green, (int) this.blue);
    }
}