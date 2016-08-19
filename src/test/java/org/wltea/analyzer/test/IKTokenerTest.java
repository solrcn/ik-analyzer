/**
 * 
 */
package org.wltea.analyzer.test;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKTokenizer;

import junit.framework.TestCase;

/**
 * @author 林良益
 *
 */
public class IKTokenerTest extends TestCase {
	
	public void testLucene3Tokenizer(){
		String t = "IK分词器Lucene Analyzer接口实现类 民生银行";
		IKTokenizer tokenizer = new IKTokenizer(new StringReader(t) , false);
		try {
			while(tokenizer.incrementToken()){
				CharTermAttribute termAtt = tokenizer.getAttribute(CharTermAttribute.class);
				System.out.println(termAtt);				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	

}
