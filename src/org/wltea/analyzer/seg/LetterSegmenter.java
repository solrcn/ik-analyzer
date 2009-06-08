/**
 * 
 */
package org.wltea.analyzer.seg;

import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.Context;

/**
 * 负责处理字母的子分词器，涵盖一下范围
 * 1.英文单词、英文加阿拉伯数字、专有名词（公司名）
 * 2.IP地址、Email
 * @author 林良益
 *
 */
public class LetterSegmenter implements ISegmenter {

	/* (non-Javadoc)
	 * @see org.wltea.analyzer.ISegmenter#nextLexeme(org.wltea.analyzer.IKSegmentation.Context)
	 */
	public Lexeme nextLexeme(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
