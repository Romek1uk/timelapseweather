package rk476.timelapseweather.dataharvester.imagemetrics;

import java.util.Arrays;

public class Histogram {

	private static final int SIZE = 256;

	private int[] _data;

	public int getData(int index) {
		return _data[index];
	}

	public int[] getData() {
		return _data;
	}

	public void setData(int index, int value) {
		_data[index] = value;
	}

	public void incrementValue(int index) {
		_data[index]++;
	}

	public int getMode() {
		int[] copy = _data.clone();
		Arrays.sort(copy);
		return copy[255];
	}

	public int getMedian() {
		int[] copy = _data.clone();
		Arrays.sort(copy);
		return copy[127];
	}

	public double getMean() {
		double mean = 0;
		for (int i = 0; i < SIZE;) {
			mean += (double) (getData(i) - mean) / ++i;
		}
		return mean;
	}

	public Histogram() {
		_data = new int[SIZE];
	}
}
