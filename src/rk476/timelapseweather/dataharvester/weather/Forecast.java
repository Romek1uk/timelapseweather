package rk476.timelapseweather.dataharvester.weather;

import com.github.dvdme.ForecastIOLib.FIOCurrently;
import com.github.dvdme.ForecastIOLib.FIODaily;
import com.github.dvdme.ForecastIOLib.ForecastIO;

public class Forecast {
	private static final String LONGITUDE = "52.200989";
	private static final String LATITUDE = "0.123079";
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

	public Forecast(String time) {
		_fio = new ForecastIO(LATITUDE, LONGITUDE, FORECAST_IO_ID);
		_fio.setUnits(ForecastIO.UNITS_SI);
		_fio.setLang(ForecastIO.LANG_ENGLISH);
		_fio.setTime(time);

		_fioCurrently = new FIOCurrently(_fio);
		_fioDaily = new FIODaily(_fio);
	}
}
