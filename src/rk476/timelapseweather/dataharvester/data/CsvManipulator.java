package rk476.timelapseweather.dataharvester.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

//TODO: This entire thing can be made more efficient by removing whole file read/writes for each individual operation.
// -- Mark the operations and then do them in a single batch
// -- Temp file is pretty stupid.

public class CsvManipulator {

	private String _inputFile;

	public String getInputFile() {
		return _inputFile;
	}

	private static Random _random = new Random();

	public CsvManipulator(String file) {
		_inputFile = file;
	}
	
	public void addColumnWithValue(int columnNumber, String title, String value) throws IOException {
	    BufferedReader reader = new BufferedReader(new FileReader(_inputFile));
	    
	    ArrayList<String> lines = new ArrayList<String>();
	    
	    String line;
	    while ((line = reader.readLine()) != null) {
		lines.add(line);
	    }
	    
	    reader.close();
	    
	    // Header
	    line = lines.remove(0);
	    String[] split = line.split(",");
	    LinkedList<String> list = new LinkedList<String>(Arrays.asList(split));
	    list.add(columnNumber, title);
	    
	    for (String s : lines) {
		
	    }
	    
	}

	public void replaceEntryOnLine(int row, int column, String value) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(_inputFile));		
		String content = "";
		
		row = row + 1; // ignore headers
		
		ArrayList<String> lines = new ArrayList<String>();
		
		String line;
		while ((line = reader.readLine()) != null) {
			lines.add(line);
		}
		
		reader.close();
		
		content = lines.get(row);
		
		String[] split = content.split(",");
		split[column] = value;
		
		content = "";
		for(String s : split) {
			content += s + ",";
		}
		content = content.substring(0, content.length() - 1); // remove last comma
		
		lines.set(row, content);
				
		BufferedWriter writer = new BufferedWriter(new FileWriter(_inputFile));
		for (String s : lines) {
			writer.write(s + "\n");
		}
		writer.close();
	}
	
	public void splitCsvFile(int quantity,
			String newDirectory) throws IOException {
		// create files
		CsvWriter[] trainfiles = new CsvWriter[quantity];
		CsvWriter[] testfiles = new CsvWriter[quantity];

		for (int i = 0; i < quantity; i++) {
			trainfiles[i] = new CsvWriter(newDirectory + "train" + i + ".csv");
			testfiles[i] = new CsvWriter(newDirectory + "test" + i + ".csv");
		}

		BufferedReader reader = new BufferedReader(new FileReader(_inputFile));

		String line;
		while ((line = reader.readLine()) != null) {
			int val = _random.nextInt(quantity);

			for (int i = 0; i < quantity; i++) {
				if (i == val)
					testfiles[val].addLine(line);
				else
					trainfiles[val].addLine(line);
			}
		}

		reader.close();
	}
}
