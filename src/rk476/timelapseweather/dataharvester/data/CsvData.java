package rk476.timelapseweather.dataharvester.data;

import rk476.timelapseweather.dataharvester.imagemetrics.Histogram;

public class CsvData {
	private String _name;
	private String _date;
	private String _time;

	private String _actualIcon;
	private String _actualSunrise;
	private String _actualSunset;
	private String _actualCloudCover;
	private String _actualPrecipitation;
	private String _actualTemperature;
	private String _actualVisibility;

	private String _predictedIcon;
	private String _predictedSunrise;
	private String _predictedSunset;
	private String _predictedCloudCover;

	private Histogram _brightnessHistogram;
	private Histogram _redHistogram;
	private Histogram _greenHistogram;
	private Histogram _blueHistogram;
	private String _averagehue;

	public Histogram getBrightnessHistogram() {
		return _brightnessHistogram;
	}

	public void setBrightnessHistogram(Histogram brightnessHistogram) {
		_brightnessHistogram = brightnessHistogram;
	}

	public Histogram getRedHistogram() {
		return _redHistogram;
	}

	public void setRedHistogram(Histogram redHistogram) {
		_redHistogram = redHistogram;
	}

	public Histogram getGreenHistogram() {
		return _greenHistogram;
	}

	public void setGreenHistogram(Histogram greenHistogram) {
		_greenHistogram = greenHistogram;
	}

	public Histogram getBlueHistogram() {
		return _blueHistogram;
	}

	public void setBlueHistogram(Histogram blueHistogram) {
		_blueHistogram = blueHistogram;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public String getDate() {
		return _date;
	}

	public void setDate(String date) {
		_date = date;
	}

	public String getTime() {
		return _time;
	}

	public void setTime(String time) {
		_time = time;
	}

	public String getActualIcon() {
		return _actualIcon;
	}

	public void setActualIcon(String actualicon) {
		_actualIcon = actualicon;
	}

	public String getActualSunrise() {
		return _actualSunrise;
	}

	public void setActualSunrise(String actualsunrise) {
		_actualSunrise = actualsunrise;
	}

	public String getActualSunset() {
		return _actualSunset;
	}

	public void setActualSunset(String actualsunset) {
		_actualSunset = actualsunset;
	}

	public String getActualCloudCover() {
		return _actualCloudCover;
	}

	public void setActualCloudCover(String actualcloudcover) {
		_actualCloudCover = actualcloudcover;
	}

	public String getActualPrecipitation() {
		return _actualPrecipitation;
	}

	public void setActualPrecipitation(String actualprecipitation) {
		_actualPrecipitation = actualprecipitation;
	}

	public String getActualTemperature() {
		return _actualTemperature;
	}

	public void setActualTemperature(String actualtemperature) {
		_actualTemperature = actualtemperature;
	}

	public String getActualVisibility() {
		return _actualVisibility;
	}

	public void setActualVisibility(String actualvisibility) {
		_actualVisibility = actualvisibility;
	}

	public String getPredictedIcon() {
		return _predictedIcon;
	}

	public void setPredictedIcon(String predictedicon) {
		_predictedIcon = predictedicon;
	}

	public String getPredictedSunrise() {
		return _predictedSunrise;
	}

	public void setPredictedSunrise(String predictedsunrise) {
		_predictedSunrise = predictedsunrise;
	}

	public String getPredictedSunset() {
		return _predictedSunset;
	}

	public void setPredictedSunset(String predictedsunset) {
		_predictedSunset = predictedsunset;
	}

	public String getPredictedCloudCover() {
		return _predictedCloudCover;
	}

	public void setPredictedCloudCover(String predictedcloudcover) {
		_predictedCloudCover = predictedcloudcover;
	}

	public String getAverageHue() {
		return _averagehue;
	}

	public void setAverageHue(String averagehue) {
		_averagehue = averagehue;
	}

}
