package org.parallelme.userlibrary;

import org.parallelme.userlibrary.datatype.Int16;
import org.parallelme.userlibrary.datatype.Int32;

public class Main {

	public static void main(String[] args) {
		int i = 0;
		int[] tmp = new int[13];
		for (int x = 0; x < tmp.length; x++) {
			tmp[x] = ++i;
		}
		Array<Int32> array = new Array<Int32>(tmp, Int32.class);
		System.out.println("Elements: ");
		array.toJavaArray(tmp);
		for (int x = 0; x < tmp.length; x++) {
			System.out.println("tmp[" + x + "] = " + tmp[x]);
		}

		System.out.println("Reduce: ");
		Int32 ret = array.reduce((element1, element2) -> {
			element1.value += element2.value;
			return element1;
		});
		System.out.println(ret.value);

		System.out.println("Map: ");
		Array<Int16> array2 = array.map(Int16.class, (element -> {
			return new Int16((short) (element.value + 10));
		}));
		array2.foreach(element -> {
			System.out.println(element.value);
		});

		System.out.println("Filter: ");
		Array<Int32> array3 = array.filter(element -> {
			return element.value > 10;
		});
		array3.foreach(element -> {
			System.out.println(element.value);
		});
	}
}
