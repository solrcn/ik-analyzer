/**
 * 
 */
package org.wltea.analyzer.seg;

import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.Context;
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.help.CharacterHelper;

/**
 * 负责处理字母的子分词器，涵盖一下范围
 * 1.英文单词、英文加阿拉伯数字、专有名词（公司名）
 * 2.IP地址、Email、URL
 * 
 * @author 林良益
 *
 */
public class LetterSegmenter implements ISegmenter {
	
	//链接符号
	public static final char[] Sign_Connector = new char[]{'-','_','.','@','&'};
	/*
	 * 词元的开始位置，
	 * 同时作为子分词器状态标识
	 * 当start > -1 时，标识当前的分词器正在处理字符
	 */
	private int start;
	/*
	 * 记录词元结束位置
	 * end记录的是在词元中最后一个出现的Letter但非Sign_Connector的字符的位置
	 */
	private int end;
	
	public LetterSegmenter(){
		start = -1;
		end = -1;
	}
	
	/* (non-Javadoc)
	 * @see org.wltea.analyzer.ISegmenter#nextLexeme(org.wltea.analyzer.IKSegmentation.Context)
	 */
	public void nextLexeme(char[] segmentBuff , Context context) {

		//读取当前位置的char	
		char input = segmentBuff[context.getCursor()];
		
		if(start == -1){//当前的分词器尚未开始处理字符			
			if(isAcceptedCharStart(input)){
				//记录起始指针的位置,标明分词器进入处理状态
				start = context.getCursor();
				end = start;
			}
			
		}else{//当前的分词器正在处理字符			
			if(isAcceptedChar(input)){
				//记录下可能的结束位置，如果是连接符结尾，则忽略
				if(!isLetterConnector(input)){
					end = context.getCursor();
				}
			}else{
				//生成已切分的词元
				Lexeme newLexeme = new Lexeme(context.getBuffOffset() , start , end - start + 1 , Lexeme.TYPE_LETTER);
				if(!Dictionary.isStopWord(segmentBuff , newLexeme.getBegin() , newLexeme.getLength())){
					context.addLexeme(newLexeme);
				}
				//设置当前分词器状态为“待处理”
				start = -1;
				end = -1;

			}			
		}
		
		//context.getCursor() == context.getAvailable() - 1读取缓冲区最后一个字符，直接输出
		if(context.getCursor() == context.getAvailable() - 1
				&& start != -1 && end != -1){
			//生成已切分的词元
			Lexeme newLexeme = new Lexeme(context.getBuffOffset() , start , end - start + 1 , Lexeme.TYPE_LETTER);
			if(!Dictionary.isStopWord(segmentBuff , newLexeme.getBegin() , newLexeme.getLength())){
				context.addLexeme(newLexeme);
			}
			//设置当前分词器状态为“待处理”
			start = -1;
			end = -1;
		}
		
		//判断是否锁定缓冲区
		if(start == -1 && end == -1){
			//对缓冲区解锁
			context.unlockBuffer(this);
		}else{
			context.lockBuffer(this);
		}
	}
	
	/**
	 * 
	 * @param input
	 * @return
	 */
	private boolean isLetterConnector(char input){
		for(char c : Sign_Connector){
			if(c == input){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断char是否是可接受的起始子符
	 * @return
	 */
	private boolean isAcceptedCharStart(char input){
		return CharacterHelper.isEnglishLetter(input) 
				|| CharacterHelper.isArabicNumber(input);
	}
	
	/**
	 * 判断char是否是可接受的字符
	 * @return
	 */
	private boolean isAcceptedChar(char input){
		return isLetterConnector(input) 
				|| CharacterHelper.isEnglishLetter(input) 
				|| CharacterHelper.isArabicNumber(input);
	}

	@Override
	public void reset() {
		//do nothing
		
	}
	

}
