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
 * UserFunctionMap is an interface that users must implement to have access to each element during
 * iteration and to provide a function with a return that can be used to build new userlibrarys.
 * It was primarily designed to be used on map calls.
 *
 * @author Wilson de Carvalho
 */
public interface UserFunctionMap<E, R> {
    R function(E element);
}