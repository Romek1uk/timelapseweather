package rk476.timelapseweather.dataharvester.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CsvWriter {
	private FileWriter _writer;

	private void generateHeaders() throws IOException {
		// Image file properties
		_writer.append("name,");
		_writer.append("date,");
		_writer.append("time,");

		// Actual weather information
		_writer.append("actual icon,");
		_writer.append("actual sunrise,");
		_writer.append("actual sunset,");
		_writer.append("actual cloud cover,");
		_writer.append("actual precipitation,");
		_writer.append("actual temperature,");
		_writer.append("actual visibility,");

		// Predicted weather information
		_writer.append("predicted icon,");
		_writer.append("predicted sunrise,");
		_writer.append("predicted sunset,");
		_writer.append("predicted cloud cover,");
		_writer.append("predicted precipitation,");
		_writer.append("predicted temperature,");
		_writer.append("predicted visibility,");

		// Image metrics
		for (int i = 0; i < 256; i++) {
			_writer.append("brightness histogram [" + i + "],");
		}
		for (int i = 0; i < 256; i++) {
			_writer.append("red histogram [" + i + "],");
		}
		for (int i = 0; i < 256; i++) {
			_writer.append("green histogram [" + i + "],");
		}
		for (int i = 0; i < 256; i++) {
			_writer.append("blue histogram [" + i + "],");
		}

		_writer.append("average hue");

		_writer.append("\n");
		
		System.out.println("Made headers");
		
		_writer.flush();
		
		System.out.println("Saved");
	}
	
	public void addLine(String line) throws IOException {
		if (!line.endsWith("\n"))
			line += "\n";
		_writer.append(line);
		_writer.flush();
	}

	public void addEntry(CsvData data) throws IOException {
		_writer.append(data.getName() + ",");
		_writer.append(data.getDate() + ",");
		_writer.append(data.getTime() + ",");
		
		_writer.append(data.getActualIcon() + ",");
		_writer.append(data.getActualSunrise() + ",");
		_writer.append(data.getActualSunset() + ",");
		_writer.append(data.getActualCloudCover() + ",");
		_writer.append(data.getActualPrecipitation() + ",");
		_writer.append(data.getActualTemperature() + ",");
		_writer.append(data.getActualVisibility() + ",");
		
		_writer.append(data.getPredictedIcon() + ",");
		_writer.append(data.getPredictedSunrise() + ",");
		_writer.append(data.getPredictedSunset() + ",");
		_writer.append(data.getPredictedCloudCover() + ",");
		_writer.append(data.getPredictedPrecipitation() + ",");
		_writer.append(data.getPredictedTemperature() + ",");
		_writer.append(data.getPredictedVisibility() + ",");
		
		for (int i = 0; i < 256; i++) {
			_writer.append(data.getBrightnessHistogram().getData(i) + ",");
		}
		for (int i = 0; i < 256; i++) {
			_writer.append(data.getRedHistogram().getData(i) + ",");
		}
		for (int i = 0; i < 256; i++) {
			_writer.append(data.getGreenHistogram().getData(i) + ",");
		}
		for (int i = 0; i < 256; i++) {
			_writer.append(data.getBlueHistogram().getData(i) + ",");
		}
		
		_writer.append(data.getAverageHue() + "\n");
		
		System.out.println("Made entry");
		
		_writer.flush();
		
		System.out.println("Saved");
	}

	public CsvWriter(String fileName) {
		File file = new File(fileName);

		System.out.println("file");
		
		try {
			if (!file.exists()) {
				System.out.println("exists");
				_writer = new FileWriter(file);
				
				generateHeaders();
			}
			else {
				_writer = new FileWriter(file, true);
			}
			
			System.out.println("writer created");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
