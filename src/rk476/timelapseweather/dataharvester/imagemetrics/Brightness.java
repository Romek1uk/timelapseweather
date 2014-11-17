package rk476.timelapseweather.dataharvester.imagemetrics;

public final class Brightness {
	//TODO: Check if the average of this matches averageBrightness function
	public static Histogram getBrightnessHistogram(Image image) {
		Histogram ret = new Histogram();
		int pixellength = image.getPixelLength();
		byte[] pixels = image.getPixels();

		for (int i = 0; i < pixels.length; i += 3) {
			if (pixellength == 4) {
				i++;
			}

			int r = pixels[i] & 0xFF;
			int g = pixels[i + 1] & 0xFF;
			int b = pixels[i + 2] & 0xFF;

			ret.incrementValue((int) ((0.2126 * r + 0.7152 * g + 0.0722 * b)));
		}

		return ret;
	}

	private Brightness() {
	}
}
