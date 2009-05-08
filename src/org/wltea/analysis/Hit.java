/**
 * 
 */
package org.wltea.analysis;

/**
 * 表示词典检索的命中结果
 * @author 林良益
 *
 */
public class Hit {
	//Hit不匹配
	private static final int UNMATCH = 0x00000000;
	//Hit完全匹配
	private static final int MATCH = 0x00000001;
	//Hit前缀匹配
	private static final int PREFIX = 0x00000010;
	
	
	//该HIT当前状态，默认未匹配
	private int hitState = UNMATCH;
	
	
	/**
	 * 判断是否完全匹配
	 */
	public boolean isMatch() {
		return (this.hitState & MATCH) > 0;
	}
	/**
	 * 
	 */
	public void setMatch() {
		this.hitState = this.hitState | MATCH;
	}
	/**
	 * 判断是否完全匹配，且是另一个词的前缀
	 */
	public boolean isMatchAndPrefix() {
		return ((this.hitState & MATCH) > 0) &&  ((this.hitState & PREFIX) > 0);
	}
	/**
	 * 判断是否是词的前缀
	 */
	public boolean isPrefix() {
		return (this.hitState & PREFIX) > 0;
	}
	/**
	 * 
	 */
	public void setPrefix() {
		this.hitState = this.hitState | PREFIX;
	}
	/**
	 * 判断是否是不匹配
	 */
	public boolean isUnmatch() {
		return this.hitState == UNMATCH ;
	}
	
	/**
	 * @param unmatch The unmatch to set.
	 */
	public void setUnmatch() {
		this.hitState = UNMATCH;
	}	
	
}
