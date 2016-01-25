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
 * Stores an element and its original bi-dimensional index to be used on 2d userlibrarys.
 *
 * @author Wilson de Carvalho
 */
public class ElementWithIndex2d<E> {
    public E element;
    public int x;
    public int y;

    public ElementWithIndex2d(E element, int x, int y) {
        this.element = element;
        this.x = x;
        this.y = y;
    }
}
