package org.wltea.analyzer;

import java.util.HashSet;
import java.util.Set;

import org.wltea.analyzer.seg.ISegmenter;

/**
 * 分词器上下文状态
 * @author 林良益
 *
 */
public class Context{
	
    //记录Reader内已分析的字串总长度
    //在分多段分析词元时，该变量累计当前的segmentBuff相对于reader的位移
	private int buffOffset;	
	//记录已读取分析的最大位置（字符在文章中的绝对位置）
	private int maxAnalyzedIndex;	
	//最近一次读入的,可处理的字串长度
	private int available;
    //最近一次分析的字串长度
    private int lastAnalyzed;	
    //当前缓冲区位置指针
    private int cursor;
    /*
     * 记录正在使用buffer的分词器对象
     * 如果set中存在有分词器对象，则buffer不能进行位移操作（处于locked状态）
     */
    private Set<ISegmenter> buffLocker;
    
    Context(){
    	buffLocker = new HashSet<ISegmenter>(4);
	}
    
    public void reset(){
    	buffOffset = 0;
    	maxAnalyzedIndex = 0;
//    	lastReadIn = 0;
    	lastAnalyzed = 0;
		cursor = 0;
		buffLocker.clear();
	}

	public int getBuffOffset() {
		return buffOffset;
	}


	public void setBuffOffset(int buffOffset) {
		this.buffOffset = buffOffset;
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


	public int getMaxAnalyzedIndex() {
		return maxAnalyzedIndex;
	}


	public void setMaxAnalyzedIndex(int maxAnalyzedIndex) {
		this.maxAnalyzedIndex = maxAnalyzedIndex;
	}
	
	public void lockBuffer(ISegmenter segmenter){
		this.buffLocker.add(segmenter);
	}
	
	public void unlockBuffer(ISegmenter segmenter){
		this.buffLocker.remove(segmenter);
	}
	
	/**
	 * 只要buffLocker中存在ISegmenter对象
	 * 则buffer被锁定
	 * @return
	 */
	public boolean isBufferLocked(){
		return this.buffLocker.size() > 0;
	}

	public int getAvailable() {
		return available;
	}

	public void setAvailable(int available) {
		this.available = available;
	}

}
