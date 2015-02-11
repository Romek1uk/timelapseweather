package rk476.timelapseweather.dataharvester.weather;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.github.dvdme.ForecastIOLib.FIOCurrently;
import com.github.dvdme.ForecastIOLib.FIODaily;
import com.github.dvdme.ForecastIOLib.ForecastIO;

public class Forecast {
	private static final String LATITUDE = "52.200989";
	private static final String LONGITUDE = "0.123079";
	private static final String FORECAST_IO_ID = "efd008c5521821b549afec26b44bf0b3";

	private ForecastIO _fio;
	private FIOCurrently _fioCurrently;
	private FIODaily _fioDaily;

	public String getIcon() {
		return _fioCurrently.get().icon();
	}

	public String getSunriseTime() {
		return _fioDaily.getDay(0).sunriseTime();
	}

	public String getSunsetTime() {
		return _fioDaily.getDay(0).sunsetTime();
	}

	public double getCloudCover() {
		return _fioCurrently.get().cloudCover();
	}

	public double getPrecipitation() {
		return _fioCurrently.get().precipIntensity();
	}

	public double getTemperature() {
		// from F to C
		return (_fioCurrently.get().temperature() - 32.0) * (5.0 / 9.0);
	}
	
	public double getVisibility() {
		return _fioCurrently.get().visibility();
	}
	
	private static String getFormattedDateTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss");
		return sdf.format(date);
	}

	public Forecast(String time) {
		System.out.println(time);
		
		_fio = new ForecastIO(LATITUDE, LONGITUDE, ForecastIO.UNITS_SI, ForecastIO.LANG_ENGLISH, FORECAST_IO_ID);
	//	_fio.setUnits(ForecastIO.UNITS_SI);
		//_fio.setLang(ForecastIO.LANG_ENGLISH);
		_fio.setTime(time);
		
		_fio.update();

		_fioCurrently = new FIOCurrently(_fio);
		_fioDaily = new FIODaily(_fio);
	}
}
