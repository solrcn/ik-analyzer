/**
 * 
 */
package org.wltea.analyzer.help;

/**
 * 字符集识别辅助工具类
 * @author 林良益
 *
 */
public class CharacterHelper {
	
	
	/**
	 * 全角字符转半角字符
	 * @return
	 */
	public static char SBC2DBC(char input){
        if (input == 12288) {
            input = (char) 32;
        }
        if (input > 65280 && input < 65375) {
            input = (char) (input - 65248);
        }
        return input;
	}

}
