/**
 * 
 */
package org.wltea.analyzer;

/**
 * IK Analyzer v3.0
 * 语义单元（词元） * 
 * @author 林良益
 *
 */
public final class Lexeme implements Comparable<Lexeme>{
	
	//词元的起始位移
	private int offset;
    //词元的相对起始位置
    private int begin;
    //词元的长度
    private int length;
    //词元文本
    private String lexemeText;
    
	Lexeme(){
		
	}
	
	Lexeme(int offset , int begin , int length){
		this.offset = offset;
		this.begin = begin;
		if(length < 0){
			throw new IllegalArgumentException("length < 0");
		}
		this.length = length;
	}
	
    /*
     * 判断词元相等算法
     * 起始位置偏移、起始位置、终止位置相同
     * @see java.lang.Object#equals(Object o)
     */
	public boolean equals(Object o){
		if(o == null){
			return false;
		}
		
		if(this == o){
			return true;
		}
		
		if(o instanceof Lexeme){
			Lexeme other = (Lexeme)o;
			if(this.offset == other.getOffset()
					&& this.begin == other.getBegin()
					&& this.length == other.getLength()){
				return true;			
			}else{
				return false;
			}
		}else{		
			return false;
		}
	}
	
    /*
     * 词元哈希编码算法
     * @see java.lang.Object#hashCode()
     */
    public int hashCode(){
    	int absBegin = getBeginPosition();
    	int absEnd = getEndPosition();
    	return  (absBegin * 37) + (absEnd * 31) + ((absBegin * absEnd) % getLength()) * 11;
    }
    
    /*
     * 词元在排序集合中的比较算法
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
	public int compareTo(Lexeme other) {
		//起始位置优先
        if(this.begin < other.getBegin()){
            return -1;
        }else if(this.begin == other.getBegin()){
        	//词元长度优先
        	if(this.length > other.getLength()){
        		return -1;
        	}else if(this.length == other.getLength()){
        		return 0;
        	}else {//this.length < other.getLength()
        		return 1;
        	}
        	
        }else{//this.begin > other.getBegin()
        	return 1;
        }
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getBegin() {
		return begin;
	}
	/**
	 * 获取词元的绝对起始位置
	 * @return
	 */
	public int getBeginPosition(){
		return offset + begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	/**
	 * 获取词元的绝对终止位置
	 * @return
	 */
	public int getEndPosition(){
		return offset + begin + length;
	}
	
	public int getLength(){
		return this.length;
	}	
	
	public void setLength(int length) {
		if(this.length < 0){
			throw new IllegalArgumentException("length < 0");
		}
		this.length = length;
	}
	
	public String getLexemeText() {
		if(lexemeText == null){
			return "";
		}
		return lexemeText;
	}

	public void setLexemeText(String lexemeText) {
		if(lexemeText == null){
			this.lexemeText = "";
			this.length = 0;
		}else{
			this.lexemeText = lexemeText;
			this.length = lexemeText.length();
		}
	}
	
}