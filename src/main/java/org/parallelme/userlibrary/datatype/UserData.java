/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.userlibrary.datatype;

/**
 * Interface for defining an user data. All user data classes must implement
 * this interface.
 *
 * @author Wilson de Carvalho
 */
public interface UserData<T> {
	public void setValue(Object obj);

	public Object getValue();
	
	public Class<T> getValueClass();
}