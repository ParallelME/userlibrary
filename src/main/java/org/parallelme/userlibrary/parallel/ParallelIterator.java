/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.userlibrary.parallel;

import org.parallelme.userlibrary.datatypes.UserData;
import org.parallelme.userlibrary.function.ForeachFunction;

/**
 * Defines the stub for a parallel iterator.
 * Parallel iterators are not implemented in Java as the target parallel code will run on the
 * selected runtime and thus must be translated by ParallelME compiler.
 *
 * @author Wilson de Carvalho
 */
@SuppressWarnings("rawtypes")
public class ParallelIterator<E extends UserData> {
    public ParallelIterator() {
    }

    public void foreach(ForeachFunction<E> ff) {
        throw new RuntimeException(
                "Parallel iterators are not implemented for Java version. "
                        +  "Use serial version or compile your code with ParallelME compiler.");
    }
}
