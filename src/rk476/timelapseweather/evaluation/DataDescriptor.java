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
	
	double sum = 0;
	inputs.sort(new IntStringComparator());
	
	for (String s : inputs) {
	    sum += Double.parseDouble(s);
	}
	
	System.out.println("Mean: " + sum/inputs.size());
	System.out.println("Median: " + inputs.get(inputs.size()/2));
    }

    public DataDescriptor(String fileName) throws Exception {
	_fileName = fileName;
    }

    public static void main(String[] args) throws Exception {
	DataDescriptor dr = new DataDescriptor("data/datamerged.csv");
	System.out.println("-- ICON --");
	dr.describeDescreteColumn(4);
	System.out.println();
	
	System.out.println("-- CLOUD COVER --");
	dr.describeContinuousColumn(7);
    }

}
