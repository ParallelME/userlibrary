/**
 *   ___  ___ ___         ___       ___
 *  |   \/   /    _ |   ||__ |\  /|/  _
 *  |___/\___\___   |___||   | \/ |\___|
 *
 *  DCC-UFMG
 */

package br.ufmg.dcc.parallelme.userlibrary;

import br.ufmg.dcc.parallelme.userlibrary.function.*;
import br.ufmg.dcc.parallelme.userlibrary.parallel.ParallelIterator;

/**
 * @author Wilson de Carvalho
 * @version 0.1
 */
public class FlatArray<E> {
    private final E[] array;
    private final int[] dimensions; // array dimensions
    private final int dim; // number of dimensions
    private final int bitsByDim; // number of bits by dimension
    private final boolean isMortonCode;
    private static long[] longRet;

    /**
     *
     * @param classType
     *             Elements' class type.
     * @param dimensions
     *             Array containing dimensions' size.
     */
    public FlatArray(Class<E> classType, boolean isMortonCode, int... dimensions) {
        this.isMortonCode = isMortonCode;
        this.dimensions = dimensions;
        this.dim = dimensions.length;
        int biggestDimension = 0;
        for (int i = 0; i < dimensions.length; i++) {
            if (dimensions[i] > biggestDimension)
                biggestDimension = dimensions[i];
        }
        // To store a multi dimensional array on a single dimensional array using morton code, we
        // must use sizes that are power of two to avoid creating invalid indexes. To guarantee
        // that, we use the closest value that is greater than the biggest value and is a power
        // of two.
        /*int arrayDim = Integer.highestOneBit(biggestDimension);
        if (biggestDimension > arrayDim)
            arrayDim <<= 1;*/
        int arrayDim = biggestDimension; // temporary
        int numElements = (int) Math.pow(arrayDim, dimensions.length);
        this.array = (E[]) java.lang.reflect.Array.newInstance(classType, numElements);
        int val = Long.SIZE / dimensions.length;
        val -= (val % dimensions.length) + 1;
        this.bitsByDim = val;
        this.longRet = new long[dim];
    }

    public int[] getDimensions() {
        return this.dimensions;
    }

    public E[] getFlatArray() {
        return array;
    }

    public void fill(UserFunctionFill<E> userFunction) {
        for (int i = 0; i < this.array.length; i++) {
            this.array[i] = userFunction.function();
        }
    }

    private int rowMajorEncode(int... indexes) {
        return (this.dimensions[0] * indexes[0]) + indexes[1];
    }

    private void rowMajorDecode(int index, int[] decoded) {
        decoded[0] = index / this.dimensions[0]; // considering always a square matrix
        decoded[1] = index - (decoded[0] * this.dimensions[0]);
    }

    private int mortonEncode(int... indexes) {
        int result = 0;
        for (int ii = this.bitsByDim; ii >= 0; ii--) {
            for (int i = 0; i < this.dim; i++) {
                result = (result << 1) | ((indexes[i] >> ii) & 1);
            }
        }
        return result;
    }

    private void mortonDecode(int value, long[] decoded) {
        for (int i = 0; i < this.dim; i++)
            decoded[i] = 0;
        for (int ii = this.bitsByDim; ii >= 0; ii -= this.dim) {
            for (int i = 0; i < this.dim; i++) {
                decoded[i] = (decoded[i] << 1) | ((value >> ii - i) & 1);
            }
        }
    }

    public E get(int... indexes) {
        if (this.isMortonCode)
            return this.array[this.mortonEncode(indexes)];
        else
            return this.array[this.rowMajorEncode(indexes)];
    }

    public void getOriginalIndex(int index, int[] ret) {
        if (this.isMortonCode) {
            this.mortonDecode(index, longRet);
            for (int i=0; i<dim; i++)
                ret[i] = (int)longRet[i];
        } else {
            this.rowMajorDecode(index, ret);
        }
    }

    public void set(E value, int... indexes) {
        if (this.isMortonCode)
            this.array[this.mortonEncode(indexes)] = value;
        else
            this.array[this.rowMajorEncode(indexes)] = value;
    }

    /**
     * Iterates over this array and applies an user function over all its elements.
     *
     * @param userFunction
     *            User function that must be applied.
     */
    public void foreach(UserFunction<E> userFunction) {
        for (int i = 0; i < this.array.length; i++) {
            userFunction.function(this.array[i]);
        }
    }

    /**
     * Iterates over this array and applies an user function over all its elements, returning
     * each element with its position on the internal array.
     *
     * @param uf
     *            User function that must be applied.
     */
    public void foreach(UserFunctionWithIndex<E> uf) {
        ElementWithIndex<E> element = new ElementWithIndex<E>();
        for (int i = 0; i < this.array.length; i++) {
            element.element = this.array[i];
            element.index = i;
            uf.function(element);
        }
    }

    public ParallelIterator<E> par() {
        return new ParallelIterator<E>(array, dimensions);
    }
}
