import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.google.common.collect.MinMaxPriorityQueue;

public class Classifier {
	public static double d(TaggedImage a, TaggedImage b){
		double[] va = a.getPCAVector();
		double[] vb = b.getPCAVector();
		
		double length = 0.0;
		
		for(int i = 0; i < va.length; i++){
			length += Math.pow(va[i] - vb[i], 2);
		}
		
		return length;
	}
	
	public static void doIt(String loc) throws IOException{
		//PrintWriter out = new PrintWriter(new FileWriter("/home/andy/x/"+loc+"/output"));
		
		for(int i = 0; i < 100; i++){
			Dataset dataset = new Dataset(new File("/home/andy/data/mnist/"+loc));
			List<TaggedImage> training_set = dataset.getTrainingSet(5000);
			List<TaggedImage> testing_set  = dataset.getAllImages();

			int total = 0;
			int correct = 0;
		
			PCA pca = PCA.train(training_set, 40);

			for(TaggedImage image : training_set){
				image.setPCAVector(pca.apply(image));
			}
			for(TaggedImage image : testing_set){
				image.setPCAVector(pca.apply(image));
			}
		
			for(TaggedImage image : testing_set){
				MinMaxPriorityQueue<SimpleEntry<Double, Tag>> queue = MinMaxPriorityQueue.orderedBy(new Comparator<SimpleEntry<Double, Tag>>(){
					public int compare(SimpleEntry<Double, Tag> arg0, SimpleEntry<Double, Tag> arg1) {
						if(arg0.getKey() > arg1.getKey()) return 1;
						if(arg0.getKey() < arg1.getKey()) return -1;
						else return 0;
					}
				}).maximumSize(3).create();

				for(TaggedImage test_image : training_set){
					double distance = d(test_image, image);
					queue.offer(new SimpleEntry<Double, Tag>(distance, test_image.getTag()));
				}
				
				Tag max_tag = null;
			
				SimpleEntry<Double, Tag> t1 = queue.poll();
				SimpleEntry<Double, Tag> t2 = queue.poll();
				SimpleEntry<Double, Tag> t3 = queue.poll();
				
				if(t2.getValue() == t3.getValue())
					max_tag = t2.getValue();
				else
					max_tag = t1.getValue();
				
				System.out.println(image.getTag()+":");
				System.out.println("    "+t1);
				System.out.println("    "+t2);
				System.out.println("    "+t3);
				
				total++;
				
				if(max_tag == image.getTag()){
					correct++;
				}
			}
			
			System.out.println("Done Run "+loc+"#"+i);
			//out.println(correct+"/"+total);
			//out.flush();
		}
		
		//out.close();
	}
	
	public static void main(String[] args) throws IOException{
		doIt("training_0x1");
	}
}
