package rk476.timelapseweather.classifier.machinelearning;

import java.io.IOException;

import rk476.timelapseweather.dataharvester.data.CsvManipulator;
import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.trees.RandomForest;
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

    public DataClassifier(String trainingfileName, int classIndex) throws Exception {
	DataSource source = new DataSource(trainingfileName);
	_trainingData = source.getDataSet();

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

	_classifier = new RandomForest();
	((RandomForest) _classifier).setOptions(Utils.splitOptions("-I 10"));

	Remove remove = new Remove();
	String splitOptions = "-R 1-" + classIndex + "," + (classIndex + 2) + "-17";
	remove.setOptions(Utils.splitOptions(splitOptions)); // Name, date,
							     // time,
							     // actuals,
							     // predicted
							     // stuff
	remove.setInputFormat(_trainingData);
	_trainingData = Filter.useFilter(_trainingData, remove);

	_classifier.buildClassifier(_trainingData);
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
	//	System.out.println("error " + i);
	    }

	    // System.out.println(s);

	}

    }

    public static void main(String[] args) throws IOException {
	String prefix = "/home/romek/projects/timelapseweather/data/";
	// "C:\\Users\\Romek\\Dropbox\\Part II\\data\\";
	// "data/";
	String data = prefix + "data.csv";

	
	
	new CsvManipulator(data).splitCsvFile(5, prefix + "split/");

	System.out.println("split");

	for (int j = 0; j < 5; j++) {
	    try {
		for (int i = 3; i < 10; i++) {
		    DataClassifier classifier = new DataClassifier(prefix + "split/" + "train" + j + ".csv", i);
		    classifier.fillFile(prefix + "split/" + "test" + j + ".csv");
		}

	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	System.out.println("Classified");
	
	

	// Merge again
	String[] files = new String[5];
	for (int i = 0; i < 5; i++) {
	    files[i] = prefix + "split/" + "test" + i + ".csv";
	}

	CsvManipulator.mergeCsvFiles(files, prefix + "datamerged.csv");

	System.out.println("Merged");

	System.out.println("Done");
    }
}
