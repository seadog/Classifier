
public class TagMap {
	public static Tag charToTag(char x){
		if(x == '0'){
			return Tag.T_0;
		}
		if(x == '1'){
			return Tag.T_1;
		}
		if(x == '2'){
			return Tag.T_2;
		}
		if(x == '3'){
			return Tag.T_3;
		}
		if(x == '4'){
			return Tag.T_4;
		}
		if(x == '5'){
			return Tag.T_5;
		}
		if(x == '6'){
			return Tag.T_6;
		}
		if(x == '7'){
			return Tag.T_7;
		}
		if(x == '8'){
			return Tag.T_8;
		}
		if(x == '9'){
			return Tag.T_9;
		}
		return null;
	}
}
