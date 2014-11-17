package rk476.timelapseweather.dataharvester.imagemetrics;

import java.awt.Color;

public final class Hue {

	public static Color getAverageHue(Image image) {
		int pixellength = image.getPixelLength();
		byte[] pixels = image.getPixels();

		int r = 0;
		int g = 0;
		int b = 0;

		for (int i = 0; i < pixels.length; i += 3) {
			if (pixellength == 4) {
				i++;
			}

			r += pixels[i] & 0xFF;
			g += pixels[i + 1] & 0xFF;
			b += pixels[i + 2] & 0xFF;
		}

		return new Color(r / pixels.length * pixellength, g / pixels.length
				* pixellength, b / pixels.length * pixellength);
	}

	public static Histogram getRedHistogram(Image image) {
		Histogram ret = new Histogram();
		int pixellength = image.getPixelLength();
		byte[] pixels = image.getPixels();

		for (int i = 0; i < pixels.length; i += 3) {
			if (pixellength == 4) {
				i++;
			}

			ret.incrementValue(pixels[i] & 0xFF);
		}
		return ret;
	}

	public static Histogram getGreenHistogram(Image image) {
		Histogram ret = new Histogram();
		int pixellength = image.getPixelLength();
		byte[] pixels = image.getPixels();

		for (int i = 0; i < pixels.length; i += 3) {
			if (pixellength == 4) {
				i++;
			}

			ret.incrementValue(pixels[i + 1] & 0xFF);
		}
		return ret;
	}

	public static Histogram getBlueHistogram(Image image) {
		Histogram ret = new Histogram();
		int pixellength = image.getPixelLength();
		byte[] pixels = image.getPixels();

		for (int i = 0; i < pixels.length; i += 3) {
			if (pixellength == 4) {
				i++;
			}

			ret.incrementValue(pixels[i + 2] & 0xFF);
		}
		return ret;
	}

	private Hue() {
	}
}
