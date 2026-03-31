package proofEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import iterators.FileCharacterIterator;
import parse.PrimitiveTokenizer;
import parse.TreeExprParser;
import proofEngine.objects.Node;
import proofEngine.objects.Rule;

public class Utility {
	
	public static Node nodeFromFile(String path) throws FileNotFoundException
	{
		File file = new File(path);
		Iterator<Character> chars = new FileCharacterIterator(file);
		List<String> tokens = new PrimitiveTokenizer(chars).tokenize();
		Node node = new Node (TreeExprParser.parseExpr(tokens));
		return node;
	}
	
	public static List<Rule> rulesFromNode(Node node) {
		List<Rule> rules = new ArrayList<Rule>();
		for(Node n : node.nodes)
		{
			rules.add(new Rule(n));
		}
		return rules;
	}

}
