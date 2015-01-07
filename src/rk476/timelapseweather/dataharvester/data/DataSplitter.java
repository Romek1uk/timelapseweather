package rk476.timelapseweather.dataharvester.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class DataSplitter {

	private final static Random RANDOM = new Random();

	public static void SplitCsvFile(String inputFile, String trainingFile,
			String testFile) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		FileWriter trainingWriter = new FileWriter(trainingFile);
		FileWriter testWriter = new FileWriter(testFile);

		String line;

		reader.readLine(); // Removes headers

		while ((line = reader.readLine()) != null) {
			if (RANDOM.nextInt(5) == 0) {
				testWriter.append(line + "\n");
			} else {
				trainingWriter.append(line + "\n");
			}
		}

		trainingWriter.flush();
		testWriter.flush();
		trainingWriter.close();
		testWriter.close();
		reader.close();
	}
}
