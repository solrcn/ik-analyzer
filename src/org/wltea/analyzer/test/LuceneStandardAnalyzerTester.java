/**
 * 
 */
package org.wltea.analyzer.test;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import junit.framework.TestCase;

/**
 * @author Administrator
 *
 */
public class LuceneStandardAnalyzerTester extends TestCase{
	
	public void testIPAdd(){
		String t = "1.12.34.33 -1-2003%123*111-11+12 2009A17B10 10:10:23wo!r+d.1{}0.16-8AAAA_B$BB@0.1.12.34.33.10.18ok?hello";
		Token ruToken = new Token();
		StandardTokenizer st = new StandardTokenizer(new StringReader(t));
		try {
			while((ruToken = st.next(ruToken)) != null){
				System.out.println(ruToken);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
