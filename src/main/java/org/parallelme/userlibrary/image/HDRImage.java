/**                                               _    __ ____
 *   _ __  ___ _____   ___   __  __   ___ __     / |  / /  __/
 *  |  _ \/ _ |  _  | / _ | / / / /  / __/ /    /  | / / /__
 *  |  __/ __ |  ___|/ __ |/ /_/ /__/ __/ /__  / / v  / /__
 *  |_| /_/ |_|_|\_\/_/ |_/____/___/___/____/ /_/  /_/____/
 *
 */

package org.parallelme.userlibrary.image;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * HDR image processing class.
 *
 * @author Pedro Caldeira
 * @author Wilson de Carvalho
 * @author Renato Utsch
 * @version 0.1
 */
public class HDRImage extends Image {
	public HDRImage(byte[] data, int width, int height) {
		this.height = height;
		this.width = width;
		this.size = width * height;
		this.pixels = new Pixel[size];
		float[] rgb = new float[3];
		for (int x = 0; x < width; x++) {
			int base = height * x;
			for (int y = 0; y < height; y++) {
				RGBE.rgbe2float(rgb, data, 4 * (y * width + x));
				pixels[base + y] = new Pixel(
						new RGBA(rgb[0], rgb[1], rgb[2], 0), x, y);
			}
		}
	}

	public Bitmap toBitmap() {
		Bitmap bitmap = Bitmap.createBitmap(this.width, this.height,
				Bitmap.Config.ARGB_8888);
		toBitmap(bitmap);
		return bitmap;
	}

	public void toBitmap(Bitmap bitmap) {
		for (int x = 0; x < width; ++x) {
			int base = x * height;
			for (int y = 0; y < height; ++y) {
				RGBA rgba = pixels[base + y].rgba;
				bitmap.setPixel(x, y, Color.argb(255,
						(int) (255.0f * rgba.red), (int) (255.0f * rgba.green),
						(int) (255.0f * rgba.blue)));
			}
		}
	}
}
