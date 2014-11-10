package intro.forecast;

import java.io.IOException;
import java.util.Calendar;

import com.github.dvdme.ForecastIOLib.FIOCurrently;
import com.github.dvdme.ForecastIOLib.FIODaily;
import com.github.dvdme.ForecastIOLib.ForecastIO;


public class ForecastGetter
{
	public static void main(String args[]) throws InterruptedException, IOException
	{	
		boolean taken = false;
		
		// Run every 5 minutes, to the nearest 5 minute actual time
		while (true)
		{
			Calendar now = Calendar.getInstance();

			
			if (now.get(Calendar.MINUTE) % 5 == 0)
			{
				if (!taken)
				{
					ForecastIO fio = new ForecastIO("efd008c5521821b549afec26b44bf0b3");
					fio.setUnits(ForecastIO.UNITS_SI);
					fio.setLang(ForecastIO.LANG_ENGLISH);
					fio.getForecast("52.200989", "0.123079");
					
					FIOCurrently current = new FIOCurrently(fio);
					
					System.out.println("Date/Time: " + current.get().time());	
					System.out.println("Cloud Cover: " + current.get().cloudCover());
					System.out.println("Icon: " + current.get().icon());
					
					FIODaily daily = new FIODaily(fio);
					
					System.out.println("Sunrise: " + daily.getDay(0).sunriseTime());
					System.out.println("Sunset: " + daily.getDay(0).sunsetTime());
					
					taken = true;
				}
			}
			else
			{
				taken = false;
			}
			
			Thread.sleep(1000);
			
		}
	}
}
