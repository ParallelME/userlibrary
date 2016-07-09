/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.userlibrary.datatype;

/**
 * Signed 16 bits integer.
 *
 * @author Wilson de Carvalho
 */
public class Int16 extends NumericalData<Short> {
    public Int16() {
        this.value = null;
    }

    public Int16(Short value) {
        this.value = value;
    }
    
	@Override
	public void setValue(Object obj) {
		value = (Short) obj;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public Class<Short> getValueClass() {
		return Short.class;
	}
}