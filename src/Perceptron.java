/**
 * You should implement your Perceptron in this class. 
 * Any methods, variables, or secondary classes could be added, but will 
 * only interact with the methods or variables in this framework.
 * 
 * You must add code for at least the 3 methods specified below. Because we
 * don't provide the weights of the Perceptron, you should create your own 
 * data structure to store the weights.
 * 
 */
public class Perceptron {

	/**
	 * The initial value for ALL weights in the Perceptron.
	 * We fix it to 0, and you CANNOT change it.
	 */
	public final double INIT_WEIGHT = 0.0;

	/**
	 * Learning rate value. You should use it in your implementation.
	 * You can set the value via command line parameter.
	 */
	public final double ALPHA;

	/**
	 * Training iterations. You should use it in your implementation.
	 * You can set the value via command line parameter.
	 */
	public final int EPOCH;

	// TODO: create weights variables, input units, and output units.
	//input units
	double[] inputs;
	//output units
	double[] outputs;
	double[][] weights;
	//weights
	//weight ij denotes from input i to output j
	/**
	 * Constructor. You should initialize the Perceptron weights in this
	 * method. Also, if necessary, you could do some operations on
	 * your own variables or objects.
	 * 
	 * @param alpha
	 * 		The value for initializing learning rate.
	 * 
	 * @param epoch
	 * 		The value for initializing training iterations.
	 * 
	 * @param featureNum
	 * 		This is the length of input feature vector. You might
	 * 		use this value to create the input units.
	 * 
	 * @param labelNum
	 * 		This is the size of label set. You might use this
	 * 		value to create the output units.
	 */
	public Perceptron(double alpha, int epoch, int featureNum, int labelNum) {
		this.ALPHA = alpha;
		this.EPOCH = epoch;
		this.inputs = new double[featureNum + 1];
		inputs[0] = 1;
		this.outputs = new double[labelNum];
		this.weights = new double[featureNum+1][labelNum];
		//initialize weights
		//rows indicate inputs
		//cols indicate outputs
		for(int i = 0; i < featureNum+1; i++){
			for(int j = 0; j < labelNum; j++){
				weights[i][j] = INIT_WEIGHT;
			}
		}
	}

	/**
	 * Train your Perceptron in this method.
	 * 
	 * @param trainingData
	 */
	public void train(Dataset trainingData) {
		//a data instance in training dataset
		Instance ins;
		//traverse every train point
		for(int j = 0; j < EPOCH; j++){
			for(int i = 0; i < trainingData.instanceList.size(); i++){
				//System.out.println(trainingData.instanceList.size());
				ins = trainingData.instanceList.get(i);
				//get new output array for every training instance
				outputs = new double[10];
				updateWeights(ins);
			}
		}
	}

	//update the weights based on current training instance
	private void updateWeights(Instance train){
		double[] desireOut = desireOut(train);
		//for(double d : desireOut)
		//	System.out.print(d + " ");
		//System.out.println();
		//put feature value in an double array with length 15
		for(int i = 1; i < 16; i++){
			inputs[i] = train.getFeatureValue().get(i-1);
		}
		//get the predicted output array for the current instance
		for(int output = 0; output < 10; output++){
			for(int input = 0; input < 16; input++){
				outputs[output] += (weights[input][output]*inputs[input]);
			}
			//sigmoid function
			outputs[output] = 1/(1 + Math.exp(-1*outputs[output]));
		}

		//update
		for(int output = 0; output < 10; output++){
			for(int input = 0; input < 16; input++){
				//update weights for sigmoid function
				weights[input][output] += 
					(ALPHA*inputs[input]*outputs[output]
					                             *(desireOut[output] - outputs[output])
					                             *(1-outputs[output]));
			}
		}
	}

	/**
	 * Test your Perceptron in this method. Refer to the homework documentation
	 * for implementation details and requirement of this method.
	 * 
	 * @param testData
	 */
	public void classify(Dataset testData){
		Instance test;
		double[] testInput = new double[16];
		testInput[0] = 1;
		double[] preOut ;
		double count = 0;
		double percent = 0.0;
		int testNum = testData.instanceList.size();
		for(int i = 0; i < testNum; i++){
			preOut = new double[10];
			test = testData.instanceList.get(i);
			//get the test input, put the feature values into the input array
			for(int j = 1; j < 16; j++){
				testInput[j] = test.getFeatureValue().get(j-1);
			}
			//get prediction output array for each test instance
			for(int output = 0; output < 10; output++){
				for(int input = 0; input < 16; input++){
					preOut[output] += weights[input][output]*testInput[input];
				}
			}
			//increment count of successful recognition if the prediction is right
			if(setLabel(preOut) == Integer.parseInt(test.getLabel())){
				count++;
			}
		}
		percent = count/testNum;
		System.out.printf("%.4f", percent);
	}

	//get the test instance label based on output vector
	private int setLabel(double[] preOut){
		//initial maximum value
		double max = Double.NEGATIVE_INFINITY;
		//initial max index
		int maxIndex = -1;
		for(int i = 0; i < 10; i++){
			if(preOut[i] > max){
				max = preOut[i];
				maxIndex = i;
			}
		}
		//answer for question 1, print out each predication label for test instances
		System.out.println(maxIndex);
		return maxIndex;
	}

	//takes an instance and return the desired output array corresponding to its label
	private double[] desireOut(Instance train){
		int label = Integer.parseInt(train.getLabel());
		double[] output = new double[10];
		output[label] = 1;
		return output;
	}
}