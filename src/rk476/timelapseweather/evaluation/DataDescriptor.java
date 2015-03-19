package rk476.timelapseweather.evaluation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class DataDescriptor {

    private String _fileName;

    public void describeDescreteColumn(int col) throws IOException {
	BufferedReader reader = new BufferedReader(new FileReader(_fileName));

	ArrayList<String> inputs = new ArrayList<>();

	String line;
	reader.readLine(); // remove headers.

	while ((line = reader.readLine()) != null) {
	    String[] elements = line.split(",");
	    inputs.add(elements[col]);
	}
	reader.close();

	HashMap<String, Integer> data = new HashMap<String, Integer>();

	int i = 0;

	for (String s : inputs) {
	    if (!data.containsKey(s)) {
		data.put(s, new Integer(0));
	    }

	    data.put(s, new Integer(data.get(s).intValue() + 1));
	}

	for (String s : data.keySet()) {
	    System.out.println(s + ": " + data.get(s) + " (" + String.format("%.2f", (double) data.get(s) * 100.0 / inputs.size()) + "%)");
	}
    }
    
    public static class IntStringComparator implements Comparator<String> {
	public int compare(String obj1, String obj2) {
	    double a = Double.parseDouble(obj1);
	    double b = Double.parseDouble(obj2);
	    if (a == b) {
		return 0;
	    }
	    if (a > b) {
		return 1;
	    }
	    return -1;
	}
    }

    public void describeContinuousColumn(int col) throws IOException {
	BufferedReader reader = new BufferedReader(new FileReader(_fileName));

	ArrayList<String> inputs = new ArrayList<>();

	String line;
	reader.readLine(); // remove headers.

	while ((line = reader.readLine()) != null) {
	    String[] elements = line.split(",");
	    inputs.add(elements[col]);
	}
	reader.close();
	
	if (col == 8) {
	    for(int i = 0; i < inputs.size(); i++) {
		inputs.set(i, String.valueOf(Double.parseDouble(inputs.get(i)) * (9.0/5.0) + 32.0));
	    }
	}

	double sum = 0;
	// inputs.sort(new IntStringComparator());

	// Generate range
	// Split into 10 buckets
	// Fill
	int[] buckets = new int[10];
	double minval = Double.parseDouble(inputs.get(0));
	double maxval = Double.parseDouble(inputs.get(inputs.size() - 1));
	double bucketsize = (maxval - minval) / 10.0;
	
	
	for (String s : inputs) {
	    double value = Double.parseDouble(s);
	    
	    buckets[(int)((Math.abs((value - minval)/maxval) * 10) % 10)]++;

	    sum += value;
	}

	for (int i = 0; i < 10; i++) {
	    System.out.println("(" + i + ") " + String.format("%.2f", minval + bucketsize * i) + " - " + String.format("%.2f", minval + bucketsize * (i+1)) + ": " + buckets[i] + 
		    " (" + String.format("%.2f", buckets[i] * 100.0 / inputs.size()) + "%)");
	}
	
	double mean = sum / inputs.size();
	double sumdifferencessquared = 0;
	for (String s : inputs) {
	    double value = Double.parseDouble(s);
	    
	    sumdifferencessquared += (mean - value) * (mean - value);
	    
	}
	
	double variance = sumdifferencessquared / inputs.size();
	
	System.out.println("Variance: " + variance);
	System.out.println("Standard Deviation: " + Math.sqrt(variance));
	System.out.println("Mean: " + mean);
	System.out.println("Median: " + inputs.get(inputs.size() / 2));
    }

    public DataDescriptor(String fileName) throws Exception {
	_fileName = fileName;
    }

    public static void main(String[] args) throws Exception {
	DataDescriptor dr = new DataDescriptor("data/data.csv");
	System.out.println("-- ICON --");
	dr.describeDescreteColumn(3);
	System.out.println();

	System.out.println("-- CLOUD COVER --");
	dr.describeContinuousColumn(6);
	System.out.println();

	
	System.out.println("-- PRECIPITATION --");
	dr.describeContinuousColumn(7);
	System.out.println();

	
	System.out.println("-- TEMPERATURE--");
	dr.describeContinuousColumn(8);
	System.out.println();

	dr = new DataDescriptor("data/datamerged.csv");
	System.out.println("-- CLOUD COVER --");
	dr.describeContinuousColumn(14 );
	System.out.println();
	
    }

}
