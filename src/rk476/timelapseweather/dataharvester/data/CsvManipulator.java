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
}