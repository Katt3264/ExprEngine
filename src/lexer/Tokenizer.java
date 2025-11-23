package lexer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Tokenizer {
	
	private static final String spacer = " \n\t";
	private static final String lowerCase = "abcdefghijklmnopqrstuvwxyz";
	private static final String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String number = "0123456789";
	private static final String alpha = upperCase + lowerCase;
	
	private static final String escapedChar = "\\\"nt";
	private static final String stringChar = alpha + number + " ";
	private static final String identifierFirstChar = alpha + "_";
	private static final String identifierChar = alpha + number + "_";
	
	private static String[] specialTokens = new String[] {
		"+", "++", "-", "--", "*", "/", "%", "^",
		"===", "==", "=", "<=", ">=", "!=",
		"(", "{", "[", "<",
		")", "}", "]", ">",
		".", ",", ";"
	};
	
	private String currentToken = "";
	private List<String> tokens = new ArrayList<String>();
	private Iterator<Character> charIter;
	
	private State state = State.EmptyToken;
	
	public Tokenizer(Iterator<Character> charIter)
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
	
	private enum State {
		EmptyToken,
		Identifier,
		Number,
		String,
		EscapeInString,
		Operator,
	};
	
	private void consume(int c)
	{
		switch(state) {
			case EmptyToken:
				stateEmptyToken(c);
				break;
			case Identifier:
				stateIdentifier(c);
				break;
			case Number:
				stateNumber(c);
				break;
			case String:
				stateString(c);
				break;
			case EscapeInString:
				stateEscapeInString(c);
				break;
			case Operator:
				stateOperator(c);
				break;
			default:
				throw new RuntimeException("invalid state: " + state);
		}
	}
	
	private void stateEmptyToken(int c)
	{
		if(c == -1) {
			
		} else if(isCharInString(c, spacer)) {
			state = State.EmptyToken;
		} else if(isCharInString(c, identifierFirstChar)) {
			appendChar(c);
			state = State.Identifier;
		} else if(isCharInString(c, number)) {
			appendChar(c);
			state = State.Number;
		} else if(c == '\"') {
			appendChar(c);
			state = State.String;
		} else if(potentialMatch(currentToken, specialTokens) != 0) {
			appendChar(c);
			state = State.Operator;
		} else {
			throw new RuntimeException("invalid char: " + (char)c + " number: " + c + " in state: " + state);
		}
	}
	
	private void stateIdentifier(int c)
	{
		if(c == -1) {
			appendToken();
		} else if(isCharInString(c, spacer)) {
			appendToken();;
			state = State.EmptyToken;
		} else if(isCharInString(c, identifierChar)) {
			appendChar(c);
			state = State.Identifier;
		} else if(c == '\"') {
			appendToken();
			appendChar(c);
			state = State.String;
		} else if(potentialMatch("" + (char)c, specialTokens) != 0) {
			appendToken();
			appendChar(c);
			state = State.Operator;
		} else {
			throw new RuntimeException("invalid char: " + (char)c + " number: " + c + " in state: " + state);
		}
	}
	
	private void stateNumber(int c)
	{
		if(isCharInString(c, number)) {
			appendChar(c);
			state = State.Number;
		} else {
			appendToken();
			stateEmptyToken(c);
		}
	}
	
	private void stateString(int c)
	{
		if(isCharInString(c, stringChar)) {
			appendChar(c);
			state = State.String;
		} else if(c == '\\') {
			appendChar(c);
			state = State.EscapeInString;
		} else if(c == '\"') {
			appendToken();
			state = State.EmptyToken;
		} else {
			throw new RuntimeException("invalid char: " + (char)c + " number: " + c + " in state: " + state);
		}
	}
	
	private void stateEscapeInString(int c)
	{
		if(isCharInString(c, escapedChar)) {
			appendChar(c);
			state = State.String;
		} else {
			throw new RuntimeException("invalid char: " + (char)c + " number: " + c + " in state: " + state);
		}
	}
	
	private void stateOperator(int c)
	{
		if(c == -1) {
			appendToken();
		} else if(potentialMatch(currentToken, specialTokens) != 0 && potentialMatch(currentToken + (char)c, specialTokens) == 0) {
			appendToken();
			stateEmptyToken(c);
		} else if(potentialMatch(currentToken + (char)c, specialTokens) != 0) {
			appendChar(c);
			state = State.Operator;
		} else {
			throw new RuntimeException("invalid char: " + (char)c + " number: " + c + " in state: " + state);
		}
	}
	
	private static int potentialMatch(String str, String[] match)
	{
		int i = 0;
		for(String s : match)
			if(s.startsWith(str))
				i++;
		
		return i;
	}
	
	private static boolean isCharInString(int c, String s)
	{
		for(char ch : s.toCharArray())
			if(ch == c)
				return true;
		
		return false;
	}

}
