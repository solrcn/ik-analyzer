/**
 * 
 */
package org.wltea.analyzer.seg;

import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.Context;

/**
 * 子分词器接口
 * @author 林良益
 *
 */
public interface ISegmenter {
	
	Lexeme nextLexeme(char[] segmentBuff , Context context);
}
