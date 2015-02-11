package rk476.timelapseweather.dataharvester;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import rk476.timelapseweather.dataharvester.camera.PiCamera;
import rk476.timelapseweather.dataharvester.data.CsvData;
import rk476.timelapseweather.dataharvester.data.CsvWriter;
import rk476.timelapseweather.dataharvester.imagemetrics.Brightness;
import rk476.timelapseweather.dataharvester.imagemetrics.Hue;
import rk476.timelapseweather.dataharvester.imagemetrics.Image;
import rk476.timelapseweather.dataharvester.weather.Forecast;

public class DataHarvester extends TimerTask {

	private static final int TIMEOUT = 5 * 60 * 1000;
	
	private static DropboxUploader _uploader;

	private static Date getNext5MinuteMark() {
		Calendar now = Calendar.getInstance();
		int mod = now.get(Calendar.MINUTE) % 5;
		now.add(Calendar.MINUTE, mod != 0 ? 5 - mod : 0);
		return now.getTime();
	}

	private static String getFormattedDateTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss");
		return sdf.format(date);
	}
 
	private static String getFormattedDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
		return sdf.format(date);
	}

	private static String getFormattedTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(date);
	}

	@Override
	public void run() {
		try {
			Date datetime = new Date();
			CsvData data = new CsvData();

			System.out.println("Enter run");

			new PiCamera().capture(getFormattedDateTime(datetime) + ".jpg");
			data.setName(getFormattedDateTime(datetime) + ".jpg");
			data.setDate(getFormattedDate(datetime));
			data.setTime(getFormattedTime(datetime));

			System.out.println("Captured");

			Forecast forecast = new Forecast(getFormattedDateTime(datetime));
			data.setActualIcon(forecast.getIcon());
			data.setActualSunrise(forecast.getSunriseTime());
			data.setActualSunset(forecast.getSunsetTime());
			data.setActualCloudCover(String.valueOf(forecast.getCloudCover()));
			data.setActualPrecipitation(String.valueOf(forecast
					.getPrecipitation()));
			data.setActualTemperature(String.valueOf(forecast.getTemperature()));
			// data.setActualVisibility(String.valueOf(forecast.getVisibility()));

			System.out.println("weather gotten");

			// TODO: this.
			data.setPredictedCloudCover("--");
			data.setPredictedIcon("--");
			data.setPredictedSunrise("--");
			data.setPredictedSunset("--");
			data.setPredictedPrecipitation("--");
			data.setPredictedTemperature("--");
			data.setPredictedVisibility("--");

			System.out.println("nulls set");

			Image image = null;
			String imageFile = getFormattedDateTime(datetime) + ".jpg";
			image = new Image(imageFile);
			System.out.println("image retrieved");
			
			
			_uploader.uploadFile(imageFile, "/project/data/images/" + imageFile); 
			System.out.println("image uploaded");
			
			if (_uploader.doesFileExist("/project/data/images/" + imageFile)) {
				Files.deleteIfExists(Paths.get(imageFile)); // Save space on the pi. 
			}
   
			data.setAverageHue(Hue.getAverageHueString(image));
			data.setBrightnessHistogram(Brightness
					.getBrightnessHistogram(image));
			data.setRedHistogram(Hue.getRedHistogram(image));
			data.setBlueHistogram(Hue.getBlueHistogram(image));
			data.setGreenHistogram(Hue.getGreenHistogram(image));

			System.out.println("data set");

			new CsvWriter("data.csv").addEntry(data);
			System.out.println("Added entry");
			
			_uploader.uploadFile("data.csv", "/project/data/data.csv");
			System.out.println("data uploaded");
			
		} catch (Exception e) {
			System.out.println(getFormattedDateTime(new Date()) + ":: ERROR");
		}

		System.out.println("end");
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println("Start");
		
		_uploader = new DropboxUploader();
		 
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new DataHarvester(), getNext5MinuteMark(),
				TIMEOUT);
		System.out.println("Next 5 minute mark is: "
				+ getFormattedDateTime(getNext5MinuteMark()));
	}
}
