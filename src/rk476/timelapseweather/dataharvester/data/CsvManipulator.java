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

	@Override
	public void close() throws IOException {
		Files.delete(new File(_tempFile).toPath());
	}

}
