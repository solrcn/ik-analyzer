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
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.dic.Hit;
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
	private int doneIndex;
	/*
	 * 词段处理队列
	 */
	List<CSeg> _CSegList;
	/*
	 * 词元集合
	 */
	private Set<Lexeme> lexemeSet;
	
	public ChineseSegmenter(){
		doneIndex = -1;
		_CSegList = new ArrayList<CSeg>();
		lexemeSet = new HashSet<Lexeme>();
		Dictionary.getInstance();
	}
	
	/* (non-Javadoc)
	 * @see org.wltea.analyzer.seg.ISegmenter#nextLexeme(org.wltea.analyzer.Context)
	 */
	public Set<Lexeme> nextLexeme(char[] segmentBuff , Context context) {
		//清空结果集
		lexemeSet.clear();
		//读取当前位置的char	
		char input = segmentBuff[context.getCursor()];
		Hit hit = null;
		if(CharacterHelper.isCJKCharacter(input)){//是中文（CJK）字符，则进行处理
			if(_CSegList.size() > 0){
				//处理词段队列
				List<CSeg> tmpList = new ArrayList<CSeg>(_CSegList);
				for(CSeg seg : tmpList){
					hit = Dictionary.matchInMainDict(segmentBuff, seg.start, context.getCursor() - seg.start + 1);
					if(hit.isMatch()){//匹配成词
						//判断是否有不可识别的词段
						if(seg.start > doneIndex + 1){
							//输出并处理从doneIndex+1 到 seg.start - 1之间的未知词段
							processUnknown(segmentBuff , doneIndex + 1 , seg.start - 1);
						}
						//输出当前的词
						Lexeme newLexeme = new Lexeme(context.getBuffOffset() , seg.start , context.getCursor() - seg.start + 1);
						lexemeSet.add(newLexeme);
						//更新goneIndex，标识已处理
						if(doneIndex < context.getCursor()){
							doneIndex = context.getCursor();
						}
						
						if(hit.isPrefix()){//同时也是前缀
							//更新seg.end、seg.type，
							seg.end = context.getCursor();
							//seg.type = CSeg.TYPE_MATCH;
							
						}else{ //后面不再可能有匹配了
							//移出当前的CSeg
							_CSegList.remove(seg);
						}
						
					}else if(hit.isPrefix()){//前缀，未匹配成词
						//更新seg.end、seg.type
						seg.end = context.getCursor();
						//seg.type = CSeg.TYPE_PRE;
						
					}else if(hit.isUnmatch()){//不匹配
						//移出当前的CSeg
						_CSegList.remove(seg);
					}
				}
			}
			
			//处理以input为开始的一个新CSeg
			hit = Dictionary.matchInMainDict(segmentBuff, context.getCursor() , 1);
			if(hit.isMatch()){//匹配成词
				//判断是否有不可识别的词段
				if(context.getCursor() > doneIndex + 1){
					//输出并处理从doneIndex+1 到 context.getCursor()- 1之间的未知
					processUnknown(segmentBuff , doneIndex + 1 , context.getCursor()- 1);
				}
				//输出当前的词
				Lexeme newLexeme = new Lexeme(context.getBuffOffset() , context.getCursor() , 1);
				lexemeSet.add(newLexeme);
				//更新doneIndex，标识已处理
				if(doneIndex < context.getCursor()){
					doneIndex = context.getCursor();
				}

				if(hit.isPrefix()){//同时也是前缀
					//向词段队列增加新的词段
					CSeg newCSeg = new CSeg();
					newCSeg.start = context.getCursor();
					newCSeg.end = newCSeg.start;
					//newCSeg.type = CSeg.TYPE_MATCH;
					_CSegList.add(newCSeg);
				}
				
			}else if(hit.isPrefix()){//前缀，未匹配成词
				//向词段队列增加新的词段
				CSeg newCSeg = new CSeg();
				newCSeg.start = context.getCursor();
				newCSeg.end = newCSeg.start;
				//newCSeg.type = CSeg.TYPE_PRE;
				_CSegList.add(newCSeg);
				
			}		
			
		}else {//输入的不是中文(CJK)字符
			if(_CSegList.size() > 0
					&&  doneIndex < context.getCursor() - 1){
				for(CSeg seg : _CSegList){
					//判断是否有不可识别的词段
					if(seg.end > doneIndex + 1){
						//输出并处理从doneIndex+1 到 seg.end之间的未知词段
						processUnknown(segmentBuff , doneIndex + 1 , seg.end);
					}
				}
			}
			//清空词段队列
			_CSegList.clear();
			//更新doneIndex，标识已处理
			if(doneIndex < context.getCursor()){
				doneIndex = context.getCursor();
			}
		}
		
		//缓冲区结束临界处理
		if(context.getCursor() == context.getAvailable() - 1){ //读取缓冲区结束的最后一个字符			
			if( _CSegList.size() > 0 //队列中还有未处理词段
				&& doneIndex < context.getCursor()){//最后一个字符还未被输出过
				for(CSeg seg : _CSegList){
					//判断是否有不可识别的词段
					if(seg.end > doneIndex + 1){
						//输出并处理从doneIndex+1 到 seg.end之间的未知词段
						processUnknown(segmentBuff , doneIndex + 1 , seg.end);
					}
				}
			}
			//清空词段队列
			_CSegList.clear();
		}
		
		//判断是否锁定缓冲区
		if(_CSegList.size() == 0){
			context.unlockBuffer(this);
			
		}else{
			context.lockBuffer(this);
	
		}
		return lexemeSet;
	}

	/**
	 * 处理未知词段
	 * @param segmentBuff 
	 * @param uStart 起始位置
	 * @param uEnd 终止位置
	 */
	private void processUnknown(char[] segmentBuff , int uStart , int uEnd){
		//TODO
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
		//private static final String TYPE_MATCH = "MATCH";//匹配
		//private static final String TYPE_PRE = "PRE"; //前缀
		//private static final String TYPE_UNKNOWN = "UNKNOWN"; //未知的
		
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
		 * 有：匹配，前缀，(未知)
		 */
		//private String type;
	}

	@Override
	public void reset() {
		//重置已处理标识
		doneIndex = -1;
		
	}
}
