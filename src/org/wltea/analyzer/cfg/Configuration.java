/**
 * 
 */
package org.wltea.analyzer.cfg;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

/**
 * IK Analyzer v3.0
 * 简单的配置管理类,单子模式
 * @author 林良益
 *
 */
public class Configuration {
	/*
	 * 分词器配置文件路径
	 */	
	private static final String FILE_NAME = "/IKAnalyzer.cfg.xml";
	//配置属性——扩展字典
	private static final String EXT_DICT = "ext_dict";
	
	private static final Configuration CFG = new Configuration();
	
	private Properties props;
	
	/*
	 * 初始化配置文件
	 */
	private Configuration(){
		
		props = new Properties();
		
		InputStream input = Configuration.class.getResourceAsStream(FILE_NAME);
		
		try {
			props.loadFromXML(input);
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取扩展字典配置路径
	 * @return
	 */
	public static List<String> getExtDictionarys(){
		List<String> extDictFiles = new ArrayList<String>(2);
		String extDictCfg = CFG.props.getProperty(EXT_DICT);
		if(extDictCfg != null){
			//使用;分割多个扩展字典配置
			String[] filePaths = extDictCfg.split(";");
			if(filePaths != null){
				for(String filePath : filePaths){
					if(filePath != null && !"".equals(filePath.trim())){
						extDictFiles.add(filePath.trim());
						//System.out.println(filePath.trim());
					}
				}
			}
		}		
		return extDictFiles;		
	}
}
