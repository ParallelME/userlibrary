/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.userlibrary;

import org.parallelme.userlibrary.datatypes.NumericalData;
import org.parallelme.userlibrary.function.Foreach;
import org.parallelme.userlibrary.function.Reduce;
import org.parallelme.userlibrary.parallel.ParallelIterable;
import org.parallelme.userlibrary.parallel.ParallelIterator;
import org.parallelme.userlibrary.parallel.Parallelizable;

/**
 * This array currently supports single-dimension arrays with numerical types
 * defined by ParallelME.
 *
 * Future versions should be written to support n-dimensional configurations.
 *
 * @author Wilson de Carvalho
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class Array<E extends NumericalData> implements Iterable<E>,
		Reducible<E>, Parallelizable<E> {
	private Object array;
	private final Class<E> typeParameterClass;

	/**
	 * @param array
	 *            Primitive type array that will provide the data for this
	 *            array.
	 * @param typeParameterClass
	 *            Class object containing necessary information from
	 *            parametrized type E. This information will be used during
	 *            sequential executions in Java.
	 */
	public Array(Object array, Class<E> typeParameterClass) {
		this.array = array;
		this.typeParameterClass = typeParameterClass;
	}

	/**
	 * Returns the inner array that was informed in the constructor with any
	 * changes that may have been performed on iterator's calls.
	 */
	public Object toJavaArray() {
		return array;
	}

	/**
	 * Fills an informed array with the data stored in the inner array.
	 */
	public void toJavaArray(final Object array) {
		if (java.lang.reflect.Array.getLength(array) != java.lang.reflect.Array
				.getLength(this.array)) {
			throw new RuntimeException("Array sizes' differ.");
		}
		for (int i = 0; i < java.lang.reflect.Array.getLength(array); i++) {
			java.lang.reflect.Array.set(array, i,
					java.lang.reflect.Array.get(this.array, i));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void foreach(Foreach<E> userFunction) {
		try {
			E foo = typeParameterClass.newInstance();
			for (int i = 0; i < java.lang.reflect.Array.getLength(array); i++) {
				foo.value = java.lang.reflect.Array.get(array, i);
				userFunction.function(foo);
				java.lang.reflect.Array.set(array, i, foo.value);
			}
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch(IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ParallelIterable<E> par() {
		return new ParallelIterator<E>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E reduce(Reduce<E> userFunction) {
		try {
			int length = java.lang.reflect.Array.getLength(array);
			if (length > 0) {
				E ret = typeParameterClass.newInstance();
				ret.value = java.lang.reflect.Array.get(array, 0);
				for (int i = 1; i < length; i++) {
					E element2 = typeParameterClass.newInstance();
					element2.value = java.lang.reflect.Array.get(array, i);
					ret = userFunction.function(ret, element2);
				}
				return ret;
			} else {
				throw new RuntimeException("Failure reducing empty array.");
			}
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch(IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
