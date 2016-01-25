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
 * Represents a pixel with all its data: color information and its coordinates.
 *
 * @author Wilson de Carvalho
 */
public class Pixel {
    public RGBA rgba;
    public int x = -1;
    public int y = -1;

    public Pixel(RGBA rgba) {
        this.rgba = rgba;
    }

    public Pixel(RGBA rgba, int x, int y) {
        this.rgba = rgba;
        this.x = x;
        this.y = y;
    }
}