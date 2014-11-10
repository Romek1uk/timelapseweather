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
	
	public static Color averageColour(BufferedImage image)
	{
		int pixellength = image.getAlphaRaster() == null ? 3 : 4;
		
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
		int pixellength = image.getAlphaRaster() == null ? 3 : 4;
		
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
	}
}
