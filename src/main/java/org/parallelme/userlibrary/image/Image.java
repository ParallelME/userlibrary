/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.userlibrary.image;

import org.parallelme.userlibrary.Iterable;
import org.parallelme.userlibrary.Reducible;

/**
 * A specific image processing iterator.
 *
 * @author Wilson de Carvalho
 * @author Renato Utsch
 * @version 0.1
 */
public interface Image extends Iterable<Pixel>, Reducible<Pixel> {
}
