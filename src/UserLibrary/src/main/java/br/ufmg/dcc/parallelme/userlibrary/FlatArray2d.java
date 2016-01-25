/**
 *   ___  ___ ___         ___       ___
 *  |   \/   /    _ |   ||__ |\  /|/  _
 *  |___/\___\___   |___||   | \/ |\___|
 *
 *  DCC-UFMG
 */

package br.ufmg.dcc.parallelme.userlibrary;

import br.ufmg.dcc.parallelme.userlibrary.function.*;

/**
 * A FlatArray2d is a bi-dimensional array stored in a form of a uni-dimensional array. It was
 * designed to support both morton and row major encoding to store elements on a single dimension
 * array.
 *
 * @author Wilson de Carvalho
 * @version 0.1
 */
public class FlatArray2d<E> {
    private final E[][] array;
    private final int height, width;

    /**
     *
     * @param classType
     *             Elements' class type.
     * @param isMortonCode
     *             True for morton encoding or false for row major (may present differences in
     *             performance for each choice).
     * @param width
     *             Width of the 2d internal array.
     * @param height
     *             Height of the 2d internal array.
     */
    public FlatArray2d(Class<? extends E> classType, boolean isMortonCode, int width, int height) {
        this.height = height;
        this.width = width;
        int[] dimensions = new int[2];
        dimensions[0] = width;
        dimensions[1] = height;
        this.array = (E[][]) java.lang.reflect.Array.newInstance(classType, dimensions);
    }

    public int[] getDimensions() {
        int[] dimensions = new int[2];
        dimensions[0] = width;
        dimensions[1] = height;
        return dimensions;
    }

    public void fill(UserFunctionFill<E> userFunction) {
        for (int x = 0; x < this.width; x++)
            for (int y = 0; y < this.height; y++)
                this.array[x][y] = userFunction.function();
    }

    public E get(int x, int y) {
        return this.array[x][y];
    }

    public void set(E value, int x, int y) {
        this.array[x][y] = value;
    }

    /**
     * Iterate over this array and apply a user function over all
     * its elements.
     *
     * @param userFunction
     *            User function that must be applied.
     */
    public void foreach(UserFunction<E> userFunction) {
        for (int x = 0; x < this.width; x++)
            for (int y = 0; y < this.height; y++)
                userFunction.function(this.array[x][y]);
    }

    /**
     * Iterates over this array and applies an user function over all its elements, returning
     * each element with its (x, y) original coordinates.
     *
     * @param uf
     *            User function that must be applied.
     */
    public void foreach(UserFunctionWithIndex2d<E> uf) {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                ElementWithIndex2d<E> element = new ElementWithIndex2d<E>(this.array[x][y], x, y);
                uf.function(element);
            }
        }
    }

    /*public ParallelIterator<E> par() {
        int[] dimensions = new int[2];
        dimensions[0] = width;
        dimensions[1] = height;
        return new ParallelIterator<E>(array, null);
    }*/
}
