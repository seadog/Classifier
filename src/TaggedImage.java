import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TaggedImage {
	private Tag image_class = null;
	private File file = null;
	private double[] vector = null;
	
	public TaggedImage(Tag tag, File file){
		this.image_class = tag;
		this.file = file;
	}
	
	public File getFile(){
		return this.file;
	}
	
	public Tag getTag(){
		return this.image_class;
	}
	
	public double[] getVector(){
		if(this.vector == null){
			BufferedImage image = null;
			try {
				image = ImageIO.read(this.file);
			} catch(IOException e) {
				System.out.println("File not found");
				e.printStackTrace();
				return null;
			}
			
			this.vector = new double[image.getWidth() * image.getHeight()];
			
			for(int i = 0; i < image.getWidth(); i++){
				for(int j = 0; j < image.getHeight(); j++){
					int rgb = image.getRGB(i, j);
					vector[i*image.getHeight() + j] = (double)(rgb & 0xFF);
				}
			}
		}
		
		return this.vector;
	}
}
