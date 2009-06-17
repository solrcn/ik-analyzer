/**
 * 
 */
package org.wltea.analyzer.seg;

import org.wltea.analyzer.Context;
import org.wltea.analyzer.Lexeme;

/**
 * 中文词元处理子分词器，涵盖一下范围
 * 1.中文词语
 * 2.姓名
 * 3.地名
 * 4.未知词
 * @author 林良益
 *
 */
public class ChineseSegmenter extends AbsSegmenter {

	/* (non-Javadoc)
	 * @see org.wltea.analyzer.seg.ISegmenter#nextLexeme(org.wltea.analyzer.Context)
	 */
	public Lexeme nextLexeme(char[] segmentBuff , Context context) {
		// TODO Auto-generated method stub
		return null;
	}


}
