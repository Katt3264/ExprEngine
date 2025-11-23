package iterators;

import java.util.Iterator;

public class StringCharacterIterator implements Iterator<Character> {

	private String str;
	private int index = -1;
	
	public StringCharacterIterator(String str)
	{
		this.str = str;
	}
	
	@Override
	public boolean hasNext() 
	{
		return (index + 1) < str.length();
	}

	@Override
	public Character next() 
	{
		index++;
		return str.charAt(index);
	}

}
