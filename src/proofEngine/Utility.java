package proofEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

import iterators.FileCharacterIterator;
import parse.PrimitiveTokenizer;
import parse.TreeExprParser;

public class Utility {
	
	public static Node nodeFromFile(String path) throws FileNotFoundException
	{
		File file = new File(path);
		Iterator<Character> chars = new FileCharacterIterator(file);
		List<String> tokens = new PrimitiveTokenizer(chars).tokenize();
		Node node = new Node (TreeExprParser.parseExpr(tokens));
		return node;
	}

}
