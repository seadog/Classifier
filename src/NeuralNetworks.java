import java.util.List;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.back.Backpropagation;

public class NeuralNetworks {
	private BasicNetwork[] networks;
	private PCA pca;
	
	private static final int NUMBER_NETWORKS = 10;
	
	private NeuralNetworks(BasicNetwork[] networks, PCA pca){
		this.networks = networks;
		this.pca = pca;
	}
	
	public double[] apply(TaggedImage image){
		double[] input = pca.apply(image);
		double[] retval = new double[NUMBER_NETWORKS];
		
		for(int i = 0; i < NUMBER_NETWORKS; i++){
			MLData ml_input = new BasicMLData(input);
			MLData ml_output = this.networks[i].compute(ml_input);
			retval[i] = ml_output.getData(0);
		}
		
		return retval;
	}
	
	public static NeuralNetworks train(List<TaggedImage> images, PCA pca){
		BasicNetwork[] networks = new BasicNetwork[NUMBER_NETWORKS];
		
		for(int i = 0; i < NUMBER_NETWORKS; i++){
			networks[i] = new BasicNetwork();
			networks[i].addLayer(new BasicLayer(new ActivationSigmoid(), true, 20));
			networks[i].addLayer(new BasicLayer(new ActivationSigmoid(), true, 100));
			networks[i].addLayer(new BasicLayer(new ActivationSigmoid(), true, 1));
			networks[i].getStructure().finalizeStructure();
			networks[i].reset();
			
			Tag ideal_tag = TagMap.charToTag(Character.forDigit(i, 10));
			double[][] input = new double[images.size()][pca.getNumComponents()];
			double[][] ideals = new double[images.size()][1];
			
			for(int j = 0; j < images.size(); j++){
				TaggedImage image = images.get(j);
				if(image.getTag() == ideal_tag){
					ideals[j][0] = 1.0;
				} else {
					ideals[j][0] = 0.0;
				}
				
				input[j] = pca.apply(image);
			}
			
			
			NeuralDataSet training_set = new BasicNeuralDataSet(input, ideals);
			Train training = new Backpropagation(networks[i], training_set);
			
			int z = 0;
			while(z++ < 600){
				training.iteration();
			}
			
		}
		
		return new NeuralNetworks(networks, pca);
	}
}
