
public enum Tag {
	T_0('0'),
	T_1('1'),
	T_2('2'),
	T_3('3'),
	T_4('4'),
	T_5('5'),
	T_6('6'),
	T_7('7'),
	T_8('8'),
	T_9('9');
	
	private final char character;
	
	private Tag(char character){
		this.character = character;
	}
	
	public char getCharacter(){
		return this.character;
	}
}
