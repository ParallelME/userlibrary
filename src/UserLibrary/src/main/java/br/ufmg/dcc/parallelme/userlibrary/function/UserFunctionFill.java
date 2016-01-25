/**
 *   ___  ___ ___         ___       ___
 *  |   \/   /    _ |   ||__ |\  /|/  _
 *  |___/\___\___   |___||   | \/ |\___|
 *
 *  DCC-UFMG
 */

package br.ufmg.dcc.parallelme.userlibrary.function;

/**
 * UserFunctionFill is an interface that users must implement to provide a return during iteration
 * of all elements of a userlibrary. It was primarily designed to be used during calls to method
 * fill to initialize a userlibrary.
 *
 * @author Wilson de Carvalho
 */
public interface UserFunctionFill<R> {
    public R function();
}