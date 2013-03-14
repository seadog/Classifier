import java.util.List;

import org.encog.engine.network.activation.ActivationElliott;
import org.encog.mathutil.randomize.FanInRandomizer;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;


public class NeuralNetworkThread extends Thread {
	private Tag tag;
	private PCA pca;
	private List<TaggedImage> images;
	
	public BasicNetwork network = null;
	
	public void run(){
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(null, true, 19));
		network.addLayer(new BasicLayer(new ActivationElliott(), true, 100));
		network.addLayer(new BasicLayer(new ActivationElliott(), false, 1));
		network.getStructure().finalizeStructure();
		network.reset();
		
		double[][] input = new double[images.size()][pca.getNumComponents()];
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
		
		new FanInRandomizer().randomize(network);
		
		NeuralDataSet training_set = new BasicNeuralDataSet(input, ideals);
		Train training = new ResilientPropagation(network, training_set);
		
		int z = 0;
		while(z++ < 600){
			training.iteration();
		}

		
		this.network = network;
	}
	
	public NeuralNetworkThread(int i, List<TaggedImage> images, PCA pca){
		this.tag = TagMap.charToTag(Character.forDigit(i, 10));
		this.pca = pca;
		this.images = images;
	}
}
