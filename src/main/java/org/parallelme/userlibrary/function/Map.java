/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.userlibrary.function;

import org.parallelme.userlibrary.datatype.UserData;

/**
 * Interface that users must implement to produce mapping operations.
 *
 * @author Wilson de Carvalho
 */
@SuppressWarnings("rawtypes")
public interface Map<R extends UserData, E extends UserData> {
	R function(E element);
}