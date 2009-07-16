/**
 * 
 */
package org.wltea.analyzer.seg;

import org.wltea.analyzer.Context;

/**
 * 子分词器接口
 * @author 林良益
 *
 */
public interface ISegmenter {
	
	void nextLexeme(char[] segmentBuff , Context context);
	
	void reset();
}
