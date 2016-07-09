/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.userlibrary.parallel;

import org.parallelme.userlibrary.CollectionLike;
import org.parallelme.userlibrary.datatype.UserData;
import org.parallelme.userlibrary.function.Filter;
import org.parallelme.userlibrary.function.Foreach;
import org.parallelme.userlibrary.function.Map;
import org.parallelme.userlibrary.function.Reduce;
import org.parallelme.userlibrary.operation.FilterLike;
import org.parallelme.userlibrary.operation.ForeachLike;
import org.parallelme.userlibrary.operation.MapLike;
import org.parallelme.userlibrary.operation.ReduceLike;

/**
 * Defines the stub for a parallel iterator. Parallel iterators are not
 * implemented in Java as the target parallel code will run on the selected
 * runtime and thus must be translated by ParallelME compiler.
 *
 * @author Wilson de Carvalho
 */
@SuppressWarnings("rawtypes")
public class ParallelOperation<E extends UserData> implements ForeachLike<E>,
		ReduceLike<E>, MapLike<E>, FilterLike<E> {
	public ParallelOperation() {
	}

	@Override
	public void foreach(Foreach<E> ff) {
		throw new RuntimeException(
				"Parallel iterators are not implemented for Java user-library. "
						+ "Use sequential version or compile your code with ParallelME compiler.");
	}

	@Override
	public E reduce(Reduce<E> userFunction) {
		throw new RuntimeException(
				"Parallel reducers are not implemented for Java user-library. "
						+ "Use sequential version or compile your code with ParallelME compiler.");
	}

	@Override
	public <R extends UserData> CollectionLike<R> map(Class<R> classR,
			Map<R, E> userFunction) {
		throw new RuntimeException(
				"Parallel mappers are not implemented for Java user-library. "
						+ "Use sequential version or compile your code with ParallelME compiler.");
	}

	@Override
	public CollectionLike<E> filter(Filter<E> userFunction) {
		throw new RuntimeException(
				"Parallel filters are not implemented for Java user-library. "
						+ "Use sequential version or compile your code with ParallelME compiler.");
	}
}
