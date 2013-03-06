import java.util.ArrayList;
import java.util.List;


public class TaggedSet {
	private List<TaggedImage> images;
	private Tag set_class;
	
	public TaggedSet(Tag tag){
		this.images = new ArrayList<>();
		this.set_class = tag;
	}
	
	public Tag getTag(){
		return this.set_class;
	}
	
	public List<TaggedImage> getAllImages(){
		return this.images;
	}
	
	public boolean addImage(TaggedImage image){
		if(image.getTag() != this.set_class){
			return false;
		} else {
			images.add(image);
			return true;
		}
	}
}
