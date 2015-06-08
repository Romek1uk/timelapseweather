package rk476.timelapseweather.classifier.machinelearning;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import rk476.timelapseweather.dataharvester.data.CsvManipulator;
import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.Utils;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToNominal;

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
					options[1] += (i + 1) + ","; // i + 1 because columns are
					// 1-indexed only when being
					// passed as arguments to
					// filters
				}
			}

			// remove last comma
			options[1] = options[1].substring(0, options[1].length() - 1);
		}

		stn.setOptions(options);
		_trainingData = Filter.useFilter(_trainingData, stn);

		_trainingData.setClassIndex(classIndex);

		_classifier = classifier;
		// ((RandomForest) _classifier).setOptions(Utils.splitOptions("-I 50"));

		// _classifier = new MultilayerPerceptron();
		// ((MultilayerPerceptron) _classifier).setHiddenLayers("3, 3, 3"); // 3
		// layer, 3
		// ((MultilayerPerceptron) _classifier).setTrainingTime(20); // 20
		// epochs

		// _classifier = new LinearRegression();
		// ((J48)_classifier).setUnpruned(true);

		Remove remove = new Remove();
		String splitOptions = "-R 1-" + classIndex + "," + (classIndex + 2) + "-17";
		remove.setOptions(Utils.splitOptions(splitOptions)); // Name, date,
		// time,
		// actuals,
		// predicted
		// stuff
		remove.setInputFormat(_trainingData);
		_trainingData = Filter.useFilter(_trainingData, remove);

		// NumericToNominal numericToNominal = new NumericToNominal();
		// numericToNominal.setInputFormat(_trainingData);
		// _trainingData = Filter.useFilter(_trainingData, numericToNominal);

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

		for (int i = 0; i < _trainingData.numInstances(); i++) {
			try {
				double label = _classifier.classifyInstance(_trainingData.instance(i));

				labelled.instance(i).setClassValue(label);

				String s = _trainingData.classAttribute().value((int) label);
				if (s.isEmpty())
					s = String.valueOf(_classifier.distributionForInstance(data.instance(i))[0]);

				manipulator.replaceEntryOnLine(i, _classIndex + 7, s);

			} catch (Exception e) {
				// System.out.println("error " + i);
			}

			// System.out.println(s);

		}
	}

	public void finalize() {
		_trainingData.clear();
		_trainingData.delete();
	}

	private static final String _prefix = "/home/romek/projects/timelapseweather/data/"; // data/;
	// //
	// "D:\\Dropbox\\Dropbox\\Part II\\project\\data\\";
	private static final String _sep = "/";

	private static void doClassify(Classifier classifier, String name) throws IOException, InterruptedException {
		System.out.println(name);

		FileUtils.copyDirectory(new File(_prefix + "split" + _sep), new File(_prefix + name + _sep + "split" + _sep));

		System.out.println("copied " + name);

		int completed = 0;

		for (int j = 0; j < 5; j++) {
			for (int i = 5; i <= 9; i++) {
				try {
					System.out.println("file: " + j);

					DataClassifier dataclassifier = new DataClassifier(_prefix + name + _sep + "split" + _sep + "train" + j + ".csv", i, classifier);

					dataclassifier.fillFile(_prefix + name + _sep + "split" + _sep + "test" + j + ".csv");
					dataclassifier.finalize();

					System.out.println("Completed: " + ++completed + "/" + "35");
				} catch (Exception e) {
					System.out.println("Error on file " + j + " and column " + i);
					e.printStackTrace();
				}
			}
		}

		System.out.println("Classified " + name);

		String[] files = new String[5];
		for (int i = 0; i < 5; i++) {
			files[i] = _prefix + name + _sep + "split" + _sep + "test" + i + ".csv";
		}

		CsvManipulator.mergeCsvFiles(files, _prefix + name + _sep + "datamerged.csv");

		System.out.println("Merged");

		System.out.println("Done");
	}

	public static void main(String[] args) throws IOException {

		// new CsvManipulator(_prefix + "data.csv").splitCsvFile(5, _prefix +
		// "split\\");

		// for (int k = 100; k <= 200; k+=100) {
		// new Thread(){
		// private int k;
		// public Thread init(int k) {
		// this.k = k;
		// return this;
		// }
		// public void run() {
		// RandomForest rf = new RandomForest();
		// rf.setMaxDepth(0);
		// rf.setNumTrees(k);
		// try {
		// doClassify(rf, "randomforests" + k);
		// } catch (IOException | InterruptedException e) {
		// e.printStackTrace();
		// }
		// }
		// }.init(k).start();
		//
		// }

		long start = System.nanoTime();

		// new CsvManipulator(_prefix + "smalldata.csv").splitCsvFile(5, _prefix
		// + "smallsplit/");

		MultilayerPerceptron rf = new MultilayerPerceptron();
		rf.setHiddenLayers("3, 5, 3");
		try {
			doClassify(rf, "asdasf");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println(start - System.nanoTime());

	}
}
