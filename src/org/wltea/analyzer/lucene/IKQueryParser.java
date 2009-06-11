/**
 * 
 */
package org.wltea.analyzer.lucene;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

/**
 * 查询分析器
 * 针对IK Analyzer V3的优化实现
 * @author 林良益
 *
 */
public final class IKQueryParser {
	
	/**
	 * 单连续字窜（不带空格符）单Field查询分析
	 * @param field
	 * @param query
	 * @return
	 * @throws IOException
	 */
	private static Query _parse(String field , String query) throws IOException{
		if(field == null){
			throw new IllegalArgumentException("parameter \"field\" is null");
		}
		if(query == null || "".equals(query.trim())){
			return new TermQuery(new Term(field));
		}
		TokenBranch root = new TokenBranch(null);		
		//对查询条件q进行分词
		StringReader input = new StringReader(query.trim());
		IKSegmentation ikSeg = new IKSegmentation(input);
		for(Lexeme l = ikSeg.next() ; l != null ; l = ikSeg.next()){
			//处理词元分支
			root.accept(new TokenBranch(l));
		}
		return root.toQuery(field);
	}
	
	/**
	 * 单条件,单Field查询分析
	 * @param queryKeyword
	 * @return
	 * @throws IOException 
	 */
	public static Query parse(String field , String query) throws IOException{
		if(field == null){
			throw new IllegalArgumentException("parameter \"field\" is null");
		}
		String[] qParts = query.split("\\s");
		if(qParts.length > 1){			
			BooleanQuery resultQuery = new BooleanQuery();
			for(String q : qParts){		
				Query partQuery = _parse(field , q);
				if(partQuery != null && 
				          (!(partQuery instanceof BooleanQuery) || ((BooleanQuery)partQuery).getClauses().length>0)){
					resultQuery.add(partQuery, Occur.SHOULD); 
				}
			}
			return resultQuery;
		}else{
			return _parse(field , query);
		}
	}
	
	/**
	 * 多Field,单条件查询分析
	 * @param queryKeyword
	 * @return
	 * @throws IOException 
	 */
	public static Query parseMultiField(String[] fields , String query) throws IOException{
		if(fields == null){
			throw new IllegalArgumentException("parameter \"fields\" is null");
		}		
		BooleanQuery resultQuery = new BooleanQuery();		
		for(String field : fields){
			if(field != null){
				Query partQuery = parse(field , query);
				if(partQuery != null && 
				          (!(partQuery instanceof BooleanQuery) || ((BooleanQuery)partQuery).getClauses().length>0)){
					resultQuery.add(partQuery, Occur.SHOULD); 
				}
			}			
		}		
		return resultQuery;
	}
	
	/**
	 * 多Field,单条件,多Occur查询分析
	 * @param queryKeyword
	 * @return
	 * @throws IOException 
	 */
	public static Query parseMultiField(String[] fields , String query ,  BooleanClause.Occur[] flags) throws IOException{
		if(fields == null){
			throw new IllegalArgumentException("parameter \"fields\" is null");
		}
		if(flags == null){
			throw new IllegalArgumentException("parameter \"flags\" is null");
		}
		
		if (flags.length != fields.length){
		      throw new IllegalArgumentException("flags.length != fields.length");
		}		
		
		BooleanQuery resultQuery = new BooleanQuery();		
		for(int i = 0; i < fields.length; i++){
			if(fields[i] != null){
				Query partQuery = parse(fields[i] , query);
				if(partQuery != null && 
				          (!(partQuery instanceof BooleanQuery) || ((BooleanQuery)partQuery).getClauses().length>0)){
					resultQuery.add(partQuery, flags[i]); 
				}
			}			
		}		
		return resultQuery;
	}
	
	/**
	 * 多Field多条件查询分析
	 * @param fields
	 * @param queries
	 * @return
	 * @throws IOException 
	 */
	public static Query parseMultiField(String[] fields , String[] queries) throws IOException{
		if(fields == null){
			throw new IllegalArgumentException("parameter \"fields\" is null");
		}				
		if(queries == null){
			throw new IllegalArgumentException("parameter \"queries\" is null");
		}				
		if (queries.length != fields.length){
		      throw new IllegalArgumentException("queries.length != fields.length");
		}
		BooleanQuery resultQuery = new BooleanQuery();		
		for(int i = 0; i < fields.length; i++){
			if(fields[i] != null){
				Query partQuery = parse(fields[i] , queries[i]);
				if(partQuery != null && 
				          (!(partQuery instanceof BooleanQuery) || ((BooleanQuery)partQuery).getClauses().length>0)){
					resultQuery.add(partQuery, Occur.SHOULD); 
				}
			}			
		}		
		return resultQuery;
	}

	/**
	 * 多Field,多条件,多Occur查询分析
	 * @param fields
	 * @param queries
	 * @param flags
	 * @return
	 * @throws IOException
	 */
	public static Query parseMultiField(String[] fields , String[] queries , BooleanClause.Occur[] flags) throws IOException{
		if(fields == null){
			throw new IllegalArgumentException("parameter \"fields\" is null");
		}				
		if(queries == null){
			throw new IllegalArgumentException("parameter \"queries\" is null");
		}
		if(flags == null){
			throw new IllegalArgumentException("parameter \"flags\" is null");
		}
		
	    if (!(queries.length == fields.length && queries.length == flags.length)){
	        throw new IllegalArgumentException("queries, fields, and flags array have have different length");
	    }

	    BooleanQuery resultQuery = new BooleanQuery();		
		for(int i = 0; i < fields.length; i++){
			if(fields[i] != null){
				Query partQuery = parse(fields[i] , queries[i]);
				if(partQuery != null && 
				          (!(partQuery instanceof BooleanQuery) || ((BooleanQuery)partQuery).getClauses().length>0)){
					resultQuery.add(partQuery, flags[i]); 
				}
			}			
		}		
		return resultQuery;
	}	
	/**
	 * 词元分支
	 * 当分词出现歧义时，采用词元分支容纳不同的歧义组合
	 * @author 林良益
	 *
	 */
	private static class TokenBranch{
		
		private Lexeme lexeme;
		private List<TokenBranch> acceptedBranchs;
		
		TokenBranch(Lexeme lexeme){
			this.lexeme = lexeme;
		}

		public Lexeme getLexeme() {
			return lexeme;
		}			
		
		/**
		 * 组合词元分支
		 * @param branch
		 * @return 返回当前branch能否合并词元分支
		 */
		boolean accept(TokenBranch branch){	
			/*		
			 * 判断当前分支是否能接纳branch
			 * 如果branch中的lexeme的字符位置没有相交，则认为可以接纳	
			 */
			if(isAcceptable(branch)){
				if(acceptedBranchs == null){
					//当前branch没有子branch，则连接到当前branch下
					acceptedBranchs = new ArrayList<TokenBranch>(2);
					acceptedBranchs.add(branch);					
				}else{
					boolean acceptedByChild = false;
					//当前branch拥有子branch，则优先由子branch接纳
					for(TokenBranch childBranch : acceptedBranchs){
						acceptedByChild = acceptedByChild || childBranch.accept(branch);
					}
					//如果所有的子branch不能接纳，则由当前branch接纳
					if(!acceptedByChild){
						acceptedBranchs.add(branch);
					}					
				}				
				return true;
			}else{			
				return false;
			}
		}
		
		/**
		 * 将分支数据转成Query逻辑
		 * @return
		 */
		Query toQuery(String fieldName){
			//生成当前branch 的query
			Query termQuery = null;
			if(lexeme != null){
				termQuery = new TermQuery(new Term(fieldName , lexeme.getLexemeText()));
			}
			
			//生成child branch 的query
			BooleanQuery orQuery = null;
			if(acceptedBranchs != null && acceptedBranchs.size() > 0){
				orQuery = new BooleanQuery();
				Query childQuery = null;
				for(TokenBranch childBranch : acceptedBranchs){
					childQuery = childBranch.toQuery(fieldName);
					if(childQuery != null){
						orQuery.add(childQuery, Occur.SHOULD);
					}
				}
			}

			//生成当前branch 的完整query
			if(termQuery != null && orQuery != null){
				BooleanQuery branchQuery = new BooleanQuery();
				branchQuery.add(termQuery, Occur.MUST);
				branchQuery.add(orQuery, Occur.MUST);
				return branchQuery;
			}else if(termQuery != null && orQuery == null){
				return termQuery;
			}else if(termQuery == null && orQuery != null){
				return orQuery;
			}else{			
				return null;
			}
		}
		
		/**
		 * 判断指定的branch能否被当前的branch接受
		 * @param branch
		 * @return
		 */
		private boolean isAcceptable(TokenBranch branch){
			if(null == this.lexeme||
					null ==  branch.getLexeme()){
				return true;
			}
			if( branch.getLexeme().getBegin() >= this.lexeme.getBegin() 
					&& branch.getLexeme().getBegin() <= this.lexeme.getEnd()){
				return false;
			}			
			if( this.lexeme.getBegin()  >= branch.getLexeme().getBegin() 
					&& this.lexeme.getBegin() <= branch.getLexeme().getEnd()){
				return false;
			}			
			return true;
		}
	
	}
}
