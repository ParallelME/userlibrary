/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 *  DCC-UFMG
 */

package br.ufmg.dcc.parallelme.userlibrary.parallel;

import br.ufmg.dcc.parallelme.userlibrary.Iterable;
import br.ufmg.dcc.parallelme.userlibrary.datatypes.UserData;

/**
 * Base class for all parallel iterables.
 *
 * @author Wilson de Carvalho
 */
@SuppressWarnings("rawtypes")
public abstract class ParallelIterable<E extends UserData> implements Iterable<E> {
}
