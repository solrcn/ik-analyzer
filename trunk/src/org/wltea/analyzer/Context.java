package org.wltea.analyzer;

import java.io.IOException;
import java.io.Reader;

/**
 * 分词器上下文状态
 * @author 林良益
 *
 */
public class Context{
	//默认缓冲区大小
	private static final int BUFF_SIZE = 1024;
	
	
    //字符窜读取缓冲
    private char[] segmentBuff;
    //记录Reader内已分析的字串总长度
    //在分多段分析词元时，该变量累计当前的segmentBuff相对于reader的位移
    private int analyzedLength;	    
    //最近一次读入的字串长度
    private int lastReadIn;
    //最近一次分析的字串长度
    private int lastAnalyzed;	
    //当前缓冲区位置指针
    private int buffIndex;
    
    Context(){
    	analyzedLength = 0;
    	lastReadIn = 0;
    	lastAnalyzed = 0;
		segmentBuff = new char[BUFF_SIZE];
		buffIndex = 0;
	}
    
    /**
     * 根据segmentBuff的上下文情况，填充segmentBuff 
     * @param reader
     * @return 返回待分析的（有效的）字串长度
     * @throws IOException 
     */
    int fillBuffer(Reader reader) throws IOException{
    	int readCount = 0;
    	if(analyzedLength == 0){
    		//首次读取reader
    		readCount = reader.read(segmentBuff);
    	}else{
    		if(lastReadIn > lastAnalyzed){
    			//上次读取的>上次处理的，将未处理的字串拷贝到segmentBuff头部
    			System.arraycopy(segmentBuff , lastAnalyzed + 1 , this.segmentBuff , 0 , lastReadIn - lastAnalyzed);
    			readCount = lastReadIn - lastAnalyzed;
    		}
    		//继续读取reader ，以onceReadIn - onceAnalyzed为起始位置，继续填充segmentBuff剩余的部分
    		readCount += reader.read(segmentBuff , lastReadIn - lastAnalyzed , BUFF_SIZE - (lastReadIn - lastAnalyzed));
    	}
    	return readCount;
    }

	public char[] getSegmentBuff() {
		return segmentBuff;
	}

	public int getAnalyzedLength() {
		return analyzedLength;
	}

	public int getLastAnalyzed() {
		return lastAnalyzed;
	}		

	void setLastReadIn(int lastReadIn) {
		this.lastReadIn = lastReadIn;
	}

	void setLastAnalyzed(int lastAnalyzed) {
		this.lastAnalyzed = lastAnalyzed;
		this.analyzedLength += lastAnalyzed;
	}

	public int getBuffIndex() {
		return buffIndex;
	}

	public void setBuffIndex(int buffIndex) {
		this.buffIndex = buffIndex;
	}
}
