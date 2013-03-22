import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedWriter;

import org.encog.Encog;


public class Classifier {
	public static void main(String[] args) throws IOException{
		Dataset dataset = new Dataset(new File("/home/andy/data/mnist/training/"));
		List<TaggedImage> training_set = dataset.getTrainingSet(5000);
		
		PrintWriter out_file = new PrintWriter(new BufferedWriter(new FileWriter("/home/andy/out.txt", true)));
		StringBuilder out_string = new StringBuilder();
		
		PCA pca = PCA.train(training_set, 30);
		System.out.println(pca.getNumComponents());
		NeuralNetworks nn = NeuralNetworks.train(training_set, pca);

		List<TaggedImage> images = dataset.getAllImages();

		for(int i = 0; i < 10; i++){
			int won = 0;
			int total = 0;

			for(TaggedImage x : images){
				if(x.getTag().ordinal() == i){
					double[] out = nn.apply(x);
					double max = 0.0;
					int index = -1;
				
					for(int j = 0; j < 10; j++){
						if(out[j] > max){
							max = out[j];
							index = j;
						}
					}

					if(index == i) won++;
					total++;
				}
			}
			
			double percent = (double)won/total * 100;
				
			out_string.append(String.format("%06.2f", percent));			
			out_string.append(" ");
		}
		
		System.out.println(out_string.toString());
		out_file.println(out_string.toString());
		out_file.close();
			
		Encog.getInstance().shutdown();
	}
}
