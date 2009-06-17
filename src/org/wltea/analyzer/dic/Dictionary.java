/**
 * 
 */
package org.wltea.analyzer.dic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.wltea.analyzer.cfg.Configuration;

/**
 * IK Analyzer v3.0
 * 词典管理类,单子模式
 * @author 林良益
 *
 */
public class Dictionary {
	/*
	 * 分词器默认字典路径 
	 */
	public static final String PATH_DIC_MAIN = "/org/wltea/analyzer/dic/main.dic";
	public static final String PATH_DIC_SURNAME = "/org/wltea/analyzer/dic/surname.dic";
	public static final String PATH_DIC_QUANTIFIER = "/org/wltea/analyzer/dic/quantifier.dic";
	
	
	/*
	 * 词典单子实例
	 */
	private static final Dictionary singleton;
	
	/*
	 * 词典初始化
	 */
	static{
		singleton = new Dictionary();
	}
	
	/*
	 * 主词典对象
	 */
	private DictSegment _MainDict;
	/*
	 * 姓氏词典
	 */
	private DictSegment _SurnameDict;
	/*
	 * 量词词典
	 */
	private DictSegment _QuantifierDict;
	
	private Dictionary(){
		//初始化系统词典
		loadMainDict();
		loadSurnameDict();
		loadQuantifierDict();
		//TODO 名词后缀、停止词
	}

	/**
	 * 加载主词典及扩展词典
	 */
	private void loadMainDict(){
		//建立一个主词典实例
		_MainDict = new DictSegment((char)0);
		//读取主词典文件
        InputStream is = Dictionary.class.getResourceAsStream(Dictionary.PATH_DIC_MAIN);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			String theWord = null;
			do {
				theWord = br.readLine();
				if (theWord != null) {
					_MainDict.fillSegment(theWord.trim().toCharArray());
				}
			} while (theWord != null);
			
		} catch (IOException ioe) {
			System.err.println("Main Dictionary loading exception.");
			ioe.printStackTrace();
			
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//加载扩展词典配置
		List<String> extDictFiles  = Configuration.getExtDictionarys();
		if(extDictFiles != null){
			for(String extDictName : extDictFiles){
				//读取扩展词典文件
				is = Dictionary.class.getResourceAsStream(extDictName);
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
					String theWord = null;
					do {
						theWord = br.readLine();
						if (theWord != null) {
							//加载扩展词典数据到主内存词典中
							_MainDict.fillSegment(theWord.trim().toCharArray());
						}
					} while (theWord != null);
					
				} catch (IOException ioe) {
					System.err.println("Extension Dictionary loading exception.");
					ioe.printStackTrace();
					
				}finally{
					try {
						if(is != null){
		                    is.close();
		                    is = null;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}	
	
	/**
	 * 加载姓氏词典
	 */
	private void loadSurnameDict(){
		//建立一个主词典实例
		_SurnameDict = new DictSegment((char)0);
		//读取姓氏词典文件
        InputStream is = Dictionary.class.getResourceAsStream(Dictionary.PATH_DIC_SURNAME);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			String theWord = null;
			do {
				theWord = br.readLine();
				if (theWord != null) {
					_SurnameDict.fillSegment(theWord.trim().toCharArray());
				}
			} while (theWord != null);
			
		} catch (IOException ioe) {
			System.err.println("Surname Dictionary loading exception.");
			ioe.printStackTrace();
			
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 加载量词词典
	 */
	private void loadQuantifierDict(){
		//建立一个主词典实例
		_QuantifierDict = new DictSegment((char)0);
		//读取量词词典文件
        InputStream is = Dictionary.class.getResourceAsStream(Dictionary.PATH_DIC_QUANTIFIER);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is , "UTF-8"), 512);
			String theWord = null;
			do {
				theWord = br.readLine();
				if (theWord != null) {
					_QuantifierDict.fillSegment(theWord.trim().toCharArray());
				}
			} while (theWord != null);
			
		} catch (IOException ioe) {
			System.err.println("Quantifier Dictionary loading exception.");
			ioe.printStackTrace();
			
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}		

	/**
	 * 词典初始化
	 * 由于IK Analyzer的词典采用Dictionary类的静态方法进行词典初始化
	 * 只有当Dictionary类被实际调用时，才会开始载入词典，
	 * 这将延长首次分词操作的时间
	 * 该方法提供了一个在应用加载阶段就初始化字典的手段
	 * 用来缩短首次分词时的时延
	 * @return
	 */
	public static Dictionary getInsance(){
		return Dictionary.singleton;
	}
	
	/**
	 * 加载扩展的词条
	 * @param words
	 */
	public static void loadExtendWords(List<String> extWords){
		if(extWords != null){
			for(String extWord : extWords){
				if (extWord != null) {
					//加载扩展词条到主内存词典中
					singleton._MainDict.fillSegment(extWord.trim().toCharArray());
				}
			}
		}
	}
	
	/**
	 * 在主词典中匹配char数组
	 * @param charArray
	 * @return
	 */
	public static Hit matchInMainDict(char[] charArray){
		return singleton._MainDict.match(charArray);
	}
	
	/**
	 * 在主词典中匹配指定位置的char数组
	 * @param charArray
	 * @param begin
	 * @param end
	 * @return
	 */
	public static Hit matchInMainDict(char[] charArray , int begin, int length){
		return singleton._MainDict.match(charArray, begin, length);
	}	
	
	/**
	 * 在姓氏词典中匹配指定位置的char数组
	 * @param charArray
	 * @param begin
	 * @param end
	 * @return
	 */
	public static Hit matchInSurnameDict(char[] charArray , int begin, int length){
		return singleton._SurnameDict.match(charArray, begin, length);
	}
	
	/**
	 * 在量词词典中匹配指定位置的char数组
	 * @param charArray
	 * @param begin
	 * @param end
	 * @return
	 */
	public static Hit matchInQuantifierDict(char[] charArray , int begin, int length){
		return singleton._QuantifierDict.match(charArray, begin, length);
	}
	
	
}
