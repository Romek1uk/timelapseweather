package rk476.timelapseweather.dataharvester.imagemetrics;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Encompasses an image file to be used with the imagemetrics package classes
 */
public class Image {

	private BufferedImage _image;

	private String _filepath;

	/**
	 * Gets the file path of the image
	 * 
	 * @return The file path of the image
	 */
	public String getFilePath() {
		return _filepath;
	}

	/**
	 * Gets the length of the pixels in bytes
	 * 
	 * @return The length of the pixels in bytes
	 */
	public int getPixelLength() {
		return _image.getAlphaRaster() == null ? 3 : 4;
	}

	/**
	 * Gets the pixels from the image, returned as an array of bytes. Each pixel
	 * will take up 3-4 indices, depending on the presence of an alpha channel.
	 * 
	 * @return The pixels from the image, returned as an array of bytes.
	 */
	public byte[] getPixels() {
		return ((DataBufferByte) _image.getRaster().getDataBuffer()).getData();
	}

	/**
	 * Creates a new Image class to be used with the imagemetrics package
	 * classes
	 * 
	 * @param filepath
	 *            The file path of the image
	 * @throws IOException
	 *             Thrown if path does not exist
	 */
	public Image(String filepath) throws IOException {
		_filepath = filepath;
		_image = ImageIO.read(new File(filepath)); // Throws IOException if
													// invalid file
	}

}
