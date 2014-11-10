package intro.images;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageMetricGetter
{
	public static double averageBrightness(BufferedImage image)
	{
		int pixellength = image.getAlphaRaster() == null ? 3 : 4;
		
		byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		
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
	}
}
