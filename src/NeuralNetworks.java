import java.util.List;

import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;

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
		NeuralNetworkThread[] threads = new NeuralNetworkThread[NUMBER_NETWORKS];
		
		for(int i = 0; i < NUMBER_NETWORKS; i++){
			threads[i] = new NeuralNetworkThread(i, images, pca);
			threads[i].start();
		}
		for(int i = 0; i < NUMBER_NETWORKS; i++){
			try {
				threads[i].join();
				networks[i] = threads[i].network;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return new NeuralNetworks(networks, pca);
	}
}
