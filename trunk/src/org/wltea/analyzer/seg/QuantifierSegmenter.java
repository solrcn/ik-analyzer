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
 * 3.时钟
 * 4.罗马数字
 * 5.数学符号 % . / 
 * @author 林良益
 *
 */
public class QuantifierSegmenter implements ISegmenter {
	//阿拉伯数字
	public static String Arabic = "0123456789";
	//GB库中的罗马字符
	public static String Rome = "ⅠⅡⅢⅣⅤⅥⅧⅨⅩⅪ";
	//英文字母中被借用的字符
	public static String Eng_Num = "IVXO";
	//中文数词
	public static String Chn_Num = "○一二三四五六七八九零壹贰叁肆伍陆柒捌玖十百千万亿拾佰仟萬億兆";
	//中文数词前缀
	public static String Chn_Num_Pre = "半第俩两三仨卅廿几";
	//中文数次尾缀
	public static String Chn_Num_Post = "几";
	//中文时钟
	public static String Chn_Clock = "点半";
	//时钟符号
	public static String Clock_Sign = ":";
	//数字符号_前缀
	public static String Num_Sign_Pre = "-+";
	//数字符号_中间
	public static String Num_Sign_Mid = "./点";
	//数字符号_后缀
	public static String Num_Sign_Post = "%‰";

	/* (non-Javadoc)
	 * @see org.wltea.analyzer.ISegmenter#nextLexeme(org.wltea.analyzer.IKSegmentation.Context)
	 */
	public Lexeme nextLexeme(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
