/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.userlibrary.image;

import org.parallelme.userlibrary.datatype.UserData;

/**
 * Support for RGB color space.
 * The attributes are public to increase user code readability and increase Java version (single
 * thread) performance.
 *
 * @author Wilson de Carvalho
 */
@SuppressWarnings("rawtypes")
public class RGB implements UserData {
    public float red = 0;
    public float green = 0;
    public float blue = 0;

    public RGB() {}

    public RGB(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

	@Override
	public void setValue(Object obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<RGB> getValueClass() {
		return RGB.class;
	}
}