/**
 * 
 */
package org.wltea.analyzer.test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

import junit.framework.TestCase;

/**
 * @author Administrator
 *
 */
public class SegmentorTester extends TestCase{
	
	public void testLetter(){
		String t = "AT&T and I.B.M Corp mail : 1.12.34.33 -1-2003%123*111-11+12 2009A17B10 10:10:23wo!r+d.1{}0.16-8AAAA_B$BB@0.1.12.34.33.10.18ok?hello";
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

		IKSegmentation ikSeg = new IKSegmentation(new StringReader(t));
		try {
			Lexeme l = null;
			while( (l = ikSeg.next()) != null){
				System.out.println(l);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void testNumberCount(){
		List<String> testStr = new ArrayList<String>();
		testStr.add("AT&T and I.B.M Corp mail : 1.12.34.33 -1-2003%123*111-11+12 2009A17B10 10:10:23wo!r+d.1{}0.16-8AAAA_B$BB@0.1.12.34.33.10.18ok?hello");
//		testStr.add("一九九五年12月31日,");
//		testStr.add("1500名常用的数量和人名的匹配 超过22万个");
//		testStr.add("据路透社报道，印度尼西亚社会事务部一官员星期二(29日)表示，" 
//				+ "日惹市附近当地时间27日晨5时53分发生的里氏6.2级地震已经造成至少5427人死亡，" 
//				+ "20000余人受伤，近20万人无家可归。");
//		testStr.add("古田县城关六一四路四百零五号");
//		testStr.add("欢迎使用阿江统计2.01版");
//		testStr.add("51千克五十一千克五万一千克两千克拉");
//		testStr.add("十一点半下班十一点下班");
		testStr.add("福州第一中学福州一中福州第三十六中赐进士及第");
		
		
		for(String t : testStr){
			System.out.println(t);	
			IKSegmentation ikSeg = new IKSegmentation(new StringReader(t));
			try {
				Lexeme l = null;
				while( (l = ikSeg.next()) != null){
					System.out.println(l);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("***************");	
		}
		
	}
	
	public void testChinese(){
		List<String> testStr = new ArrayList<String>();
		testStr.add("中华人民共和国");
		testStr.add("中华人民共和国香港特别行政区");
		testStr.add("据路透社报道，印度尼西亚社会事务部一官员星期二(29日)表示，" 
		+ "日惹市附近当地时间27日晨5时53分发生的里氏6.2级地震已经造成至少5427人死亡，" 
		+ "20000余人受伤，近20万人无家可归。");
		for(String t : testStr){
			System.out.println(t);	
			IKSegmentation ikSeg = new IKSegmentation(new StringReader(t));
			try {
				Lexeme l = null;
				while( (l = ikSeg.next()) != null){
					System.out.println(l);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("***************");	
		}
		
	}	

}
