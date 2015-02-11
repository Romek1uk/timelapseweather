package rk476.timelapseweather.evaluation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Evaluator {
	
	public static void evaluateFile(String fileName, int inputCol, int comparisonCol) throws IOException {
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
		
		try { // RMS if possible
			double msetotal = 0;
			int size = inputs.size();
			
			for (int i = 0; i < inputs.size(); i++) {
				if (inputs.get(i).equals("--") || comparisons.get(i).equals("--")) {
					// System.out.println("crap: " + i + " (inputs: " + inputs.get(i) + " ; comparisons: " + comparisons.get(i) + ")");
					size--;
				}
				else
				{
					double in = Double.parseDouble(inputs.get(i));
					double act = Double.parseDouble(comparisons.get(i));
					msetotal += (in - act)*(in - act);
				}
			}
			
			System.out.println("RMSE");
			
			System.out.println(msetotal);
			System.out.println(size);
			
			double mse = Math.sqrt(msetotal/(double)size);
			System.out.println(mse);
		}
		catch (NumberFormatException e)
		{
			int correct = 0;
			
			for (int i = 0; i < inputs.size(); i++) {
				correct += inputs.get(i).equals(comparisons.get(i)) ? 1 : 0;
			}
			
			System.out.println("Discrete");
			
			System.out.println(correct);
			System.out.println(inputs.size());
			System.out.println(((double)correct)/((double)inputs.size()));
		}
		
		System.out.println("-------");
	
		reader.close();
	}
	
	public static void main(String args[]) {
		String filename = "data/split/test0.csv";
		
		for(int i = 3; i < 10; i++)
		{
			try {
				evaluateFile(filename, i, i + 7);
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}

		
		System.out.println("asdasd");
	}
	
}
