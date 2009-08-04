/**
 * 
 */
package org.wltea.analyzer.test;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;

import junit.framework.TestCase;

/**
 * @author linly
 * 
 */
public class CustomerTest extends TestCase {

	public static void main(String[] args) {
		String s = "";
//		IKAnalyzer analyzer = new IKAnalyzer();
//		IndexWriter writer = null;
//		Directory directory = new RAMDirectory();
		try {
//			writer = new IndexWriter(directory, analyzer, true,
//					MaxFieldLength.UNLIMITED);
			SAXReader reader = new SAXReader();
			reader.setEncoding("utf-8");
			org.dom4j.Document document = reader.read("D:/Users/linly/Downloads/xml/text.xml");
			Element e = (Element) document
					.selectObject("/SEGMENT/DOCUMENT/CONTENT");
			s = e.getText();
			
			IKSegmentation ikSeg = new IKSegmentation(new StringReader(s) ,true);
			try {
				Lexeme l = null;
				while( (l = ikSeg.next()) != null){
					//System.out.println(l);
				}
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}			
//			System.out.println("=====" + s);
//			System.out.println("=====" + s.length());
//			Document doc = new Document();
//			Field field = new Field("context", s, Field.Store.NO,
//					Field.Index.ANALYZED);
//			doc.add(field);
//			writer.addDocument(doc, analyzer);
//			writer.close();
//		} catch (CorruptIndexException e1) {
//			e1.printStackTrace();
//		} catch (LockObtainFailedException e1) {
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			e1.printStackTrace();
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
