/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.userlibrary.operation;

import org.parallelme.userlibrary.datatype.UserData;
import org.parallelme.userlibrary.function.Foreach;

/**
 * Base interface for all collections that need to implement foreach operations on ParallelME.
 *
 * @author Wilson de Carvalho
 */
@SuppressWarnings("rawtypes")
public interface ForeachLike<E extends UserData> {
    /**
     * Applies a Foreach function in all elements.
     *
     * @param userFunction
     *            The user function that must be applied.
     */
    void foreach(Foreach<E> userFunction);
}
