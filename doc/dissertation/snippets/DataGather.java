public class DataHarvester extends TimerTask {

	// 5 minute intervals
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

			// Take photo
			new PiCamera().capture(getFormattedDateTime(datetime) + ".jpg");
			data.setName(getFormattedDateTime(datetime) + ".jpg");
			data.setDate(getFormattedDate(datetime));
			data.setTime(getFormattedTime(datetime));

			// Get forecast
			Forecast forecast = new Forecast(getFormattedDateTime(datetime));
			data.setActualIcon(forecast.getIcon());
			data.setActualSunrise(forecast.getSunriseTime());
			data.setActualSunset(forecast.getSunsetTime());
			data.setActualCloudCover(String.valueOf(forecast.getCloudCover()));
			data.setActualPrecipitation(String.valueOf(forecast.getPrecipitation()));
			data.setActualTemperature(String.valueOf(forecast.getTemperature()));
			// data.setActualVisibility(String.valueOf(forecast.getVisibility()));

			// Predicted data will be filled later
			data.setPredictedCloudCover("--");
			data.setPredictedIcon("--");
			data.setPredictedSunrise("--");
			data.setPredictedSunset("--");
			data.setPredictedPrecipitation("--");
			data.setPredictedTemperature("--");
			data.setPredictedVisibility("--");

			// Retrieve captured image
			String imageFile = getFormattedDateTime(datetime) + ".jpg";
			Image image = new Image(imageFile);
			
			// Upload image
			_uploader.uploadFile(imageFile, "/project/data/images/" + imageFile); 
			System.out.println("image uploaded");
			
			if (_uploader.doesFileExist("/project/data/images/" + imageFile)) {
				Files.deleteIfExists(Paths.get(imageFile)); // Save space on the pi. 
			}
   
			// Image feature extraction
			data.setAverageHue(Hue.getAverageHueString(image));
			data.setBrightnessHistogram(Brightness.getBrightnessHistogram(image));
			data.setRedHistogram(Hue.getRedHistogram(image));
			data.setBlueHistogram(Hue.getBlueHistogram(image));
			data.setGreenHistogram(Hue.getGreenHistogram(image));

			// Add line to csv file
			new CsvWriter("data.csv").addEntry(data);
			
			// Upload csv file
			_uploader.uploadFile("data.csv", "/project/data/data.csv");
			
		} catch (Exception e) {
			e.printStackTrace();
			EmailSender.sendEmail(e.message);
		}
	}
	
	public static void main(String[] args) throws IOException {
		_uploader = new DropboxUploader();
		 
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new DataHarvester(), getNext5MinuteMark(), TIMEOUT);
	}
}

