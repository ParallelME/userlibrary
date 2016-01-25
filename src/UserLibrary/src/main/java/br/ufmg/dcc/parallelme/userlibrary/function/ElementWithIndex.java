/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / _ / /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 *  DCC-UFMG
 */

package br.ufmg.dcc.parallelme.userlibrary.function;

/**
 * Stores an element and its index. The index is the one used on the userlibrary's underlying data
 * structure and can be reverted to its original index (the n-dimensional index of the original
 * user userlibrary) using an appropriate function.
 *
 * @author Wilson de Carvalho
 */
public class ElementWithIndex<E> {
    public E element;
    public int index;

    public ElementWithIndex() {
    }

    public ElementWithIndex(E element, int index) {
        this.element = element;
        this.index = index;
    }
}
