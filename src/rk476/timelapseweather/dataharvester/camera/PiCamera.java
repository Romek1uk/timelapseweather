package rk476.timelapseweather.dataharvester.camera;

public class PiCamera {
	private static final String DIRECTORY = "";

	public void capture(String fileName) {
		String command = "raspistill --quality 100 --output " + DIRECTORY
				+ fileName;

		try {
			Process process = Runtime.getRuntime().exec(command);
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
}
