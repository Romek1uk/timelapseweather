package rk476.timelapseweather.dataharvester.data;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Random;

//TODO: This entire thing can be made more efficient by removing whole file read/writes for each individual operation.
// -- Mark the operations and then do them in a single batch
// -- Temp file is pretty stupid.

public class CsvManipulator implements Closeable {

	private String _inputFile;
	private String _outputFile;
	private String _tempFile;

	public String getInputFile() {
		return _inputFile;
	}

	public String getOutputFile() {
		return _outputFile;
	}

	public String getTempFile() {
		return _tempFile;
	}

	private static Random _random = new Random();

	public static void splitCsvFile(String inputFile, int quantity,
			String newDirectory) throws IOException {
		// create files
		CsvWriter[] trainfiles = new CsvWriter[quantity];
		CsvWriter[] testfiles = new CsvWriter[quantity];

		for (int i = 0; i < quantity; i++) {
			trainfiles[i] = new CsvWriter(newDirectory + "train" + i + ".csv");
			testfiles[i] = new CsvWriter(newDirectory + "test" + i + ".csv");
		}

		BufferedReader reader = new BufferedReader(new FileReader(inputFile));

		String line;
		while ((line = reader.readLine()) != null) {
			int val = _random.nextInt(quantity);

			for (int i = 0; i < quantity; i++) {
				if (i == val)
					trainfiles[val].addLine(line);
				else
					testfiles[val].addLine(line);
			}
		}

		reader.close();
	}

	public CsvManipulator(String inputFile, String outputFile, String tempFile)
			throws IOException {
		_inputFile = inputFile;
		_outputFile = outputFile;
		_tempFile = tempFile;

		Files.copy(new File(inputFile).toPath(), new File(tempFile).toPath(),
				StandardCopyOption.REPLACE_EXISTING);
	}

	private String removeEntryOnLine(String line, int entry) {
		String[] entries = line.split(",");
		String ret = "";
		for (int i = 0; i < entries.length; i++) {
			if (i != entry) {
				ret += entries[i] + ",";
			}
		}
		return ret.substring(0, ret.length() - 1); // remove last comma
	}

	public void removeColumn(int column) throws IOException {
		FileWriter writer = new FileWriter(_outputFile);
		BufferedReader reader = new BufferedReader(new FileReader(_tempFile));

		String line;
		while ((line = reader.readLine()) != null) {
			writer.append(removeEntryOnLine(line, column) + "\n");
		}

		writer.close();
		reader.close();

		Files.copy(new File(_outputFile).toPath(),
				new File(_tempFile).toPath(),
				StandardCopyOption.REPLACE_EXISTING);
	}

	private String swapEntriesOnLine(String line, int entryA, int entryB) {
		String[] entries = line.split(",");
		String ret = "";

		String temp = null;

		for (int i = 0; i < entries.length; i++) {
			if (i == entryA) {
				temp = entries[i];
				ret += entries[entryB] + ",";
			} else if (i == entryB) {
				ret += temp + ",";
			} else {
				ret += entries[i] + ",";
			}
		}
		return ret.substring(0, ret.length() - 1); // remove last comma
	}

	public void swapColumns(int columnA, int columnB) throws IOException {
		FileWriter writer = new FileWriter(_outputFile);
		BufferedReader reader = new BufferedReader(new FileReader(_tempFile));

		if (columnA > columnB) {
			int temp = columnA;
			columnA = columnB;
			columnB = temp;
		}

		String line;
		while ((line = reader.readLine()) != null) {
			writer.append(swapEntriesOnLine(line, columnA, columnB) + "\n");
		}

		writer.close();
		reader.close();

		Files.copy(new File(_outputFile).toPath(),
				new File(_tempFile).toPath(),
				StandardCopyOption.REPLACE_EXISTING);
	}

	@Override
	public void close() throws IOException {
		Files.delete(new File(_tempFile).toPath());
	}

}
