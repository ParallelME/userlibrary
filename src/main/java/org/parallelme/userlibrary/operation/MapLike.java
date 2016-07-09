/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.userlibrary.operation;

import org.parallelme.userlibrary.CollectionLike;
import org.parallelme.userlibrary.datatype.UserData;
import org.parallelme.userlibrary.function.Map;

/**
 * Base interface for all collections that need to implement map operations on ParallelME.
 *
 * @author Wilson de Carvalho
 */
@SuppressWarnings("rawtypes")
public interface MapLike<E extends UserData> {
    /**
     * Applies a Map function in all elements.
     *
     * @param userFunction
     *            The user function that must be applied.
     */
    <R extends UserData> CollectionLike<R> map(Class<R> classR, Map<R, E> userFunction);
}
