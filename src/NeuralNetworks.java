import java.util.List;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

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
	
	public static NeuralNetworks train(List<TaggedImage> images, PCA pca) {
		BasicNetwork[] networks = new BasicNetwork[NUMBER_NETWORKS];
		
		for(int i = 0; i < NUMBER_NETWORKS; i++){
			BasicNetwork network = new BasicNetwork();
			Tag tag = TagMap.charToTag(Character.forDigit(i, 10));
			
			network.addLayer(new BasicLayer(new ActivationSigmoid(), true, pca.getNumComponents()));
			network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 200));
			network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 1));
			network.getStructure().finalizeStructure();
			network.reset();
			
			double[][] input = new double[images.size()][];
			double[][] ideals = new double[images.size()][1];
			
			for(int j = 0; j < images.size(); j++){
				TaggedImage image = images.get(j);
				if(image.getTag() == tag){
					ideals[j][0] = 1.0;
				} else {
					ideals[j][0] = 0.0;
				}
				
				input[j] = pca.apply(image);
			}
			
			NeuralDataSet training_set = new BasicNeuralDataSet(input, ideals);
			Train training = new ResilientPropagation(network, training_set);
			
			int z = 0;
			while(z++ < 400){
				training.iteration();
			}
			
			networks[i] = network;
		}
		
		return new NeuralNetworks(networks, pca);
	}
}
