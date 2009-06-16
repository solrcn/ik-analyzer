package org.wltea.analyzer;

/**
 * 分词器上下文状态
 * @author 林良益
 *
 */
public class Context{

    //记录Reader内已分析的字串总长度
    //在分多段分析词元时，该变量累计当前的segmentBuff相对于reader的位移
    private int analyzedLength;	    
    //最近一次读入的字串长度
    private int lastReadIn;
    //最近一次分析的字串长度
    private int lastAnalyzed;	
    //当前缓冲区位置指针
    private int cursor;
    
    
    Context(){
    	analyzedLength = 0;
    	lastReadIn = 0;
    	lastAnalyzed = 0;
		cursor = 0;
	}


	public int getAnalyzedLength() {
		return analyzedLength;
	}


	public void setAnalyzedLength(int analyzedLength) {
		this.analyzedLength = analyzedLength;
	}


	public int getLastReadIn() {
		return lastReadIn;
	}


	public void setLastReadIn(int lastReadIn) {
		this.lastReadIn = lastReadIn;
	}


	public int getLastAnalyzed() {
		return lastAnalyzed;
	}


	public void setLastAnalyzed(int lastAnalyzed) {
		this.lastAnalyzed = lastAnalyzed;
	}


	public int getCursor() {
		return cursor;
	}


	public void setCursor(int cursor) {
		this.cursor = cursor;
	}
}
