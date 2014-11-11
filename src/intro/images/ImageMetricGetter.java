package intro.images;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageMetricGetter
{
	
	private static byte[] getPixels(BufferedImage image)
	{
		return ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	}
	
	private static int getPixelLength(BufferedImage image)
	{
		return image.getAlphaRaster() == null ? 3 : 4;
	}
	
	public static int[] brightnessHistogram(BufferedImage image)
	{
		int[] ret = new int[101]; // 0-100%
		int pixellength = getPixelLength(image);
		byte[] pixels = getPixels(image);
		
		for (int i = 0; i < pixels.length; i += 3)
		{
			if (pixellength == 4)
			{
				i++;
			}
			
			int r = pixels[i] & 0xFF;
			int g = pixels[i + 1] & 0xFF;
			int b = pixels[i + 2] & 0xFF;
			
			ret[(int)((0.2126 * r + 0.7152 * g + 0.0722 * b)/2.55)]++;
		}
		
		return ret;
	}
	
	public static Color averageColour(BufferedImage image)
	{
		int pixellength = getPixelLength(image);
		
		byte[] pixels = getPixels(image);
		
		int r = 0;
		int g = 0;
		int b = 0;
		
		for(int i = 0; i < pixels.length; i += 3)
		{
			if (pixellength == 4)
			{
				i++;
			}
			
			r += pixels[i] & 0xFF;
			g += pixels[i + 1] & 0xFF;
			b += pixels[i + 2] & 0xFF;
		}
		
		return new Color(r/pixels.length*pixellength, g/pixels.length*pixellength, b/pixels.length*pixellength);
	}
		
	public static double averageBrightness(BufferedImage image)
	{
		int pixellength = getPixelLength(image);
		
		byte[] pixels = getPixels(image);
		
		double brightness = 0;
		
		for(int i = 0; i < pixels.length; i += 3)
		{
			if (pixellength == 4)
			{
				i++;
			}
			
			int r = pixels[i] & 0xFF;
			int g = pixels[i + 1] & 0xFF;
			int b = pixels[i + 2] & 0xFF;
			
			brightness += 0.2126 * r + 0.7152 * g + 0.0722 * b;
		}
		
		return brightness / pixels.length * pixellength;
	}
	
	public static void main(String args[])
	{	
		String path = "D:\\Images\\white.jpg";
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(path));
		}
		catch (IOException e)
		{
			System.out.println("No such file: " + path);
			return;
		}
		
		System.out.println(image.getAlphaRaster() == null);
		
		double brightness = averageBrightness(image);
		System.out.println(brightness);
		System.out.println(averageColour(image));
		
		System.out.println(brightnessHistogram(image));
	}
}
