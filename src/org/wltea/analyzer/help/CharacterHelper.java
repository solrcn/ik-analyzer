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
	 * 进行字符规格化（全角转半角，大写转小写处理）
	 * @param input
	 * @return char
	 */
	public static char regularize(char input){
        if (input == 12288) {
            input = (char) 32;
            
        }else if (input > 65280 && input < 65375) {
            input = (char) (input - 65248);
            
        }else if (input >= 'A' && input <= 'Z') {
        	input += 32;
		}
        
        return input;
	}

}
