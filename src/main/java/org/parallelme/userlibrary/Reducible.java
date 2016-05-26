/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.userlibrary;

import org.parallelme.userlibrary.datatypes.UserData;
import org.parallelme.userlibrary.function.Reduce;

/**
 * Base interface for all reducible types existent on ParallelME. Generic reduction operations are
 * defined here.
 *
 * @author Wilson de Carvalho
 */
@SuppressWarnings("rawtypes")
public interface Reducible<E extends UserData> {
    /**
     * Applies a reduction function in all elements of this reducible.
     *
     * @param userFunction
     *            The user function that must be applied.
     */
    E reduce(Reduce<E> userFunction);
}
