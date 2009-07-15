/**
 * 
 */
package org.wltea.analyzer.seg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.wltea.analyzer.Context;
import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.help.CharacterHelper;

/**
 * 中文词元处理子分词器，涵盖一下范围
 * 1.中文词语
 * 2.姓名
 * 3.地名
 * 4.未知词
 * @author 林良益
 *
 */
public class ChineseSegmenter implements ISegmenter {
	/*
	 * 已完成处理的位置
	 */
	private int goneIndex;
	/*
	 * 词段处理队列
	 */
	List<CSeg> _CSegList;
	/*
	 * 词元集合
	 */
	private Set<Lexeme> lexemeSet;
	
	public ChineseSegmenter(){
		goneIndex = -1;
		_CSegList = new ArrayList<CSeg>();
		lexemeSet = new HashSet<Lexeme>();
	}
	
	/* (non-Javadoc)
	 * @see org.wltea.analyzer.seg.ISegmenter#nextLexeme(org.wltea.analyzer.Context)
	 */
	public Set<Lexeme> nextLexeme(char[] segmentBuff , Context context) {
		//清空结果集
		lexemeSet.clear();
		//读取当前位置的char	
		char input = segmentBuff[context.getCursor()];
		
		if(CharacterHelper.isCJKCharacter(input)){//是中文字符，则进行处理
			// TODO Auto-generated method stub
			
		}
		return lexemeSet;
	}

	/**
	 * 词段（内部对象）
	 * @author linly
	 *
	 */
	private class CSeg{
		/*
		 * 词段分类常量
		 */
		private static final String TYPE_MATCH = "MATCH";//匹配
		private static final String TYPE_PRE = "PRE"; //前缀
		private static final String TYPE_UNKNOWN = "UNKNOWN"; //未知的
		
		/*
		 * 词段开始位置
		 */
		private int start;
		/*
		 * 词段的结束位置
		 */
		private int end;
		/*
		 * 词段分类
		 * 有：匹配，前缀，未知
		 */
		private String type;
	}
}
