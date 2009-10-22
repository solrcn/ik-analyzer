/**
 * 
 */
package org.wltea.analyzer.lucene;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;


/**
 * IK Analyzer v3.0
 * Lucene Tokenizer适配器类
 * 它封装了IKSegmentation实现
 * 
 * @author 林良益
 *
 */
public final class IKTokenizer extends Tokenizer {
	
	//IK分词器实现
	private IKSegmentation _IKImplement;
	
	/**
	 * Lucene Tokenizer适配器类构造函数
	 * @param in
	 * @param isMaxWordLength 当为true时，分词器进行最大词长切分；当为false是，采用最细粒度切分
	 */
	public IKTokenizer(Reader in , boolean isMaxWordLength) {
		 super(in);
		_IKImplement = new IKSegmentation(in , isMaxWordLength);
	}	
	
    /* (non-Javadoc)
     * @see org.apache.lucene.analysis.TokenStream#next()
     */
	public Token next() throws IOException {
		return toToken(_IKImplement.next());
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.lucene.analysis.Tokenizer#reset(java.io.Reader)
	 */
	public void reset(Reader input) throws IOException {
		super.reset(input);
		_IKImplement.setInput(input);
	}	
	/**
	 * 转化Lexeme语义单元为lucene的Token对象
	 * @param lexeme
	 * @return
	 */
	private Token toToken(Lexeme lexeme){
		if(lexeme == null){
			return null;
		}
		Token token = new Token();
		token.setStartOffset(lexeme.getBeginPosition());		
		token.setEndOffset(lexeme.getEndPosition());
		token.setTermBuffer(lexeme.getLexemeText());
		return token;
	}
	
}
