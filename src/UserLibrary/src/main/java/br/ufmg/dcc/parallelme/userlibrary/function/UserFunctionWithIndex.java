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
 * UserFunctionWithIndex is an interface that users must implement to have access to each element
 * with its specific index on the inner data structure during iteration.
 *
 * @author Wilson de Carvalho
 */
public interface UserFunctionWithIndex<E> {
    void function(ElementWithIndex<E> element);
}