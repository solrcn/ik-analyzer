/**
 * 
 */
package org.wltea.analysis;

import java.io.IOException;
import java.io.Reader;
import java.util.TreeSet;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;

/**
 * IK Analyzer v3.0
 * 词元分解器
 * 单个词元分解器负责一个Reader内字串的分析
 * @author 林良益
 *
 */
final class IKTokenizer extends Tokenizer {
	
	//词元容器池
	private TokenPool tokenPool;
	//分析器上下文
	private Context context;

    
    
	IKTokenizer(Reader input){
		super(input);
		context = new Context();
		tokenPool = new TokenPool();
	}
	
    /* (non-Javadoc)
     * @see org.apache.lucene.analysis.TokenStream#next()
     */
	public final Token next() throws IOException {
		if(tokenPool.isEmpty()){
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
            	return tokenPool.pull();
            }
		}else{
			//读取词元池中的已有词元
			return tokenPool.pull();
		}	
	}
	
	/**
	 * 分析缓冲区字串，并返回第一个词元
	 * @param available 缓冲区内有效的字符长度（待分析的字串长度）
	 * @return 本次处理的字串长度
	 */
	private int analyze(int available){
		return 0;
	}
	
	
	/**
	 * 分词器上下文状态
	 * @author 林良益
	 *
	 */
	class Context{
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

	    private Context(){
	    	analyzedLength = 0;
	    	lastReadIn = 0;
	    	lastAnalyzed = 0;
			segmentBuff = new char[BUFF_SIZE];
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
	}	
	
	/**
	 * 词元容器
	 * @author 林良益
	 *
	 */
	class TokenPool{
		
	    //词元组,存贮切分完成的词元代理
	    private TreeSet<TokenDelegate> tokenDelegateSet;
	    
	    private TokenPool(){
	    	tokenDelegateSet = new TreeSet<TokenDelegate>();
	    }
		/**
		 * 向容器压入切分出的词元对象代理
		 * @param tokenDelegate
		 */
		void push(TokenDelegate tokenDelegate){
			//TODO 这里要实现某种排歧义的算法
		}
		
		/**
		 * 从容器中按顺序，逐个取出词元对象
		 * @return
		 */
		Token pull(){
			if(!isEmpty()){
				TokenDelegate tokenDelegate = tokenDelegateSet.pollFirst();
				return tokenDelegate.toToken();
			}else{
				return null;
			}
		}
		
		/**
		 * 
		 * @return
		 */
		int size(){
			return tokenDelegateSet.size();
		}
		
		/**
		 * 
		 * @return
		 */
		boolean isEmpty(){
			return tokenDelegateSet.isEmpty();
		}
	}
	
	/**
	 * 词元代理
	 * 对Token的封装
	 * @author 林良益
	 *
	 */
	class TokenDelegate implements Comparable<TokenDelegate>{
		
		private TokenDelegate(){
			
		}
		
		Token toToken() {
			return null;
		}

		public int compareTo(TokenDelegate other) {
			// TODO Auto-generated method stub
			return 0;
		}
		
	}
}
