/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.userlibrary.image;

import org.parallelme.userlibrary.function.ForeachFunction;
import org.parallelme.userlibrary.parallel.ParallelIterable;

/**
 * Parallel iterator for images.
 *
 * @author Wilson de Carvalho
 */
public class ImageParallelIterator extends ParallelIterable<RGBA> {
    @Override
    public void foreach(ForeachFunction<RGBA> userFunction) {
        // TODO Criar a implementacao aqui
    }
}
