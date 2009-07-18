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

//	public static boolean isSpaceLetter(char input){
//		return input == 8 || input == 9 
//				|| input == 10 || input == 13 
//				|| input == 32 || input == 160;
//	}
	
	public static boolean isEnglishLetter(char input){
		return (input >= 'a' && input <= 'z') 
				|| (input >= 'A' && input <= 'Z');
	}
	
	public static boolean isArabicNumber(char input){
		return input >= '0' && input <= '9';
	}
	
	public static boolean isCJKCharacter(char input){
		return Character.UnicodeBlock.of(input) 
					== Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS;
	}
	
	/**
	 * 全角字符转半角字符
	 * @return char 半角字符
	 * 
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
