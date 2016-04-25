/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 *  DCC-UFMG
 */

package br.ufmg.dcc.parallelme.userlibrary.image;

/**
 * Support for RGBA color space.
 * The attributes are public to increase user code readability and increase Java version (single
 * thread) performance.
 *
 * @author Wilson de Carvalho
 */
public final class RGBA extends RGB {
    public float alpha = 0;

    public RGBA() {}

    public RGBA(float red, float green, float blue, float alpha) {
        super(red, green, blue);
        this.alpha = alpha;
    }
}