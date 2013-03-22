import java.util.List;


public class PCA {
	private PrincipleComponentAnalysis pca;
	private int components;
	
	protected PCA(PrincipleComponentAnalysis pca, int components){
		this.pca = pca;
		this.components = components;
	}
	
	public double[] apply(TaggedImage image){
		return this.pca.sampleToEigenSpace(image.getVector());
	}
	
	public int getNumComponents(){
		return this.components;
	}
	
	public static PCA train(List<TaggedImage> images, int components){
		PrincipleComponentAnalysis pca = new PrincipleComponentAnalysis();
		pca.setup(images.size(), images.get(0).getVector().length);
		
		for(TaggedImage image : images){
			pca.addSample(image.getVector());
		}
		
		pca.computeBasis(components);
		return new PCA(pca, components);
	}
}
