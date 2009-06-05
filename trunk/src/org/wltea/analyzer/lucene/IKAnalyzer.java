/**
 * 
 */
package org.wltea.analyzer.lucene;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

/**
 * 实现Lucene Analyzer接口,基于IK分词算法的中文分词器
 * @author 林良益
 *
 */
public class IKAnalyzer extends Analyzer {
	
	public IKAnalyzer(){
		super();
	}

	/* (non-Javadoc)
	 * @see org.apache.lucene.analysis.Analyzer#tokenStream(java.lang.String, java.io.Reader)
	 */
	@Override
	public TokenStream tokenStream(String fieldName, Reader reader) {
		return new IKTokenizer(reader);
	}

}
