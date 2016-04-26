/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 *  DCC-UFMG
 */

package br.ufmg.dcc.parallelme.userlibrary.datatypes;

/**
 * Base class for all indexed and valued data.
 *
 * @param <T>
 *     Type that will be used to store class data.
 *
 * @author Wilson de Carvalho.
 */
public class IndexedValuedData<T> implements UserData<T> {
    public T value;
    public int[] index;
}