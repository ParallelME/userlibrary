/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 *  DCC-UFMG
 */

package br.ufmg.dcc.parallelme.userlibrary;

import br.ufmg.dcc.parallelme.userlibrary.datatypes.UserData;
import br.ufmg.dcc.parallelme.userlibrary.function.ForeachFunction;

/**
 * Base interface for all iterable types existent on ParallelME. Generic iteration operations are
 * defined here.
 *
 * @author Wilson de Carvalho
 */
@SuppressWarnings("rawtypes")
public interface Iterable<E extends UserData> {
    /**
     * Applies an user function over all elements of this iterable.
     *
     * @param userFunction
     *            The user function that must be applied.
     */
    void foreach(ForeachFunction<E> userFunction);
}
