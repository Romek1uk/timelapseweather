package rk476.timelapseweather.evaluation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static rk476.timelapseweather.evaluation.DataType.*;

public class Evaluator {
    private static final DataType[] TYPES = { DISCRETE, TIME, TIME, CONTINUOUS, CONTINUOUS, CONTINUOUS, CONTINUOUS };
    private static final String[] TITLES = { "icon", "sunrise", "sunset", "cloudcover", "percipitation", "temp", "vis" };

    private static void evaluateDiscreteData(ArrayList<String> inputs, ArrayList<String> comparisons) {
	int correct = 0;
	int size = inputs.size();

	for (int i = 0; i < inputs.size(); i++) {
	    if (inputs.get(i).equals("--") || comparisons.get(i).equals("--") || inputs.get(i).equals("\"--\"") || comparisons.get(i).equals("\"--\"")) {
		size--;
	    } else {
		correct += inputs.get(i).replaceAll("\"", "").equals(comparisons.get(i).replaceAll("\"", "")) ? 1 : 0;
	    }
	}
	System.out.println(correct + "/" + size + " (" + ((double) correct) / ((double) size) + "%)");
	System.out.println("Failed: " + (inputs.size() - size) + "/" + inputs.size());
    }

    private static void evaluateTimeData(ArrayList<String> inputs, ArrayList<String> comparisons) {
	// TODO: Edge cases (time passes midnight)
	int msetotal = 0;
	int size = inputs.size();

	for (int i = 0; i < inputs.size(); i++) {
	    if (inputs.get(i).equals("--") || comparisons.get(i).equals("--")) {
		size--;
	    } else {
		String[] ins = inputs.get(i).split(":");
		int in = Integer.parseInt(ins[0]) * 3600 + Integer.parseInt(ins[1]) * 60 + Integer.parseInt(ins[2]);

		String[] acts = comparisons.get(i).split(":");
		int act = Integer.parseInt(acts[0]) * 3600 + Integer.parseInt(acts[1]) * 60 + Integer.parseInt(acts[2]);

		msetotal += (in - act) * (in - act);
	    }
	}
	double mse = Math.sqrt((double) msetotal / (double) size);
	System.out.println(mse + " seconds");
	System.out.println("Failed: " + (inputs.size() - size) + "/" + inputs.size());
    }

    private static void evaluateContinuousData(ArrayList<String> inputs, ArrayList<String> comparisons) {
	double msetotal = 0;
	int size = inputs.size();

	for (int i = 0; i < inputs.size(); i++) {
	    if (inputs.get(i).equals("--") || comparisons.get(i).equals("--")) {
		// System.out.println("crap: " + i + " (inputs: " +
		// inputs.get(i) + " ; comparisons: " + comparisons.get(i) +
		// ")");
		size--;
	    } else {
		double in = Double.parseDouble(inputs.get(i));
		double act = Double.parseDouble(comparisons.get(i));
		msetotal += (in - act) * (in - act);
	    }
	}
	double mse = Math.sqrt(msetotal / (double) size);
	System.out.println(mse);
	System.out.println("Failed: " + (inputs.size() - size) + "/" + inputs.size());
    }

    public static void evaluateData(String fileName, int inputCol, int comparisonCol, DataType type) throws IOException {
	BufferedReader reader = new BufferedReader(new FileReader(fileName));

	ArrayList<String> inputs = new ArrayList<>();
	ArrayList<String> comparisons = new ArrayList<>();

	String line;
	reader.readLine(); // remove headers.

	while ((line = reader.readLine()) != null) {
	    String[] elements = line.split(",");
	    inputs.add(elements[inputCol]);
	    comparisons.add(elements[comparisonCol]);
	}
	reader.close();

	if (type == DISCRETE) {
	    evaluateDiscreteData(inputs, comparisons);
	} else if (type == CONTINUOUS) {
	    evaluateContinuousData(inputs, comparisons);
	} else if (type == TIME) {
	    evaluateTimeData(inputs, comparisons);
	} else
	    System.out.println("OTHER DATA TYPE");
    }

    public static void main(String args[]) {
	for (int k = 10; k <= 100; k += 10) {
	    System.out.println("------ " + k);

	    String filename = "data/datamerged.csv";// "data/split/test0.csv";
	    // "data/randomforest/forest" + k + "trees/datamerged.csv";//

	    for (int i = 4; i < 11; i++) {
		try {
		    System.out.println(TITLES[i - 4] + ": " + TYPES[i - 4]);
		    evaluateData(filename, i, i + 7, TYPES[i - 4]);
		    System.out.println();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}

	// String filename =
	// "data/randomforest/forest10trees/datamerged.csv";//"data/datamerged.csv";//
	// "data/split/test0.csv";
	//
	// for (int i = 4; i < 11; i++) {
	// try {
	// System.out.println(TITLES[i - 4] + ": " + TYPES[i - 4]);
	// evaluateData(filename, i, i + 7, TYPES[i - 4]);
	// System.out.println();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
    }

}
