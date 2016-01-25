/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / _ / /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 *  DCC-UFMG
 */

package br.ufmg.dcc.parallelme.userlibrary.image;

/**
 * Support for RGB color space.
 * The attributes are public to increase user code readability and increase Java version (single
 * thread) performance.
 *
 * @author Wilson de Carvalho
 */
public class RGB {
    public float red = 0;
    public float green = 0;
    public float blue = 0;

    public RGB() {}

    public RGB(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
}