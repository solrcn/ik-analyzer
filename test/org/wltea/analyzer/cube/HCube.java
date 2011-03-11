/**
 * 
 */
package org.wltea.analyzer.cube;

import org.wltea.analyzer.Lexeme;

/**
 * @author linliangyi
 *
 */
public class HCube {

	private int left = -1;
	
	private int right = -1;
	
	
	/**
	 * 
	 * @param lexeme
	 * @return
	 */
	public boolean accept(Lexeme lexeme){
		if(left == -1 && right == -1){
			//accept
			
		}
		return false;
	}
	
	class Path{
		
	}
}
