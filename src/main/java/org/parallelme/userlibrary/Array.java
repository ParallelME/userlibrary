/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.userlibrary;

import org.parallelme.userlibrary.datatypes.NumericalData;
import org.parallelme.userlibrary.function.*;
import org.parallelme.userlibrary.parallel.ParallelIterator;

/**
 * @author Wilson de Carvalho
 */
@SuppressWarnings("rawtypes")
public class Array<E extends NumericalData> {
    private Object array;
    private final Class<E> typeParameterClass;
    private final int numDimensions;

    /**
     *
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
        // Number of dimensions fixed in 1, but in future versions we may have support for
        // multidimensional arrays.
        this.numDimensions = 1;
    }

    /**
     * Returns the inner array that was informed in the constructor with
	 * any changes that may have been performed on iterator's calls.
     */
    public Object toJavaArray() {
        return array;
    }

    /**
     * Iterates over this array and applies an user function over all its
     * elements.
     *
     * @param userFunction
     *            User function that must be applied.
     */
    public void foreach(ForeachFunction<E> userFunction) {
        this.runForeach(userFunction, this.array, new int[numDimensions], 0);
    }

    @SuppressWarnings("unchecked")
    private void runForeach(ForeachFunction<E> userFunction, Object array,
                            int[] index, int currDimension) {
        if (currDimension < (numDimensions - 1)) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(array); i++) {
                index[currDimension] = i;
                Object tmpArray = java.lang.reflect.Array.get(array, i);
                this.runForeach(userFunction, tmpArray, index,
                        currDimension + 1);
            }
        } else {
            E foo;
            try {
                for (int i = 0; i < java.lang.reflect.Array.getLength(array); i++) {
                    index[currDimension] = i;
                    foo = typeParameterClass.newInstance();
                    foo.value = java.lang.reflect.Array.get(array, i);
                    foo.index = index;
                    userFunction.function(foo);
                    java.lang.reflect.Array.set(array, i, foo.value);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    public ParallelIterator<E> par() {
        return new ParallelIterator<E>();
    }
}
