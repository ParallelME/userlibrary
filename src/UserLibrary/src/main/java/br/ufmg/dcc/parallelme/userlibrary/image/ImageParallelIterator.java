/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 *  DCC-UFMG
 */

package br.ufmg.dcc.parallelme.userlibrary.image;

import br.ufmg.dcc.parallelme.userlibrary.function.ForeachFunction;
import br.ufmg.dcc.parallelme.userlibrary.parallel.ParallelIterable;

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
