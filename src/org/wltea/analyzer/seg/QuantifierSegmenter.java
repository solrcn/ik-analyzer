/**
 * 
 */
package org.wltea.analyzer.seg;

import java.util.Set;
import java.util.TreeSet;

import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.Context;
import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.dic.Hit;
import org.wltea.analyzer.help.CharacterHelper;

/**
 * 数量词子分词器，涵盖一下范围
 * 1.阿拉伯数字，阿拉伯数字+中文量词
 * 2.中文数字+中文量词
 * 3.时间,日期
 * 4.罗马数字
 * 5.数学符号 % . / 
 * @author 林良益
 *
 */
public class QuantifierSegmenter implements ISegmenter {

	//阿拉伯数字0-9
	public static final String NC_ARABIC = "Anum";
	//阿拉伯数词前缀（货币符号）
	public static String Arabic_Num_Pre = "-+$￥";//Apre
	public static final String NC_ANP = "Apre";	
	//阿拉伯数词链接符号
	public static String Arabic_Num_Mid = ",./:Ee";//Amid
	public static final String NC_ANM = "Amid";
	//阿拉伯数词后缀
	public static String Arabic_Num_End = "%‰";//Aend
	public static final String NC_ANE = "Aend";
	
	//中文数词
	public static String Chn_Num = "○一二两三四五六七八九十零壹贰叁肆伍陆柒捌玖拾百千万亿拾佰仟萬億兆卅廿";//Cnum
	public static final String NC_CHINESE = "Cnum";
	//中文序数词（数词前缀）
	public static String Chn_Num_Pre = "第";//Cpre
	public static final String NC_CNP = "Cpre";
	//中文数词连接符
	public static String Chn_Num_Mid = "点";//Cmid
	public static final String NC_CNM = "Cmid";
	//中文约数词（数词结尾）
	public static String Chn_Num_End = "几多余半";//Cend
	public static final String NC_CNE = "Cend";
	
	//GB库中的罗马字符(起始、中间、结束)
	public static String Rome_Num = "ⅠⅡⅢⅣⅤⅥⅧⅨⅩⅪ"; //Rnum
	public static final String NC_ROME = "Rnum";
	
	//英文字母中被借用的字符
//	public static String Eng_Num = "IVX"; //Enum
//	public static final String NC_ENGLISH = "Enum";	

	//非数词字符
	public static final String NaN = "NaN";
	
	
	/*
	 * 词元的开始位置，
	 * 同时作为子分词器状态标识
	 * 当start > -1 时，标识当前的分词器正在处理字符
	 */
	private int nStart;
	/*
	 * 记录词元结束位置
	 * end记录的是在词元中最后一个出现的合理的数词结束
	 */
	private int nEnd;
	/*
	 * 当前数词的状态 
	 */
	private String nStatus;	
	
	/*
	 * 量词起始位置
	 */
	private int countStart;
	/*
	 * 量词终止位置
	 */
	private int countEnd;
	/*
	 * 词元集合
	 */
	private TreeSet<Lexeme> lexemeSet;	
	
	public QuantifierSegmenter(){
		nStart = -1;
		nEnd = -1;
		nStatus = NaN;
		
		countStart = -1;
		countEnd = -1;
		lexemeSet = new TreeSet<Lexeme>();
		
	}
	
	/* (non-Javadoc)
	 * @see org.wltea.analyzer.ISegmenter#nextLexeme(org.wltea.analyzer.IKSegmentation.Context)
	 */
	public Set<Lexeme> nextLexeme(char[] segmentBuff , Context context) {
		//清空结果集
		lexemeSet.clear();
		
		//数词处理部分
		processNumber(segmentBuff , context);
		
		//量词处理部分
		
		if(countStart == -1){//未开始处理量词
			//当前游标的位置紧挨着数词
			if((lexemeSet.size() > 0 && nStart == -1)
					|| (nEnd != -1 && nEnd == context.getCursor() - 1)){				
				//量词处理
				processCount(segmentBuff , context);
			
			}
		}else{//已开始处理量词
			//量词处理
			processCount(segmentBuff , context);
		}
		
		//判断是否锁定缓冲区
		if(this.nStart == -1 && this.nEnd == -1 && NaN.equals(this.nStatus)
				&& this.countStart == -1 && this.countEnd == -1){
			//对缓冲区解锁
			context.unlockBuffer(this);
		}else{
			context.lockBuffer(this);
		}
		
		return lexemeSet;
	}

	/**
	 * 数词处理
	 * @param segmentBuff
	 * @param context
	 */
	private void processNumber(char[] segmentBuff , Context context){		
		//数词字符识别
		String inputStatus = nIdentify(segmentBuff , context);
		
		if(nStart == -1 && NaN.equals(nStatus)){
			//当前的分词器尚未开始处理字符
			onNaNStatus(inputStatus , context);
			
		}else if(NC_ANP.equals(nStatus)){ 
			//当前为阿拉伯数字前缀	
			onANPStatus(inputStatus , context);
			
		}else if(NC_ARABIC.equals(nStatus)){
			//当前为阿拉伯数字
			onARABICStatus(inputStatus , context);
			
		}else if(NC_ANM.equals(nStatus)){
			//当前为阿拉伯数字链接符
			onANMStatus(inputStatus , context);
			
		}else if(NC_ANE.equals(nStatus)){
			//当前为阿拉伯数字结束符
			onANEStatus(inputStatus , context);
			
		}else if(NC_CNP.equals(nStatus)){
			//当前为中文数字前缀
			onCNPStatus(inputStatus , context);
			
		}else if(NC_CHINESE.equals(nStatus)){
			//当前为中文数字
			onCHINESEStatus(inputStatus , context);
			
		}else if(NC_CNM.equals(nStatus)){
			//当前为中文数字连接符
			onCNMStatus(inputStatus , context);
			
		}else if(NC_CNE.equals(nStatus)){
			//当前为中文数字结束符
			onCNEStatus(inputStatus , context);
			
		}else if(NC_ROME.equals(nStatus)){
			//当前为罗马数字
			onROMEStatus(inputStatus , context);			
			
		}
//		else if(NC_ENGLISH.equals(nStatus)){
//			//当前为英文借代数字
//			onENGLISHStatus(inputStatus , context);
//			
//		}
		
		//读到缓冲区最后一个字符，还有尚未输出的数词
		if(context.getCursor() == context.getAvailable() - 1
				&& nStart != -1 && nEnd != -1){
			//输出数词
			outputNumLexeme(context);
			//重置数词状态
			nReset();
		}				
	}
	
	/**
	 * 当前为NaN状态时，状态机的处理(状态转换)
	 * @param inputStatus
	 * @param context
	 */
	private void onNaNStatus(String inputStatus ,  Context context){
		if(NC_CNP.equals(inputStatus)){//中文数词前缀
			//记录起始位置
			nStart = context.getCursor();
			//记录当前的字符状态
			nStatus = inputStatus;	
			
		}else if(NC_CHINESE.equals(inputStatus)){//中文数词
			//记录起始位置
			nStart = context.getCursor();
			//记录当前的字符状态
			nStatus = inputStatus;
			//记录可能的结束位置
			nEnd = context.getCursor();
			
		}else if(NC_CNE.equals(inputStatus)){//中文数词后缀
			//记录起始位置
			nStart = context.getCursor();
			//记录当前的字符状态
			nStatus = inputStatus;
			//记录可能的结束位置
			nEnd = context.getCursor();
			
		}else if(NC_ANP.equals(inputStatus)){//阿拉伯数字前缀
			//记录起始位置
			nStart = context.getCursor();
			//记录当前的字符状态
			nStatus = inputStatus;
			
		}else if(NC_ARABIC.equals(inputStatus)){//阿拉伯数字
			//记录起始位置
			nStart = context.getCursor();
			//记录当前的字符状态
			nStatus = inputStatus;
			//记录可能的结束位置
			nEnd = context.getCursor();
			
		}else if(NC_ROME.equals(inputStatus)){//罗马数字
			//记录起始位置
			nStart = context.getCursor();
			//记录当前的字符状态
			nStatus = inputStatus;
			//记录可能的结束位置
			nEnd = context.getCursor();	
			
//		}else if(NC_ENGLISH.equals(inputStatus)){//英文代用数字
//			//记录起始位置
//			nStart = context.getCursor();
//			//记录当前的字符状态
//			nStatus = inputStatus;
//			//记录可能的结束位置
//			nEnd = context.getCursor();	
			
		}else{
			//对NaN , NC_ANM ，NC_ANE和NC_CNM 不做处理
		}
	}
	
	
	/**
	 * 当前为ANP状态时，状态机的处理(状态转换)
	 * @param inputStatus
	 * @param context
	 */
	private void onANPStatus(String inputStatus ,  Context context){
		if(NC_ANP.equals(inputStatus)){//阿拉伯数字前缀
			//忽略先前的字串，重新记录起始位置
			nStart = context.getCursor();
			
		}else if(NC_ARABIC.equals(inputStatus)){//阿拉伯数字
			//记录当前的字符状态
			nStatus = inputStatus;
			//记录可能的结束位置
			nEnd = context.getCursor();
			
		}else{
			//重置数词状态
			nReset();
			//进入初始态进行处理
			onNaNStatus(inputStatus , context);
			
		}
	}
	
	
	/**
	 * 当前为ARABIC状态时，状态机的处理(状态转换)
	 * @param inputStatus
	 * @param context
	 */
	private void onARABICStatus(String inputStatus ,  Context context){
		if(NC_ARABIC.equals(inputStatus)){//阿拉伯数字
			//保持状态不变
			//记录可能的结束位置
			nEnd = context.getCursor();
			
		}else if(NC_ANM.equals(inputStatus)){//阿拉伯数字连接符
			//记录当前的字符状态
			nStatus = inputStatus;
			
		}else if(NC_ANE.equals(inputStatus)){//阿拉伯数字后缀
			//记录当前的字符状态
			nStatus = inputStatus;
			//记录可能的结束位置
			nEnd = context.getCursor();
			//输出数词
			outputNumLexeme(context);
			//重置数词状态
			nReset();
			
		}else{
			//输出数词
			outputNumLexeme(context);
			//重置数词状态
			nReset();
			//进入初始态进行处理
			onNaNStatus(inputStatus , context);	
			
		}
		
	}
	
	/**
	 * 当前为ANM状态时，状态机的处理(状态转换)
	 * @param inputStatus
	 * @param context
	 */
	private void onANMStatus(String inputStatus ,  Context context){
		if (NC_ARABIC.equals(inputStatus)){//阿拉伯数字
			//记录当前的字符状态
			nStatus = inputStatus;
			//记录可能的结束位置
			nEnd = context.getCursor();
			
		}else if (NC_ANP.equals(inputStatus)){//阿拉伯数字前缀
			//记录当前的字符状态
			nStatus = inputStatus;
			
		}else{
			//输出可能存在的数词
			outputNumLexeme(context);
			//重置数词状态
			nReset();
			//进入初始态进行处理
			onNaNStatus(inputStatus , context);
			
		}		
	}

	
	/**
	 * 当前为ANE状态时，状态机的处理(状态转换)
	 * @param inputStatus
	 * @param context
	 */
	private void onANEStatus(String inputStatus ,  Context context){
		//输出可能存在的数词
		outputNumLexeme(context);
		//重置数词状态
		nReset();
		//进入初始态进行处理
		onNaNStatus(inputStatus , context);
				
	}	
	
	
	/**
	 *  当前为CNP状态时，状态机的处理(状态转换)
	 * @param inputStatus
	 * @param context
	 */
	private void onCNPStatus(String inputStatus ,  Context context){
		if(NC_CHINESE.equals(inputStatus)){//中文数字
			//记录可能的结束位置
			nEnd = context.getCursor() - 1;
			//输出可能存在的数词
			outputNumLexeme(context);
			//重置数词状态
			nReset();
			//进入初始态进行处理
			onNaNStatus(inputStatus , context);			

			
		}else if(NC_ARABIC.equals(inputStatus)){//阿拉伯数字
			//记录可能的结束位置
			nEnd = context.getCursor() - 1;
			//输出可能存在的数词
			outputNumLexeme(context);
			//重置数词状态
			nReset();
			//进入初始态进行处理
			onNaNStatus(inputStatus , context);	
			
		}else if(NC_ROME.equals(inputStatus)){//罗马数字
			//记录可能的结束位置
			nEnd = context.getCursor() - 1;
			//输出可能存在的数词
			outputNumLexeme(context);
			//重置数词状态
			nReset();
			//进入初始态进行处理
			onNaNStatus(inputStatus , context);	
			
//		}else if(NC_ENGLISH.equals(inputStatus)){//英文借代数字
//			//记录当前的字符状态
//			nStatus = inputStatus;
//			//记录可能的结束位置
//			nEnd = context.getCursor();			
			
		}else{
			//重置数词状态
			nReset();
			//进入初始态进行处理
			onNaNStatus(inputStatus , context);
			
		}
	}
	
	/**
	 * 当前为CHINESE状态时，状态机的处理(状态转换)
	 * @param inputStatus
	 * @param context
	 */
	private void onCHINESEStatus(String inputStatus ,  Context context){
		if(NC_CHINESE.equals(inputStatus)){//中文数字
			//记录可能的结束位置
			nEnd = context.getCursor();
			
		}else if(NC_CNM.equals(inputStatus)){//中文数字链接符
			//记录当前的字符状态
			nStatus = inputStatus;
			
		}else if(NC_CNE.equals(inputStatus)){//中文数字结束符
			//记录当前的字符状态
			nStatus = inputStatus;
			//记录可能的结束位置
			nEnd = context.getCursor();
			
		}else{//其他输入
			//输出可能存在的数词
			outputNumLexeme(context);
			//重置数词状态
			nReset();
			//进入初始态进行处理
			onNaNStatus(inputStatus , context);
			
		}
	}
	
	/**
	 * 当前为CNM状态时，状态机的处理(状态转换)
	 * @param inputStatus
	 * @param context
	 */
	private void onCNMStatus(String inputStatus ,  Context context){
		if(NC_CHINESE.equals(inputStatus)){//中文数字
			//记录当前的字符状态
			nStatus = inputStatus;
			//记录可能的结束位置
			nEnd = context.getCursor();
			
		}else if(NC_CNE.equals(inputStatus)){//中文数字结束符
			//记录当前的字符状态
			nStatus = inputStatus;
			//记录可能的结束位置
			nEnd = context.getCursor();
			
		}else{//其他输入
			//输出可能存在的数词
			outputNumLexeme(context);
			//重置数词状态
			nReset();
			//进入初始态进行处理
			onNaNStatus(inputStatus , context);
			
		}		
	}
	
	/**
	 * 当前为CNE状态时，状态机的处理(状态转换)
	 * @param inputStatus
	 * @param context
	 */
	private void onCNEStatus(String inputStatus ,  Context context){
		//输出可能存在的数词
		outputNumLexeme(context);
		//重置数词状态
		nReset();
		//进入初始态进行处理
		onNaNStatus(inputStatus , context);
				
	}
	
	/**
	 * 当前为ROME状态时，状态机的处理(状态转换)
	 * @param inputStatus
	 * @param context
	 */
	private void onROMEStatus(String inputStatus ,  Context context){
		if(NC_ROME.equals(inputStatus)){//罗马数字
			//记录可能的结束位置
			nEnd = context.getCursor();
			
		}else{//其他输入
			//输出可能存在的数词
			outputNumLexeme(context);
			//重置数词状态
			nReset();
			//进入初始态进行处理
			onNaNStatus(inputStatus , context);
			
		}
	}
	
//	/**
//	 * 当前为ENGLISH状态时，状态机的处理(状态转换)
//	 * @param inputStatus
//	 * @param context
//	 */
//	private void onENGLISHStatus(String inputStatus ,  Context context){
//		if(NC_ENGLISH.equals(inputStatus)){//英文借代数字
//			//记录可能的结束位置
//			nEnd = context.getCursor();
//			
//		}else{//其他输入
//			//输出可能存在的数词
//			outputNumLexeme(context);
//			//重置数词状态
//			nReset();
//			//进入初始态进行处理
//			onNaNStatus(inputStatus , context);
//			
//		}
//	}	
//	
	/**
	 * 添加数词词元到结果集
	 * @param context
	 */
	private void outputNumLexeme(Context context){
		if(nStart > -1 && nEnd > -1){
			//生成已切分的词元
			Lexeme newLexeme = new Lexeme(context.getBuffOffset() ,nStart , nEnd - nStart + 1);
			lexemeSet.add(newLexeme);
		}
	}
	
	/**
	 * 添加量词词元到结果集
	 * @param context
	 */
	private void outputCountLexeme(Context context){
		if(countStart > -1 && countEnd > -1){
			//生成已切分的词元
			Lexeme countLexeme = new Lexeme(context.getBuffOffset() ,countStart , countEnd - countStart + 1);
			lexemeSet.add(countLexeme);
		}

	}	
	
	/**
	 * 重置数词的状态
	 */
	private void nReset(){
		this.nStart = -1;
		this.nEnd = -1;
		this.nStatus = NaN;
	}
	
	/**
	 * 识别数字字符类型
	 * @param input
	 * @return
	 */
	private String nIdentify(char[] segmentBuff , Context context){
		
		//读取当前位置的char	
		char input = segmentBuff[context.getCursor()];
		
		String type = NaN;
		
		if(CharacterHelper.isArabicNumber(input)){
			 type = NC_ARABIC;
			 
		}else if(Arabic_Num_Pre.indexOf(input) >= 0){
			type = NC_ANP;
			
		}else if(Arabic_Num_Mid.indexOf(input) >= 0){
			type = NC_ANM;
			
		}else if(Arabic_Num_End.indexOf(input) >= 0){
			type = NC_ANE;
			
		}else if(Chn_Num.indexOf(input) >= 0){
			type = NC_CHINESE;
			
		}else if(Chn_Num_Pre.indexOf(input) >= 0){
			type = NC_CNP;
			
		}else if(Chn_Num_Mid.indexOf(input) >= 0){
			type = NC_CNM;
			
		}else if(Chn_Num_End.indexOf(input) >= 0){
			type = NC_CNE;
			
		}else if(Rome_Num.indexOf(input) >= 0){
			type = NC_ROME;
			
		}
//		else if(Eng_Num.indexOf(input) >= 0){
//			type = NC_ENGLISH;
//			
//		}

		return type;
	}

	/**
	 * 处理中文量词
	 * @param segmentBuff
	 * @param context
	 */
	private void processCount(char[] segmentBuff , Context context){
		Hit hit = null;
		//System.out.println(context.getCursor());

		if(countStart == -1){
			hit = Dictionary.matchInQuantifierDict(segmentBuff , context.getCursor() , 1);
		}else{
			hit = Dictionary.matchInQuantifierDict(segmentBuff , countStart , context.getCursor() - countStart + 1);
		}
		
		if(hit != null){
			if(hit.isPrefix()){
				if(countStart == -1){
					//设置量词的开始
					countStart = context.getCursor();
				}
			}
			
			if(hit.isMatch()){
				if(countStart == -1){
					countStart = context.getCursor();
				}
				//设置量词可能的结束
				countEnd = context.getCursor();
				//输出可能存在的量词
				outputCountLexeme(context);
			}
			
			if(hit.isUnmatch()){
				if(countStart != -1){
					//输出可能存在的量词
					outputCountLexeme(context);
					//重置量词状态
					countStart = -1;
					countEnd = -1;
				}
			}
		}
		
		//读到缓冲区最后一个字符，还有尚未输出的量词
		if(context.getCursor() == context.getAvailable() - 1
				&& countStart != -1 && countEnd != -1){
			//输出可能存在的量词
			outputCountLexeme(context);
			//重置量词状态
			countStart = -1;
			countEnd = -1;
		}
	}

}
