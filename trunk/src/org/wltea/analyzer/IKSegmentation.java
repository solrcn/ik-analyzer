/**
 * 
 */
package org.wltea.analyzer;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.TreeSet;

import org.wltea.analyzer.cfg.Configuration;
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
	//词元容器池
	private LexemePool lexemePool;
	//分词器上下文
	private Context context;
	//分词处理器列表
	private List<ISegmenter> segmenters;

    
    
	public IKSegmentation(Reader input){
		this.input = input ;
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
			//可处理的字串长度
			int available = context.fillBuffer(input);			
            if(available <= 0){
                return null;
            }else{
            	//记录从Reader中读入的字符长度
            	context.setLastReadIn(available);
            	//分词处理
            	int analyzed = analyze(available);
            	//记录已分析的字符长度，同时累计已分析的字符长度
            	context.setLastAnalyzed(analyzed);
            	//读取词元池中的词元
            	return lexemePool.pull();
            }
		}else{
			//读取词元池中的已有词元
			return lexemePool.pull();
		}	
	}
	
	/**
	 * 分析缓冲区字串，并返回第一个词元
	 * @param available 缓冲区内有效的字符长度（待分析的字串长度）
	 * @return 本次处理的字串长度
	 */
	private int analyze(int available){
		Lexeme lexeme = null;
		for(int buffIndex = 0 ; buffIndex < available ;  buffIndex++){
			//TODO 
			context.setBuffIndex(buffIndex);
			for(ISegmenter segmenter : segmenters){
				lexeme = segmenter.nextLexeme(context);
				if(lexeme != null){
					lexemePool.push(lexeme, context.getSegmentBuff());
				}
			}
			//TODO 判断何时终止当前buffer的处理
			
		}
		return 0;
	}
	
	/**
	 * 词元容器
	 * @author 林良益
	 *
	 */
	private class LexemePool{
		
	    //词元组,存贮切分完成的词元代理
	    private TreeSet<Lexeme> lexemeSet;
	    
	    private LexemePool(){
	    	lexemeSet = new TreeSet<Lexeme>();
	    }
		/**
		 * 向容器压入切分出的词元对象代理
		 * @param lexeme
		 */
	    private void push(Lexeme lexeme , char[] segmentBuff){
			//生成lexeme的词元文本
			String lexemeText = new String(segmentBuff , lexeme.getBegin() , lexeme.getLexemeLength());
			lexeme.setLexemeText(lexemeText);
			lexemeSet.add(lexeme);
		}		
		/**
		 * 从容器中按顺序，逐个取出词元对象
		 * @return
		 */
		private Lexeme pull(){
			if(!isEmpty()){
				return lexemeSet.pollFirst();
			}else{
				return null;
			}
		}		
		/**
		 * 
		 * @return
		 */
		private boolean isEmpty(){
			return lexemeSet.isEmpty();
		}
	}

}
