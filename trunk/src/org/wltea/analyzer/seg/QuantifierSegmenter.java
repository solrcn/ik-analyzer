/**
 * 
 */
package org.wltea.analyzer.seg;

import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.Context;

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
public class QuantifierSegmenter extends AbsSegmenter  {

	
	//1 
	//一
	//壹
	//Ⅰ+期  -- RnQt
	//V+期  -- EnQt
	//11万3千9百 -- AnCd
	//11万3 -- AnCdAn
	//十一万三千、 二十万 -- CnCd
	//百八十万 -- CdCnCd
	//卅一 ， 廿一
	
	//第一 -- 
	//第一+名,第一+个
	//第I + 集
	//第Ⅴ+ 部
	
	//半+天，几+位，多+人,
	//二十几+个 ， 三十多+张
	
	//十一点五；十一点五万
	
	//十一 + 点
	
	//十一点钟
	//十一点半
	
	// $300  ￥500
	
	// -1223.333； +12  ； 12+5i(复数)
	
	// 111,222,222.00  , 
	// 1/23 2009/01/01  23:41:00
	// 100%  0.5‰
	
	
	//GB库中的罗马字符(起始、中间、结束)
	public static final String Rome = "ⅠⅡⅢⅣⅤⅥⅧⅨⅩⅪ"; //Rn
	
	//英文字母中被借用的字符
	public static final String Eng_Num = "IVX"; //En
	
	//中文数词
	public static final String Chn_Num = "○一二两三四五六七八九十零壹贰叁肆伍陆柒捌玖拾";//Cn
	//中文数词_位词
	public static final String Chn_Num_Digit = "百千万亿拾佰仟萬億兆"; //Cd
	//中文数词前缀
	public static final String Chn_Num_Pre = "半第卅廿几";//Cp
	//中文数词中间
	public static final String Chn_Num_Mid = "点";//Cm
	//中文数词尾缀
	public static final String Chn_Num_Post = "几多";//Co
	//中文时钟
	public static final String Chn_Clock = "点半";//Oo
	
	//货币符号
	public static final String Money_Sign = "$￥";//M
	
	public static final String Arabic_Num_Sign_Pre = "-+";//Ap
	//阿拉伯数字符号_中间
	public static final String Arabic_Num_Sign_Mid = ",./";//Am
	//阿拉伯数字符号_后缀
	public static String Arabic_Num_Sign_Post = "%‰";//Ao
	
	//阿拉伯数字 An
	//量词 Qt
	
	
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
	
	public QuantifierSegmenter(){
		start = -1;
		end = -1;
	}
	
	/* (non-Javadoc)
	 * @see org.wltea.analyzer.ISegmenter#nextLexeme(org.wltea.analyzer.IKSegmentation.Context)
	 */
	public Lexeme nextLexeme(char[] segmentBuff , Context context) {
		//读取当前位置的char	
		char input = segmentBuff[context.getCursor()];
		
		if(start == -1){//当前的分词器尚未开始处理字符	
			
		}else{
			
		}
		return null;
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
//	private int identify(char input){
//		
//	}

}
