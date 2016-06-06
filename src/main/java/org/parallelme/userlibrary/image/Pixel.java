/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.userlibrary.image;

import org.parallelme.userlibrary.datatypes.UserData;

/**
 * Represents a pixel with all its data: color information and its coordinates.
 *
 * @author Wilson de Carvalho
 */
public class Pixel implements UserData {
    public RGBA rgba;
    public int x = -1;
    public int y = -1;

    public Pixel() { }

    public Pixel(RGBA rgba) {
        this.rgba = rgba;
    }

    public Pixel(RGBA rgba, int x, int y) {
        this.rgba = rgba;
        this.x = x;
        this.y = y;
    }

    public Pixel(float red, float green, float blue, float alpha, int x, int y) {
        this.rgba = new RGBA(red, green, blue, alpha);
        this.x = x;
        this.y = y;
    }
}