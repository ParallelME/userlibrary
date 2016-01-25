/**
 *   ___  ___ ___         ___       ___
 *  |   \/   /    _ |   ||__ |\  /|/  _
 *  |___/\___\___   |___||   | \/ |\___|
 *
 *  DCC-UFMG
 */

package br.ufmg.dcc.parallelme.userlibrary.parallel;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import br.ufmg.dcc.parallelme.userlibrary.function.*;

/**
 * Parallel iterator prototype. There is a tons of duplicated code in this class, but this is just
 * an prototype to have a glimpse on native java parallel approach on Android, so forget about good
 * programming practices here.
 *
 * @author Wilson de Carvalho
 */
public class ParallelIterator<E> {
    private final E[] array;
    private final int[] dimensions;
    private static ForkJoinPool executor = new ForkJoinPool();

    public ParallelIterator(E[] array, int... dimensions) {
        this.array = array;
        this.dimensions = dimensions;
    }

    public void foreach(UserFunction<E> uf) {
        executor.invoke(new ComputeUserFunction<E>(this.array, 0, this.array.length, uf));
    }

    public void foreach(UserFunctionWithIndex<E> uf) {
        executor.invoke(new ComputeUserFunctionWithIndex<E>(this.array, 0, this.array.length, uf));
    }

    static class ComputeUserFunction<T> extends RecursiveAction {
        final T[] array;
        final int lo, hi;
        final UserFunction<T> uf;

        ComputeUserFunction(T[] array, int lo, int hi, UserFunction uf) {
            this.array = array;
            this.lo = lo;
            this.hi = hi;
            this.uf = uf;
       }

        ComputeUserFunction(T[] array, UserFunction<T> uf) {
            this(array, 0, array.length, uf);
        }

        protected void compute() {
            if (hi - lo < THRESHOLD)
                computeSequentially(lo, hi);
            else {
                int mid = (lo + hi) >>> 1;
                invokeAll(new ComputeUserFunction(array, lo, mid, uf),
                        new ComputeUserFunction(array, mid, hi, uf));
            }
        }
        // implementation details follow:
        static final int THRESHOLD = 100;
        void computeSequentially(int lo, int hi) {
            for (int i=lo; i<hi; i++) {
                this.uf.function(array[i]);
            }
        }
    }


    static class ComputeUserFunctionWithIndex<T> extends RecursiveAction {
        final T[] array;
        final int lo, hi;
        final UserFunctionWithIndex<T> uf;

        ComputeUserFunctionWithIndex(T[] array, int lo, int hi, UserFunctionWithIndex uf) {
            this.array = array;
            this.lo = lo;
            this.hi = hi;
            this.uf = uf;
        }

        ComputeUserFunctionWithIndex(T[] array, UserFunctionWithIndex<T> uf) {
            this(array, 0, array.length, uf);
        }

        protected void compute() {
            if (hi - lo < THRESHOLD)
                computeSequentially(lo, hi);
            else {
                int mid = (lo + hi) >>> 1;
                invokeAll(new ComputeUserFunctionWithIndex(array, lo, mid, uf),
                        new ComputeUserFunctionWithIndex(array, mid, hi, uf));
            }
        }
        // implementation details follow:
        static final int THRESHOLD = 1000;
        void computeSequentially(int lo, int hi) {
            for (int i=lo; i<hi; i++) {
                this.uf.function(new ElementWithIndex(array[i], i));
            }
        }
    }

    static class ComputeUserFunctionFill<T> extends RecursiveAction {
        final T[] array;
        final int lo, hi;
        final UserFunctionFill<T> ufr;

        ComputeUserFunctionFill(T[] array, int lo, int hi, UserFunctionFill ufr) {
            this.array = array;
            this.lo = lo;
            this.hi = hi;
            this.ufr = ufr;
        }

        ComputeUserFunctionFill(T[] array, UserFunctionFill<T> ufr) {
            this(array, 0, array.length, ufr);
        }

        protected void compute() {
            if (hi - lo < THRESHOLD)
                computeSequentially(lo, hi);
            else {
                int mid = (lo + hi) >>> 1;
                invokeAll(new ComputeUserFunctionFill(array, lo, mid, ufr),
                        new ComputeUserFunctionFill(array, mid, hi, ufr));
            }
        }
        // implementation details follow:
        static final int THRESHOLD = 100;
        void computeSequentially(int lo, int hi) {
            for (int i=lo; i<hi; i++) {
                array[i] = this.ufr.function();
            }
        }
    }
}
