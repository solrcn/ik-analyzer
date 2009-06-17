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
	
	
	
//    /**
//     * 判断字符类型
//     * 字符分为:英文字母\数字\CJK(汉字)\符号(拉丁字集)
//     * @param theOne
//     * @return
//     */
//    public int identify(char onechar){
//        int charType = Dictionary.BASECHARTYPE_NULL;
//        
//        if((onechar >= 'a' && onechar <= 'z') || (onechar >= 'A' && onechar <= 'Z')){
//            charType = charType | Dictionary.BASECHARTYPE_LETTER;
//        }
//        
//        if(this.isNumber(onechar)){
//            charType = charType | Dictionary.BASECHARTYPE_NUMBER;
//            if(this.isCHNNumber(onechar)){
//            	charType = charType | Dictionary.NUMBER_CHN;
//            }
//            if(this.isOtherDigit(onechar)){
//            	charType = charType | Dictionary.NUMBER_OTHER;
//            }
//        }
//       
//        if(Character.UnicodeBlock.of(onechar) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS){
//            charType = charType | Dictionary.BASECHARTYPE_CJK;
//        }
//       
//        if(charType == Dictionary.BASECHARTYPE_NULL){
//            charType = charType | Dictionary.BASECHARTYPE_OTHER;
//			if(this.isNbSign(onechar)){
//				charType = charType | Dictionary.OTHER_NUMSIGN;
//			}
//			if(this.isConnector(onechar)){
//				charType = charType | Dictionary.OTHER_CONNECTOR;
//			}
//        }
//        return charType;
//    }	
//	
	
	
	public static boolean isSpaceLetter(char input){
		return input == 8 || input == 9 
				|| input == 10 || input == 13 
				|| input == 32 || input == 160;
	}
	
	public static boolean isEnglishLetter(char input){
		return (input >= 'a' && input <= 'z') 
				|| (input >= 'A' && input <= 'Z');
	}
	
	public static boolean isArabicNumber(char input){
		return input >= '0' && input <= '9';
	}
	
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
