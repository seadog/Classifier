import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Dataset {
	List<TaggedSet> imagesets;
	
	public Dataset(File base){
		this.imagesets = new ArrayList<>();
		
		for(File dir : base.listFiles()){
			String dir_name = dir.getAbsolutePath();
			Tag dir_tag = TagMap.charToTag(dir_name.charAt(dir_name.length()-1));

			imagesets.add(new TaggedSet(dir_tag));
			
			File[] images = dir.listFiles();
			for(File image : images){
				TaggedImage x = new TaggedImage(dir_tag, image);
				for(TaggedSet y : imagesets){
					if(y.getTag() == dir_tag){
						y.addImage(x);
					}
				}
			}
		}
	}
	
	public List<TaggedImage> getTrainingSet(int samples_per_class){
		List<TaggedImage> retval = new ArrayList<>();
		
		for(TaggedSet set : this.imagesets){
			List<TaggedImage> set_images = set.getAllImages();
			Collections.shuffle(set_images);
			for(int i = 0; i < samples_per_class; i++){
				retval.add(set_images.remove(0));
			}
		}
		
		return retval;
	}
	
	public List<TaggedImage> getAllImages(){
		List<TaggedImage> retval = new ArrayList<>();
		
		for(TaggedSet set : this.imagesets){
			retval.addAll(set.getAllImages());
		}
		
		return retval;
	}
}
