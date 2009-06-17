/**
 * 
 */
package org.wltea.analyzer.seg;

import org.wltea.analyzer.Context;
import org.wltea.analyzer.Lexeme;

/**
 * 子分词器抽象父类
 * @author 林良益
 *
 */
public abstract class AbsSegmenter implements ISegmenter {

	/* (non-Javadoc)
	 * @see org.wltea.analyzer.seg.ISegmenter#nextLexeme(org.wltea.analyzer.Context)
	 */
	public abstract Lexeme nextLexeme(char[] segmentBuff , Context context);
	

}
