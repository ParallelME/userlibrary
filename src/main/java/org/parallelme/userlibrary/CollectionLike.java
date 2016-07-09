/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.userlibrary;

import org.parallelme.userlibrary.datatype.UserData;
import org.parallelme.userlibrary.operation.FilterLike;
import org.parallelme.userlibrary.operation.ForeachLike;
import org.parallelme.userlibrary.operation.MapLike;
import org.parallelme.userlibrary.operation.ReduceLike;
import org.parallelme.userlibrary.parallel.ParallelLike;

@SuppressWarnings("rawtypes")
public abstract class CollectionLike<E extends UserData> implements
		ForeachLike<E>, ReduceLike<E>, MapLike<E>, FilterLike<E>,
		ParallelLike<E> {
}
