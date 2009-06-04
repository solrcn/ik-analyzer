/**
 * 
 */
package org.wltea.analysis.help.statistics;

import java.io.UnsupportedEncodingException;

/**
 * @author linliangyi
 *
 */
public class CharaterTest {
	
	
	public static void main(String[] args){
		char[] charArray = null;
		
//		charArray = new char[]{'我','是','中','文','字','啊'};
//
//		for(int i = 0 ; i < charArray.length ; i++){
//			System.out.println(  Integer.toBinaryString(charArray[i])   );
//			System.out.println(  Integer.toBinaryString(charArray[i] & 0xFF00));
//			System.out.println(  Integer.toBinaryString(charArray[i] & 0x00FF ));
//			
//		}
		System.out.println( "****************************" );
		
		String str = new String("123adfa！@#￥%……&×（）||!@#$%^&*()我是中文  字啊尻垚");
//		charArray = str.toCharArray();
//		for(int i = 0 ; i < charArray.length ; i++){
//			System.out.println(  Integer.toBinaryString(charArray[i])   );
//			System.out.println(charArray[i]);
//			System.out.println(  Integer.toBinaryString(charArray[i] & 0xFF00));
//			System.out.println(  Integer.toBinaryString(charArray[i] & 0x00FF ));
//			
//		}
		
		
		byte[] byteArray = null;

//		System.out.println( "系统默认 ****************************" );
//		byteArray = str.getBytes();
//		
//		for(int i = 0 ; i < byteArray.length ; i=i+2){
//			System.out.print(  Integer.toBinaryString(byteArray[i] &  0xFF)  + "  ");
//			System.out.println(  Integer.toBinaryString(byteArray[i+1] &  0xFF)  );
//		}
//
		System.out.println( " GBK ****************************" );
		
		try {
			byteArray = str.getBytes("GB2312");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int i = 0 ; i < byteArray.length ; i++){
			System.out.print(  (byteArray[i] &  0xFF)  + "  ");
			//System.out.println(  (byteArray[i+1] &  0xFF)  );
			//区位码的区码在16-87
			//区位码的位码在1-94 （其中对于第55 区的位码，最大为89）
			System.out.print(  (byteArray[i] &  0xFF)-  0xA0  + "  | ");
		}

//		System.out.println( "去空格 ****************************" );		
//		try {
//			byteArray = str.replaceAll("\\s", "").getBytes("GBK");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		for(int i = 0 ; i < byteArray.length ; i=i+2){
//			System.out.print(  (byteArray[i] &  0xFF)  + "  ");
//			System.out.println(  (byteArray[i+1] &  0xFF)  );
//			//区位码的区码在16-87
//			System.out.print(  (byteArray[i] &  0xFF)-  0xA0  + " ");
//			//区位码的位码在1-94 （其中对于第55 区的位码，最大为89）
//			System.out.println( (byteArray[i+1]  &  0xFF) -  0xA0  );
//		}
		
//		System.out.println( " UTF-8 ****************************" );
//		
//		try {
//			byteArray = str.getBytes("UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		for(int i = 0 ; i < byteArray.length ; i=i+3){
//			System.out.print(  Integer.toBinaryString(byteArray[i] &  0xFF)  + "  ");
//			System.out.print(  Integer.toBinaryString(byteArray[i+1] &  0xFF) + "  ");
//			System.out.println(  Integer.toBinaryString(byteArray[i+2] &  0xFF) );
//		}		
//		
//		System.out.println( " UTF-16 ****************************" );
//		
//		try {
//			byteArray = str.getBytes("UTF-16");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		for(int i = 0 ; i < byteArray.length ; i=i+2){
//			System.out.print(  Integer.toBinaryString(byteArray[i] &  0xFF)  + "  ");
//			System.out.println(  Integer.toBinaryString(byteArray[i+1] &  0xFF)  );
//		}
		
//		System.out.println( " GB2312 ****************************" );
//		
//		try {
//			byteArray = str.getBytes("GB2312");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		for(int i = 0 ; i < byteArray.length ; i=i+2){
//			System.out.print(  Integer.toBinaryString(byteArray[i] &  0xFF)  + "  ");
//			System.out.println(  Integer.toBinaryString(byteArray[i+1] &  0xFF)  );
//		}		
//		
	}
	

}
