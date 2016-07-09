/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.userlibrary.operation;

import org.parallelme.userlibrary.datatype.UserData;
import org.parallelme.userlibrary.function.Reduce;

/**
 * Base interface for all collections that need to implement reduce operations on ParallelME.
 *
 * @author Wilson de Carvalho
 */
@SuppressWarnings("rawtypes")
public interface ReduceLike<E extends UserData> {
    /**
     * Applies a Reduce function in all elements.
     *
     * @param userFunction
     *            The user function that must be applied.
     */
    E reduce(Reduce<E> userFunction);
}
