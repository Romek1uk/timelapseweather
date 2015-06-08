public class DataClassifier {

    private Classifier _classifier;
    private int _classIndex;
    Instances _trainingData;

    public DataClassifier(String trainingfileName, int classIndex, Classifier classifier) throws Exception {
		DataSource source = new DataSource(trainingfileName);

		try {
			_trainingData = source.getDataSet();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(trainingfileName);
			return;
		}

		// String attribute change in weka, fix.
		StringToNominal stn = new StringToNominal();
		stn.setInputFormat(_trainingData);
		String[] options = new String[2];
		options[0] = "-R";
		options[1] = "";

		if (_trainingData.checkForStringAttributes()) {
			for (int i = 0; i < _trainingData.numAttributes(); i++) {
				if (_trainingData.attribute(i).type() == Attribute.STRING) {
					options[1] += (i + 1) + ","; // i + 1 because columns are 1-indexed only when being passed as arguments to filters
				}
			}

			// remove last comma
			options[1] = options[1].substring(0, options[1].length() - 1);
		}

		
		stn.setOptions(options);
		_trainingData = Filter.useFilter(_trainingData, stn);

		_trainingData.setClassIndex(classIndex);

		_classifier = classifier;

		// Remove name/data/time from training features
		Remove remove = new Remove();
		String splitOptions = "-R 1-" + classIndex + "," + (classIndex + 2) + "-17";
		remove.setOptions(Utils.splitOptions(splitOptions)); // Name, date, time, actuals, predicted, features
		remove.setInputFormat(_trainingData);
		_trainingData = Filter.useFilter(_trainingData, remove);

		try {
			_classifier.buildClassifier(_trainingData);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(trainingfileName);
			return;
		}

		_classIndex = classIndex;
	}

	public void fillFile(String fileName) throws Exception {
		DataSource source = new DataSource(fileName);
		Instances data = source.getDataSet();

		data.setClassIndex(_classIndex);

		_trainingData.delete();

		for (int i = 0; i < data.numInstances(); i++) {
			_trainingData.add(data.instance(i));
		}

		CsvManipulator manipulator = new CsvManipulator(fileName);

		Instances labelled = new Instances(data);

		// Keep labels, remove training data.
		for (int i = 0; i < _trainingData.numInstances(); i++) {
			try {
				double label = _classifier.classifyInstance(_trainingData.instance(i));

				labelled.instance(i).setClassValue(label);

				String s = _trainingData.classAttribute().value((int) label);
				if (s.isEmpty())
					s = String.valueOf(_classifier.distributionForInstance(data.instance(i))[0]);

				manipulator.replaceEntryOnLine(i, _classIndex + 7, s);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void finalize() {
		_trainingData.clear();
		_trainingData.delete();
	}

	private static final String _prefix = "/home/romek/projects/timelapseweather/data/";
	private static final String _sep = "/";

	private static void doClassify(Classifier classifier, String name) throws IOException, InterruptedException {
		FileUtils.copyDirectory(new File(_prefix + "split" + _sep), new File(_prefix + name + _sep + "split" + _sep)); // Creates a local copy of the data

		for (int j = 0; j < 5; j++) { // Files
			for (int i = 5; i <= 9; i++) { // Columns
				try {
					// Train classifier
					DataClassifier dataclassifier = new DataClassifier(_prefix + name + _sep + "split" + _sep + "train" + j + ".csv", i, classifier);
				
					// Use classifier
					dataclassifier.fillFile(_prefix + name + _sep + "split" + _sep + "test" + j + ".csv");
					dataclassifier.finalize();
				} catch (Exception e) {
					System.out.println("Error on file " + j + " and column " + i);
					e.printStackTrace();
				}
			}
		}

		// Merge separate files
		String[] files = new String[5];
		for (int i = 0; i < 5; i++) {
			files[i] = _prefix + name + _sep + "split" + _sep + "test" + i + ".csv";
		}

		CsvManipulator.mergeCsvFiles(files, _prefix + name + _sep + "datamerged.csv");
	}

	public static void main(String[] args) throws IOException {
		// Split the csv file into partitions to be used for cross validation. Does so randomly.
		new CsvManipulator(_prefix + "data.csv").splitCsvFile(5, _prefix + "split\\");

		MultilayerPerceptron mlp = new MultilayerPerceptron();
		mlp.setHiddenLayers("3, 5, 3");
		try {
			doClassify(mlp, "mlp");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}

