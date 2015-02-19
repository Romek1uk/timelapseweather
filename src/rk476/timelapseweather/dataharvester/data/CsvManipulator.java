package rk476.timelapseweather.dataharvester.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
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

    private String addValueToLine(String line, int col, String content) {
	LinkedList<String> list = new LinkedList<String>(Arrays.asList(line.split(",")));
	list.add(col, content);

	String ret = "";
	for (String s : list) {
	    ret += s + ",";
	}

	return ret.substring(0, ret.length() - 1);
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
	lines.set(0, addValueToLine(lines.get(0), columnNumber, title));

	for (int i = 1; i < lines.size(); i++) { // Ignore header
	    lines.set(i, addValueToLine(lines.get(i), columnNumber, value));
	}

	// TODO: Duplicate code with replaceEntryOnLine
	BufferedWriter writer = new BufferedWriter(new FileWriter(_inputFile));
	for (String s : lines) {
	    writer.write(s + "\n");
	}
	writer.close();
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
	for (String s : split) {
	    content += s + ",";
	}
	content = content.substring(0, content.length() - 1); // remove last
							      // comma

	lines.set(row, content);

	BufferedWriter writer = new BufferedWriter(new FileWriter(_inputFile));
	for (String s : lines) {
	    writer.write(s + "\n");
	}
	writer.close();
    }

    public void splitCsvFile(int quantity, String newDirectory) throws IOException {
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

	for (int i = 0; i < quantity; i++) {
	    new CsvManipulator(newDirectory + "train" + i + ".csv").addColumnWithValue(0, "split", Integer.toString(i));
	    new CsvManipulator(newDirectory + "test" + i + ".csv").addColumnWithValue(0, "split", Integer.toString(i));
	}
    }

    private static int getMinuteFromLine(String input) {
	String[] splitDate = input.split(",")[2].split("-");
	String[] splitTime = input.split(",")[3].split(":");

	int result = 0;
	
	System.out.println(input);

	// Not exact values for minutes per day/month/year (too high)
	result += (Integer.parseInt(splitDate[0]) - 2014) * 1440 + Integer.parseInt(splitDate[1]) * 44000 + Integer.parseInt(splitDate[2]) * 526000;

	result += Integer.parseInt(splitTime[0]) * 60 + Integer.parseInt(splitTime[1]);

	return result;

    }
    
    private static boolean andBoolArray(boolean[] input) {
	for (boolean b : input) {
	    if (!b)
		return false;
	}
	return true;
    }
    
    private static int getSmallestIndex(int[] input) {
	int max = Integer.MAX_VALUE;
	int index = 0;
	for (int i = 0; i < input.length; i++) {
	    if (input[i] < max) {
		max = input[i];
		index = i;
	    }
	}
	return index;
    }

    public static void mergeCsvFiles(String[] inputFiles, String outputFile) throws IOException {
	// Merge in date order!
	CsvWriter writer = new CsvWriter(outputFile, true);

	BufferedReader[] readers = new BufferedReader[inputFiles.length];
	String[] lines = new String[inputFiles.length];
	boolean[] empty = new boolean[inputFiles.length];
	int[] max = new int[inputFiles.length];

	for (int i = 0; i < inputFiles.length; i++) {
	    readers[i] = new BufferedReader(new FileReader(inputFiles[i]));
	    
	    readers[i].readLine(); // headers
	    
	    lines[i] = readers[i].readLine();
	    
	    // TODO: Work around for strange writing headers twice bug??!?!
	    if (lines[i].split(",")[1].equals("name")) {
		lines[i] = readers[i].readLine();
	    }
	    
	    empty[i] = lines[i] == null;
	    max[i] = empty[i] ? Integer.MAX_VALUE : getMinuteFromLine(lines[i]);
	}

	while (!andBoolArray(empty)) { // while at least one isn't empty
	    int i = getSmallestIndex(max);
	    writer.addLine(lines[i]);
	    System.out.println("added + " + lines[i]);
	    lines[i] = readers[i].readLine();
	    
	    if (lines[i] == null) {
		empty[i] = true;
		max[i] = Integer.MAX_VALUE;
	    }
	}

	for (Reader r : readers) {
	    r.close();
	}
    }

    public static void main(String[] args) {
    }
}