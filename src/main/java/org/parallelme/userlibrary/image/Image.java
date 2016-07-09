/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.userlibrary.image;

import org.parallelme.userlibrary.Array;
import org.parallelme.userlibrary.datatype.UserData;
import org.parallelme.userlibrary.function.Foreach;
import org.parallelme.userlibrary.function.Map;
import org.parallelme.userlibrary.function.Reduce;
import org.parallelme.userlibrary.operation.ForeachLike;
import org.parallelme.userlibrary.operation.MapLike;
import org.parallelme.userlibrary.operation.ReduceLike;
import org.parallelme.userlibrary.parallel.ParallelLike;
import org.parallelme.userlibrary.parallel.ParallelOperation;

/**
 * Abstract image class with generic operations for all image types defined in
 * ParallelME.
 *
 * @author Wilson de Carvalho
 * @version 0.1
 */
public abstract class Image implements ForeachLike<Pixel>, ReduceLike<Pixel>,
		MapLike<Pixel>, ParallelLike<Pixel> {
	protected int width;
	protected int height;
	protected int size;
	protected Pixel[] pixels;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void foreach(Foreach<Pixel> userFunction) {
		for (int i = 0; i < size; i++)
			userFunction.function(pixels[i]);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Pixel reduce(Reduce<Pixel> userFunction) {
		Pixel pixel1 = pixels[0];
		for (int i = 1; i < size; i++) {
			Pixel pixel = pixels[i];
			pixel1 = userFunction.function(pixel1, new Pixel(pixel.rgba, pixel.x, pixel.y));
		}
		return pixel1;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public <R extends UserData> Array<R> map(Class<R> classR,
			Map<R, Pixel> userFunction) {
		try {
			R foo = classR.newInstance();
			Object retArray = java.lang.reflect.Array.newInstance(
					foo.getValueClass(), size);
			if (size > 0) {
				for (int i = 0; i < size; i++) {
					Pixel pixel = pixels[i];
					R ret = userFunction.function(new Pixel(pixel.rgba, pixel.x, pixel.y));
					java.lang.reflect.Array.set(retArray, i, ret.getValue());
				}
			}
			return new Array<R>(retArray, classR);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public ParallelOperation<Pixel> par() {
		return new ParallelOperation<Pixel>();
	}
}
