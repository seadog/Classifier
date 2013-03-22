import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TaggedImage {
	private Tag image_class = null;
	private File file = null;
	private double[] vector = null;
	private int width = -1;
	private int height = -1;
	
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
	
	public int getWidth(){
		if(this.width == -1){
			getVector();
		}
		return this.width;
	}
	
	public int getHeight(){
		if(this.height == -1){
			getVector();
		}
		return this.height;
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
			
			this.width = image.getWidth();
			this.height = image.getHeight();
			this.vector = new double[image.getWidth() * image.getHeight()];
			
			byte[] data = ((DataBufferByte)image.getData().getDataBuffer()).getData();
			for(int i = 0; i < data.length; i++){
				this.vector[i] = (double)(data[i] & 0xFF);
			}
		}
		
		return this.vector;
	}
}
