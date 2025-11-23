package treeParse;

import java.util.ArrayList;
import java.util.List;

public class TreeExprParser {
	
	public static TreeNode<String> parseExpr(List<String> tokens)
	{
		List<String> nTokens = new ArrayList<String>(tokens);
		
		TreeNode<String> node = parse(nTokens);
		
		if(nTokens.size() != 0)
			throw new RuntimeException("not all tokens consumed");
		
		return node;
	}
	
	private static TreeNode<String> parse(List<String> tokens)
	{
		// ? OR ( ? )
		
		TreeNode<String> node = new TreeNode<String>();
		
		if(tokens.size() != 0) 
		{
			if(isOpenBracket(tokens.get(0)))
			{
				tokens.remove(0); // remove "("
				while(true)
				{
					node.nodes.add(parse(tokens));
					
					if(tokens.size() == 0)
						throw new RuntimeException("out of tokens");
					
					if(isCloseBracket(tokens.get(0)))
						break;
				}
				tokens.remove(0); // remove ")"
			}
			else if (isCloseBracket(tokens.get(0)))
			{
				// no label -> ()
			}
			else
				node.label = tokens.remove(0);
		}
		else
			throw new RuntimeException("out of tokens");
		
		return node;
	}
	
	private static boolean isOpenBracket(Object obj)
	{
		if(obj instanceof String)
			return ((String)obj).equals("(");
		
		return false;
	}
	
	private static boolean isCloseBracket(Object obj)
	{
		if(obj instanceof String)
			return ((String)obj).equals(")");
		
		return false;
	}

}
