/**
 * 
 */
package org.wltea.analyzer;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.help.CharacterHelper;
import org.wltea.analyzer.seg.ISegmenter;

/**
 * IK Analyzer v3.0
 * IK主分词器
 * 注：IKSegmentation是一个lucene无关的通用分词器
 * @author 林良益
 *
 */
public final class IKSegmentation{
	
	private Reader input;
	
	//默认缓冲区大小
	private static final int BUFF_SIZE = 1024;
	//缓冲区耗尽的临界值
	private static final int BUFF_EXHAUST_CRITICAL = 64;	
    //字符窜读取缓冲
    private char[] segmentBuff;
    
	//词元容器池
	private LexemePool lexemePool;
	//分词器上下文
	private Context context;
	//分词处理器列表
	private List<ISegmenter> segmenters;
    
    
	public IKSegmentation(Reader input){
		this.input = input ;
		segmentBuff = new char[BUFF_SIZE];
		context = new Context();
		lexemePool = new LexemePool();
		segmenters = Configuration.loadSegmenter();
	}
	
	/**
	 * 获取下一个语义单元
	 * @return 没有更多的词元，则返回null
	 * @throws IOException
	 */
	public Lexeme next() throws IOException {
		if(lexemePool.isEmpty()){
			/*
			 * 从reader中读取数据，填充buffer
			 * 如果reader是分次读入buffer的，那么buffer要进行移位处理
			 * 移位处理上次读入的但未处理的数据
			 */
			int available = fillBuffer(input);
			
            if(available <= 0){
                return null;
            }else{
            	
            	//分词处理
        		Set<Lexeme> lexemeSet = null;
        		int buffIndex = 0;
        		for( ; buffIndex < available ;  buffIndex++){
        			//标识最大分析位置
        			if(context.getBuffOffset() + buffIndex > context.getMaxAnalyzedIndex()){
        				context.setMaxAnalyzedIndex(context.getBuffOffset() + buffIndex );
        			}
        			//移动缓冲区指针
        			context.setCursor(buffIndex);
        			//进行全角转半角处理
        			segmentBuff[buffIndex] = CharacterHelper.SBC2DBC(segmentBuff[buffIndex]);
        			//遍历子分词器
        			for(ISegmenter segmenter : segmenters){
        				lexemeSet = segmenter.nextLexeme(segmentBuff , context);
        				if(lexemeSet != null && lexemeSet.size() > 0){
        					lexemePool.push(lexemeSet, segmentBuff);
        				}
        			}
        			/*
        			 * 满足一下条件时，
        			 * 1.available == BUFF_SIZE 表示buffer满载
        			 * 2.available - buffIndex > 1 && available - buffIndex < BUFF_EXHAUST_CRITICAL 表示当前指针处于临界区内
        			 * 3.!context.isBufferLocked()表示没有segmenter在占用buffer
        			 * 要中断当前循环（buffer要进行移位，并再读取数据的操作）
        			 */        			
        			if(available == BUFF_SIZE
        					&& available - buffIndex > 1
        					&& available - buffIndex < BUFF_EXHAUST_CRITICAL
        					&& !context.isBufferLocked()){
        				break;
        			}
        		}
        		//System.out.println(available + " : " +  buffIndex);
            	//记录最近一次分析的字符长度
        		context.setLastAnalyzed(buffIndex);
            	//同时累计已分析的字符长度
        		context.setBuffOffset(context.getBuffOffset() + buffIndex);
            	//读取词元池中的词元
            	return lexemePool.pull();
            }
		}else{
			//读取词元池中的已有词元
			return lexemePool.pull();
		}	
	}
	
    /**
     * 根据context的上下文情况，填充segmentBuff 
     * @param reader
     * @return 返回待分析的（有效的）字串长度
     * @throws IOException 
     */
    private int fillBuffer(Reader reader) throws IOException{
    	int readCount = 0;
    	if(context.getBuffOffset() == 0){
    		//首次读取reader
    		readCount = reader.read(segmentBuff);
    	}else{
    		int offset = context.getAvailable() - context.getLastAnalyzed();
    		if(offset > 0){
    			//最近一次读取的>最近一次处理的，将未处理的字串拷贝到segmentBuff头部
    			System.arraycopy(segmentBuff , context.getLastAnalyzed() , this.segmentBuff , 0 , offset);
    			readCount = offset;
    		}
    		//继续读取reader ，以onceReadIn - onceAnalyzed为起始位置，继续填充segmentBuff剩余的部分
    		readCount += reader.read(segmentBuff , offset , BUFF_SIZE - offset);
    	}            	
    	//记录最后一次从Reader中读入的可用字符长度
    	context.setAvailable(readCount);
    	return readCount;
    }	
	
	/**
	 * 词元容器
	 * @author 林良益
	 *
	 */
	private class LexemePool{
		
	    //词元组,存贮切分完成的词元代理
	    private TreeSet<Lexeme> lexemeTreeSet;
	    
	    private LexemePool(){
	    	lexemeTreeSet = new TreeSet<Lexeme>();
	    }
		/**
		 * 向容器压入切分出的词元对象代理
		 * @param lexeme
		 */
	    private void push(Set<Lexeme> lexemeSet , char[] segmentBuff){
	    	for(Lexeme lexeme : lexemeSet){
				//生成lexeme的词元文本
				String lexemeText = new String(segmentBuff , lexeme.getBegin() , lexeme.getLength());
				lexeme.setLexemeText(lexemeText);
				lexemeTreeSet.add(lexeme);
	    	}
		}		
		/**
		 * 从容器中按顺序，逐个取出词元对象
		 * @return
		 */
		private Lexeme pull(){
			if(!isEmpty()){
				return lexemeTreeSet.pollFirst();
			}else{
				return null;
			}
		}		
		/**
		 * 
		 * @return
		 */
		private boolean isEmpty(){
			return lexemeTreeSet.isEmpty();
		}
	}

}
