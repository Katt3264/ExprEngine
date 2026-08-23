package parse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PrimitiveTokenizer {
	
	private static final String spacer = " \n\t";
	
	private boolean inComment = false;
	private String currentToken = "";
	private List<String> tokens = new ArrayList<String>();
	private Iterator<Character> charIter;
	
	
	public PrimitiveTokenizer(Iterator<Character> charIter)
	{
		this.charIter = charIter;
	}
	
	public List<String> tokenize()
	{
		while(charIter.hasNext())
			consume(charIter.next());
		
		consume(-1);
		
		return tokens;
	}
	
	private void appendChar(int i)
	{
		currentToken += (char)i;
	}
	
	private void appendToken()
	{
		tokens.add(currentToken);
		currentToken = "";
	}
	
	private void consume(int c)
	{
		if(inComment)
		{
			if(c == '\n')
				inComment = false;
		}
		else if(c == '#')
		{
			inComment = true;
			if(currentToken.length() != 0)
				appendToken();
		}
		else if(c == -1)
		{
			if(currentToken.length() != 0)
				appendToken();
		}
		else if (isCharInString(c, spacer))
		{
			if(currentToken.length() != 0)
				appendToken();
		}
		else
		{
			appendChar(c);
		}
	}
	
	private static boolean isCharInString(int c, String s)
	{
		for(char ch : s.toCharArray())
			if(ch == c)
				return true;
		
		return false;
	}

}
