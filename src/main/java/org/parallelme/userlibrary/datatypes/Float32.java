/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.userlibrary.datatypes;

/**
 * Signed 32 bits float.
 *
 * @author Wilson de Carvalho
 */
public class Float32 extends NumericalData<Float> {
    public Float32() {
        this.value = null;
    }

    public Float32(Float value) {
        this.value = value;
    }
}