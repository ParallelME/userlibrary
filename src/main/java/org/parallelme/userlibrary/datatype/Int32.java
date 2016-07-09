/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.userlibrary.datatype;

/**
 * Signed 32 bits integer.
 *
 * @author Wilson de Carvalho
 */
public class Int32 extends NumericalData<Integer> {
    public Int32() {
        this.value = null;
    }

    public Int32(Integer value) {
        this.value = value;
    }
    
	@Override
	public void setValue(Object obj) {
		value = (Integer) obj;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public Class<Integer> getValueClass() {
		return Integer.class;
	}
}