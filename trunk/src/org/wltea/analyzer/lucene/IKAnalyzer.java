/**
 * 
 */
package org.wltea.analyzer.lucene;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

/**
 * 实现Lucene Analyzer
 * 基于IKTokenizer的中文分词器
 * 
 * @author 林良益
 *
 */
public final class IKAnalyzer extends Analyzer {
	
	private boolean isMaxWordLength = false;
	
	public IKAnalyzer(){
		this(false);
	}
	
	public IKAnalyzer(boolean isMaxWordLength){
		super();
		this.isMaxWordLength = isMaxWordLength;
	}

	/* (non-Javadoc)
	 * @see org.apache.lucene.analysis.Analyzer#tokenStream(java.lang.String, java.io.Reader)
	 */
	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		return new IKTokenizer(reader , isMaxWordLength);
	}

}
