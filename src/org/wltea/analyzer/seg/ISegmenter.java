/**
 * 
 */
package org.wltea.analyzer.seg;

import java.util.Set;

import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.Context;

/**
 * 子分词器接口
 * @author 林良益
 *
 */
public interface ISegmenter {
	
	Set<Lexeme> nextLexeme(char[] segmentBuff , Context context);
	
	void reset();
}
